package id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.service;

import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.dto.LowonganForm;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.entity.Lowongan;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.repository.LowonganRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LowonganServiceTest {

    private LowonganRepository lowonganRepository;
    private LowonganService lowonganService;

    @BeforeEach
    void setUp() {
        lowonganRepository = mock(LowonganRepository.class);
        lowonganService = new LowonganService(lowonganRepository);
    }

    @Test
    void testCreateLowongan_success() {
        LowonganForm form = LowonganForm.builder()
            .mataKuliah("Pemrograman Lanjut")
            .tahunAjaran("2024/2025")
            .semester("Ganjil")
            .jumlahAsistenDibutuhkan(3)
            .build();

        when(lowonganRepository.existsByMataKuliahAndSemesterAndTahunAjaran(
            form.getMataKuliah(), form.getSemester(), form.getTahunAjaran()))
            .thenReturn(false);

        Lowongan created = lowonganService.createLowongan(form);

        ArgumentCaptor<Lowongan> captor = ArgumentCaptor.forClass(Lowongan.class);
        assertEquals(captor.getValue(), created);
        verify(lowonganRepository).save(captor.capture());
        assertEquals("Pemrograman Lanjut", captor.getValue().getMataKuliah());
        assertEquals("Ganjil", captor.getValue().getSemester());
    }

    @Test
    void testCreateLowongan_duplicate_throwsException() {
        LowonganForm form = LowonganForm.builder()
            .mataKuliah("Pemrograman Lanjut")
            .tahunAjaran("2024/2025")
            .semester("Ganjil")
            .jumlahAsistenDibutuhkan(3)
            .build();

        when(lowonganRepository.existsByMataKuliahAndSemesterAndTahunAjaran(
            form.getMataKuliah(), form.getSemester(), form.getTahunAjaran()))
            .thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> lowonganService.createLowongan(form));
    }

    @Test
    void testUpdateLowongan_success() {
        UUID id = UUID.randomUUID();
        Lowongan existing = Lowongan.builder()
            .id(id)
            .mataKuliah("PBO")
            .tahunAjaran("2024/2025")
            .semester("Ganjil")
            .jumlahDibutuhkan(2)
            .build();

        LowonganForm form = LowonganForm.builder()
            .mataKuliah("Algo")
            .tahunAjaran("2024/2025")
            .semester("Genap")
            .jumlahAsistenDibutuhkan(5)
            .build();

        when(lowonganRepository.findById(id)).thenReturn(Optional.of(existing));
        when(lowonganRepository.existsByMataKuliahAndSemesterAndTahunAjaran(
            form.getMataKuliah(), form.getSemester(), form.getTahunAjaran()))
            .thenReturn(false);

        Lowongan updated = lowonganService.updateLowongan(id, form);

        assertEquals("Algo", updated.getMataKuliah());
        assertEquals("Genap", updated.getSemester());
        assertEquals(5, updated.getJumlahDibutuhkan());
    }

    @Test
    void testUpdateLowongan_notFound_throwsException() {
        UUID id = UUID.randomUUID();
        LowonganForm form = LowonganForm.builder()
            .mataKuliah("Dummy")
            .tahunAjaran("2024/2025")
            .semester("Genap")
            .jumlahAsistenDibutuhkan(1)
            .build();

        when(lowonganRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> lowonganService.updateLowongan(id, form));
    }

    @Test
    void testGetLowonganById_success() {
        UUID id = UUID.randomUUID();
        Lowongan lowongan = Lowongan.builder().id(id).mataKuliah("PBO").build();
        when(lowonganRepository.findById(id)).thenReturn(Optional.of(lowongan));

        Optional<Lowongan> result = lowonganService.getLowonganById(id);

        assertTrue(result.isPresent());
        assertEquals("PBO", result.get().getMataKuliah());
    }
}
