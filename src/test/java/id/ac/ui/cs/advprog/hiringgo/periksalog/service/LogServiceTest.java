package id.ac.ui.cs.advprog.hiringgo.periksalog.service;

import id.ac.ui.cs.advprog.hiringgo.entity.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.model.Log;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.model.enums.StatusLog;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.model.enums.TipeKategori;
import id.ac.ui.cs.advprog.hiringgo.entity.MataKuliah;
import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogDTO;
import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogStatusUpdateDTO;
import id.ac.ui.cs.advprog.hiringgo.periksalog.repository.LogRepository;
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

    private Log log1;
    private Log log2;

    @BeforeEach
    void setUp() {
        mahasiswa = new Mahasiswa();
        mahasiswa.setId("mahasiswa-123");
        mahasiswa.setNamaLengkap("Mahasiswa Test");
        mahasiswa.setNPM("1234567890");

        mataKuliah = new MataKuliah();
        mataKuliah.setKodeMataKuliah("CS-001");
        mataKuliah.setNamaMataKuliah("Advanced Programming");

        log1 = new Log();
        log1.setId("log-1");
        log1.setJudul("Asistensi Lab 1");
        log1.setKeterangan("Membantu mahasiswa dengan lab 1");
        log1.setKategori(TipeKategori.ASISTENSI);
        log1.setWaktuMulai(LocalTime.of(10, 0));
        log1.setWaktuSelesai(LocalTime.of(12, 0));
        log1.setTanggalLog(LocalDate.now());
        log1.setStatus(StatusLog.DIPROSES);
        log1.setMahasiswaId("mahasiswa-123");
        log1.setMataKuliahId("CS-001");
        log1.setCreatedAt(LocalDate.now());

        log2 = new Log();
        log2.setId("log-2");
        log2.setJudul("Koreksi Tugas");
        log2.setKeterangan("Mengoreksi tugas 1");
        log2.setKategori(TipeKategori.MENGOREKSI);
        log2.setWaktuMulai(LocalTime.of(13, 0));
        log2.setWaktuSelesai(LocalTime.of(15, 0));
        log2.setTanggalLog(LocalDate.now());
        log2.setStatus(StatusLog.DIPROSES);
        log2.setMahasiswaId("mahasiswa-123");
        log2.setMataKuliahId("CS-001");
        log2.setCreatedAt(LocalDate.now());
    }
    @Test
    void getAllLogsByDosenId_ShouldReturnListOfLogs() {
        // Arrange
        String dosenId = "dosen-123";
        when(logRepository.findAllLogsByDosenId(dosenId)).thenReturn(Arrays.asList(log1, log2));
        when(mahasiswaRepository.findById("mahasiswa-123")).thenReturn(Optional.of(mahasiswa));
        when(mataKuliahService.findByKode("CS-001")).thenReturn(mataKuliah);

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

        verify(logRepository).findAllLogsByDosenId(dosenId);
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
        when(logRepository.save(any(Log.class))).thenAnswer(i -> i.getArguments()[0]);
        when(mahasiswaRepository.findById("mahasiswa-123")).thenReturn(Optional.of(mahasiswa));
        when(mataKuliahService.findByKode("CS-001")).thenReturn(mataKuliah);

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

        // Update assertion to match the new error message format
        assertTrue(exception.getMessage().contains("Log not found with ID"));

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
}