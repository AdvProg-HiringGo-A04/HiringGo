package id.ac.ui.cs.advprog.hiringgo.periksalog.service;

import id.ac.ui.cs.advprog.hiringgo.entity.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.model.Log;
import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogDTO;
import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogStatusUpdateDTO;
import id.ac.ui.cs.advprog.hiringgo.periksalog.repository.LogRepository;
import id.ac.ui.cs.advprog.hiringgo.repository.MahasiswaRepository;
import id.ac.ui.cs.advprog.hiringgo.entity.MataKuliah;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {
    private final LogRepository logRepository;
    private final MahasiswaRepository mahasiswaRepository;
    private final MataKuliahService mataKuliahService;

    @Override
    public List<LogDTO> getAllLogsByDosenId(String dosenId) {
        List<Log> logs = logRepository.findAllLogsByDosenId(dosenId);
        return logs.stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public LogDTO updateLogStatus(String dosenId, LogStatusUpdateDTO logStatusUpdateDTO) {
        String logId = logStatusUpdateDTO.getLogId();

        validateDosenOwnership(logId, dosenId);

        Log log = findLogById(logId);
        log.setStatus(logStatusUpdateDTO.getStatus());
        log.setUpdatedAt(LocalDate.now());

        Log updatedLog = logRepository.save(log);
        return convertToDTO(updatedLog);
    }

    @Override
    public boolean isLogOwnedByDosen(String logId, String dosenId) {
        return logRepository.isLogOwnedByDosen(logId, dosenId);
    }

    private void validateDosenOwnership(String logId, String dosenId) {
        if (!isLogOwnedByDosen(logId, dosenId)) {
            throw new SecurityException("You don't have permission to update this log");
        }
    }

    private Log findLogById(String logId) {
        return logRepository.findById(logId)
                .orElseThrow(() -> new NoSuchElementException("Log not found with ID: " + logId));
    }

    private LogDTO convertToDTO(Log log) {
        Duration duration = Duration.between(log.getWaktuMulai(), log.getWaktuSelesai());
        double durationInHours = duration.toMinutes() / 60.0;

        Mahasiswa mahasiswa = mahasiswaRepository.findById(log.getMahasiswaId())
                .orElseThrow(() -> new NoSuchElementException("Mahasiswa not found with ID: " + log.getMahasiswaId()));

        MataKuliah mataKuliah = mataKuliahService.getMataKuliahByKode(log.getMataKuliahId());

        return LogDTO.builder()
                .id(log.getId())
                .judul(log.getJudul())
                .keterangan(log.getKeterangan())
                .kategori(log.getKategori())
                .waktuMulai(log.getWaktuMulai())
                .waktuSelesai(log.getWaktuSelesai())
                .tanggalLog(log.getTanggalLog())
                .pesanUntukDosen(log.getPesan())
                .status(log.getStatus())
                .mahasiswaName(mahasiswa.getNamaLengkap())
                .mataKuliahName(mataKuliah.getNamaMataKuliah())
                .mataKuliahCode(mataKuliah.getKodeMataKuliah())
                .durationInHours(durationInHours)
                .build();
    }
}