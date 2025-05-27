package id.ac.ui.cs.advprog.hiringgo.periksalog.service;

import id.ac.ui.cs.advprog.hiringgo.entity.Log;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.enums.StatusLog;
import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogDTO;
import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogStatusUpdateDTO;
import id.ac.ui.cs.advprog.hiringgo.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class PeriksaLogServiceImpl implements PeriksaLogService {

    private static final String LOG_NOT_FOUND_MESSAGE = "Log not found with ID: %s";

    private final LogRepository logRepository;
    private final ValidationService validationService;
    private final LogConverterService logConverterService;

    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<LogDTO>> getAllLogsByDosenIdAsync(String dosenId) {
        return CompletableFuture.supplyAsync(() -> {
            validationService.validateDosenId(dosenId);
            log.info("Async: Fetching all logs for dosen ID: {}", dosenId);

            List<Log> logs = logRepository.findAllLogsByDosenId(dosenId);
            log.info("Async: Found {} logs for dosen ID: {}", logs.size(), dosenId);

            return logs.parallelStream()
                    .map(logConverterService::convertToDTO)
                    .toList();
        }).exceptionally(throwable -> {
            log.error("Async error while fetching logs for dosen ID: {}", dosenId, throwable);
            throw new RuntimeException("Failed to fetch logs", throwable);
        });
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<LogDTO> updateLogStatusAsync(String dosenId, LogStatusUpdateDTO logStatusUpdateDTO) {
        return CompletableFuture.supplyAsync(() -> {
            validationService.validateInputs(dosenId, logStatusUpdateDTO);

            String logId = logStatusUpdateDTO.getLogId();
            log.info("Async: Updating log status for logId: {} by dosen: {} to status: {}",
                    logId, dosenId, logStatusUpdateDTO.getStatus());

            validationService.validateDosenOwnership(logId, dosenId, this);

            Log logEntity = findLogById(logId);
            updateLogFields(logEntity, logStatusUpdateDTO);

            Log updatedLog = logRepository.save(logEntity);
            log.info("Async: Successfully updated log status for logId: {}", logId);

            return logConverterService.convertToDTO(updatedLog);
        }).exceptionally(throwable -> {
            log.error("Async error while updating log status for logId: {}", logStatusUpdateDTO.getLogId(), throwable);
            if (throwable.getCause() instanceof SecurityException) {
                throw (SecurityException) throwable.getCause();
            }
            throw new RuntimeException("Failed to update log status", throwable);
        });
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<Boolean> isLogOwnedByDosenAsync(String logId, String dosenId) {
        return CompletableFuture.supplyAsync(() -> {
            validationService.validateLogId(logId);
            validationService.validateDosenId(dosenId);

            boolean isOwned = logRepository.isLogOwnedByDosen(logId, dosenId);
            log.debug("Async: Log ownership check - logId: {}, dosenId: {}, isOwned: {}", logId, dosenId, isOwned);

            return isOwned;
        }).exceptionally(throwable -> {
            log.error("Async error while checking log ownership for logId: {} and dosenId: {}", logId, dosenId, throwable);
            return false;
        });
    }

    @Override
    public List<LogDTO> getAllLogsByDosenId(String dosenId) {
        validationService.validateDosenId(dosenId);
        log.info("Fetching all logs for dosen ID: {}", dosenId);

        List<Log> logs = logRepository.findAllLogsByDosenId(dosenId);
        log.info("Found {} logs for dosen ID: {}", logs.size(), dosenId);

        return logs.stream()
                .map(logConverterService::convertToDTO)
                .toList();
    }

    @Override
    public LogDTO updateLogStatus(String dosenId, LogStatusUpdateDTO logStatusUpdateDTO) {
        validationService.validateInputs(dosenId, logStatusUpdateDTO);

        String logId = logStatusUpdateDTO.getLogId();
        log.info("Updating log status for logId: {} by dosen: {} to status: {}",
                logId, dosenId, logStatusUpdateDTO.getStatus());

        validationService.validateDosenOwnership(logId, dosenId, this);

        Log logEntity = findLogById(logId);
        updateLogFields(logEntity, logStatusUpdateDTO);

        Log updatedLog = logRepository.save(logEntity);
        log.info("Successfully updated log status for logId: {}", logId);

        return logConverterService.convertToDTO(updatedLog);
    }

    @Override
    public boolean isLogOwnedByDosen(String logId, String dosenId) {
        validationService.validateLogId(logId);
        validationService.validateDosenId(dosenId);

        boolean isOwned = logRepository.isLogOwnedByDosen(logId, dosenId);
        log.debug("Log ownership check - logId: {}, dosenId: {}, isOwned: {}", logId, dosenId, isOwned);

        return isOwned;
    }

    private Log findLogById(String logId) {
        return logRepository.findById(logId)
                .orElseThrow(() -> {
                    log.error("Log not found with ID: {}", logId);
                    return new NoSuchElementException(String.format(LOG_NOT_FOUND_MESSAGE, logId));
                });
    }

    private void updateLogFields(Log logEntity, LogStatusUpdateDTO logStatusUpdateDTO) {
        logEntity.setStatus(StatusLog.valueOf(logStatusUpdateDTO.getStatus().name()));
        logEntity.setUpdatedAt(LocalDate.now());
    }
}