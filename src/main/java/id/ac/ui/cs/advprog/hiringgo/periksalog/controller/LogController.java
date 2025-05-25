package id.ac.ui.cs.advprog.hiringgo.periksalog.controller;

import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogDTO;
import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogStatusUpdateDTO;
import id.ac.ui.cs.advprog.hiringgo.periksalog.service.LogService;
import id.ac.ui.cs.advprog.hiringgo.model.WebResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class LogController {
    private final LogService logService;

    @GetMapping
    public CompletableFuture<ResponseEntity<WebResponse<List<LogDTO>>>> getAllLogs(@AuthenticationPrincipal User user) {
        if (!isDosenUser(user)) {
            return CompletableFuture.completedFuture(
                    ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(WebResponse.<List<LogDTO>>builder()
                                    .errors("Access forbidden: Only lecturers can access logs")
                                    .build())
            );
        }

        return logService.getAllLogsByDosenIdAsync(user.getId())
                .thenApply(logs -> {
                    if (logs.isEmpty()) {
                        return ResponseEntity.ok(WebResponse.<List<LogDTO>>builder()
                                .data(logs)
                                .build());
                    }
                    return ResponseEntity.ok(WebResponse.<List<LogDTO>>builder()
                            .data(logs)
                            .build());
                })
                .exceptionally(e -> {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(WebResponse.<List<LogDTO>>builder()
                                    .errors("An error occurred while fetching logs: " + e.getCause().getMessage())
                                    .build());
                });
    }

    @PutMapping("/{logId}/status")
    public CompletableFuture<ResponseEntity<WebResponse<LogDTO>>> updateLogStatus(
            @AuthenticationPrincipal User user,
            @PathVariable("logId") String logId,
            @RequestBody LogStatusUpdateDTO logStatusUpdateDTO) {

        if (!isDosenUser(user)) {
            return CompletableFuture.completedFuture(
                    ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(WebResponse.<LogDTO>builder()
                                    .errors("Access forbidden: Only lecturers can update log status")
                                    .build())
            );
        }

        // Ensure the logId in path matches the one in request body
        logStatusUpdateDTO.setLogId(logId);

        return logService.updateLogStatusAsync(user.getId(), logStatusUpdateDTO)
                .thenApply(updatedLog -> {
                    return ResponseEntity.ok(WebResponse.<LogDTO>builder()
                            .data(updatedLog)
                            .build());
                })
                .exceptionally(e -> {
                    Throwable cause = e.getCause();
                    if (cause instanceof SecurityException) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body(WebResponse.<LogDTO>builder()
                                        .errors(cause.getMessage())
                                        .build());
                    } else {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(WebResponse.<LogDTO>builder()
                                        .errors("An error occurred while updating log status: " + cause.getMessage())
                                        .build());
                    }
                });
    }

    private boolean isDosenUser(User user) {
        return user != null && "DOSEN".equals(user.getRole());
    }
}