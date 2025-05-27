package id.ac.ui.cs.advprog.hiringgo.dashboard.service;

import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.LowonganDTO;
import id.ac.ui.cs.advprog.hiringgo.entity.Lowongan;
import id.ac.ui.cs.advprog.hiringgo.entity.MataKuliah;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LowonganConverterTest {

    @Mock
    private Logger log;

    @Test
    void convertListToDTO_WithValidList_ShouldConvertAll() {
        // Given
        List<Lowongan> lowonganList = Arrays.asList(
                createLowongan("1", "Math", "2023/2024", "Ganjil"),
                createLowongan("2", "Physics", "2023/2024", "Genap")
        );

        // When
        List<LowonganDTO> result = LowonganConverter.convertListToDTO(lowonganList, log);

        // Then
        assertEquals(2, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals("Math", result.get(0).getMataKuliahName());
        assertEquals("2", result.get(1).getId());
        assertEquals("Physics", result.get(1).getMataKuliahName());
    }

    @Test
    void convertListToDTO_WithNullList_ShouldReturnEmptyList() {
        // When
        List<LowonganDTO> result = LowonganConverter.convertListToDTO(null, log);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void convertListToDTO_WithEmptyList_ShouldReturnEmptyList() {
        // When
        List<LowonganDTO> result = LowonganConverter.convertListToDTO(Collections.emptyList(), log);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void convertListToDTO_WithNullElement_ShouldFilterOut() {
        // Given
        List<Lowongan> lowonganList = Arrays.asList(
                createLowongan("1", "Math", "2023/2024", "Ganjil"),
                null,
                createLowongan("2", "Physics", "2023/2024", "Genap")
        );

        // When
        List<LowonganDTO> result = LowonganConverter.convertListToDTO(lowonganList, log);

        // Then
        assertEquals(2, result.size());
        verify(log).warn("Attempted to convert null Lowongan to DTO");
    }

    @Test
    void convertToDTO_WithValidLowongan_ShouldConvert() {
        // Given
        Lowongan lowongan = createLowongan("1", "Math", "2023/2024", "Ganjil");

        // When
        LowonganDTO result = LowonganConverter.convertToDTO(lowongan, log);

        // Then
        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("Math", result.getMataKuliahName());
        assertEquals("2023/2024", result.getTahunAjaran());
        assertEquals("Ganjil", result.getSemester());
    }

    @Test
    void convertToDTO_WithNullLowongan_ShouldReturnNull() {
        // When
        LowonganDTO result = LowonganConverter.convertToDTO(null, log);

        // Then
        assertNull(result);
        verify(log).warn("Attempted to convert null Lowongan to DTO");
    }

    @Test
    void convertToDTO_WithNullMataKuliah_ShouldUseEmptyString() {
        // Given
        Lowongan lowongan = new Lowongan();
        lowongan.setId("1");
        lowongan.setTahunAjaran("2023/2024");
        lowongan.setSemester("Ganjil");
        lowongan.setMataKuliah(null);

        // When
        LowonganDTO result = LowonganConverter.convertToDTO(lowongan, log);

        // Then
        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("", result.getMataKuliahName());
        assertEquals("2023/2024", result.getTahunAjaran());
        assertEquals("Ganjil", result.getSemester());
    }

    @Test
    void convertToDTO_WithNullFields_ShouldUseEmptyStrings() {
        // Given
        Lowongan lowongan = new Lowongan();
        lowongan.setId(null);
        lowongan.setTahunAjaran(null);
        lowongan.setSemester(null);
        MataKuliah mataKuliah = new MataKuliah();
        mataKuliah.setNamaMataKuliah("Math");
        lowongan.setMataKuliah(mataKuliah);

        // When
        LowonganDTO result = LowonganConverter.convertToDTO(lowongan, log);

        // Then
        assertNotNull(result);
        assertEquals("", result.getId());
        assertEquals("Math", result.getMataKuliahName());
        assertEquals("", result.getTahunAjaran());
        assertEquals("", result.getSemester());
    }

    private Lowongan createLowongan(String id, String mataKuliahName, String tahunAjaran, String semester) {
        Lowongan lowongan = new Lowongan();
        lowongan.setId(id);
        lowongan.setTahunAjaran(tahunAjaran);
        lowongan.setSemester(semester);

        MataKuliah mataKuliah = new MataKuliah();
        mataKuliah.setNamaMataKuliah(mataKuliahName);
        lowongan.setMataKuliah(mataKuliah);

        return lowongan;
    }
}
