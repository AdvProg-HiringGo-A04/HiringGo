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
        
        return mapToLogResponse(log);
    }
    
    @Override
    public LogResponse createLog(LogRequest logRequest, String mahasiswaId) {
        validateLog(logRequest, false);
        
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
        Log existingLog = logRepository.findById(logId)
                .orElseThrow(() -> new LogNotFoundException(logId));
        
        if (!existingLog.getMahasiswaId().equals(mahasiswaId)) {
            throw new LogNotFoundException("Log tidak ditemukan atau Anda tidak memiliki akses");
        }
        
        // Hanya log dengan status PENDING yang dapat diubah
        if (existingLog.getStatus() != StatusLog.DIPROSES) {
            throw new InvalidLogException(Map.of("status", "Log yang sudah " + existingLog.getStatus().getDisplayName() + " tidak dapat diubah"));
        }
        
        // Update fields hanya jika ada nilai baru yang tidak null
        if (logRequest.getJudul() != null) {
            existingLog.setJudul(logRequest.getJudul());
        }
        if (logRequest.getKeterangan() != null) {
            existingLog.setKeterangan(logRequest.getKeterangan());
        }
        if (logRequest.getKategori() != null) {
            existingLog.setKategori(logRequest.getKategori());
        }
        if (logRequest.getWaktuMulai() != null) {
            existingLog.setWaktuMulai(logRequest.getWaktuMulai());
        }
        if (logRequest.getWaktuSelesai() != null) {
            existingLog.setWaktuSelesai(logRequest.getWaktuSelesai());
        }
        if (logRequest.getTanggalLog() != null) {
            existingLog.setTanggalLog(logRequest.getTanggalLog());
        }
        if (logRequest.getPesan() != null) {
            existingLog.setPesan(logRequest.getPesan());
        }

        // Selalu set updatedAt setiap kali ada perubahan
        existingLog.setUpdatedAt(LocalDate.now());

        Log updatedLog = logRepository.save(existingLog);
        return mapToLogResponse(updatedLog);
    }
    
    @Override
    public void deleteLog(String logId, String mahasiswaId) {
        Log log = logRepository.findById(logId)
                .orElseThrow(() -> new LogNotFoundException(logId));
        
        if (!log.getMahasiswaId().equals(mahasiswaId)) {
            throw new LogNotFoundException("Log tidak ditemukan atau Anda tidak memiliki akses");
        }
        
        // Hanya log dengan status PENDING yang dapat dihapus
        if (log.getStatus() != StatusLog.DIPROSES) {
            throw new InvalidLogException(Map.of("status", "Log yang sudah " + log.getStatus().getDisplayName() + " tidak dapat dihapus"));
        }
        
        logRepository.delete(log);
    }
    
    @Override
    public boolean validateEnrollment(String mataKuliahId, String mahasiswaId) {
        // Di implementasi nyata, kita mungkin akan memanggil service lain untuk melakukan validasi
        // Apakah mahasiswa sudah terdaftar di lowongan tersebut
        // Untuk simplicity kita cek di repository saja
        return logRepository.existsByMataKuliahIdAndMahasiswaId(mataKuliahId, mahasiswaId);
    }
    
    private void validateLog(LogRequest logRequest, boolean isUpdate) {
        Map<String, String> allErrors = new HashMap<>();
        
        // Jalankan semua validator
        List<LogValidator> validators = validatorFactory.createValidators();
        for (LogValidator validator : validators) {
            Map<String, String> errors = validator.validate(logRequest, isUpdate);
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
