package id.ac.ui.cs.advprog.hiringgo.matakuliah.service;

import id.ac.ui.cs.advprog.hiringgo.entity.MataKuliah;
import id.ac.ui.cs.advprog.hiringgo.repository.MataKuliahRepository;
import id.ac.ui.cs.advprog.hiringgo.service.MataKuliahService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class MataKuliahServiceTest {

    @Mock
    private MataKuliahRepository mataKuliahRepository;

    @InjectMocks
    private MataKuliahService mataKuliahService;

    private MataKuliah mataKuliah1;

    private MataKuliah mataKuliah2;

    @BeforeEach
    void setUp() {
        mataKuliah1 = new MataKuliah();
        mataKuliah1.setKodeMataKuliah("CSCM602223");
        mataKuliah1.setNamaMataKuliah("Pemrograman Lanjut");
        mataKuliah1.setDeskripsiMataKuliah("Belajar konsep lanjutan pemrograman.");
        mataKuliah1.setDosenPengampu(List.of());

        mataKuliah2 = new MataKuliah();
        mataKuliah2.setKodeMataKuliah("CSGE601021");
        mataKuliah2.setNamaMataKuliah("Dasar-dasar Pemrograman 2");
        mataKuliah2.setDeskripsiMataKuliah("Belajar dasar pemrograman 2.");
        mataKuliah2.setDosenPengampu(List.of());
    }

    @Test
    void testFindAllMataKuliahSuccess() {
        Mockito.when(mataKuliahRepository.findAll())
                .thenReturn(List.of(mataKuliah1, mataKuliah2));

        List<MataKuliah> mataKuliah = mataKuliahService.findAll();

        assertNotNull(mataKuliah);
        assertEquals(mataKuliah1.getKodeMataKuliah(), mataKuliah.getFirst().getKodeMataKuliah());
        assertEquals(mataKuliah2.getKodeMataKuliah(), mataKuliah.get(1).getKodeMataKuliah());
    }

    @Test
    void testFindAllMataKuliahEmpty() {
        Mockito.when(mataKuliahRepository.findAll())
                .thenReturn(List.of());

        List<MataKuliah> mataKuliah = mataKuliahService.findAll();

        assertNull(mataKuliah);
    }

    @Test
    void testFindMataKuliahByKodeSuccess() {
        Mockito.when(mataKuliahRepository.findById(mataKuliah1.getKodeMataKuliah()))
                .thenReturn(Optional.of(mataKuliah1));

        Optional<MataKuliah> mataKuliah = mataKuliahService.findByKode(mataKuliah1.getKodeMataKuliah());

        assertTrue(mataKuliah.isPresent());
        assertNotNull(mataKuliah1.getKodeMataKuliah(), mataKuliah.get().getKodeMataKuliah());
    }

    @Test
    void testFindMataKuliahByKodeWhenKodeIsNotFound() {
        Mockito.when(mataKuliahRepository.findById(mataKuliah1.getKodeMataKuliah()))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            mataKuliahService.findByKode(mataKuliah1.getKodeMataKuliah());
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}
