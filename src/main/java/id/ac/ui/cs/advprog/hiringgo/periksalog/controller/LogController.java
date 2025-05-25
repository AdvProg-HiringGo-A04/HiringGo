package id.ac.ui.cs.advprog.hiringgo.periksalog.controller;

import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.enums.Role;
import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogDTO;
import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogStatusUpdateDTO;
import id.ac.ui.cs.advprog.hiringgo.periksalog.service.LogService;
import id.ac.ui.cs.advprog.hiringgo.model.WebResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
@Validated
public class LogController {

    private static final String ACCESS_FORBIDDEN_MESSAGE = "Access forbidden: Only lecturers can access logs";
    private static final String UPDATE_FORBIDDEN_MESSAGE = "Access forbidden: Only lecturers can update log status";

    private final LogService logService;

    @GetMapping
    public ResponseEntity<WebResponse<List<LogDTO>>> getAllLogs(
            @AuthenticationPrincipal User user) {
        log.info("Attempting to fetch all logs for user: {}", getUserIdSafely(user));

        if (!isDosenUser(user)) {
            log.warn("Unauthorized access attempt by user: {} with role: {}",
                    getUserIdSafely(user), getUserRoleSafely(user));
            return createForbiddenResponse(ACCESS_FORBIDDEN_MESSAGE);
        }

        try {
            List<LogDTO> logs = logService.getAllLogsByDosenId(user.getId());
            return createSuccessResponse(logs);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @PutMapping("/{logId}/status")
    public ResponseEntity<WebResponse<LogDTO>> updateLogStatus(
            @AuthenticationPrincipal User user,
            @PathVariable("logId") @NotBlank String logId,
            @RequestBody @Valid LogStatusUpdateDTO logStatusUpdateDTO) {

        log.info("Attempting to update log status for logId: {} by user: {}",
                logId, getUserIdSafely(user));

        if (!isDosenUser(user)) {
            log.warn("Unauthorized status update attempt by user: {} with role: {} for logId: {}",
                    getUserIdSafely(user), getUserRoleSafely(user), logId);
            return createForbiddenResponse(UPDATE_FORBIDDEN_MESSAGE);
        }

        logStatusUpdateDTO.setLogId(logId);

        try {
            LogDTO updatedLog = logService.updateLogStatus(user.getId(), logStatusUpdateDTO);
            log.info("Successfully updated log status for logId: {} to status: {}",
                    logId, logStatusUpdateDTO.getStatus());
            return createSuccessResponse(updatedLog);
        } catch (SecurityException e) {
            log.warn("Security exception while updating log status for logId: {}", logId, e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(WebResponse.<LogDTO>builder()
                            .errors(e.getMessage())
                            .build());
        } catch (Exception e) {
            log.error("Error occurred while updating log status for logId: {}", logId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(WebResponse.<LogDTO>builder()
                            .errors(getErrorMessage(e, "An error occurred while updating log status"))
                            .build());
        }
    }

    private boolean isDosenUser(User user) {
        return user != null && Role.DOSEN.equals(user.getRole());
    }

    private String getUserIdSafely(User user) {
        return user != null ? user.getId() : "unknown";
    }

    private String getUserRoleSafely(User user) {
        return user != null ? user.getRole().name() : "unknown";
    }

    private <T> ResponseEntity<WebResponse<T>> createForbiddenResponse(String message) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(WebResponse.<T>builder()
                        .errors(message)
                        .build());
    }

    private ResponseEntity<WebResponse<List<LogDTO>>> createSuccessResponse(List<LogDTO> logs) {
        log.info("Successfully fetched {} logs", logs != null ? logs.size() : 0);
        return ResponseEntity.ok(WebResponse.<List<LogDTO>>builder()
                .data(logs)
                .build());
    }

    private ResponseEntity<WebResponse<LogDTO>> createSuccessResponse(LogDTO logDTO) {
        return ResponseEntity.ok(WebResponse.<LogDTO>builder()
                .data(logDTO)
                .build());
    }

    private ResponseEntity<WebResponse<List<LogDTO>>> handleException(Exception exception) {
        log.error("Error occurred while fetching logs", exception);
        String errorMessage = getErrorMessage(exception, "An error occurred while fetching logs");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(WebResponse.<List<LogDTO>>builder()
                        .errors(errorMessage)
                        .build());
    }

    private String getErrorMessage(Exception exception, String defaultMessage) {
        if (exception.getMessage() != null) {
            return defaultMessage + ": " + exception.getMessage();
        }
        return defaultMessage;
    }
}