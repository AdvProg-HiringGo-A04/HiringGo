package id.ac.ui.cs.advprog.hiringgo.periksalog.service;

import id.ac.ui.cs.advprog.hiringgo.common.model.*;
import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogDTO;
import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogStatusUpdateDTO;
import id.ac.ui.cs.advprog.hiringgo.periksalog.repository.LogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @InjectMocks
    private LogServiceImpl logService;

    private User dosen;
    private User mahasiswa;
    private MataKuliah mataKuliah;
    private Lowongan lowongan;
    private Log log1;
    private Log log2;

    @BeforeEach
    void setUp() {
        dosen = User.builder()
                .id(1L)
                .fullName("Dosen Test")
                .email("dosen@test.com")
                .role(UserRole.DOSEN)
                .build();

        mahasiswa = User.builder()
                .id(2L)
                .fullName("Mahasiswa Test")
                .email("mahasiswa@test.com")
                .role(UserRole.MAHASISWA)
                .build();

        mataKuliah = MataKuliah.builder()
                .id(1L)
                .name("Advanced Programming")
                .code("CS-001")
                .build();

        lowongan = Lowongan.builder()
                .id(1L)
                .mataKuliah(mataKuliah)
                .tahunAjaran(2025)
                .semester("GENAP")
                .build();

        log1 = Log.builder()
                .id(1L)
                .judul("Asistensi Lab 1")
                .keterangan("Membantu mahasiswa dengan lab 1")
                .kategori(LogCategory.ASISTENSI)
                .waktuMulai(LocalTime.of(10, 0))
                .waktuSelesai(LocalTime.of(12, 0))
                .tanggalLog(LocalDate.now())
                .status(LogStatus.PENDING)
                .mahasiswa(mahasiswa)
                .lowongan(lowongan)
                .build();

        log2 = Log.builder()
                .id(2L)
                .judul("Koreksi Tugas")
                .keterangan("Mengoreksi tugas 1")
                .kategori(LogCategory.MENGOREKSI)
                .waktuMulai(LocalTime.of(13, 0))
                .waktuSelesai(LocalTime.of(15, 0))
                .tanggalLog(LocalDate.now())
                .status(LogStatus.PENDING)
                .mahasiswa(mahasiswa)
                .lowongan(lowongan)
                .build();
    }

    @Test
    void getAllLogsByDosenId_ShouldReturnListOfLogs() {
        // Arrange
        Long dosenId = 1L;
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

        verify(logRepository).findAllLogsByDosenId(dosenId);
    }

    @Test
    void updateLogStatus_WhenLogFound_ShouldUpdateAndReturnUpdatedLog() {
        // Arrange
        Long dosenId = 1L;
        Long logId = 1L;
        LogStatus newStatus = LogStatus.APPROVED;

        LogStatusUpdateDTO updateDTO = new LogStatusUpdateDTO();
        updateDTO.setLogId(logId);
        updateDTO.setStatus(newStatus);

        when(logRepository.isLogOwnedByDosen(logId, dosenId)).thenReturn(true);
        when(logRepository.findById(logId)).thenReturn(Optional.of(log1));
        when(logRepository.save(any(Log.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        LogDTO result = logService.updateLogStatus(dosenId, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(LogStatus.APPROVED, result.getStatus());

        verify(logRepository).isLogOwnedByDosen(logId, dosenId);
        verify(logRepository).findById(logId);
        verify(logRepository).save(any(Log.class));
    }

    @Test
    void updateLogStatus_WhenLogNotOwnedByDosen_ShouldThrowSecurityException() {
        // Arrange
        Long dosenId = 1L;
        Long logId = 1L;

        LogStatusUpdateDTO updateDTO = new LogStatusUpdateDTO();
        updateDTO.setLogId(logId);
        updateDTO.setStatus(LogStatus.APPROVED);

        when(logRepository.isLogOwnedByDosen(logId, dosenId)).thenReturn(false);

        // Act & Assert
        SecurityException exception = assertThrows(SecurityException.class, () -> {
            logService.updateLogStatus(dosenId, updateDTO);
        });

        assertEquals("You don't have permission to update this log", exception.getMessage());

        verify(logRepository).isLogOwnedByDosen(logId, dosenId);
        verify(logRepository, never()).findById(anyLong());
        verify(logRepository, never()).save(any(Log.class));
    }

    @Test
    void updateLogStatus_WhenLogNotFound_ShouldThrowNoSuchElementException() {
        // Arrange
        Long dosenId = 1L;
        Long logId = 999L;

        LogStatusUpdateDTO updateDTO = new LogStatusUpdateDTO();
        updateDTO.setLogId(logId);
        updateDTO.setStatus(LogStatus.APPROVED);

        when(logRepository.isLogOwnedByDosen(logId, dosenId)).thenReturn(true);
        when(logRepository.findById(logId)).thenReturn(Optional.empty());

        // Act & Assert
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            logService.updateLogStatus(dosenId, updateDTO);
        });

        assertEquals("Log not found", exception.getMessage());

        verify(logRepository).isLogOwnedByDosen(logId, dosenId);
        verify(logRepository).findById(logId);
        verify(logRepository, never()).save(any(Log.class));
    }

    @Test
    void isLogOwnedByDosen_ShouldDelegateToRepository() {
        // Arrange
        Long dosenId = 1L;
        Long logId = 1L;
        when(logRepository.isLogOwnedByDosen(logId, dosenId)).thenReturn(true);

        // Act
        boolean result = logService.isLogOwnedByDosen(logId, dosenId);

        // Assert
        assertTrue(result);
        verify(logRepository).isLogOwnedByDosen(logId, dosenId);
    }
}