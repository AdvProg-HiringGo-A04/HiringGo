package id.ac.ui.cs.advprog.hiringgo.periksalog.controller;

import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.enums.Role;
import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogDTO;
import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogStatusUpdateDTO;
import id.ac.ui.cs.advprog.hiringgo.periksalog.service.LogService;
import id.ac.ui.cs.advprog.hiringgo.model.WebResponse;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.model.enums.TipeKategori;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.model.enums.StatusLog;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LogControllerTest {

    @Mock
    private LogService logService;

    @InjectMocks
    private LogController logController;

    private User dosen;
    private User mahasiswa;
    private User admin;
    private LogDTO logDTO1;
    private LogDTO logDTO2;
    private LogStatusUpdateDTO updateDTO;

    @BeforeEach
    void setUp() {
        // Setup different user roles
        dosen = new User();
        dosen.setId("dosen-123");
        dosen.setEmail("dosen@test.com");
        dosen.setRole(Role.DOSEN);

        mahasiswa = new User();
        mahasiswa.setId("mahasiswa-123");
        mahasiswa.setEmail("mahasiswa@test.com");
        mahasiswa.setRole(Role.MAHASISWA);

        admin = new User();
        admin.setId("admin-123");
        admin.setEmail("admin@test.com");
        admin.setRole(Role.ADMIN);

        logDTO1 = LogDTO.builder()
                .id("log-1")
                .judul("Log Test 1")
                .keterangan("Test description")
                .kategori(TipeKategori.ASISTENSI)
                .waktuMulai(LocalTime.of(9, 0))
                .waktuSelesai(LocalTime.of(10, 0))
                .tanggalLog(LocalDate.now())
                .pesanUntukDosen("Test message")
                .status(StatusLog.DIPROSES)
                .mahasiswaName("Mahasiswa Test")
                .mataKuliahName("Test Course")
                .mataKuliahCode("TEST101")
                .durationInHours(1.0)
                .build();

        logDTO2 = LogDTO.builder()
                .id("log-2")
                .judul("Log Test 2")
                .keterangan("Test description 2")
                .kategori(TipeKategori.MENGOREKSI)
                .waktuMulai(LocalTime.of(10, 0))
                .waktuSelesai(LocalTime.of(11, 0))
                .tanggalLog(LocalDate.now())
                .pesanUntukDosen("Test message 2")
                .status(StatusLog.DITERIMA)
                .mahasiswaName("Mahasiswa Test")
                .mataKuliahName("Test Course 2")
                .mataKuliahCode("TEST102")
                .durationInHours(1.0)
                .build();

        updateDTO = LogStatusUpdateDTO.builder()
                .logId("log-1")
                .status(StatusLog.DITERIMA)
                .build();
    }

    @Test
    void getAllLogs_WithDosenRole_ShouldReturnLogs() {
        // Arrange
        List<LogDTO> expectedLogs = Arrays.asList(logDTO1, logDTO2);
        when(logService.getAllLogsByDosenId(dosen.getId())).thenReturn(expectedLogs);

        // Act
        ResponseEntity<WebResponse<List<LogDTO>>> response = logController.getAllLogs(dosen);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());
        assertEquals(2, response.getBody().getData().size());
        assertNull(response.getBody().getErrors());
        verify(logService).getAllLogsByDosenId(dosen.getId());
    }

    @Test
    void getAllLogs_WithNullUser_ShouldReturnForbidden() {
        // Act
        ResponseEntity<WebResponse<List<LogDTO>>> response = logController.getAllLogs(null);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertTrue(response.getBody().getErrors().contains("Access forbidden"));
        verify(logService, never()).getAllLogsByDosenId(anyString());
    }

    @Test
    void getAllLogs_WithNonDosenRole_ShouldReturnForbidden() {
        // Act
        ResponseEntity<WebResponse<List<LogDTO>>> response = logController.getAllLogs(mahasiswa);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertTrue(response.getBody().getErrors().contains("Access forbidden"));
        verify(logService, never()).getAllLogsByDosenId(anyString());
    }

    @Test
    void getAllLogs_WithEmptyList_ShouldReturnOkWithEmptyList() {
        // Arrange
        when(logService.getAllLogsByDosenId(dosen.getId())).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<WebResponse<List<LogDTO>>> response = logController.getAllLogs(dosen);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());
        assertTrue(response.getBody().getData().isEmpty());
        verify(logService).getAllLogsByDosenId(dosen.getId());
    }

    @Test
    void getAllLogs_WhenServiceThrowsException_ShouldReturnInternalServerError() {
        // Arrange
        when(logService.getAllLogsByDosenId(dosen.getId()))
                .thenThrow(new RuntimeException("Service error"));

        // Act
        ResponseEntity<WebResponse<List<LogDTO>>> response = logController.getAllLogs(dosen);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertTrue(response.getBody().getErrors().contains("Service error"));
        verify(logService).getAllLogsByDosenId(dosen.getId());
    }

    @Test
    void updateLogStatus_WithDosenRole_ShouldUpdateAndReturnLog() {
        // Arrange
        String logId = "log-1";
        when(logService.updateLogStatus(dosen.getId(), updateDTO)).thenReturn(logDTO1);

        // Act
        ResponseEntity<WebResponse<LogDTO>> response =
                logController.updateLogStatus(dosen, logId, updateDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());
        assertEquals(logDTO1, response.getBody().getData());
        assertNull(response.getBody().getErrors());
        assertEquals(logId, updateDTO.getLogId()); // Verify logId was set
        verify(logService).updateLogStatus(dosen.getId(), updateDTO);
    }

    @Test
    void updateLogStatus_WithNullUser_ShouldReturnForbidden() {
        // Act
        String logId = "log-1";
        ResponseEntity<WebResponse<LogDTO>> response =
                logController.updateLogStatus(null, logId, updateDTO);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertTrue(response.getBody().getErrors().contains("Access forbidden"));
        verify(logService, never()).updateLogStatus(anyString(), any());
    }

    @Test
    void updateLogStatus_WithNonDosenRole_ShouldReturnForbidden() {
        // Act
        String logId = "log-1";
        ResponseEntity<WebResponse<LogDTO>> response =
                logController.updateLogStatus(mahasiswa, logId, updateDTO);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertTrue(response.getBody().getErrors().contains("Access forbidden"));
        verify(logService, never()).updateLogStatus(anyString(), any());
    }

    @Test
    void updateLogStatus_WhenSecurityException_ShouldReturnForbidden() {
        // Arrange
        String logId = "log-1";
        when(logService.updateLogStatus(dosen.getId(), updateDTO))
                .thenThrow(new SecurityException("Permission denied"));

        // Act
        ResponseEntity<WebResponse<LogDTO>> response =
                logController.updateLogStatus(dosen, logId, updateDTO);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertEquals("Permission denied", response.getBody().getErrors());
        verify(logService).updateLogStatus(dosen.getId(), updateDTO);
    }

    @Test
    void updateLogStatus_WhenGenericException_ShouldReturnInternalServerError() {
        // Arrange
        String logId = "log-1";
        when(logService.updateLogStatus(dosen.getId(), updateDTO))
                .thenThrow(new RuntimeException("Some error"));

        // Act
        ResponseEntity<WebResponse<LogDTO>> response =
                logController.updateLogStatus(dosen, logId, updateDTO);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertTrue(response.getBody().getErrors().contains("Some error"));
        verify(logService).updateLogStatus(dosen.getId(), updateDTO);
    }

    @Test
    void updateLogStatus_WhenNoSuchElementException_ShouldReturnInternalServerError() {
        // Arrange
        String logId = "log-1";
        when(logService.updateLogStatus(dosen.getId(), updateDTO))
                .thenThrow(new NoSuchElementException("Log not found"));

        // Act
        ResponseEntity<WebResponse<LogDTO>> response =
                logController.updateLogStatus(dosen, logId, updateDTO);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertTrue(response.getBody().getErrors().contains("Log not found"));
        verify(logService).updateLogStatus(dosen.getId(), updateDTO);
    }

    @Test
    void updateLogStatus_ShouldSetLogIdFromPathVariable() {
        // Arrange
        String logId = "different-log-id";
        LogStatusUpdateDTO dtoWithoutId = LogStatusUpdateDTO.builder()
                .status(StatusLog.DITERIMA)
                .build();
        when(logService.updateLogStatus(dosen.getId(), dtoWithoutId)).thenReturn(logDTO1);

        // Act
        logController.updateLogStatus(dosen, logId, dtoWithoutId);

        // Assert
        assertEquals(logId, dtoWithoutId.getLogId());
        verify(logService).updateLogStatus(dosen.getId(), dtoWithoutId);
    }

    @Test
    void getAllLogs_WhenServiceReturnsNull_ShouldHandleGracefully() {
        // Arrange
        when(logService.getAllLogsByDosenId(dosen.getId())).thenReturn(null);

        // Act
        ResponseEntity<WebResponse<List<LogDTO>>> response = logController.getAllLogs(dosen);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData()); // Null is acceptable for data
        verify(logService).getAllLogsByDosenId(dosen.getId());
    }
}