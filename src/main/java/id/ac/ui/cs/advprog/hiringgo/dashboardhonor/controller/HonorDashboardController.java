package id.ac.ui.cs.advprog.hiringgo.dashboardhonor.controller;

import id.ac.ui.cs.advprog.hiringgo.dashboardhonor.model.DashboardHonorDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboardhonor.model.Log;
import id.ac.ui.cs.advprog.hiringgo.dashboardhonor.service.HonorDashboardService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/honor")
public class HonorDashboardController {

    private final HonorDashboardService service;

    public HonorDashboardController(HonorDashboardService service) {
        this.service = service;
    }

    @GetMapping("/logs")
    public Map<String, List<Log>> getLogsForMahasiswa(
            @RequestParam String mahasiswaId,
            @RequestParam int year,
            @RequestParam int month
    ) {
        YearMonth period = YearMonth.of(year, month);
        return service.getLogsForMahasiswaByPeriod(mahasiswaId, period);
    }

    @GetMapping
    public Map<String, BigDecimal> getHonor(
            @RequestParam String mahasiswaId,
            @RequestParam int year,
            @RequestParam int month
    ) {
        YearMonth period = YearMonth.of(year, month);
        return service.calculateHonor(mahasiswaId, period);
    }

    @GetMapping("/table")
    public List<DashboardHonorDTO> getHonorTable(
            @RequestParam String mahasiswaId,
            @RequestParam int year,
            @RequestParam int month
    ) {
        YearMonth period = YearMonth.of(year, month);
        return service.getHonorTable(mahasiswaId, period);
    }
}
