package id.ac.ui.cs.advprog.hiringgo.dashboardhonor.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.*;

class DashboardHonorDTOTest {

    @Test
    void constructorAndGetters_populatesAllFields() {
        String vacancyName   = "Matematika Diskret";
        YearMonth period     = YearMonth.of(2025, 5);
        BigDecimal totalHours    = new BigDecimal("2");
        BigDecimal ratePerHour   = new BigDecimal("27500");
        BigDecimal totalHonor    = new BigDecimal("55000");

        DashboardHonorDTO row = new DashboardHonorDTO(
                vacancyName,
                period,
                totalHours,
                ratePerHour,
                totalHonor
        );
        assertEquals(vacancyName,    row.getVacancyName(),   "vacancyName harus sesuai");
        assertEquals(period,         row.getPeriod(),        "period harus sesuai");
        assertEquals(totalHours,     row.getTotalHours(),    "totalHours harus sesuai");
        assertEquals(ratePerHour,    row.getRatePerHour(),   "ratePerHour harus sesuai");
        assertEquals(totalHonor,     row.getTotalHonor(),    "totalHonor harus sesuai");
    }
}
