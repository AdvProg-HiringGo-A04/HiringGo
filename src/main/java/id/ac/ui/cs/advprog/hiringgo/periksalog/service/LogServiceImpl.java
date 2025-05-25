package id.ac.ui.cs.advprog.hiringgo.periksalog.service;

import id.ac.ui.cs.advprog.hiringgo.entity.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.entity.Log;
import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogDTO;
import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogStatusUpdateDTO;
import id.ac.ui.cs.advprog.hiringgo.repository.LogRepository;
import id.ac.ui.cs.advprog.hiringgo.entity.MataKuliah;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.model.enums.StatusLog;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.model.enums.TipeKategori;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private static final String LOG_NOT_FOUND_MESSAGE = "Log not found with ID: %s";
    private static final String PERMISSION_DENIED_MESSAGE = "You don't have permission to update this log";
    private static final double MINUTES_PER_HOUR = 60.0;

    private final LogRepository logRepository;

    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<LogDTO>> getAllLogsByDosenIdAsync(String dosenId) {
        return CompletableFuture.supplyAsync(() -> {
            validateDosenId(dosenId);
            log.info("Async: Fetching all logs for dosen ID: {}", dosenId);

            List<Log> logs = logRepository.findAllLogsByDosenId(dosenId);
            log.info("Async: Found {} logs for dosen ID: {}", logs.size(), dosenId);

            return logs.parallelStream() // Use parallel stream for processing multiple logs
                    .map(this::convertToDTO)
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
            validateInputs(dosenId, logStatusUpdateDTO);

            String logId = logStatusUpdateDTO.getLogId();
            log.info("Async: Updating log status for logId: {} by dosen: {} to status: {}",
                    logId, dosenId, logStatusUpdateDTO.getStatus());

            validateDosenOwnership(logId, dosenId);

            Log logEntity = findLogById(logId);
            updateLogFields(logEntity, logStatusUpdateDTO);

            Log updatedLog = logRepository.save(logEntity);
            log.info("Async: Successfully updated log status for logId: {}", logId);

            return convertToDTO(updatedLog);
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
            validateLogId(logId);
            validateDosenId(dosenId);

            boolean isOwned = logRepository.isLogOwnedByDosen(logId, dosenId);
            log.debug("Async: Log ownership check - logId: {}, dosenId: {}, isOwned: {}", logId, dosenId, isOwned);

            return isOwned;
        }).exceptionally(throwable -> {
            log.error("Async error while checking log ownership for logId: {} and dosenId: {}", logId, dosenId, throwable);
            return false; // Default to false for security
        });
    }

    @Override
    public List<LogDTO> getAllLogsByDosenId(String dosenId) {
        validateDosenId(dosenId);
        log.info("Fetching all logs for dosen ID: {}", dosenId);

        List<Log> logs = logRepository.findAllLogsByDosenId(dosenId);
        log.info("Found {} logs for dosen ID: {}", logs.size(), dosenId);

        return logs.stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public LogDTO updateLogStatus(String dosenId, LogStatusUpdateDTO logStatusUpdateDTO) {
        validateInputs(dosenId, logStatusUpdateDTO);

        String logId = logStatusUpdateDTO.getLogId();
        log.info("Updating log status for logId: {} by dosen: {} to status: {}",
                logId, dosenId, logStatusUpdateDTO.getStatus());

        validateDosenOwnership(logId, dosenId);

        Log logEntity = findLogById(logId);
        updateLogFields(logEntity, logStatusUpdateDTO);

        Log updatedLog = logRepository.save(logEntity);
        log.info("Successfully updated log status for logId: {}", logId);

        return convertToDTO(updatedLog);
    }

    @Override
    public boolean isLogOwnedByDosen(String logId, String dosenId) {
        validateLogId(logId);
        validateDosenId(dosenId);

        boolean isOwned = logRepository.isLogOwnedByDosen(logId, dosenId);
        log.debug("Log ownership check - logId: {}, dosenId: {}, isOwned: {}", logId, dosenId, isOwned);

        return isOwned;
    }

    private CompletableFuture<Void> validateDosenOwnershipAsync(String logId, String dosenId) {
        return isLogOwnedByDosenAsync(logId, dosenId)
                .thenAccept(isOwned -> {
                    if (!isOwned) {
                        log.warn("Permission denied: Dosen {} attempted to access log {}", dosenId, logId);
                        throw new SecurityException(PERMISSION_DENIED_MESSAGE);
                    }
                });
    }

    private void validateDosenId(String dosenId) {
        if (!StringUtils.hasText(dosenId)) {
            throw new IllegalArgumentException("Dosen ID cannot be null or empty");
        }
    }

    private void validateLogId(String logId) {
        if (!StringUtils.hasText(logId)) {
            throw new IllegalArgumentException("Log ID cannot be null or empty");
        }
    }

    private void validateInputs(String dosenId, LogStatusUpdateDTO logStatusUpdateDTO) {
        validateDosenId(dosenId);

        if (logStatusUpdateDTO == null) {
            throw new IllegalArgumentException("LogStatusUpdateDTO cannot be null");
        }

        validateLogId(logStatusUpdateDTO.getLogId());

        if (logStatusUpdateDTO.getStatus() == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
    }

    private void validateDosenOwnership(String logId, String dosenId) {
        if (!isLogOwnedByDosen(logId, dosenId)) {
            log.warn("Permission denied: Dosen {} attempted to access log {}", dosenId, logId);
            throw new SecurityException(PERMISSION_DENIED_MESSAGE);
        }
    }

    private Log findLogById(String logId) {
        return logRepository.findById(logId)
                .orElseThrow(() -> {
                    log.error("Log not found with ID: {}", logId);
                    return new NoSuchElementException(String.format(LOG_NOT_FOUND_MESSAGE, logId));
                });
    }

    private void updateLogFields(Log logEntity, LogStatusUpdateDTO logStatusUpdateDTO) {
        logEntity.setStatus(logStatusUpdateDTO.getStatus().name());
        logEntity.setUpdatedAt(LocalDate.now());
    }

    private LogDTO convertToDTO(Log logEntity) {
        if (logEntity == null) {
            log.warn("Attempted to convert null Log to DTO");
            return null;
        }

        try {
            double durationInHours = calculateDurationInHours(logEntity.getWaktuMulai(), logEntity.getWaktuSelesai());

            Mahasiswa mahasiswa = logEntity.getMahasiswa();
            if (mahasiswa == null) {
                log.warn("No mahasiswa found for log ID: {}", logEntity.getId());
            }

            MataKuliah mataKuliah = null;
            if (logEntity.getLowongan() != null) {
                mataKuliah = logEntity.getLowongan().getMataKuliah();
            }

            if (mataKuliah == null) {
                log.warn("No mata kuliah found for log ID: {}", logEntity.getId());
            }

            return LogDTO.builder()
                    .id(logEntity.getId())
                    .judul(logEntity.getJudul())
                    .keterangan(logEntity.getKeterangan())
                    .kategori(parseKategoriFromString(logEntity.getKategori()))
                    .waktuMulai(logEntity.getWaktuMulai())
                    .waktuSelesai(logEntity.getWaktuSelesai())
                    .tanggalLog(logEntity.getTanggalLog())
                    .pesanUntukDosen(logEntity.getPesan())
                    .status(parseStatusFromString(logEntity.getStatus()))
                    .mahasiswaName(getMahasiswaName(mahasiswa))
                    .mataKuliahName(getMataKuliahName(mataKuliah))
                    .mataKuliahCode(getMataKuliahCode(mataKuliah))
                    .durationInHours(durationInHours)
                    .build();
        } catch (Exception e) {
            log.error("Error converting Log to DTO for log ID: {}", logEntity.getId(), e);
            throw e;
        }
    }

    private double calculateDurationInHours(LocalTime waktuMulai, LocalTime waktuSelesai) {
        if (waktuMulai == null || waktuSelesai == null) {
            log.warn("Invalid time values for duration calculation: start={}, end={}", waktuMulai, waktuSelesai);
            return 0.0;
        }

        Duration duration = Duration.between(waktuMulai, waktuSelesai);
        return duration.toMinutes() / MINUTES_PER_HOUR;
    }

    private String getMahasiswaName(Mahasiswa mahasiswa) {
        return mahasiswa != null ? mahasiswa.getNamaLengkap() : "Unknown Student";
    }

    private String getMataKuliahName(MataKuliah mataKuliah) {
        return mataKuliah != null ? mataKuliah.getNamaMataKuliah() : "Unknown Course";
    }

    private String getMataKuliahCode(MataKuliah mataKuliah) {
        return mataKuliah != null ? mataKuliah.getKodeMataKuliah() : "Unknown Code";
    }

    private TipeKategori parseKategoriFromString(String kategori) {
        if (!StringUtils.hasText(kategori)) {
            log.warn("Empty kategori string, returning default value");
            return TipeKategori.values()[0];
        }

        try {
            return TipeKategori.valueOf(kategori.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("Invalid kategori value: {}, returning default", kategori);
            return TipeKategori.values()[0];
        }
    }

    private StatusLog parseStatusFromString(String status) {
        if (!StringUtils.hasText(status)) {
            log.warn("Empty status string, returning default value");
            return StatusLog.values()[0];
        }

        try {
            return StatusLog.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("Invalid status value: {}, returning default", status);
            return StatusLog.values()[0];
        }
    }
}