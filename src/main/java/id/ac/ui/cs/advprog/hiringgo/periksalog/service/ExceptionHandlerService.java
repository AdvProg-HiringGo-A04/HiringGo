package id.ac.ui.cs.advprog.hiringgo.periksalog.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExceptionHandlerService {

    private static final String DEFAULT_ERROR_MESSAGE = "An unexpected error occurred";

    public String extractErrorMessage(Throwable throwable, String context) {
        if (throwable == null) {
            log.warn("Null throwable received for context: {}", context);
            return DEFAULT_ERROR_MESSAGE;
        }

        String message = throwable.getMessage();
        if (message == null || message.trim().isEmpty()) {
            log.warn("Empty error message for context: {}, throwable type: {}",
                    context, throwable.getClass().getSimpleName());
            return context + ": " + DEFAULT_ERROR_MESSAGE;
        }

        return context + ": " + message;
    }

    public String extractErrorMessage(Exception exception, String context) {
        if (exception == null) {
            log.warn("Null exception received for context: {}", context);
            return DEFAULT_ERROR_MESSAGE;
        }

        String message = exception.getMessage();
        if (message == null || message.trim().isEmpty()) {
            log.warn("Empty error message for context: {}, exception type: {}",
                    context, exception.getClass().getSimpleName());
            return context + ": " + DEFAULT_ERROR_MESSAGE;
        }

        return context + ": " + message;
    }

    public boolean isSecurityException(Throwable throwable) {
        return throwable != null && throwable.getCause() instanceof SecurityException;
    }

    public void logSecurityException(String logId, Throwable throwable) {
        log.warn("Security exception while processing logId: {}", logId, throwable);
    }

    public void logGeneralException(String logId, Throwable throwable) {
        log.error("Error occurred while processing logId: {}", logId, throwable);
    }

    public void logAuthenticationError(String operation, Exception exception) {
        log.error("Authentication error for operation: {}", operation, exception);
    }
}