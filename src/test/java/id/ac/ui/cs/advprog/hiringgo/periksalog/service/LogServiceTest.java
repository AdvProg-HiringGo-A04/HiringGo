package id.ac.ui.cs.advprog.hiringgo.periksalog.service;

import id.ac.ui.cs.advprog.hiringgo.entity.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.entity.Log;  // Fixed import - use the entity package
import id.ac.ui.cs.advprog.hiringgo.entity.Lowongan;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.model.enums.StatusLog;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.model.enums.TipeKategori;
import id.ac.ui.cs.advprog.hiringgo.entity.MataKuliah;
import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogDTO;
import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogStatusUpdateDTO;
import id.ac.ui.cs.advprog.hiringgo.repository.LogRepository;
import id.ac.ui.cs.advprog.hiringgo.repository.MahasiswaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import id.ac.ui.cs.advprog.hiringgo.matakuliah.service.MataKuliahService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LogServiceTest {

    @Mock
    private LogRepository logRepository;

    @Mock
    private MahasiswaRepository mahasiswaRepository;

    @Mock
    private MataKuliahService mataKuliahService;

    @InjectMocks
    private LogServiceImpl logService;

    private Mahasiswa mahasiswa;
    private MataKuliah mataKuliah;
    private Lowongan lowongan;
    private Log log1;
    private Log log2;

    @BeforeEach
    void setUp() {
        // Setup Mahasiswa
        mahasiswa = new Mahasiswa();
        mahasiswa.setId("mahasiswa-123");
        mahasiswa.setNamaLengkap("Mahasiswa Test");
        mahasiswa.setNPM("1234567890");

        // Setup MataKuliah
        mataKuliah = new MataKuliah();
        mataKuliah.setKodeMataKuliah("CS-001");
        mataKuliah.setNamaMataKuliah("Advanced Programming");

        // Setup Lowongan
        lowongan = new Lowongan();
        lowongan.setId("lowongan-123");
        lowongan.setMataKuliah(mataKuliah);

        // Setup Log 1 with proper entity relationships
        log1 = new Log();
        log1.setId("log-1");
        log1.setJudul("Asistensi Lab 1");
        log1.setKeterangan("Membantu mahasiswa dengan lab 1");
        log1.setKategori(TipeKategori.ASISTENSI.name()); // Store as string in entity
        log1.setWaktuMulai(LocalTime.of(10, 0));
        log1.setWaktuSelesai(LocalTime.of(12, 0));
        log1.setTanggalLog(LocalDate.now());
        log1.setStatus(StatusLog.DIPROSES.name()); // Store as string in entity
        log1.setMahasiswa(mahasiswa); // Use relationship instead of ID
        log1.setLowongan(lowongan); // Use relationship instead of direct mata kuliah ID
        log1.setCreatedAt(LocalDate.now());

        // Setup Log 2 with proper entity relationships
        log2 = new Log();
        log2.setId("log-2");
        log2.setJudul("Koreksi Tugas");
        log2.setKeterangan("Mengoreksi tugas 1");
        log2.setKategori(TipeKategori.MENGOREKSI.name()); // Store as string in entity
        log2.setWaktuMulai(LocalTime.of(13, 0));
        log2.setWaktuSelesai(LocalTime.of(15, 0));
        log2.setTanggalLog(LocalDate.now());
        log2.setStatus(StatusLog.DIPROSES.name()); // Store as string in entity
        log2.setMahasiswa(mahasiswa); // Use relationship instead of ID
        log2.setLowongan(lowongan); // Use relationship instead of direct mata kuliah ID
        log2.setCreatedAt(LocalDate.now());
    }

    @Test
    void getAllLogsByDosenId_ShouldReturnListOfLogs() {
        // Arrange
        String dosenId = "dosen-123";
        when(logRepository.findAllLogsByDosenId(dosenId)).thenReturn(Arrays.asList(log1, log2));

        // Act
        List<LogDTO> result = logService.getAllLogsByDosenId(dosenId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Asistensi Lab 1", result.get(0).getJudul());
        assertEquals("Koreksi Tugas", result.get(1).getJudul());
        assertEquals(2.0, result.get(0).getDurationInHours());
        assertEquals(2.0, result.get(1).getDurationInHours());
        assertEquals("Mahasiswa Test", result.get(0).getMahasiswaName());
        assertEquals("Advanced Programming", result.get(0).getMataKuliahName());
        assertEquals("CS-001", result.get(0).getMataKuliahCode());
        assertEquals(TipeKategori.ASISTENSI, result.get(0).getKategori());
        assertEquals(StatusLog.DIPROSES, result.get(0).getStatus());

        verify(logRepository).findAllLogsByDosenId(dosenId);
    }

    @Test
    void getAllLogsByDosenId_WithNullMahasiswa_ShouldReturnUnknownStudent() {
        // Arrange
        String dosenId = "dosen-123";
        log1.setMahasiswa(null); // Set mahasiswa to null
        when(logRepository.findAllLogsByDosenId(dosenId)).thenReturn(Arrays.asList(log1));

        // Act
        List<LogDTO> result = logService.getAllLogsByDosenId(dosenId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Unknown Student", result.get(0).getMahasiswaName());
    }

    @Test
    void getAllLogsByDosenId_WithNullLowongan_ShouldReturnUnknownCourse() {
        // Arrange
        String dosenId = "dosen-123";
        log1.setLowongan(null); // Set lowongan to null
        when(logRepository.findAllLogsByDosenId(dosenId)).thenReturn(Arrays.asList(log1));

        // Act
        List<LogDTO> result = logService.getAllLogsByDosenId(dosenId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Unknown Course", result.get(0).getMataKuliahName());
        assertEquals("Unknown Code", result.get(0).getMataKuliahCode());
    }

    @Test
    void updateLogStatus_WhenLogFound_ShouldUpdateAndReturnUpdatedLog() {
        // Arrange
        String dosenId = "dosen-123";
        String logId = "log-1";
        StatusLog newStatus = StatusLog.DITERIMA;

        LogStatusUpdateDTO updateDTO = new LogStatusUpdateDTO();
        updateDTO.setLogId(logId);
        updateDTO.setStatus(newStatus);

        when(logRepository.isLogOwnedByDosen(logId, dosenId)).thenReturn(true);
        when(logRepository.findById(logId)).thenReturn(Optional.of(log1));
        when(logRepository.save(any(Log.class))).thenAnswer(i -> {
            Log savedLog = (Log) i.getArguments()[0];
            // Verify that the status was updated to string format
            assertEquals(StatusLog.DITERIMA.name(), savedLog.getStatus());
            return savedLog;
        });

        // Act
        LogDTO result = logService.updateLogStatus(dosenId, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(StatusLog.DITERIMA, result.getStatus());

        verify(logRepository).isLogOwnedByDosen(logId, dosenId);
        verify(logRepository).findById(logId);
        verify(logRepository).save(any(Log.class));
    }

    @Test
    void updateLogStatus_WhenLogNotOwnedByDosen_ShouldThrowSecurityException() {
        // Arrange
        String dosenId = "dosen-123";
        String logId = "log-1";

        LogStatusUpdateDTO updateDTO = new LogStatusUpdateDTO();
        updateDTO.setLogId(logId);
        updateDTO.setStatus(StatusLog.DITERIMA);

        when(logRepository.isLogOwnedByDosen(logId, dosenId)).thenReturn(false);

        // Act & Assert
        SecurityException exception = assertThrows(SecurityException.class, () -> {
            logService.updateLogStatus(dosenId, updateDTO);
        });

        assertEquals("You don't have permission to update this log", exception.getMessage());

        verify(logRepository).isLogOwnedByDosen(logId, dosenId);
        verify(logRepository, never()).findById(anyString());
        verify(logRepository, never()).save(any(Log.class));
    }

    @Test
    void updateLogStatus_WhenLogNotFound_ShouldThrowNoSuchElementException() {
        // Arrange
        String dosenId = "dosen-123";
        String logId = "non-existent-log";

        LogStatusUpdateDTO updateDTO = new LogStatusUpdateDTO();
        updateDTO.setLogId(logId);
        updateDTO.setStatus(StatusLog.DITERIMA);

        when(logRepository.isLogOwnedByDosen(logId, dosenId)).thenReturn(true);
        when(logRepository.findById(logId)).thenReturn(Optional.empty());

        // Act & Assert
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            logService.updateLogStatus(dosenId, updateDTO);
        });

        assertTrue(exception.getMessage().contains("Log not found with ID"));
        assertTrue(exception.getMessage().contains(logId));

        verify(logRepository).isLogOwnedByDosen(logId, dosenId);
        verify(logRepository).findById(logId);
        verify(logRepository, never()).save(any(Log.class));
    }

    @Test
    void isLogOwnedByDosen_ShouldDelegateToRepository() {
        // Arrange
        String dosenId = "dosen-123";
        String logId = "log-1";
        when(logRepository.isLogOwnedByDosen(logId, dosenId)).thenReturn(true);

        // Act
        boolean result = logService.isLogOwnedByDosen(logId, dosenId);

        // Assert
        assertTrue(result);
        verify(logRepository).isLogOwnedByDosen(logId, dosenId);
    }

    @Test
    void isLogOwnedByDosen_WhenNotOwned_ShouldReturnFalse() {
        // Arrange
        String dosenId = "dosen-123";
        String logId = "log-1";
        when(logRepository.isLogOwnedByDosen(logId, dosenId)).thenReturn(false);

        // Act
        boolean result = logService.isLogOwnedByDosen(logId, dosenId);

        // Assert
        assertFalse(result);
        verify(logRepository).isLogOwnedByDosen(logId, dosenId);
    }

    @Test
    void getAllLogsByDosenId_WithEmptyDosenId_ShouldThrowIllegalArgumentException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            logService.getAllLogsByDosenId("");
        });

        assertEquals("Dosen ID cannot be null or empty", exception.getMessage());
        verify(logRepository, never()).findAllLogsByDosenId(anyString());
    }

    @Test
    void getAllLogsByDosenId_WithNullDosenId_ShouldThrowIllegalArgumentException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            logService.getAllLogsByDosenId(null);
        });

        assertEquals("Dosen ID cannot be null or empty", exception.getMessage());
        verify(logRepository, never()).findAllLogsByDosenId(anyString());
    }

    @Test
    void updateLogStatus_WithNullDTO_ShouldThrowIllegalArgumentException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            logService.updateLogStatus("dosen-123", null);
        });

        assertEquals("LogStatusUpdateDTO cannot be null", exception.getMessage());
    }

    @Test
    void updateLogStatus_WithNullStatus_ShouldThrowIllegalArgumentException() {
        // Arrange
        LogStatusUpdateDTO updateDTO = new LogStatusUpdateDTO();
        updateDTO.setLogId("log-1");
        updateDTO.setStatus(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            logService.updateLogStatus("dosen-123", updateDTO);
        });

        assertEquals("Status cannot be null", exception.getMessage());
    }

    @Test
    void parseKategoriFromString_WithInvalidValue_ShouldReturnDefault() {
        // This is testing the private method indirectly through convertToDTO
        // Arrange
        String dosenId = "dosen-123";
        log1.setKategori("INVALID_KATEGORI");
        when(logRepository.findAllLogsByDosenId(dosenId)).thenReturn(Arrays.asList(log1));

        // Act
        List<LogDTO> result = logService.getAllLogsByDosenId(dosenId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        // Should return the first enum value as default
        assertEquals(TipeKategori.values()[0], result.get(0).getKategori());
    }

    @Test
    void parseStatusFromString_WithInvalidValue_ShouldReturnDefault() {
        // This is testing the private method indirectly through convertToDTO
        // Arrange
        String dosenId = "dosen-123";
        log1.setStatus("INVALID_STATUS");
        when(logRepository.findAllLogsByDosenId(dosenId)).thenReturn(Arrays.asList(log1));

        // Act
        List<LogDTO> result = logService.getAllLogsByDosenId(dosenId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        // Should return the first enum value as default
        assertEquals(StatusLog.values()[0], result.get(0).getStatus());
    }
}