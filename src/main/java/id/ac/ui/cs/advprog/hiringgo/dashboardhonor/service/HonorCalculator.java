package id.ac.ui.cs.advprog.hiringgo.dashboardhonor.service;

import java.math.BigDecimal;

public interface HonorCalculator {
    BigDecimal calculate(BigDecimal totalHours);
    BigDecimal getRatePerHour();
}