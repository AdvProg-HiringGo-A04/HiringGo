package id.ac.ui.cs.advprog.hiringgo.dashboardhonor.service;

import id.ac.ui.cs.advprog.hiringgo.dashboardhonor.model.HonorResponse;
import id.ac.ui.cs.advprog.hiringgo.entity.Log;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.enums.StatusLog;
import id.ac.ui.cs.advprog.hiringgo.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HonorService {

    @Autowired
    private LogRepository logRepository;

    private static final double HONOR_PER_JAM = 27500.0;

    public List<HonorResponse> getHonorsByMahasiswaAndPeriod(String mahasiswaId, int year, int month) {
        try {
            YearMonth ym = YearMonth.of(year, month);
            LocalDate start = ym.atDay(1);
            LocalDate end = ym.atEndOfMonth();

            List<Log> logs = logRepository.findByTanggalLogBetweenAndMahasiswaIdOrderByMataKuliahNamaMataKuliahAsc(start, end, mahasiswaId);

            if (logs == null || logs.isEmpty()) {
                return List.of();
            }

            return logs.stream()
                    .filter(log -> log != null)
                    .map(log -> {
                        double jam = calculateHoursFromLog(log);
                        double bayar = jam * HONOR_PER_JAM;

                        return HonorResponse.builder()
                                .tanggalLog(log.getTanggalLog())
                                .mahasiswa(log.getMahasiswa())
                                .mataKuliahNama(getMataKuliahName(log))
                                .totalJam(jam)
                                .honorPerJam(HONOR_PER_JAM)
                                .totalPembayaran(bayar)
                                .status(String.valueOf(StatusLog.DIPROSES))
                                .build();
                    })
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }


    private double calculateHoursFromLog(Log log) {
        try {
            if (log.getWaktuMulai() != null && log.getWaktuSelesai() != null) {
                Duration duration = Duration.between(log.getWaktuMulai(), log.getWaktuSelesai());
                return duration.toMinutes() / 60.0;
            }
            return 0.0;
        } catch (Exception e) {
            return 0.0;
        }
    }

    private String getMataKuliahName(Log log) {
        try {
            if (log.getLowongan() != null && log.getLowongan().getMataKuliah() != null) {
                return log.getLowongan().getMataKuliah().getNamaMataKuliah();
            }
            if (log.getMataKuliah() != null && log.getMataKuliah().getNamaMataKuliah() != null) {
                return log.getMataKuliah().getNamaMataKuliah();
            }
            return "Unknown";
        } catch (Exception e) {
            return "Unknown";
        }
    }
}