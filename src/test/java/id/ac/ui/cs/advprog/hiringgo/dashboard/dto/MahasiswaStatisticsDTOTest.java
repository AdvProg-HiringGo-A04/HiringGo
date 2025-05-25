// Create test file: src/test/java/id/ac/ui/cs/advprog/hiringgo/dashboard/dto/MahasiswaStatisticsDTOTest.java
package id.ac.ui.cs.advprog.hiringgo.dashboard.dto;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MahasiswaStatisticsDTOTest {

    @Test
    void testBuilder() {
        // Create sample LowonganDTO list
        List<LowonganDTO> acceptedLowonganList = Arrays.asList(
                new LowonganDTO("1L", "Programming", 2023, "GANJIL"),
                new LowonganDTO("2L", "Algorithms", 2023, "GENAP")
        );

        MahasiswaStatisticsDTO dto = MahasiswaStatisticsDTO.builder()
                .openLowonganCount(5L)
                .acceptedLowonganCount(2L)
                .rejectedLowonganCount(1L)
                .pendingLowonganCount(3L)
                .totalLogHours(25.5)
                .totalInsentif(701250.0)  // 25.5 hours * 27500
                .acceptedLowonganList(acceptedLowonganList)
                .build();

        assertEquals(5L, dto.getOpenLowonganCount());
        assertEquals(2L, dto.getAcceptedLowonganCount());
        assertEquals(1L, dto.getRejectedLowonganCount());
        assertEquals(3L, dto.getPendingLowonganCount());
        assertEquals(25.5, dto.getTotalLogHours());
        assertEquals(701250.0, dto.getTotalInsentif());
        assertEquals(2, dto.getAcceptedLowonganList().size());
        assertEquals("Programming", dto.getAcceptedLowonganList().get(0).getMataKuliahName());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        MahasiswaStatisticsDTO dto = new MahasiswaStatisticsDTO();
        List<LowonganDTO> acceptedLowonganList = Arrays.asList(
                new LowonganDTO("1L", "Programming", 2023, "GANJIL")
        );

        dto.setOpenLowonganCount(5L);
        dto.setAcceptedLowonganCount(1L);
        dto.setRejectedLowonganCount(2L);
        dto.setPendingLowonganCount(3L);
        dto.setTotalLogHours(10.0);
        dto.setTotalInsentif(275000.0);
        dto.setAcceptedLowonganList(acceptedLowonganList);

        assertEquals(5L, dto.getOpenLowonganCount());
        assertEquals(1L, dto.getAcceptedLowonganCount());
        assertEquals(2L, dto.getRejectedLowonganCount());
        assertEquals(3L, dto.getPendingLowonganCount());
        assertEquals(10.0, dto.getTotalLogHours());
        assertEquals(275000.0, dto.getTotalInsentif());
        assertEquals(1, dto.getAcceptedLowonganList().size());
    }

    @Test
    void testAllArgsConstructor() {
        List<LowonganDTO> acceptedLowonganList = Arrays.asList(
                new LowonganDTO("1L", "Programming", 2023, "GANJIL")
        );

        MahasiswaStatisticsDTO dto = new MahasiswaStatisticsDTO(
                5L, 1L, 2L, 3L, 10.0, 275000.0, acceptedLowonganList);

        assertEquals(5L, dto.getOpenLowonganCount());
        assertEquals(1L, dto.getAcceptedLowonganCount());
        assertEquals(2L, dto.getRejectedLowonganCount());
        assertEquals(3L, dto.getPendingLowonganCount());
        assertEquals(10.0, dto.getTotalLogHours());
        assertEquals(275000.0, dto.getTotalInsentif());
        assertEquals(1, dto.getAcceptedLowonganList().size());
    }
}