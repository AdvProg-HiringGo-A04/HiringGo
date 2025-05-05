package id.ac.ui.cs.advprog.hiringgo.manajemenLog.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import id.ac.ui.cs.advprog.hiringgo.manajemenLog.dto.LogRequest;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.dto.LogResponse;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.exception.InvalidLogException;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.service.LogService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/log")
@RequiredArgsConstructor
public class LogController {
    private final LogService logService;

    // list matkul yg dia asdosin (dari dashboard mahasiswa) == gaperlu bikin endpoint lagi atau... perlu..?

    // list log per matkul
    @GetMapping("/matakuliah/{mataKuliahId}")
    public ResponseEntity<List<LogResponse>> getAllLogs(
            @PathVariable("mataKuliahId") String mataKuliahId,
            @RequestHeader("X-Student-ID") String mahasiswaId) {
        
        if (!logService.validateEnrollment(mataKuliahId, mahasiswaId)) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Mahasiswa tidak terdaftar pada lowongan mata kuliah ini");
            throw new InvalidLogException(error);
        }
        
        List<LogResponse> logs = logService.getAllLogs(mataKuliahId, mahasiswaId);
        return ResponseEntity.ok(logs);
    }
    
    @GetMapping("/{logId}")
    public ResponseEntity<LogResponse> getLogById(
            @PathVariable("logId") String logId,
            @RequestHeader("X-Student-ID") String mahasiswaId) {
        LogResponse log = logService.getLogById(logId, mahasiswaId);
        return ResponseEntity.ok(log);
    }
    
    @PostMapping
    public ResponseEntity<LogResponse> createLog(
            @Valid @RequestBody LogRequest logRequest,
            @RequestHeader("X-Student-ID") String mahasiswaId) {
        
        if (!logService.validateEnrollment(logRequest.getMataKuliahId(), mahasiswaId)) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Mahasiswa tidak terdaftar pada lowongan mata kuliah ini");
            throw new InvalidLogException(error);
        }
        
        LogResponse createdLog = logService.createLog(logRequest, mahasiswaId);
        return new ResponseEntity<>(createdLog, HttpStatus.CREATED);
    }
    
    @PutMapping("/{logId}")
    public ResponseEntity<LogResponse> updateLog(
            @PathVariable("logId") String logId,
            @Valid @RequestBody LogRequest logRequest,
            @RequestHeader("X-Student-ID") String mahasiswaId) {
        
        if (!logService.validateEnrollment(logRequest.getMataKuliahId(), mahasiswaId)) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Mahasiswa tidak terdaftar pada lowongan mata kuliah ini");
            throw new InvalidLogException(error);
        }
        
        LogResponse updatedLog = logService.updateLog(logId, logRequest, mahasiswaId);
        return ResponseEntity.ok(updatedLog);
    }
    
    @DeleteMapping("/{logId}")
    public ResponseEntity<Void> deleteLog(
            @PathVariable("logId") String logId,
            @RequestHeader("X-Student-ID") String mahasiswaId) {
        logService.deleteLog(logId, mahasiswaId);
        return ResponseEntity.noContent().build();
    }
}
