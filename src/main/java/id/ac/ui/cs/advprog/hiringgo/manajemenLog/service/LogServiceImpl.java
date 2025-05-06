package id.ac.ui.cs.advprog.hiringgo.manajemenLog.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import id.ac.ui.cs.advprog.hiringgo.manajemenLog.dto.LogRequest;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.dto.LogResponse;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.exception.InvalidLogException;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.exception.LogNotFoundException;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.model.Log;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.model.enums.StatusLog;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.repository.LogRepository;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.validation.LogValidator;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.validation.LogValidatorFactory;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService{
    private final LogRepository logRepository;
    private final LogValidatorFactory validatorFactory;
    
    @Override
    public List<LogResponse> getAllLogs(String mataKuliahId, String mahasiswaId) {
        validateEnrollment(mataKuliahId, mahasiswaId);

        List<Log> logs = logRepository.findByMataKuliahIdAndMahasiswaId(mataKuliahId, mahasiswaId);
        return logs.stream()
                .map(this::mapToLogResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public LogResponse getLogById(String logId, String mahasiswaId) {
        Log log = logRepository.findById(logId)
                .orElseThrow(() -> new LogNotFoundException(logId));
        
        if (!log.getMahasiswaId().equals(mahasiswaId)) {
            throw new LogNotFoundException("Log tidak ditemukan atau Anda tidak memiliki akses");
        }

        validateEnrollment(log.getMataKuliahId(), mahasiswaId);
        
        return mapToLogResponse(log);
    }
    
    @Override
    public LogResponse createLog(LogRequest logRequest, String mahasiswaId) {
        validateEnrollment(logRequest.getMataKuliahId(), mahasiswaId);
        validateLog(logRequest);
        
        Log log = Log.builder()
                .id(UUID.randomUUID().toString())
                .judul(logRequest.getJudul())
                .keterangan(logRequest.getKeterangan())
                .kategori(logRequest.getKategori())
                .waktuMulai(logRequest.getWaktuMulai())
                .waktuSelesai(logRequest.getWaktuSelesai())
                .tanggalLog(logRequest.getTanggalLog())
                .pesan(logRequest.getPesan())
                .status(StatusLog.DIPROSES)
                .mataKuliahId(logRequest.getMataKuliahId())
                .mahasiswaId(mahasiswaId)
                .createdAt(LocalDate.now())
                .build();
        
        Log savedLog = logRepository.save(log);
        return mapToLogResponse(savedLog);
    }
    
    @Override
    public LogResponse updateLog(String logId, LogRequest logRequest, String mahasiswaId) {
        validateEnrollment(logRequest.getMataKuliahId(), mahasiswaId);
        validateLog(logRequest);

        Log existingLog = logRepository.findById(logId)
                .orElseThrow(() -> new LogNotFoundException(logId));
        
        if (!existingLog.getMahasiswaId().equals(mahasiswaId)) {
            throw new LogNotFoundException("Log tidak ditemukan atau Anda tidak memiliki akses");
        }

        if (!existingLog.getMataKuliahId().equals(logRequest.getMataKuliahId())) {
            throw new InvalidLogException(Map.of("mataKuliahId", 
                    "Log ini tidak terkait dengan mata kuliah yang diminta"));
        }

        // Hanya log dengan status PENDING yang dapat diubah
        if (existingLog.getStatus() != StatusLog.DIPROSES) {
            throw new InvalidLogException(Map.of("status", "Log yang sudah " + existingLog.getStatus().getDisplayName() + " tidak dapat diubah"));
        }
        
      
        if (logRequest.getKeterangan() != null) {
            existingLog.setKeterangan(logRequest.getKeterangan());
        }

        if (logRequest.getPesan() != null) {
            existingLog.setPesan(logRequest.getPesan());
        }
        
        existingLog.setJudul(logRequest.getJudul());
        existingLog.setKategori(logRequest.getKategori());
        existingLog.setWaktuMulai(logRequest.getWaktuMulai());
        existingLog.setWaktuSelesai(logRequest.getWaktuSelesai());
        existingLog.setTanggalLog(logRequest.getTanggalLog());
        existingLog.setUpdatedAt(LocalDate.now());

        Log updatedLog = logRepository.save(existingLog);
        return mapToLogResponse(updatedLog);
    }
    
    @Override
    public void deleteLog(String logId, String mataKuliahId, String mahasiswaId) {
        validateEnrollment(mataKuliahId, mahasiswaId);

        Log log = logRepository.findById(logId)
                .orElseThrow(() -> new LogNotFoundException(logId));
        
        if (!log.getMahasiswaId().equals(mahasiswaId)) {
            throw new LogNotFoundException("Log tidak ditemukan atau Anda tidak memiliki akses");
        }

        if (!log.getMataKuliahId().equals(mataKuliahId)) {
            throw new InvalidLogException(Map.of("mataKuliahId", 
                    "Log ini tidak terkait dengan mata kuliah yang diminta"));
        }
        
        // Hanya log dengan status PENDING yang dapat dihapus
        if (log.getStatus() != StatusLog.DIPROSES) {
            throw new InvalidLogException(Map.of("status", "Log yang sudah " + log.getStatus().getDisplayName() + " tidak dapat dihapus"));
        }
        
        logRepository.delete(log);
    }
    
    private void validateEnrollment(String mataKuliahId, String mahasiswaId) {
        if (!logRepository.existsByMataKuliahIdAndMahasiswaId(mataKuliahId, mahasiswaId)) {
            Map<String, String> error = new HashMap<>();
            error.put("enrollment", "Mahasiswa tidak terdaftar pada lowongan mata kuliah ini");
            throw new InvalidLogException(error);
        }
    }
    
    private void validateLog(LogRequest logRequest) {
        Map<String, String> allErrors = new HashMap<>();
        
        // Jalankan semua validator
        List<LogValidator> validators = validatorFactory.createValidators();
        for (LogValidator validator : validators) {
            Map<String, String> errors = validator.validate(logRequest);
            allErrors.putAll(errors);
        }
        
        if (!allErrors.isEmpty()) {
            throw new InvalidLogException(allErrors);
        }
    }
    
    private LogResponse mapToLogResponse(Log log) {
        return LogResponse.builder()
                .id(log.getId())
                .judul(log.getJudul())
                .keterangan(log.getKeterangan())
                .kategori(log.getKategori())
                .kategoriDisplayName(log.getKategori().getDisplayName())
                .waktuMulai(log.getWaktuMulai())
                .waktuSelesai(log.getWaktuSelesai())
                .tanggalLog(log.getTanggalLog())
                .pesan(log.getPesan())
                .status(log.getStatus())
                .statusDisplayName(log.getStatus().getDisplayName())
                .mataKuliahId(log.getMataKuliahId())
                .mahasiswaId(log.getMahasiswaId())
                .createdAt(log.getCreatedAt())
                .updatedAt(log.getUpdatedAt())
                .build();
    }
}
