// Create test file: src/test/java/id/ac/ui/cs/advprog/hiringgo/dashboard/dto/AdminStatisticsDTOTest.java
package id.ac.ui.cs.advprog.hiringgo.dashboard.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AdminStatisticsDTOTest {

    @Test
    void testBuilder() {
        AdminStatisticsDTO dto = AdminStatisticsDTO.builder()
                .totalDosen(10L)
                .totalMahasiswa(100L)
                .totalMataKuliah(20L)
                .totalLowongan(30L)
                .build();

        assertEquals(10L, dto.getTotalDosen());
        assertEquals(100L, dto.getTotalMahasiswa());
        assertEquals(20L, dto.getTotalMataKuliah());
        assertEquals(30L, dto.getTotalLowongan());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        AdminStatisticsDTO dto = new AdminStatisticsDTO();
        dto.setTotalDosen(10L);
        dto.setTotalMahasiswa(100L);
        dto.setTotalMataKuliah(20L);
        dto.setTotalLowongan(30L);

        assertEquals(10L, dto.getTotalDosen());
        assertEquals(100L, dto.getTotalMahasiswa());
        assertEquals(20L, dto.getTotalMataKuliah());
        assertEquals(30L, dto.getTotalLowongan());
    }

    @Test
    void testAllArgsConstructor() {
        AdminStatisticsDTO dto = new AdminStatisticsDTO(10L, 100L, 20L, 30L);

        assertEquals(10L, dto.getTotalDosen());
        assertEquals(100L, dto.getTotalMahasiswa());
        assertEquals(20L, dto.getTotalMataKuliah());
        assertEquals(30L, dto.getTotalLowongan());
    }
}