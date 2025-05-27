package id.ac.ui.cs.advprog.hiringgo.periksalog.util;

import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.enums.Role;
import id.ac.ui.cs.advprog.hiringgo.model.WebResponse;
import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResponseBuilderTest {

    private ResponseBuilder responseBuilder;

    @BeforeEach
    void setUp() {
        responseBuilder = new ResponseBuilder();
    }

    @Test
    void testCreateForbiddenResponse() {
        String message = "Access denied";
        ResponseEntity<WebResponse<String>> response = responseBuilder.createForbiddenResponse(message);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals(message, response.getBody().getErrors());
        assertNull(response.getBody().getData());
    }

    @Test
    void testCreateUnauthorizedResponse() {
        String message = "Unauthorized access";
        ResponseEntity<WebResponse<String>> response = responseBuilder.createUnauthorizedResponse(message);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(message, response.getBody().getErrors());
        assertNull(response.getBody().getData());
    }

    @Test
    void testCreateSuccessResponseWithLogsList() {
        LogDTO log1 = LogDTO.builder().id("1").judul("Test Log 1").build();
        LogDTO log2 = LogDTO.builder().id("2").judul("Test Log 2").build();
        List<LogDTO> logs = Arrays.asList(log1, log2);

        ResponseEntity<WebResponse<List<LogDTO>>> response = responseBuilder.createSuccessResponse(logs);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(logs, response.getBody().getData());
        assertNull(response.getBody().getErrors());
    }

    @Test
    void testCreateSuccessResponseWithLogsListNull() {
        ResponseEntity<WebResponse<List<LogDTO>>> response = responseBuilder.createSuccessResponse((List<LogDTO>) null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody().getData());
        assertNull(response.getBody().getErrors());
    }

    @Test
    void testCreateSuccessResponseWithSingleLog() {
        LogDTO log = LogDTO.builder().id("1").judul("Test Log").build();

        ResponseEntity<WebResponse<LogDTO>> response = responseBuilder.createSuccessResponse(log);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(log, response.getBody().getData());
        assertNull(response.getBody().getErrors());
    }

    @Test
    void testCreateInternalServerErrorResponse() {
        String errorMessage = "Internal server error";
        ResponseEntity<WebResponse<List<LogDTO>>> response = responseBuilder.createInternalServerErrorResponse(errorMessage);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(errorMessage, response.getBody().getErrors());
        assertNull(response.getBody().getData());
    }

    @Test
    void testCreateInternalServerErrorResponseForLog() {
        String errorMessage = "Internal server error for log";
        ResponseEntity<WebResponse<LogDTO>> response = responseBuilder.createInternalServerErrorResponseForLog(errorMessage);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(errorMessage, response.getBody().getErrors());
        assertNull(response.getBody().getData());
    }

    @Test
    void testCreateSecurityExceptionResponse() {
        String message = "Security exception";
        ResponseEntity<WebResponse<LogDTO>> response = responseBuilder.createSecurityExceptionResponse(message);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals(message, response.getBody().getErrors());
        assertNull(response.getBody().getData());
    }

    @Test
    void testHandleException() {
        Exception exception = new RuntimeException("Test error");
        ResponseEntity<WebResponse<List<LogDTO>>> response = responseBuilder.handleException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().getErrors().contains("An error occurred while fetching logs"));
        assertTrue(response.getBody().getErrors().contains("Test error"));
        assertNull(response.getBody().getData());
    }

    @Test
    void testHandleExceptionWithNullMessage() {
        Exception exception = new RuntimeException((String) null);
        ResponseEntity<WebResponse<List<LogDTO>>> response = responseBuilder.handleException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while fetching logs", response.getBody().getErrors());
        assertNull(response.getBody().getData());
    }

    @Test
    void testHandleAsyncUpdateExceptionWithSecurityException() {
        String logId = "test-log-id";
        SecurityException securityException = new SecurityException("Access denied");
        RuntimeException throwable = new RuntimeException(securityException);

        ResponseEntity<WebResponse<LogDTO>> response = responseBuilder.handleAsyncUpdateException(throwable, logId);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Access denied", response.getBody().getErrors());
        assertNull(response.getBody().getData());
    }

    @Test
    void testHandleAsyncUpdateExceptionWithOtherException() {
        String logId = "test-log-id";
        RuntimeException throwable = new RuntimeException("General error");

        ResponseEntity<WebResponse<LogDTO>> response = responseBuilder.handleAsyncUpdateException(throwable, logId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().getErrors().contains("An error occurred while updating log status"));
        assertTrue(response.getBody().getErrors().contains("General error"));
        assertNull(response.getBody().getData());
    }

    @Test
    void testHandleAsyncUpdateExceptionWithNullMessage() {
        String logId = "test-log-id";
        RuntimeException throwable = new RuntimeException((String) null);

        ResponseEntity<WebResponse<LogDTO>> response = responseBuilder.handleAsyncUpdateException(throwable, logId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while updating log status", response.getBody().getErrors());
        assertNull(response.getBody().getData());
    }

    @Test
    void testGetUserIdSafely() {
        User user = new User();
        user.setId("user-123");

        assertEquals("user-123", responseBuilder.getUserIdSafely(user));
        assertEquals("unknown", responseBuilder.getUserIdSafely(null));
    }

    @Test
    void testGetUserRoleSafely() {
        User user = new User();
        user.setRole(Role.DOSEN);

        assertEquals("DOSEN", responseBuilder.getUserRoleSafely(user));
        assertEquals("unknown", responseBuilder.getUserRoleSafely(null));
    }

    @Test
    void testGetErrorMessageWithThrowable() {
        Throwable throwable = new RuntimeException("Test error");
        String result = responseBuilder.getErrorMessage(throwable, "Default message");

        assertEquals("Default message: Test error", result);
    }

    @Test
    void testGetErrorMessageWithThrowableAndNullMessage() {
        Throwable throwable = new RuntimeException((String) null);
        String result = responseBuilder.getErrorMessage(throwable, "Default message");

        assertEquals("Default message", result);
    }

    @Test
    void testGetErrorMessageWithException() {
        Exception exception = new RuntimeException("Test error");
        String result = responseBuilder.getErrorMessage(exception, "Default message");

        assertEquals("Default message: Test error", result);
    }

    @Test
    void testGetErrorMessageWithExceptionAndNullMessage() {
        Exception exception = new RuntimeException((String) null);
        String result = responseBuilder.getErrorMessage(exception, "Default message");

        assertEquals("Default message", result);
    }
}
