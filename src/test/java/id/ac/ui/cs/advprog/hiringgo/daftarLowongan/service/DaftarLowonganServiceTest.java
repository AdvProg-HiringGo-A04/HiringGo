package id.ac.ui.cs.advprog.hiringgo.daftarLowongan.service;

import id.ac.ui.cs.advprog.hiringgo.daftarLowongan.exception.EntityNotFoundException;
import id.ac.ui.cs.advprog.hiringgo.daftarLowongan.exception.InvalidDataException;
import id.ac.ui.cs.advprog.hiringgo.entity.Lowongan;
import id.ac.ui.cs.advprog.hiringgo.entity.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.entity.PendaftarLowongan;
import id.ac.ui.cs.advprog.hiringgo.repository.MahasiswaRepository;
import id.ac.ui.cs.advprog.hiringgo.repository.LowonganRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DaftarLowonganServiceTest {

    private LowonganRepository lowonganRepository;
    private MahasiswaRepository mahasiswaRepository;
    private DaftarLowonganService daftarLowonganService;

    @BeforeEach
    void setUp() {
        lowonganRepository = Mockito.mock(LowonganRepository.class);
        mahasiswaRepository = Mockito.mock(MahasiswaRepository.class);
        daftarLowonganService = new DaftarLowonganService(lowonganRepository, mahasiswaRepository);
    }

    @Test
    void testFailedIfIPKInvalid() {
        int sks = 20;
        double ipk = 4.5;
        String lowonganId = "1";
        String mahasiswaId = "1001";

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> {
            daftarLowonganService.registerToLowongan(sks, ipk, lowonganId, mahasiswaId);
        });

        assertTrue(ex.getErrors().containsKey("ipk"));
        assertEquals("IPK harus antara 0 dan 4", ex.getErrors().get("ipk"));
    }

    @Test
    void testFailedIfSKSNegative() {
        int sks = -5;
        double ipk = 3.0;
        String lowonganId = "1";
        String mahasiswaId = "1001";

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> {
            daftarLowonganService.registerToLowongan(sks, ipk, lowonganId, mahasiswaId);
        });

        assertTrue(ex.getErrors().containsKey("sks"));
        assertEquals("SKS tidak boleh negatif", ex.getErrors().get("sks"));
    }

    @Test
    void testSuccessForValidData() {
        int sks = 18;
        double ipk = 3.25;
        String lowonganId = "low1";
        String mahasiswaId = "mhs1";

        Lowongan lowongan = new Lowongan();
        Mahasiswa mahasiswa = new Mahasiswa();

        lowongan.setId(lowonganId);
        mahasiswa.setId(mahasiswaId);

        when(lowonganRepository.findById(lowonganId)).thenReturn(Optional.of(lowongan));
        when(mahasiswaRepository.findById(mahasiswaId)).thenReturn(Optional.of(mahasiswa));
        when(lowonganRepository.findPendaftarByLowonganAndMahasiswa(lowonganId, mahasiswaId)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> daftarLowonganService.registerToLowongan(sks, ipk, lowonganId, mahasiswaId));

        verify(lowonganRepository, times(1)).save(any(Lowongan.class));
    }

    @Test
    void testFailedIfIPKOver4() {
        int sks = 20;
        double ipk = 4.01;
        String lowonganId = "1";
        String mahasiswaId = "1001";

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> {
            daftarLowonganService.registerToLowongan(sks, ipk, lowonganId, mahasiswaId);
        });

        assertTrue(ex.getErrors().containsKey("ipk"));
        assertEquals("IPK harus antara 0 dan 4", ex.getErrors().get("ipk"));
    }

    @Test
    void testFailedIfLowonganNotFound() {
        int sks = 20;
        double ipk = 3.5;
        String lowonganId = "invalidLowongan";
        String mahasiswaId = "1001";

        when(lowonganRepository.findById(lowonganId)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            daftarLowonganService.registerToLowongan(sks, ipk, lowonganId, mahasiswaId);
        });

        assertTrue(ex.getErrors().containsKey("lowonganId"));
        assertEquals("Lowongan tidak ditemukan", ex.getErrors().get("lowonganId"));
    }

    @Test
    void testFailedIfMahasiswaNotFound() {
        int sks = 20;
        double ipk = 3.5;
        String lowonganId = "low1";
        String mahasiswaId = "invalidMahasiswa";

        when(lowonganRepository.findById(lowonganId)).thenReturn(Optional.of(new Lowongan()));
        when(mahasiswaRepository.findById(mahasiswaId)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            daftarLowonganService.registerToLowongan(sks, ipk, lowonganId, mahasiswaId);
        });

        assertTrue(ex.getErrors().containsKey("mahasiswaId"));
        assertEquals("Mahasiswa tidak ditemukan", ex.getErrors().get("mahasiswaId"));
    }

    @Test
    void testFailedIfMahasiswaAlreadyRegistered() {
        int sks = 20;
        double ipk = 3.5;
        String lowonganId = "low1";
        String mahasiswaId = "mhs1";

        Lowongan lowongan = new Lowongan();
        Mahasiswa mahasiswa = new Mahasiswa();

        lowongan.setId(lowonganId);
        mahasiswa.setId(mahasiswaId);

        when(lowonganRepository.findById(lowonganId)).thenReturn(Optional.of(lowongan));
        when(mahasiswaRepository.findById(mahasiswaId)).thenReturn(Optional.of(mahasiswa));
        when(lowonganRepository.findPendaftarByLowonganAndMahasiswa(lowonganId, mahasiswaId)).thenReturn(Optional.of(new PendaftarLowongan()));

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            daftarLowonganService.registerToLowongan(sks, ipk, lowonganId, mahasiswaId);
        });

        assertTrue(ex.getErrors().containsKey("mahasiswaId"));
        assertEquals("Mahasiswa sudah mendaftar ke lowongan ini", ex.getErrors().get("mahasiswaId"));
    }
}
