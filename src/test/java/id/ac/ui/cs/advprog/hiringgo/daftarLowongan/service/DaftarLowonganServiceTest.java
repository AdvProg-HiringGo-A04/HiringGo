package id.ac.ui.cs.advprog.hiringgo.daftarLowongan.service;

import id.ac.ui.cs.advprog.hiringgo.daftarLowongan.command.DaftarLowonganCommand;
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

    @Test
    void testFailedIfLowonganNotFound() {
        DaftarLowonganCommand command = new DaftarLowonganCommand(20, 3.5, "invalidLowongan", "1001");

        when(lowonganRepository.findById("invalidLowongan")).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            daftarLowonganService.execute(command);
        });

        assertTrue(ex.getErrors().containsKey("lowonganId"));
        assertEquals("Lowongan tidak ditemukan", ex.getErrors().get("lowonganId"));
    }

    @Test
    void testFailedIfMahasiswaNotFound() {
        DaftarLowonganCommand command = new DaftarLowonganCommand(20, 3.5, "low1", "invalidMahasiswa");

        when(lowonganRepository.findById("low1")).thenReturn(Optional.of(new Lowongan()));
        when(mahasiswaRepository.findById("invalidMahasiswa")).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            daftarLowonganService.execute(command);
        });

        assertTrue(ex.getErrors().containsKey("mahasiswaId"));
        assertEquals("Mahasiswa tidak ditemukan", ex.getErrors().get("mahasiswaId"));
    }

    @Test
    void testFailedIfMahasiswaAlreadyRegistered() {
        DaftarLowonganCommand command = new DaftarLowonganCommand(20, 3.5, "low1", "mhs1");

        Lowongan lowongan = new Lowongan();
        Mahasiswa mahasiswa = new Mahasiswa();

        lowongan.setId("low1");
        mahasiswa.setId("mhs1");

        when(lowonganRepository.findById("low1")).thenReturn(Optional.of(lowongan));
        when(mahasiswaRepository.findById("mhs1")).thenReturn(Optional.of(mahasiswa));
        when(lowonganRepository.findPendaftarByLowonganAndMahasiswa("low1", "mhs1")).thenReturn(Optional.of(new PendaftarLowongan()));

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            daftarLowonganService.execute(command);
        });

        assertTrue(ex.getErrors().containsKey("mahasiswaId"));
        assertEquals("Mahasiswa sudah mendaftar ke lowongan ini", ex.getErrors().get("mahasiswaId"));
    }

    @Test
    void testAsyncExecutionSuccess() throws Exception {
        DaftarLowonganCommand command = new DaftarLowonganCommand(20, 3.5, "low1", "mhs1");

        Lowongan lowongan = new Lowongan();
        Mahasiswa mahasiswa = new Mahasiswa();

        lowongan.setId("low1");
        mahasiswa.setId("mhs1");

        when(lowonganRepository.findById("low1")).thenReturn(Optional.of(lowongan));
        when(mahasiswaRepository.findById("mhs1")).thenReturn(Optional.of(mahasiswa));
        when(lowonganRepository.findPendaftarByLowonganAndMahasiswa("low1", "mhs1")).thenReturn(Optional.empty());

        CompletableFuture<Void> future = daftarLowonganService.executeAsync(command);

        // Ensure the async task completes successfully
        assertDoesNotThrow(() -> future.get());
        verify(lowonganRepository, times(1)).save(any(Lowongan.class));
    }

    @Test
    void testAsyncExecutionFailure() throws Exception {
        DaftarLowonganCommand command = new DaftarLowonganCommand(20, 3.5, "invalidLowongan", "1001");

        when(lowonganRepository.findById("invalidLowongan")).thenReturn(Optional.empty());

        CompletableFuture<Void> future = daftarLowonganService.executeAsync(command);

        // Ensure the async task fails
        assertThrows(Exception.class, () -> future.get());
    }

    @Test
    void testExecuteAsyncBatchSuccess() throws Exception {
        DaftarLowonganCommand command1 = new DaftarLowonganCommand(20, 3.5, "low1", "mhs1");
        DaftarLowonganCommand command2 = new DaftarLowonganCommand(18, 3.0, "low2", "mhs2");

        List<DaftarLowonganCommand> commands = Arrays.asList(command1, command2);

        Lowongan lowongan1 = new Lowongan();
        Lowongan lowongan2 = new Lowongan();
        Mahasiswa mahasiswa1 = new Mahasiswa();
        Mahasiswa mahasiswa2 = new Mahasiswa();

        lowongan1.setId("low1");
        lowongan2.setId("low2");
        mahasiswa1.setId("mhs1");
        mahasiswa2.setId("mhs2");

        when(lowonganRepository.findById("low1")).thenReturn(Optional.of(lowongan1));
        when(lowonganRepository.findById("low2")).thenReturn(Optional.of(lowongan2));
        when(mahasiswaRepository.findById("mhs1")).thenReturn(Optional.of(mahasiswa1));
        when(mahasiswaRepository.findById("mhs2")).thenReturn(Optional.of(mahasiswa2));
        when(lowonganRepository.findPendaftarByLowonganAndMahasiswa("low1", "mhs1")).thenReturn(Optional.empty());
        when(lowonganRepository.findPendaftarByLowonganAndMahasiswa("low2", "mhs2")).thenReturn(Optional.empty());

        CompletableFuture<Map<String, String>> future = daftarLowonganService.executeAsyncBatch(commands);

        // Ensure the async task completes successfully
        Map<String, String> result = future.get();
        assertEquals("SUCCESS", result.get("low1"));
        assertEquals("SUCCESS", result.get("low2"));
    }

    @Test
    void testExecuteAsyncBatchWithFailure() throws Exception {
        DaftarLowonganCommand command1 = new DaftarLowonganCommand(20, 3.5, "low1", "mhs1");
        DaftarLowonganCommand command2 = new DaftarLowonganCommand(-5, 3.0, "low2", "mhs2"); // Invalid SKS

        List<DaftarLowonganCommand> commands = Arrays.asList(command1, command2);

        Lowongan lowongan1 = new Lowongan();
        Mahasiswa mahasiswa1 = new Mahasiswa();
        Mahasiswa mahasiswa2 = new Mahasiswa();

        lowongan1.setId("low1");
        mahasiswa1.setId("mhs1");
        mahasiswa2.setId("mhs2");

        when(lowonganRepository.findById("low1")).thenReturn(Optional.of(lowongan1));
        when(mahasiswaRepository.findById("mhs1")).thenReturn(Optional.of(mahasiswa1));
        when(mahasiswaRepository.findById("mhs2")).thenReturn(Optional.of(mahasiswa2));
        when(lowonganRepository.findPendaftarByLowonganAndMahasiswa("low1", "mhs1")).thenReturn(Optional.empty());
        when(lowonganRepository.findPendaftarByLowonganAndMahasiswa("low2", "mhs2")).thenReturn(Optional.empty());

        // The second command should fail due to invalid SKS value
        CompletableFuture<Map<String, String>> future = daftarLowonganService.executeAsyncBatch(commands);

        Map<String, String> result = future.get();
        assertEquals("SUCCESS", result.get("low1"));
        assertTrue(result.get("low2").startsWith("FAILED"));
    }

    @Test
    void testExecuteAsyncBatchWithMultipleFailures() throws Exception {
        DaftarLowonganCommand command1 = new DaftarLowonganCommand(20, 3.5, "low1", "mhs1");
        DaftarLowonganCommand command2 = new DaftarLowonganCommand(-5, 3.0, "low2", "mhs2"); // Invalid SKS
        DaftarLowonganCommand command3 = new DaftarLowonganCommand(20, 3.5, "low3", "mhs3"); // Invalid Lowongan ID

        List<DaftarLowonganCommand> commands = Arrays.asList(command1, command2, command3);

        Lowongan lowongan1 = new Lowongan();
        Lowongan lowongan2 = new Lowongan();
        Mahasiswa mahasiswa1 = new Mahasiswa();
        Mahasiswa mahasiswa2 = new Mahasiswa();

        lowongan1.setId("low1");
        lowongan2.setId("low3"); // Invalid Lowongan ID
        mahasiswa1.setId("mhs1");
        mahasiswa2.setId("mhs2");

        when(lowonganRepository.findById("low1")).thenReturn(Optional.of(lowongan1));
        when(mahasiswaRepository.findById("mhs1")).thenReturn(Optional.of(mahasiswa1));
        when(mahasiswaRepository.findById("mhs2")).thenReturn(Optional.of(mahasiswa2));
        when(lowonganRepository.findPendaftarByLowonganAndMahasiswa("low1", "mhs1")).thenReturn(Optional.empty());
        when(lowonganRepository.findPendaftarByLowonganAndMahasiswa("low2", "mhs2")).thenReturn(Optional.empty());
        when(lowonganRepository.findById("low3")).thenReturn(Optional.empty()); // For Invalid Lowongan ID

        CompletableFuture<Map<String, String>> future = daftarLowonganService.executeAsyncBatch(commands);

        Map<String, String> result = future.get();
        assertEquals("SUCCESS", result.get("low1"));
        assertTrue(result.get("low2").startsWith("FAILED"));
        assertTrue(result.get("low3").startsWith("FAILED"));
    }
}
