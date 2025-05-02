package id.ac.ui.cs.advprog.hiringgo.dashboardhonor.service;

import id.ac.ui.cs.advprog.hiringgo.dashboardhonor.model.Log;
import id.ac.ui.cs.advprog.hiringgo.dashboardhonor.model.Lowongan;
import id.ac.ui.cs.advprog.hiringgo.dashboardhonor.repository.LowonganRepositoryImpl;
import id.ac.ui.cs.advprog.hiringgo.dashboardhonor.repository.LogRepositoryImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
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
}