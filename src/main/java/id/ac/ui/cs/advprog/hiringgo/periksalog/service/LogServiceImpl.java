package id.ac.ui.cs.advprog.hiringgo.periksalog.service;

import id.ac.ui.cs.advprog.hiringgo.common.model.Log;
import id.ac.ui.cs.advprog.hiringgo.common.model.LogStatus;
import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogDTO;
import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogStatusUpdateDTO;
import id.ac.ui.cs.advprog.hiringgo.periksalog.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {
    private final LogRepository logRepository;

    @Override
    public List<LogDTO> getAllLogsByDosenId(Long dosenId) {
        List<Log> logs = logRepository.findAllLogsByDosenId(dosenId);
        return logs.stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public LogDTO updateLogStatus(Long dosenId, LogStatusUpdateDTO logStatusUpdateDTO) {
        Long logId = logStatusUpdateDTO.getLogId();

        if (!isLogOwnedByDosen(logId, dosenId)) {
            throw new SecurityException("You don't have permission to update this log");
        }

        Log log = logRepository.findById(logId)
                .orElseThrow(() -> new NoSuchElementException("Log not found"));

        log.setStatus(logStatusUpdateDTO.getStatus());
        Log updatedLog = logRepository.save(log);

        return convertToDTO(updatedLog);
    }

    @Override
    public boolean isLogOwnedByDosen(Long logId, Long dosenId) {
        return logRepository.isLogOwnedByDosen(logId, dosenId);
    }

    private LogDTO convertToDTO(Log log) {
        Duration duration = Duration.between(log.getWaktuMulai(), log.getWaktuSelesai());
        double durationInHours = duration.toMinutes() / 60.0;

        return LogDTO.builder()
                .id(log.getId())
                .judul(log.getJudul())
                .keterangan(log.getKeterangan())
                .kategori(log.getKategori())
                .waktuMulai(log.getWaktuMulai())
                .waktuSelesai(log.getWaktuSelesai())
                .tanggalLog(log.getTanggalLog())
                .pesanUntukDosen(log.getPesanUntukDosen())
                .status(log.getStatus())
                .mahasiswaName(log.getMahasiswa().getFullName())
                .mataKuliahName(log.getLowongan().getMataKuliah().getName())
                .mataKuliahCode(log.getLowongan().getMataKuliah().getCode())
                .durationInHours(durationInHours)
                .build();
    }
}