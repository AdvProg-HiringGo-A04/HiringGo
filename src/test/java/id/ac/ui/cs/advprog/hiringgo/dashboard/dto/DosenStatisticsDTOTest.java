// Create test file: src/test/java/id/ac/ui/cs/advprog/hiringgo/dashboard/dto/DosenStatisticsDTOTest.java
package id.ac.ui.cs.advprog.hiringgo.dashboard.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DosenStatisticsDTOTest {

    @Test
    void testBuilder() {
        DosenStatisticsDTO dto = DosenStatisticsDTO.builder()
                .totalMataKuliah(5L)
                .totalMahasiswaAssistant(25L)
                .openLowonganCount(3L)
                .build();

        assertEquals(5L, dto.getTotalMataKuliah());
        assertEquals(25L, dto.getTotalMahasiswaAssistant());
        assertEquals(3L, dto.getOpenLowonganCount());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        DosenStatisticsDTO dto = new DosenStatisticsDTO();
        dto.setTotalMataKuliah(5L);
        dto.setTotalMahasiswaAssistant(25L);
        dto.setOpenLowonganCount(3L);

        assertEquals(5L, dto.getTotalMataKuliah());
        assertEquals(25L, dto.getTotalMahasiswaAssistant());
        assertEquals(3L, dto.getOpenLowonganCount());
    }

    @Test
    void testAllArgsConstructor() {
        DosenStatisticsDTO dto = new DosenStatisticsDTO(5L, 25L, 3L);

        assertEquals(5L, dto.getTotalMataKuliah());
        assertEquals(25L, dto.getTotalMahasiswaAssistant());
        assertEquals(3L, dto.getOpenLowonganCount());
    }
}