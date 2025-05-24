package id.ac.ui.cs.advprog.hiringgo.exception;

import id.ac.ui.cs.advprog.hiringgo.model.WebResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.server.ResponseStatusException;

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
}