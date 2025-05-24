package id.ac.ui.cs.advprog.hiringgo.dashboardhonor.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HonorResponseTest {

    @Test
    void testGetFormattedHonor_withLargeNumber() {
        HonorResponse resp = HonorResponse.builder()
                .totalPembayaran(1234567.0)
                .build();
        assertEquals("Rp 1,234,567", resp.getFormattedHonor());
    }

    @Test
    void testGetFormattedJam_integer() {
        HonorResponse resp = HonorResponse.builder()
                .totalJam(3.0)
                .build();
        assertEquals("3 jam", resp.getFormattedJam());
    }

    @Test
    void testGetFormattedJam_fractionalExactHalf() {
        HonorResponse resp = HonorResponse.builder()
                .totalJam(2.5)
                .build();
        assertEquals("2.5 jam", resp.getFormattedJam());
    }

    @Test
    void testGetFormattedJam_fractionalRounding() {
        HonorResponse resp = HonorResponse.builder()
                .totalJam(2.25)
                .build();
        assertEquals("2.3 jam", resp.getFormattedJam());
    }
}
