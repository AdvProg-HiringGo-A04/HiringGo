package id.ac.ui.cs.advprog.hiringgo.periksalog.service;

import id.ac.ui.cs.advprog.hiringgo.entity.Log;
import id.ac.ui.cs.advprog.hiringgo.entity.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.entity.MataKuliah;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.enums.StatusLog;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.enums.TipeKategori;
import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.LocalTime;

@Slf4j
@Service
public class LogConverterService {

    private static final double MINUTES_PER_HOUR = 60.0;

    public LogDTO convertToDTO(Log logEntity) {
        if (logEntity == null) {
            log.warn("Attempted to convert null Log to DTO");
            return null;
        }

        try {
            double durationInHours = calculateDurationInHours(logEntity.getWaktuMulai(), logEntity.getWaktuSelesai());

            Mahasiswa mahasiswa = logEntity.getMahasiswa();
            if (mahasiswa == null) {
                log.warn("No mahasiswa found for log ID: {}", logEntity.getId());
            }

            MataKuliah mataKuliah = extractMataKuliah(logEntity);
            if (mataKuliah == null) {
                log.warn("No mata kuliah found for log ID: {}", logEntity.getId());
            }

            return LogDTO.builder()
                    .id(logEntity.getId())
                    .judul(logEntity.getJudul())
                    .keterangan(logEntity.getKeterangan())
                    .kategori(parseKategoriFromString(String.valueOf(logEntity.getKategori())))
                    .waktuMulai(logEntity.getWaktuMulai())
                    .waktuSelesai(logEntity.getWaktuSelesai())
                    .tanggalLog(logEntity.getTanggalLog())
                    .pesanUntukDosen(logEntity.getPesan())
                    .status(parseStatusFromString(String.valueOf(logEntity.getStatus())))
                    .mahasiswaName(getMahasiswaName(mahasiswa))
                    .mataKuliahName(getMataKuliahName(mataKuliah))
                    .mataKuliahCode(getMataKuliahCode(mataKuliah))
                    .durationInHours(durationInHours)
                    .build();
        } catch (Exception e) {
            log.error("Error converting Log to DTO for log ID: {}", logEntity.getId(), e);
            throw e;
        }
    }

    private MataKuliah extractMataKuliah(Log logEntity) {
        if (logEntity.getLowongan() != null) {
            return logEntity.getLowongan().getMataKuliah();
        }
        return null;
    }

    private double calculateDurationInHours(LocalTime waktuMulai, LocalTime waktuSelesai) {
        if (waktuMulai == null || waktuSelesai == null) {
            log.warn("Invalid time values for duration calculation: start={}, end={}", waktuMulai, waktuSelesai);
            return 0.0;
        }

        Duration duration = Duration.between(waktuMulai, waktuSelesai);
        return duration.toMinutes() / MINUTES_PER_HOUR;
    }

    private String getMahasiswaName(Mahasiswa mahasiswa) {
        return mahasiswa != null ? mahasiswa.getNamaLengkap() : "Unknown Student";
    }

    private String getMataKuliahName(MataKuliah mataKuliah) {
        return mataKuliah != null ? mataKuliah.getNamaMataKuliah() : "Unknown Course";
    }

    private String getMataKuliahCode(MataKuliah mataKuliah) {
        return mataKuliah != null ? mataKuliah.getKodeMataKuliah() : "Unknown Code";
    }

    private TipeKategori parseKategoriFromString(String kategori) {
        if (!StringUtils.hasText(kategori)) {
            log.warn("Empty kategori string, returning default value");
            return TipeKategori.values()[0];
        }

        try {
            return TipeKategori.valueOf(kategori.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("Invalid kategori value: {}, returning default", kategori);
            return TipeKategori.values()[0];
        }
    }

    private StatusLog parseStatusFromString(String status) {
        if (!StringUtils.hasText(status)) {
            log.warn("Empty status string, returning default value");
            return StatusLog.values()[0];
        }

        try {
            return StatusLog.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("Invalid status value: {}, returning default", status);
            return StatusLog.values()[0];
        }
    }
}