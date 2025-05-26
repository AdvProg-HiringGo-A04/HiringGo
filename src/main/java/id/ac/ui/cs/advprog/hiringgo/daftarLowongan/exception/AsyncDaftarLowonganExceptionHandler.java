package id.ac.ui.cs.advprog.hiringgo.daftarLowongan.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;

import id.ac.ui.cs.advprog.hiringgo.common.ApiResponse;

import java.util.Map;
import java.util.concurrent.CompletionException;

@Slf4j
@ControllerAdvice
public class AsyncDaftarLowonganExceptionHandler {

    @ExceptionHandler(CompletionException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleCompletionException(CompletionException ex) {
        log.error("Async operation failed", ex);

        Throwable cause = ex.getCause();
        if (cause instanceof EntityNotFoundException) {
            EntityNotFoundException enfe = (EntityNotFoundException) cause;
            return new ResponseEntity<>(
                    new ApiResponse<>("Pendaftaran gagal: " + enfe.getMessage(), enfe.getErrors()),
                    HttpStatus.NOT_FOUND
            );
        }

        if (cause instanceof InvalidDataException) {
            InvalidDataException ide = (InvalidDataException) cause;
            return new ResponseEntity<>(
                    new ApiResponse<>("Data tidak valid: " + ide.getMessage(), ide.getErrors()),
                    HttpStatus.BAD_REQUEST
            );
        }

        return new ResponseEntity<>(
                new ApiResponse<>("Terjadi kesalahan saat memproses pendaftaran", null),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(AsyncRequestTimeoutException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleAsyncTimeout(AsyncRequestTimeoutException ex) {
        log.error("Async request timeout", ex);

        return new ResponseEntity<>(
                new ApiResponse<>("Permintaan timeout. Silakan coba lagi.", null),
                HttpStatus.REQUEST_TIMEOUT
        );
    }
}