package id.ac.ui.cs.advprog.hiringgo.periksalog.util;

import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.model.WebResponse;
import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ResponseBuilder {

    public <T> ResponseEntity<WebResponse<T>> createForbiddenResponse(String message) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(WebResponse.<T>builder()
                        .errors(message)
                        .build());
    }

    public <T> ResponseEntity<WebResponse<T>> createUnauthorizedResponse(String message) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(WebResponse.<T>builder()
                        .errors(message)
                        .build());
    }

    public ResponseEntity<WebResponse<List<LogDTO>>> createSuccessResponse(List<LogDTO> logs) {
        log.info("Successfully fetched {} logs", logs != null ? logs.size() : 0);
        return ResponseEntity.ok(WebResponse.<List<LogDTO>>builder()
                .data(logs)
                .build());
    }

    public ResponseEntity<WebResponse<LogDTO>> createSuccessResponse(LogDTO logDTO) {
        return ResponseEntity.ok(WebResponse.<LogDTO>builder()
                .data(logDTO)
                .build());
    }

    public ResponseEntity<WebResponse<List<LogDTO>>> createInternalServerErrorResponse(String errorMessage) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(WebResponse.<List<LogDTO>>builder()
                        .errors(errorMessage)
                        .build());
    }

    public ResponseEntity<WebResponse<LogDTO>> createInternalServerErrorResponseForLog(String errorMessage) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(WebResponse.<LogDTO>builder()
                        .errors(errorMessage)
                        .build());
    }

    public ResponseEntity<WebResponse<LogDTO>> createSecurityExceptionResponse(String message) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(WebResponse.<LogDTO>builder()
                        .errors(message)
                        .build());
    }

    public ResponseEntity<WebResponse<List<LogDTO>>> handleException(Exception exception) {
        log.error("Error occurred while fetching logs", exception);
        String errorMessage = getErrorMessage(exception, "An error occurred while fetching logs");
        return createInternalServerErrorResponse(errorMessage);
    }

    public ResponseEntity<WebResponse<LogDTO>> handleAsyncUpdateException(Throwable throwable, String logId) {
        if (throwable.getCause() instanceof SecurityException) {
            log.warn("Async: Security exception while updating log status for logId: {}", logId, throwable);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(WebResponse.<LogDTO>builder()
                            .errors(throwable.getCause().getMessage())
                            .build());
        }

        log.error("Async: Error occurred while updating log status for logId: {}", logId, throwable);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(WebResponse.<LogDTO>builder()
                        .errors(getErrorMessage(throwable, "An error occurred while updating log status"))
                        .build());
    }

    public String getUserIdSafely(User user) {
        return user != null ? user.getId() : "unknown";
    }

    public String getUserRoleSafely(User user) {
        return user != null ? user.getRole().name() : "unknown";
    }

    public String getErrorMessage(Throwable throwable, String defaultMessage) {
        if (throwable.getMessage() != null) {
            return defaultMessage + ": " + throwable.getMessage();
        }
        return defaultMessage;
    }

    public String getErrorMessage(Exception exception, String defaultMessage) {
        if (exception.getMessage() != null) {
            return defaultMessage + ": " + exception.getMessage();
        }
        return defaultMessage;
    }
}