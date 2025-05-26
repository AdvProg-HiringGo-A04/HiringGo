package id.ac.ui.cs.advprog.hiringgo.exception;

import id.ac.ui.cs.advprog.hiringgo.daftarLowongan.exception.EntityNotFoundException;
import id.ac.ui.cs.advprog.hiringgo.daftarLowongan.exception.InvalidDataException;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.exception.InvalidLogException;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.exception.LogNotFoundException;
import id.ac.ui.cs.advprog.hiringgo.model.WebResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GlobalExceptionHandlerTest {

    @Test
    void testHandleGenericException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        Exception ex = new Exception("Something went wrong");

        ResponseEntity<WebResponse<?>> response = handler.handleGenericException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertEquals("Internal server error", response.getBody().getErrors());
    }

    @Test
    void testHandleConstraintViolation() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ConstraintViolation<?> violation1 = mock(ConstraintViolation.class);
        ConstraintViolation<?> violation2 = mock(ConstraintViolation.class);

        when(violation1.getMessage()).thenReturn("Must not blank");
        when(violation2.getMessage()).thenReturn("Must not blank");

        Set<ConstraintViolation<?>> violations = Set.of(violation1, violation2);
        ConstraintViolationException ex = new ConstraintViolationException(violations);

        ResponseEntity<WebResponse<?>> response = handler.handleConstraintViolation(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertEquals("Must not blank; Must not blank", response.getBody().getErrors());
    }

    @Test
    void testHandleResponseStatusException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ResponseStatusException ex = new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found");

        ResponseEntity<WebResponse<?>> response = handler.handleResponseStatusException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertEquals("Resource not found", response.getBody().getErrors());
    }

    @Test
    void testHandleMethodNotAllowed() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        HttpRequestMethodNotSupportedException ex =
                new HttpRequestMethodNotSupportedException("POST", Set.of("GET", "PUT"));

        ResponseEntity<WebResponse<?>> response = handler.handleMethodNotAllowed(ex);

        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertEquals("Method not allowed: POST", response.getBody().getErrors());
    }

    @Test
    void testHandleHttpMessageNotReadable() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        HttpInputMessage mockInputMessage = mock(HttpInputMessage.class);
        Exception cause = new RuntimeException("JSON parse error");

        HttpMessageNotReadableException ex =
                new HttpMessageNotReadableException("Malformed request", cause, mockInputMessage);

        ResponseEntity<WebResponse<?>> response = handler.handleHttpMessageNotReadable(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertEquals("Request body is missing or malformed", response.getBody().getErrors());
    }

    @Test
    void testHandleMissingParams() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        MissingServletRequestParameterException ex = mock(MissingServletRequestParameterException.class);

        when(ex.getParameterName()).thenReturn("param1");

        ResponseEntity<WebResponse<?>> response = handler.handleMissingParams(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertEquals("Required parameter 'param1'", response.getBody().getErrors());
    }

    @Test
    void testHandleMethodArgumentTypeMismatch() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        MethodArgumentTypeMismatchException ex = mock(MethodArgumentTypeMismatchException.class);

        when(ex.getName()).thenReturn("param1");

        ResponseEntity<WebResponse<?>> response = handler.handleTypeMismatch(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertEquals("Parameter 'param1' is invalid", response.getBody().getErrors());
    }

    @Test
    void testHandleInvalidDataException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        InvalidDataException ex = mock(InvalidDataException.class);
        Map<String, String> errors = Map.of("field1", "Invalid value", "field2", "Cannot be empty");
        when(ex.getErrors()).thenReturn(errors);

        ResponseEntity<Map<String, Object>> response = handler.handleInvalidDataException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Data tidak valid", response.getBody().get("message"));
        assertEquals(errors, response.getBody().get("errors"));
    }

    @Test
    void testHandleEntityNotFoundException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        EntityNotFoundException ex = mock(EntityNotFoundException.class);
        Map<String, String> errors = Map.of("entity", "Entity not found");
        when(ex.getErrors()).thenReturn(errors);

        ResponseEntity<Map<String, Object>> response = handler.handleEntityNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Data tidak ditemukan", response.getBody().get("message"));
        assertEquals(errors, response.getBody().get("errors"));
    }

    @Test
    void testHandleLogNotFoundException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        LogNotFoundException ex = mock(LogNotFoundException.class);
        Map<String, String> errors = Map.of("log", "Log entry not found");
        when(ex.getErrors()).thenReturn(errors);

        ResponseEntity<Map<String, Object>> response = handler.handleLogNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Validasi log gagal", response.getBody().get("message"));
        assertEquals(errors, response.getBody().get("errors"));
    }

    @Test
    void testHandleInvalidLogException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        InvalidLogException ex = mock(InvalidLogException.class);
        Map<String, String> errors = Map.of("log", "Invalid log entry");
        when(ex.getErrors()).thenReturn(errors);

        ResponseEntity<Map<String, Object>> response = handler.handleInvalidLogException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Validasi log gagal", response.getBody().get("message"));
        assertEquals(errors, response.getBody().get("errors"));
    }
}