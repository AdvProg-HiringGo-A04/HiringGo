package id.ac.ui.cs.advprog.hiringgo.periksalog.controller;

import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogDTO;
import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogStatusUpdateDTO;
import id.ac.ui.cs.advprog.hiringgo.periksalog.service.LogService;
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
    public ResponseEntity<List<LogDTO>> getAllLogs(@AuthenticationPrincipal User user) {
        if (!isDosenUser(user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        try {
            List<LogDTO> logs = logService.getAllLogsByDosenId(user.getId());
            if (logs.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/status")
    public ResponseEntity<LogDTO> updateLogStatus(@AuthenticationPrincipal User user,
                                                  @RequestBody LogStatusUpdateDTO logStatusUpdateDTO) {
        if (!isDosenUser(user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        try {
            LogDTO updatedLog = logService.updateLogStatus(user.getId(), logStatusUpdateDTO);
            return ResponseEntity.ok(updatedLog);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private boolean isDosenUser(User user) {
        return user != null && "DOSEN".equals(user.getRole());
    }
}