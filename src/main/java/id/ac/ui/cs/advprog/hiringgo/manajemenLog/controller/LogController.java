package id.ac.ui.cs.advprog.hiringgo.manajemenLog.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import id.ac.ui.cs.advprog.hiringgo.common.ApiResponse;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.dto.LogRequest;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.dto.LogResponse;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.service.LogService;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:8000", allowedHeaders = "*")
@RestController
@RequestMapping("/api/log")
@RequiredArgsConstructor
public class LogController {
    private final LogService logService;

    // list matkul yg dia asdosin (dari dashboard mahasiswa) == gaperlu bikin endpoint lagi atau... perlu..?

    // list log per matkul
    @GetMapping("/matakuliah/{mataKuliahId}")
    public ResponseEntity<ApiResponse<List<LogResponse>>> getAllLogs(
            @PathVariable("mataKuliahId") String mataKuliahId,
            @RequestHeader("X-Student-ID") String mahasiswaId) {
        
        List<LogResponse> logs = logService.getAllLogs(mataKuliahId, mahasiswaId);
        return ResponseEntity.ok(new ApiResponse<>("Successfully fetched all logs", logs));
    }
    
    @GetMapping("/{logId}")
    public ResponseEntity<ApiResponse<LogResponse>> getLogById(
            @PathVariable("logId") String logId,
            @RequestHeader("X-Student-ID") String mahasiswaId) {
        LogResponse log = logService.getLogById(logId, mahasiswaId);
        return ResponseEntity.ok(new ApiResponse<>("Successfully fetched log", log));
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<LogResponse>> createLog(
            @Valid @RequestBody LogRequest logRequest,
            @RequestHeader("X-Student-ID") String mahasiswaId) {
        
        LogResponse createdLog = logService.createLog(logRequest, mahasiswaId);
        return new ResponseEntity<>(new ApiResponse<>("Successfully created log", createdLog), HttpStatus.CREATED);
    }
    
    @PutMapping("/{logId}")
    public ResponseEntity<ApiResponse<LogResponse>> updateLog(
            @PathVariable("logId") String logId,
            @Valid @RequestBody LogRequest logRequest,
            @RequestHeader("X-Student-ID") String mahasiswaId) {
        
        LogResponse updatedLog = logService.updateLog(logId, logRequest, mahasiswaId);
        return ResponseEntity.ok(new ApiResponse<>("Successfully updated log", updatedLog));
    }
    
    @DeleteMapping("{mataKuliahId}/{logId}")
    public ResponseEntity<ApiResponse<String>> deleteLog(
            @PathVariable("mataKuliahId") String mataKuliahId,
            @PathVariable("logId") String logId,
            @RequestHeader("X-Student-ID") String mahasiswaId) {
        logService.deleteLog(logId, mataKuliahId, mahasiswaId);
        return ResponseEntity.ok(new ApiResponse<>("Successfully deleted log", null));
    }

    @GetMapping("/total/{mataKuliahId}")
    public Map<String, Double> getTotalJamPerBulan(
            @PathVariable("mataKuliahId") String mataKuliahId,
            @RequestHeader("X-Student-ID") String mahasiswaId) {
        return logService.getTotalJamPerBulan(mataKuliahId, mahasiswaId);
    }
    
}
