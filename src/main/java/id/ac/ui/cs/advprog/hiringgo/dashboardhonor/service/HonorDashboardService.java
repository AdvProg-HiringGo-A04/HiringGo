package id.ac.ui.cs.advprog.hiringgo.dashboardhonor.service;

import id.ac.ui.cs.advprog.hiringgo.dashboardhonor.model.DashboardHonorDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboardhonor.model.Log;
import id.ac.ui.cs.advprog.hiringgo.dashboardhonor.model.Lowongan;
import id.ac.ui.cs.advprog.hiringgo.dashboardhonor.repository.LowonganRepositoryImpl;
import id.ac.ui.cs.advprog.hiringgo.dashboardhonor.repository.LogRepositoryImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HonorDashboardService {
    private final HonorCalculatorImpl calculator;
    private final LowonganRepositoryImpl lowonganRepo;
    private final LogRepositoryImpl logRepo;

    public HonorDashboardService(
            HonorCalculatorImpl calculator,
            LowonganRepositoryImpl lowonganRepo,
            LogRepositoryImpl logRepo
    ) {
        this.calculator = calculator;
        this.lowonganRepo = lowonganRepo;
        this.logRepo = logRepo;
    }

    public Map<String, List<Log>> getLogsForMahasiswaByPeriod(
            String mahasiswaId,
            YearMonth period
    ) {
        LocalDateTime from = period.atDay(1).atStartOfDay();
        LocalDateTime to   = period.atEndOfMonth().atTime(23, 59, 59);

        return lowonganRepo.findByMahasiswaId(mahasiswaId).stream()
                .collect(Collectors.toMap(
                        Lowongan::getId,
                        vac -> logRepo.findByLowonganId(vac.getId(), from, to)
                ));
    }

    public Map<String, BigDecimal> calculateHonor(
            String mahasiswaId,
            YearMonth period
    ) {
        Map<String, List<Log>> logsByVac = getLogsForMahasiswaByPeriod(mahasiswaId, period);

        Map<String, BigDecimal> honorPerVac = new HashMap<>();
        for (Map.Entry<String, List<Log>> entry : logsByVac.entrySet()) {
            String lowonganId = entry.getKey();
            List<Log> logs    = entry.getValue();

            BigDecimal totalHours = logs.stream()
                    .map(Log::getHours)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal honor = calculator.calculate(totalHours);

            honorPerVac.put(lowonganId, honor);
        }

        return honorPerVac;
    }

    public List<DashboardHonorDTO> getHonorTable(
            String mahasiswaId,
            YearMonth period
    ) {
        List<Lowongan> vacs = lowonganRepo.findByMahasiswaId(mahasiswaId);

        List<DashboardHonorDTO> rows = new ArrayList<>();
        BigDecimal rate = calculator.getRatePerHour();

        for (Lowongan vac : vacs) {
            LocalDateTime from = period.atDay(1).atStartOfDay();
            LocalDateTime to   = period.atEndOfMonth().atTime(23, 59, 59);
            List<Log> logs = logRepo.findByLowonganId(vac.getId(), from, to);

            BigDecimal totalHours = logs.stream()
                    .map(Log::getHours)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalHonor = calculator.calculate(totalHours);

            rows.add(new DashboardHonorDTO(
                    vac.getName(),
                    period,
                    totalHours,
                    rate,
                    totalHonor
            ));
        }
        return rows;
    }
}