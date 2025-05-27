package id.ac.ui.cs.advprog.hiringgo.periksalog.service;

import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogStatusUpdateDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class ValidationService {

    private static final String PERMISSION_DENIED_MESSAGE = "You don't have permission to update this log";

    public void validateDosenId(String dosenId) {
        if (!StringUtils.hasText(dosenId)) {
            throw new IllegalArgumentException("Dosen ID cannot be null or empty");
        }
    }

    public void validateLogId(String logId) {
        if (!StringUtils.hasText(logId)) {
            throw new IllegalArgumentException("Log ID cannot be null or empty");
        }
    }

    public void validateLogStatusUpdateDTO(LogStatusUpdateDTO logStatusUpdateDTO) {
        if (logStatusUpdateDTO == null) {
            throw new IllegalArgumentException("LogStatusUpdateDTO cannot be null");
        }

        validateLogId(logStatusUpdateDTO.getLogId());

        if (logStatusUpdateDTO.getStatus() == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
    }

    public void validateInputs(String dosenId, LogStatusUpdateDTO logStatusUpdateDTO) {
        validateDosenId(dosenId);
        validateLogStatusUpdateDTO(logStatusUpdateDTO);
    }

    public void validateDosenOwnership(String logId, String dosenId, PeriksaLogService logService) {
        if (!logService.isLogOwnedByDosen(logId, dosenId)) {
            log.warn("Permission denied: Dosen {} attempted to access log {}", dosenId, logId);
            throw new SecurityException(PERMISSION_DENIED_MESSAGE);
        }
    }
}