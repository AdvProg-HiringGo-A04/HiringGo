package id.ac.ui.cs.advprog.hiringgo.matakuliah.service;

import id.ac.ui.cs.advprog.hiringgo.entity.MataKuliah;
import id.ac.ui.cs.advprog.hiringgo.matakuliah.model.CreateMataKuliahRequest;
import id.ac.ui.cs.advprog.hiringgo.matakuliah.model.UpdateMataKuliahRequest;
import id.ac.ui.cs.advprog.hiringgo.repository.MataKuliahRepository;
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
    private MataKuliahServiceImpl mataKuliahService;

    private MataKuliah mataKuliah1;

    private MataKuliah mataKuliah2;

    private MataKuliah updatedMataKuliah1;

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

        updatedMataKuliah1 = new MataKuliah();
        updatedMataKuliah1.setKodeMataKuliah("CSCM602223");
        updatedMataKuliah1.setNamaMataKuliah("Pemrograman Lanjut" + " Edited");
        updatedMataKuliah1.setDeskripsiMataKuliah("Belajar konsep lanjutan pemrograman." + " Edited");
        updatedMataKuliah1.setDosenPengampu(List.of());
    }

    @Test
    void testCreateMataKuliahSuccess() {
        Mockito.when(mataKuliahRepository.existsById(mataKuliah1.getKodeMataKuliah()))
                .thenReturn(false);

        Mockito.when(mataKuliahRepository.save(Mockito.any(MataKuliah.class)))
                .thenReturn(mataKuliah1);

        CreateMataKuliahRequest request = new CreateMataKuliahRequest();
        request.setKodeMataKuliah(mataKuliah1.getKodeMataKuliah());
        request.setNamaMataKuliah(mataKuliah1.getNamaMataKuliah());
        request.setDeskripsiMataKuliah(mataKuliah1.getDeskripsiMataKuliah());
        request.setDosenPengampu(mataKuliah1.getDosenPengampu());

        MataKuliah mataKuliah = mataKuliahService.createMataKuliah(request);

        assertNotNull(mataKuliah);
        assertEquals(mataKuliah1.getKodeMataKuliah(), mataKuliah.getKodeMataKuliah());
        assertEquals(mataKuliah1.getNamaMataKuliah(), mataKuliah.getNamaMataKuliah());
        assertEquals(mataKuliah1.getDeskripsiMataKuliah(), mataKuliah.getDeskripsiMataKuliah());
        verify(mataKuliahRepository, times(1)).existsById(mataKuliah1.getKodeMataKuliah());
        verify(mataKuliahRepository, times(1)).save(Mockito.any(MataKuliah.class));
    }

    @Test
    void testCreateMataKuliahWhenKodeIsDuplicate() {
        Mockito.when(mataKuliahRepository.existsById(mataKuliah1.getKodeMataKuliah()))
                .thenReturn(true);

        CreateMataKuliahRequest request = new CreateMataKuliahRequest();
        request.setKodeMataKuliah(mataKuliah1.getKodeMataKuliah());
        request.setNamaMataKuliah(mataKuliah1.getNamaMataKuliah());
        request.setDeskripsiMataKuliah(mataKuliah1.getDeskripsiMataKuliah());
        request.setDosenPengampu(mataKuliah1.getDosenPengampu());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            mataKuliahService.createMataKuliah(request);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        verify(mataKuliahRepository, times(1)).existsById(mataKuliah1.getKodeMataKuliah());
        verify(mataKuliahRepository, times(0)).save(Mockito.any(MataKuliah.class));
    }

    @Test
    void testFindAllMataKuliahSuccess() {
        Mockito.when(mataKuliahRepository.findAll())
                .thenReturn(List.of(mataKuliah1, mataKuliah2));

        List<MataKuliah> mataKuliah = mataKuliahService.findAll();

        assertNotNull(mataKuliah);
        assertEquals(mataKuliah1.getKodeMataKuliah(), mataKuliah.getFirst().getKodeMataKuliah());
        assertEquals(mataKuliah2.getKodeMataKuliah(), mataKuliah.get(1).getKodeMataKuliah());
        verify(mataKuliahRepository, times(1)).findAll();
    }

    @Test
    void testFindAllMataKuliahEmpty() {
        Mockito.when(mataKuliahRepository.findAll())
                .thenReturn(List.of());

        List<MataKuliah> mataKuliah = mataKuliahService.findAll();

        assertTrue(mataKuliah.isEmpty());
        verify(mataKuliahRepository, times(1)).findAll();
    }

    @Test
    void testFindMataKuliahByKodeSuccess() {
        Mockito.when(mataKuliahRepository.findById(mataKuliah1.getKodeMataKuliah()))
                .thenReturn(Optional.of(mataKuliah1));

        MataKuliah mataKuliah = mataKuliahService.findByKode(mataKuliah1.getKodeMataKuliah());

        assertNotNull(mataKuliah);
        assertNotNull(mataKuliah1.getKodeMataKuliah(), mataKuliah.getKodeMataKuliah());
        verify(mataKuliahRepository, times(1)).findById(mataKuliah1.getKodeMataKuliah());
    }

    @Test
    void testFindMataKuliahByKodeWhenKodeIsNotFound() {
        Mockito.when(mataKuliahRepository.findById(mataKuliah1.getKodeMataKuliah()))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            mataKuliahService.findByKode(mataKuliah1.getKodeMataKuliah());
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(mataKuliahRepository, times(1)).findById(mataKuliah1.getKodeMataKuliah());
    }

    @Test
    void testUpdateAllMataKuliahFieldsSuccess() {
        Mockito.when(mataKuliahRepository.findById(mataKuliah1.getKodeMataKuliah()))
                .thenReturn(Optional.of(mataKuliah1));

        UpdateMataKuliahRequest mataKuliahUpdate = new UpdateMataKuliahRequest();
        mataKuliahUpdate.setNamaMataKuliah(updatedMataKuliah1.getNamaMataKuliah());
        mataKuliahUpdate.setDeskripsiMataKuliah(updatedMataKuliah1.getDeskripsiMataKuliah());
        mataKuliahUpdate.setDosenPengampu(updatedMataKuliah1.getDosenPengampu());

        Mockito.when(mataKuliahRepository.save(Mockito.any(MataKuliah.class)))
                .thenReturn(updatedMataKuliah1);

        MataKuliah mataKuliah = mataKuliahService.updateMataKuliah(mataKuliah1.getKodeMataKuliah(), mataKuliahUpdate);

        assertEquals(mataKuliahUpdate.getNamaMataKuliah(), mataKuliah.getNamaMataKuliah());
        assertEquals(mataKuliahUpdate.getDeskripsiMataKuliah(), mataKuliah.getDeskripsiMataKuliah());
        assertEquals(mataKuliahUpdate.getDosenPengampu().size(), mataKuliah.getDosenPengampu().size());
        verify(mataKuliahRepository, times(1)).findById(mataKuliah1.getKodeMataKuliah());
        verify(mataKuliahRepository, times(1)).save(Mockito.any(MataKuliah.class));
    }

    @Test
    void testUpdateOnlyNamaMataKuliahSuccess() {
        Mockito.when(mataKuliahRepository.findById(mataKuliah1.getKodeMataKuliah()))
                .thenReturn(Optional.of(mataKuliah1));

        UpdateMataKuliahRequest mataKuliahUpdate = new UpdateMataKuliahRequest();
        mataKuliahUpdate.setNamaMataKuliah(updatedMataKuliah1.getNamaMataKuliah());

        Mockito.when(mataKuliahRepository.save(Mockito.any(MataKuliah.class)))
                .thenReturn(updatedMataKuliah1);

        MataKuliah mataKuliah = mataKuliahService.updateMataKuliah(mataKuliah1.getKodeMataKuliah(), mataKuliahUpdate);

        assertEquals(mataKuliahUpdate.getNamaMataKuliah(), mataKuliah.getNamaMataKuliah());
        verify(mataKuliahRepository, times(1)).findById(mataKuliah1.getKodeMataKuliah());
        verify(mataKuliahRepository, times(1)).save(Mockito.any(MataKuliah.class));
    }

    @Test
    void testUpdateOnlyDeskripsiMataKuliahSuccess() {
        Mockito.when(mataKuliahRepository.findById(mataKuliah1.getKodeMataKuliah()))
                .thenReturn(Optional.of(mataKuliah1));

        UpdateMataKuliahRequest mataKuliahUpdate = new UpdateMataKuliahRequest();
        mataKuliahUpdate.setDeskripsiMataKuliah(updatedMataKuliah1.getDeskripsiMataKuliah());

        Mockito.when(mataKuliahRepository.save(Mockito.any(MataKuliah.class)))
                .thenReturn(updatedMataKuliah1);

        MataKuliah mataKuliah = mataKuliahService.updateMataKuliah(mataKuliah1.getKodeMataKuliah(), mataKuliahUpdate);

        assertEquals(mataKuliahUpdate.getDeskripsiMataKuliah(), mataKuliah.getDeskripsiMataKuliah());
        verify(mataKuliahRepository, times(1)).findById(mataKuliah1.getKodeMataKuliah());
        verify(mataKuliahRepository, times(1)).save(Mockito.any(MataKuliah.class));
    }

    @Test
    void testUpdateOnlyDosenPengampuMataKuliahSuccess() {
        Mockito.when(mataKuliahRepository.findById(mataKuliah1.getKodeMataKuliah()))
                .thenReturn(Optional.of(mataKuliah1));

        UpdateMataKuliahRequest mataKuliahUpdate = new UpdateMataKuliahRequest();
        mataKuliahUpdate.setDosenPengampu(updatedMataKuliah1.getDosenPengampu());

        Mockito.when(mataKuliahRepository.save(Mockito.any(MataKuliah.class)))
                .thenReturn(updatedMataKuliah1);

        MataKuliah mataKuliah = mataKuliahService.updateMataKuliah(mataKuliah1.getKodeMataKuliah(), mataKuliahUpdate);

        assertEquals(mataKuliahUpdate.getDosenPengampu().size(), mataKuliah.getDosenPengampu().size());
        verify(mataKuliahRepository, times(1)).findById(mataKuliah1.getKodeMataKuliah());
        verify(mataKuliahRepository, times(1)).save(Mockito.any(MataKuliah.class));
    }

    @Test
    void testUpdateMataKuliahWhenKodeIsNotFound() {
        Mockito.when(mataKuliahRepository.findById(mataKuliah1.getKodeMataKuliah()))
                .thenReturn(Optional.empty());

        UpdateMataKuliahRequest mataKuliahUpdate = new UpdateMataKuliahRequest();
        mataKuliahUpdate.setNamaMataKuliah(updatedMataKuliah1.getNamaMataKuliah());
        mataKuliahUpdate.setDeskripsiMataKuliah(updatedMataKuliah1.getDeskripsiMataKuliah());
        mataKuliahUpdate.setDosenPengampu(updatedMataKuliah1.getDosenPengampu());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            mataKuliahService.updateMataKuliah(mataKuliah1.getKodeMataKuliah(), mataKuliahUpdate);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(mataKuliahRepository, times(1)).findById(mataKuliah1.getKodeMataKuliah());
        verify(mataKuliahRepository, times(0)).save(Mockito.any(MataKuliah.class));
    }

    @Test
    void testDeleteMataKuliahSuccess() {
        Mockito.when(mataKuliahRepository.findById(mataKuliah1.getKodeMataKuliah()))
                .thenReturn(Optional.ofNullable(mataKuliah1));

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
