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

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class LogController {
    private final LogService logService;

    @GetMapping
    public ResponseEntity<WebResponse<List<LogDTO>>> getAllLogs(@AuthenticationPrincipal User user) {
        if (!isDosenUser(user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(WebResponse.<List<LogDTO>>builder()
                            .errors("Access forbidden: Only lecturers can access logs")
                            .build());
        }

        try {
            List<LogDTO> logs = logService.getAllLogsByDosenId(user.getId());
            if (logs.isEmpty()) {
                return ResponseEntity.ok(WebResponse.<List<LogDTO>>builder()
                        .data(logs)
                        .build());
            }
            return ResponseEntity.ok(WebResponse.<List<LogDTO>>builder()
                    .data(logs)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(WebResponse.<List<LogDTO>>builder()
                            .errors("An error occurred while fetching logs: " + e.getMessage())
                            .build());
        }
    }

    @PutMapping("/{logId}/status")
    public ResponseEntity<WebResponse<LogDTO>> updateLogStatus(
            @AuthenticationPrincipal User user,
            @PathVariable("logId") String logId,
            @RequestBody LogStatusUpdateDTO logStatusUpdateDTO) {

        if (!isDosenUser(user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(WebResponse.<LogDTO>builder()
                            .errors("Access forbidden: Only lecturers can update log status")
                            .build());
        }

        // Ensure the logId in path matches the one in request body
        logStatusUpdateDTO.setLogId(logId);

        try {
            LogDTO updatedLog = logService.updateLogStatus(user.getId(), logStatusUpdateDTO);
            return ResponseEntity.ok(WebResponse.<LogDTO>builder()
                    .data(updatedLog)
                    .build());
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(WebResponse.<LogDTO>builder()
                            .errors(e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(WebResponse.<LogDTO>builder()
                            .errors("An error occurred while updating log status: " + e.getMessage())
                            .build());
        }
    }

    private boolean isDosenUser(User user) {
        return user != null && "DOSEN".equals(user.getRole());
    }
}