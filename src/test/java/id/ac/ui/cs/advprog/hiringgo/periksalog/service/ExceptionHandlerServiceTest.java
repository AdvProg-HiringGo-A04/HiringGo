package id.ac.ui.cs.advprog.hiringgo.periksalog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExceptionHandlerServiceTest {

    @InjectMocks
    private ExceptionHandlerService exceptionHandlerService;

    @Test
    void extractErrorMessage_WithThrowableAndMessage_ReturnsFormattedMessage() {
        Throwable throwable = new RuntimeException("Test error");
        String context = "Test context";

        String result = exceptionHandlerService.extractErrorMessage(throwable, context);

        assertEquals("Test context: Test error", result);
    }

    @Test
    void extractErrorMessage_WithNullThrowable_ReturnsDefaultMessage() {
        String context = "Test context";

        String result = exceptionHandlerService.extractErrorMessage((Throwable) null, context);

        assertEquals("An unexpected error occurred", result);
    }

    @Test
    void extractErrorMessage_WithEmptyMessage_ReturnsContextWithDefault() {
        Throwable throwable = new RuntimeException("");
        String context = "Test context";

        String result = exceptionHandlerService.extractErrorMessage(throwable, context);

        assertEquals("Test context: An unexpected error occurred", result);
    }

    @Test
    void extractErrorMessage_WithWhitespaceMessage_ReturnsContextWithDefault() {
        Throwable throwable = new RuntimeException("   ");
        String context = "Test context";

        String result = exceptionHandlerService.extractErrorMessage(throwable, context);

        assertEquals("Test context: An unexpected error occurred", result);
    }

    @Test
    void extractErrorMessage_WithNullMessage_ReturnsContextWithDefault() {
        Throwable throwable = new RuntimeException((String) null);
        String context = "Test context";

        String result = exceptionHandlerService.extractErrorMessage(throwable, context);

        assertEquals("Test context: An unexpected error occurred", result);
    }

    @Test
    void extractErrorMessage_WithExceptionAndMessage_ReturnsFormattedMessage() {
        Exception exception = new IllegalArgumentException("Test exception");
        String context = "Test context";

        String result = exceptionHandlerService.extractErrorMessage(exception, context);

        assertEquals("Test context: Test exception", result);
    }

    @Test
    void extractErrorMessage_WithNullException_ReturnsDefaultMessage() {
        String context = "Test context";

        String result = exceptionHandlerService.extractErrorMessage((Exception) null, context);

        assertEquals("An unexpected error occurred", result);
    }

    @Test
    void extractErrorMessage_WithExceptionEmptyMessage_ReturnsContextWithDefault() {
        Exception exception = new IllegalArgumentException("");
        String context = "Test context";

        String result = exceptionHandlerService.extractErrorMessage(exception, context);

        assertEquals("Test context: An unexpected error occurred", result);
    }

    @Test
    void isSecurityException_WithSecurityException_ReturnsTrue() {
        Throwable throwable = new RuntimeException(new SecurityException("Security error"));

        boolean result = exceptionHandlerService.isSecurityException(throwable);

        assertTrue(result);
    }

    @Test
    void isSecurityException_WithNonSecurityException_ReturnsFalse() {
        Throwable throwable = new RuntimeException("Regular error");

        boolean result = exceptionHandlerService.isSecurityException(throwable);

        assertFalse(result);
    }

    @Test
    void isSecurityException_WithNullThrowable_ReturnsFalse() {
        boolean result = exceptionHandlerService.isSecurityException(null);

        assertFalse(result);
    }

    @Test
    void isSecurityException_WithNullCause_ReturnsFalse() {
        Throwable throwable = new RuntimeException();

        boolean result = exceptionHandlerService.isSecurityException(throwable);

        assertFalse(result);
    }

    @Test
    void logSecurityException_WithValidParameters_LogsWarning() {
        String logId = "log123";
        Throwable throwable = new RuntimeException(new SecurityException("Security error"));

        // This method only logs, so we just verify it doesn't throw exceptions
        assertDoesNotThrow(() -> exceptionHandlerService.logSecurityException(logId, throwable));
    }

    @Test
    void logGeneralException_WithValidParameters_LogsError() {
        String logId = "log123";
        Throwable throwable = new RuntimeException("General error");

        // This method only logs, so we just verify it doesn't throw exceptions
        assertDoesNotThrow(() -> exceptionHandlerService.logGeneralException(logId, throwable));
    }

    @Test
    void logAuthenticationError_WithValidParameters_LogsError() {
        String operation = "user authentication";
        Exception exception = new IllegalArgumentException("Auth error");

        // This method only logs, so we just verify it doesn't throw exceptions
        assertDoesNotThrow(() -> exceptionHandlerService.logAuthenticationError(operation, exception));
    }
}