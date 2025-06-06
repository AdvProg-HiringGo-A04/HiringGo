// Create test file: src/test/java/id/ac/ui/cs/advprog/hiringgo/dashboard/dto/LowonganDTOTest.java
package id.ac.ui.cs.advprog.hiringgo.dashboard.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LowonganDTOTest {

    @Test
    void testBuilder() {
        LowonganDTO dto = LowonganDTO.builder()
                .id("1L")
                .mataKuliahName("Programming")
                .tahunAjaran("2023")
                .semester("GANJIL")
                .build();

        assertEquals("1L", dto.getId());
        assertEquals("Programming", dto.getMataKuliahName());
        assertEquals("2023", dto.getTahunAjaran());
        assertEquals("GANJIL", dto.getSemester());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        LowonganDTO dto = new LowonganDTO();
        dto.setId("1L");
        dto.setMataKuliahName("Programming");
        dto.setTahunAjaran("2023");
        dto.setSemester("GANJIL");

        assertEquals("1L", dto.getId());
        assertEquals("Programming", dto.getMataKuliahName());
        assertEquals("2023", dto.getTahunAjaran());
        assertEquals("GANJIL", dto.getSemester());
    }

    @Test
    void testAllArgsConstructor() {
        LowonganDTO dto = new LowonganDTO("1L", "Programming", "2023", "GANJIL");

        assertEquals("1L", dto.getId());
        assertEquals("Programming", dto.getMataKuliahName());
        assertEquals("2023", dto.getTahunAjaran());
        assertEquals("GANJIL", dto.getSemester());
    }
}