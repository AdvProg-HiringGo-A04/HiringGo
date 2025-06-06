package id.ac.ui.cs.advprog.hiringgo.exception;

import id.ac.ui.cs.advprog.hiringgo.daftarLowongan.exception.EntityNotFoundException;
import id.ac.ui.cs.advprog.hiringgo.daftarLowongan.exception.InvalidDataException;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.exception.InvalidLogException;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.exception.LogNotFoundException;
import id.ac.ui.cs.advprog.hiringgo.model.WebResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<WebResponse<?>> handleConstraintViolation(ConstraintViolationException ex) {
        String errors = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));

        WebResponse<?> response = WebResponse.builder()
                .errors(errors)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<WebResponse<?>> handleResponseStatusException(ResponseStatusException ex) {
        WebResponse<?> response = WebResponse.builder()
                .errors(ex.getReason())
                .build();

        return new ResponseEntity<>(response, ex.getStatusCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<WebResponse<?>> handleGenericException(Exception ex) {
        WebResponse<?> response = WebResponse.builder()
                .errors("Internal server error")
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<WebResponse<?>> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        WebResponse<?> response = WebResponse.builder()
                .errors("Method not allowed: " + ex.getMethod())
                .build();

        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<WebResponse<?>> handleMissingParams(MissingServletRequestParameterException ex) {
        String name = ex.getParameterName();
        String message = "Required parameter '" + name + "'";
        WebResponse<?> response = WebResponse.builder()
                .errors(message)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<WebResponse<?>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String paramName = ex.getName();
        String message = "Parameter '" + paramName + "' is invalid";
        WebResponse<?> response = WebResponse.builder()
                .errors(message)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<WebResponse<?>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        WebResponse<?> response = WebResponse.builder()
                .errors("Request body is missing or malformed")
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LogNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleLogNotFoundException(LogNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Validasi log gagal");
        response.put("errors", ex.getErrors());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidLogException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidLogException(InvalidLogException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Validasi log gagal");
        response.put("errors", ex.getErrors());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidDataException(InvalidDataException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Data tidak valid");
        response.put("errors", ex.getErrors());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFoundException(EntityNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Data tidak ditemukan");
        response.put("errors", ex.getErrors());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<WebResponse<?>> handleValidationException(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        WebResponse<?> response = WebResponse.builder()
                .errors(errors)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
