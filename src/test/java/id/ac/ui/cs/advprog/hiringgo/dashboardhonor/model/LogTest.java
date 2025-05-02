package id.ac.ui.cs.advprog.hiringgo.dashboardhonor.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LogTest {

    @Test
    void testGetWorkLog() {
        Log log = new Log();
        LocalDateTime start = LocalDateTime.of(2025, 5, 10, 8, 15);
        LocalDateTime end = LocalDateTime.of(2025, 5, 10, 10, 45);
        log.setStart(start);
        log.setEnd(end);

        BigDecimal expected = new BigDecimal("2.50");
        assertEquals(0, expected.compareTo(log.getHours()));
    }
}
