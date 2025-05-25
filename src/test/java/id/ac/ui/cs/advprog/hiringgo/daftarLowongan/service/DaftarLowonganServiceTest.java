package id.ac.ui.cs.advprog.hiringgo.daftarLowongan.service;

import id.ac.ui.cs.advprog.hiringgo.daftarLowongan.command.DaftarLowonganCommand;
import id.ac.ui.cs.advprog.hiringgo.daftarLowongan.exception.InvalidDataException;
import id.ac.ui.cs.advprog.hiringgo.entity.Lowongan;
import id.ac.ui.cs.advprog.hiringgo.entity.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.repository.LowonganRepository;
import id.ac.ui.cs.advprog.hiringgo.repository.MahasiswaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

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
        DaftarLowonganCommand command = new DaftarLowonganCommand(20, 4.5, "1", "1001");

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> {
            daftarLowonganService.execute(command);
        });

        assertTrue(ex.getErrors().containsKey("ipk"));
        assertEquals("IPK harus antara 0 dan 4", ex.getErrors().get("ipk"));
    }

    @Test
    void testFailedIfSKSNegative() {
        DaftarLowonganCommand command = new DaftarLowonganCommand(-5, 3.0, "1", "1001");

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> {
            daftarLowonganService.execute(command);
        });

        assertTrue(ex.getErrors().containsKey("sks"));
        assertEquals("SKS tidak boleh negatif", ex.getErrors().get("sks"));
    }

    @Test
    void testSuccessForValidData() {
        DaftarLowonganCommand command = new DaftarLowonganCommand(18, 3.25, "low1", "mhs1");

        Lowongan lowongan = new Lowongan();
        Mahasiswa mahasiswa = new Mahasiswa();

        lowongan.setId("low1");
        mahasiswa.setId("mhs1");

        when(lowonganRepository.findById("low1")).thenReturn(Optional.of(lowongan));
        when(mahasiswaRepository.findById("mhs1")).thenReturn(Optional.of(mahasiswa));
        when(lowonganRepository.findPendaftarByLowonganAndMahasiswa("low1", "mhs1")).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> daftarLowonganService.execute(command));

        verify(lowonganRepository, times(1)).save(any(Lowongan.class));
    }

    @Test
    void testFailedIfIPKOver4() {
        DaftarLowonganCommand command = new DaftarLowonganCommand(20, 4.01, "1", "1001");

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> {
            daftarLowonganService.execute(command);
        });

        assertTrue(ex.getErrors().containsKey("ipk"));
        assertEquals("IPK harus antara 0 dan 4", ex.getErrors().get("ipk"));
    }
}
