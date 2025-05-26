package id.ac.ui.cs.advprog.hiringgo.manajemenLog.service;

import id.ac.ui.cs.advprog.hiringgo.entity.Log;
import id.ac.ui.cs.advprog.hiringgo.entity.Lowongan;
import id.ac.ui.cs.advprog.hiringgo.entity.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.dto.LogRequest;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.dto.LogResponse;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.enums.StatusLog;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.exception.InvalidLogException;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.exception.LogNotFoundException;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.validation.LogValidator;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.validation.LogValidatorFactory;
import id.ac.ui.cs.advprog.hiringgo.repository.AsdosMataKuliahRepository;
import id.ac.ui.cs.advprog.hiringgo.repository.LogRepository;
import id.ac.ui.cs.advprog.hiringgo.repository.LowonganRepository;
import id.ac.ui.cs.advprog.hiringgo.repository.MahasiswaRepository;
import id.ac.ui.cs.advprog.hiringgo.repository.MataKuliahRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService{
    private final LogRepository logRepository;
    private final AsdosMataKuliahRepository asdosRepository;
    private final LogValidatorFactory validatorFactory;
    private final MahasiswaRepository mahasiswaRepository;
    private final MataKuliahRepository mataKuliahRepository;
    private final LowonganRepository lowonganRepository;


    @Override
    public List<LogResponse> getAllLogs(String lowonganId, String mahasiswaId) {
        validateEnrollment(lowonganId, mahasiswaId);

        List<Log> logs = logRepository.findByLowonganIdAndMahasiswaIdOrderByTanggalLogDescWaktuMulaiDesc(lowonganId, mahasiswaId);
        return logs.stream()
                .map(this::mapToLogResponse)
                .collect(Collectors.toList());
    }

    @Override
    public LogResponse getLogById(String logId, String mahasiswaId) {
        Log log = logRepository.findById(logId)
                .orElseThrow(() -> new LogNotFoundException(Map.of("logId", "Log tidak ditemukan")));

        if (!log.getMahasiswa().getId().equals(mahasiswaId)) {
            throw new LogNotFoundException(Map.of("mahasiswaId", "Log tidak ditemukan atau Anda tidak memiliki akses"));
        }

        validateEnrollment(log.getLowongan().getId(), mahasiswaId);

        return mapToLogResponse(log);
    }

    @Override
    public LogResponse createLog(LogRequest logRequest, String mahasiswaId) {
        validateEnrollment(logRequest.getLowonganId(), mahasiswaId);
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
                .mahasiswa(getMahasiswaOrThrow(mahasiswaId))
                .lowongan(getLowonganOrThrow(logRequest.getLowonganId()))
                .createdAt(LocalDate.now())
                .build();

        Log savedLog = logRepository.save(log);
        return mapToLogResponse(savedLog);
    }

    @Override
    public LogResponse updateLog(String logId, LogRequest logRequest, String mahasiswaId) {
        validateEnrollment(logRequest.getLowonganId(), mahasiswaId);
        validateLog(logRequest);

        Log existingLog = logRepository.findById(logId)
                .orElseThrow(() -> new LogNotFoundException(Map.of("logId", "Log tidak ditemukan")));

        if (!existingLog.getMahasiswa().getId().equals(mahasiswaId)) {
            throw new LogNotFoundException(Map.of("mahasiswaId", "Log tidak ditemukan atau Anda tidak memiliki akses"));
        }

        if (!existingLog.getLowongan().getMataKuliah().getKodeMataKuliah().equals(logRequest.getMataKuliahId())) {
            throw new InvalidLogException(Map.of("mataKuliahId",
                    "Log ini tidak terkait dengan mata kuliah yang diminta"));
        }

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
    public void deleteLog(String logId, String lowonganId, String mahasiswaId) {
        validateEnrollment(lowonganId, mahasiswaId);

        Log log = logRepository.findById(logId)
                .orElseThrow(() -> new LogNotFoundException(Map.of("logId", "Log tidak ditemukan")));

        if (!log.getMahasiswa().getId().equals(mahasiswaId)) {
            throw new LogNotFoundException(Map.of("mahasiswaId", "Log tidak ditemukan atau Anda tidak memiliki akses"));
        }

        if (!log.getLowongan().getId().strip().equals(lowonganId.strip())) {
            throw new InvalidLogException(Map.of("mataKuliahId",
                    "Log ini tidak terkait dengan mata kuliah yang diminta"));
        }

        if (log.getStatus() != StatusLog.DIPROSES) {
            throw new InvalidLogException(Map.of("status", "Log yang sudah " + log.getStatus().getDisplayName() + " tidak dapat dihapus"));
        }

        logRepository.delete(log);
    }

    @Override
    public Map<String, Double> getTotalJamPerBulan(String lowonganId, String mahasiswaId) {
        validateEnrollment(lowonganId, mahasiswaId);

        List<Log> logs = logRepository.findByLowonganIdAndMahasiswaIdOrderByTanggalLogDescWaktuMulaiDesc(lowonganId, mahasiswaId);

        return logs.stream()
            .collect(Collectors.groupingBy(
                log -> {
                    int month = log.getTanggalLog().getMonthValue();
                    int year = log.getTanggalLog().getYear();
                    return String.format("%02d-%d", month, year);
                },
                Collectors.summingDouble(log -> {
                    Duration durasi = Duration.between(log.getWaktuMulai(), log.getWaktuSelesai());
                    double jam = durasi.toMinutes() / 60.0;
                    return Math.round(jam * 1000.0) / 1000.0;
                })
            ));
    }

    private void validateEnrollment(String mataKuliahId, String mahasiswaId) {
        if (!asdosRepository.existsByMahasiswaIdAndLowonganId(mahasiswaId, mataKuliahId)) {
            throw new InvalidLogException(Map.of("enrollment", "Mahasiswa tidak terdaftar pada lowongan mata kuliah ini"));
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
                .mataKuliahId(log.getLowongan().getMataKuliah().getKodeMataKuliah())
                .mahasiswaId(log.getMahasiswa().getId())
                .lowonganId(log.getLowongan().getId())
                .createdAt(log.getCreatedAt())
                .updatedAt(log.getUpdatedAt())
                .build();
    }

    private Mahasiswa getMahasiswaOrThrow(String id) {
        return mahasiswaRepository.findById(id)
                .orElseThrow(() -> new InvalidLogException(Map.of("mahasiswaId", "Mahasiswa tidak ditemukan")));
    }

    private Lowongan getLowonganOrThrow(String id) {
        return lowonganRepository.findById(id)
                .orElseThrow(() -> new InvalidLogException(Map.of("lowonganId", "Lowongan tidak ditemukan")));
    }
}
