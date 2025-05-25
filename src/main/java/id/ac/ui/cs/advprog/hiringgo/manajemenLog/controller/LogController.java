package id.ac.ui.cs.advprog.hiringgo.manajemenLog.controller;

import id.ac.ui.cs.advprog.hiringgo.enums.Role;
import id.ac.ui.cs.advprog.hiringgo.security.CurrentUserProvider;
import id.ac.ui.cs.advprog.hiringgo.security.annotation.AllowedRoles;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import id.ac.ui.cs.advprog.hiringgo.common.ApiResponse;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.dto.LogRequest;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.dto.LogResponse;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.service.LogService;

import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin(origins = "http://localhost:8000", allowedHeaders = "*")
@RestController
@RequestMapping("/api/log")
@RequiredArgsConstructor
public class LogController {
    private final LogService logService;
    private final CurrentUserProvider currentUserProvider;

    // list log per matkul
    @AllowedRoles({Role.MAHASISWA})
    @GetMapping("/matakuliah/{lowonganId}")
    public ResponseEntity<ApiResponse<List<LogResponse>>> getAllLogs(
            @PathVariable("lowonganId") String lowonganId) {

        String mahasiswaId = currentUserProvider.getCurrentUserId();
        List<LogResponse> logs = logService.getAllLogs(lowonganId, mahasiswaId);

        log.info("User with email '{}' and role '{}' fetched all logs for mata kuliah '{}'",
                currentUserProvider.getCurrentUserEmail(), currentUserProvider.getCurrentUserRole(), lowonganId);

        return ResponseEntity.ok(new ApiResponse<>("Successfully fetched all logs", logs));
    }

    @AllowedRoles({Role.MAHASISWA})
    @GetMapping("/{logId}")
    public ResponseEntity<ApiResponse<LogResponse>> getLogById(
            @PathVariable("logId") String logId) {

        String mahasiswaId = currentUserProvider.getCurrentUserId();
        LogResponse logs = logService.getLogById(logId, mahasiswaId);

        log.info("User with email '{}' and role '{}' fetched log '{}'",
                currentUserProvider.getCurrentUserEmail(), currentUserProvider.getCurrentUserRole(), logId);

        return ResponseEntity.ok(new ApiResponse<>("Successfully fetched log", logs));
    }

    @AllowedRoles({Role.MAHASISWA})
    @PostMapping
    public ResponseEntity<ApiResponse<LogResponse>> createLog(
            @Valid @RequestBody LogRequest logRequest) {

        String mahasiswaId = currentUserProvider.getCurrentUserId();
        LogResponse createdLog = logService.createLog(logRequest, mahasiswaId);

        log.info("User with email '{}' and role '{}' created log",
                currentUserProvider.getCurrentUserEmail(), currentUserProvider.getCurrentUserRole());

        return new ResponseEntity<>(new ApiResponse<>("Successfully created log", createdLog), HttpStatus.CREATED);
    }

    @AllowedRoles({Role.MAHASISWA})
    @PutMapping("/{logId}")
    public ResponseEntity<ApiResponse<LogResponse>> updateLog(
            @PathVariable("logId") String logId,
            @Valid @RequestBody LogRequest logRequest) {

        String mahasiswaId = currentUserProvider.getCurrentUserId();
        LogResponse updatedLog = logService.updateLog(logId, logRequest, mahasiswaId);

        log.info("User with email '{}' and role '{}' updated log '{}'",
                currentUserProvider.getCurrentUserEmail(), currentUserProvider.getCurrentUserRole(), logId);

        return ResponseEntity.ok(new ApiResponse<>("Successfully updated log", updatedLog));
    }

    @AllowedRoles({Role.MAHASISWA})
    @DeleteMapping("{lowonganId}/{logId}")
    public ResponseEntity<ApiResponse<String>> deleteLog(
            @PathVariable("lowonganId") String lowonganId,
            @PathVariable("logId") String logId) {

        String mahasiswaId = currentUserProvider.getCurrentUserId();
        logService.deleteLog(logId, lowonganId, mahasiswaId);

        log.info("User with email '{}' and role '{}' deleted log '{}' from mata kuliah '{}'",
                currentUserProvider.getCurrentUserEmail(), currentUserProvider.getCurrentUserRole(), logId, lowonganId);

        return ResponseEntity.ok(new ApiResponse<>("Successfully deleted log", null));
    }

    @AllowedRoles({Role.MAHASISWA})
    @GetMapping("/total/{lowonganId}")
    public Map<String, Double> getTotalJamPerBulan(
            @PathVariable("lowonganId") String lowonganId) {

        String mahasiswaId = currentUserProvider.getCurrentUserId();

        log.info("User with email '{}' and role '{}' fetched total jam per bulan for mata kuliah '{}'",
                currentUserProvider.getCurrentUserEmail(), currentUserProvider.getCurrentUserRole(), lowonganId);

        return logService.getTotalJamPerBulan(lowonganId, mahasiswaId);
    }
    
}
