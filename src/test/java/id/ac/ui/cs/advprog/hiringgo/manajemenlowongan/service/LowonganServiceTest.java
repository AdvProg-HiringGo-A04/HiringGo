package id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.service;

import id.ac.ui.cs.advprog.hiringgo.entity.MataKuliah;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.dto.LowonganForm;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.entity.Lowongan;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.repository.LowonganRepository;
import id.ac.ui.cs.advprog.hiringgo.matakuliah.service.MataKuliahService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LowonganServiceTest {

    private LowonganRepository lowonganRepository;
    private MataKuliahService mataKuliahService; // NEW
    private LowonganService lowonganService;

    private MataKuliah dummyMK;

    @BeforeEach
    void setUp() {
        lowonganRepository = mock(LowonganRepository.class);
        mataKuliahService = mock(MataKuliahService.class);
        lowonganService = new LowonganService(lowonganRepository, mataKuliahService);

        dummyMK = new MataKuliah();
        dummyMK.setKodeMataKuliah("CS101");
        dummyMK.setNamaMataKuliah("Pemrograman Lanjut");
    }

    @Test
    void testCreateLowongan_success() {
        LowonganForm form = LowonganForm.builder()
            .kodeMataKuliah("CS101")
            .tahunAjaran("2024/2025")
            .semester("Ganjil")
            .jumlahAsistenDibutuhkan(3)
            .build();

        when(mataKuliahService.findByKode("CS101")).thenReturn(dummyMK);
        when(lowonganRepository.existsByMataKuliahAndSemesterAndTahunAjaran(dummyMK, "Ganjil", "2024/2025"))
            .thenReturn(false);
        when(lowonganRepository.save(any(Lowongan.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        Lowongan created = lowonganService.createLowongan(form);

        assertEquals(dummyMK, created.getMataKuliah());
        assertEquals("Ganjil", created.getSemester());
        assertEquals("2024/2025", created.getTahunAjaran());
        assertEquals(3, created.getJumlahDibutuhkan());
    }

    @Test
    void testCreateLowongan_duplicate_throwsException() {
        LowonganForm form = LowonganForm.builder()
            .kodeMataKuliah("CS101")
            .tahunAjaran("2024/2025")
            .semester("Ganjil")
            .jumlahAsistenDibutuhkan(3)
            .build();

        when(mataKuliahService.findByKode("CS101")).thenReturn(dummyMK);
        when(lowonganRepository.existsByMataKuliahAndSemesterAndTahunAjaran(dummyMK, "Ganjil", "2024/2025"))
            .thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> lowonganService.createLowongan(form));
    }

    @Test
    void testUpdateLowongan_success() {
        String id = UUID.randomUUID().toString();
        Lowongan existing = Lowongan.builder()
            .id(id)
            .mataKuliah(dummyMK)
            .tahunAjaran("2024/2025")
            .semester("Ganjil")
            .jumlahDibutuhkan(2)
            .build();

        MataKuliah newMK = new MataKuliah();
        newMK.setKodeMataKuliah("CS102");
        newMK.setNamaMataKuliah("Algoritma");

        LowonganForm form = LowonganForm.builder()
            .kodeMataKuliah("CS102")
            .tahunAjaran("2024/2025")
            .semester("Genap")
            .jumlahAsistenDibutuhkan(5)
            .build();

        when(lowonganRepository.findById(id)).thenReturn(Optional.of(existing));
        when(mataKuliahService.findByKode("CS102")).thenReturn(newMK);
        when(lowonganRepository.existsByMataKuliahAndSemesterAndTahunAjaran(newMK, "Genap", "2024/2025"))
            .thenReturn(false);
        when(lowonganRepository.save(any(Lowongan.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        Lowongan updated = lowonganService.updateLowongan(id, form);

        assertEquals(newMK, updated.getMataKuliah());
        assertEquals("Genap", updated.getSemester());
        assertEquals("2024/2025", updated.getTahunAjaran());
        assertEquals(5, updated.getJumlahDibutuhkan());
    }

    @Test
    void testUpdateLowongan_notFound_throwsException() {
        String id = UUID.randomUUID().toString();
        LowonganForm form = LowonganForm.builder()
            .kodeMataKuliah("CS103")
            .tahunAjaran("2024/2025")
            .semester("Genap")
            .jumlahAsistenDibutuhkan(1)
            .build();

        when(lowonganRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> lowonganService.updateLowongan(id, form));
    }
}
