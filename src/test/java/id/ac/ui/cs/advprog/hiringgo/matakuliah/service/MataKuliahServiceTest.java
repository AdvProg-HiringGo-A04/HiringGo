package id.ac.ui.cs.advprog.hiringgo.matakuliah.service;

import id.ac.ui.cs.advprog.hiringgo.entity.Dosen;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
    void testCreateMataKuliahSuccess() {
        Mockito.when(mataKuliahRepository.existsById(mataKuliah1.getKodeMataKuliah()))
                .thenReturn(false);

        Mockito.when(mataKuliahRepository.save(mataKuliah1))
                .thenReturn(mataKuliah1);

        MataKuliah mataKuliah = mataKuliahService.createMataKuliah(mataKuliah1);

        assertNotNull(mataKuliah);
        assertEquals(mataKuliah1.getKodeMataKuliah(), mataKuliah.getKodeMataKuliah());
        assertEquals(mataKuliah1.getNamaMataKuliah(), mataKuliah.getNamaMataKuliah());
        assertEquals(mataKuliah1.getDeskripsiMataKuliah(), mataKuliah.getDeskripsiMataKuliah());
        verify(mataKuliahRepository, times(1)).existsById(mataKuliah1.getKodeMataKuliah());
        verify(mataKuliahRepository, times(1)).save(mataKuliah1);
    }

    @Test
    void testCreateMataKuliahWhenKodeIsDuplicate() {
        Mockito.when(mataKuliahRepository.existsById(mataKuliah1.getKodeMataKuliah()))
                .thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            mataKuliahService.createMataKuliah(mataKuliah1);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        verify(mataKuliahRepository, times(1)).existsById(mataKuliah1.getKodeMataKuliah());
        verify(mataKuliahRepository, times(0)).save(mataKuliah1);
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

    @Test
    void testUpdateMataKuliahSuccess() {
        Mockito.when(mataKuliahRepository.findById(mataKuliah1.getKodeMataKuliah()))
                .thenReturn(Optional.of(mataKuliah1));

        Dosen dosen = new Dosen();
        dosen.setId(UUID.randomUUID().toString());
        dosen.setNIP("198403262023012008");
        dosen.setNamaLengkap("John Doe");

        MataKuliah mataKuliahUpdate = new MataKuliah();
        mataKuliahUpdate.setKodeMataKuliah(mataKuliah1.getKodeMataKuliah());
        mataKuliahUpdate.setNamaMataKuliah(mataKuliah1.getNamaMataKuliah() + " Edited");
        mataKuliahUpdate.setDeskripsiMataKuliah(mataKuliah1.getDeskripsiMataKuliah() + " Edited");
        mataKuliahUpdate.setDosenPengampu(List.of(dosen));

        Mockito.when(mataKuliahRepository.save(Mockito.any(MataKuliah.class)))
                .thenReturn(mataKuliahUpdate);

        MataKuliah mataKuliah = mataKuliahService.updateMataKuliah(mataKuliahUpdate.getKodeMataKuliah(), mataKuliahUpdate);

        assertEquals(mataKuliahUpdate.getKodeMataKuliah(), mataKuliah.getKodeMataKuliah());
        assertEquals(mataKuliahUpdate.getNamaMataKuliah(), mataKuliah.getNamaMataKuliah());
        assertEquals(mataKuliahUpdate.getDeskripsiMataKuliah(), mataKuliah.getDeskripsiMataKuliah());
        assertEquals(mataKuliahUpdate.getDosenPengampu().getFirst().getId(), mataKuliah.getDosenPengampu().getFirst().getId());
    }

    @Test
    void testUpdateMataKuliahWhenKodeIsNotFound() {
        Mockito.when(mataKuliahRepository.findById(mataKuliah1.getKodeMataKuliah()))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            mataKuliahService.updateMataKuliah(mataKuliah1.getKodeMataKuliah());
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void testDeleteMataKuliahSuccess() {
        Mockito.when(mataKuliahRepository.findById(mataKuliah1.getKodeMataKuliah()))
                .thenReturn(Optional.of(mataKuliah1));

        doNothing().when(mataKuliahRepository).deleteById(mataKuliah1.getKodeMataKuliah());

        mataKuliahService.deleteMataKuliah(mataKuliah1.getKodeMataKuliah());

        verify(mataKuliahRepository, times(1)).findById(mataKuliah1.getKodeMataKuliah());
        verify(mataKuliahRepository, times(1)).deleteById(mataKuliah1.getKodeMataKuliah());
    }

    @Test
    void testDeleteMataKuliahWhenKodeIsNotFound() {
        Mockito.when(mataKuliahRepository.findById(mataKuliah1.getKodeMataKuliah()))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            mataKuliahService.deleteMataKuliah(mataKuliah1.getKodeMataKuliah());
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(mataKuliahRepository, times(1)).findById(mataKuliah1.getKodeMataKuliah());
        verify(mataKuliahRepository, times(0)).deleteById(mataKuliah1.getKodeMataKuliah());
    }
}
