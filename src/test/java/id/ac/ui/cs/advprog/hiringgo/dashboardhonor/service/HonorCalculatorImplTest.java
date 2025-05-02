package id.ac.ui.cs.advprog.hiringgo.dashboardhonor.service;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HonorCalculatorImplTest {

    private final HonorCalculatorImpl calculator = new HonorCalculatorImpl();

    @Test
    void testLogWithZeroHour() {
        assertEquals(BigDecimal.ZERO, calculator.calculate(BigDecimal.ZERO), "0 jam harus menghasilkan honor 0");
    }

    @Test
    void testLogWithOneHour() {
        assertEquals(new BigDecimal("27500"), calculator.calculate(BigDecimal.ONE), "1 jam harus menghasilkan Rp 27.500");
    }

    @Test
    void testLogWithFractionalHour() {
        assertEquals(new BigDecimal("41250"), calculator.calculate(new BigDecimal("1.5")), "1,5 jam harus menghasilkan Rp 41.250");
    }

    @Test
    void testLogWithRoundUp() {
        assertEquals(new BigDecimal("64488"), calculator.calculate(new BigDecimal("2.345")),
                "Hasil perkalian dengan desimal harus dibulatkan HALF_UP ke skala 0"
        );
    }
}
