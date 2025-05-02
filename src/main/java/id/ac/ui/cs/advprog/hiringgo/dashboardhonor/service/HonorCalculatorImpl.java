package id.ac.ui.cs.advprog.hiringgo.dashboardhonor.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class HonorCalculatorImpl implements HonorCalculator {
    private static final BigDecimal RATE_PER_HOUR = new BigDecimal("27500");

    @Override
    public BigDecimal calculate(BigDecimal totalHours) {
        return totalHours.multiply(RATE_PER_HOUR).setScale(0, RoundingMode.HALF_UP);
    }
}