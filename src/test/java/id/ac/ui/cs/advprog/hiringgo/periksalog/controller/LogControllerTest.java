package id.ac.ui.cs.advprog.hiringgo.periksalog.controller;

import id.ac.ui.cs.advprog.hiringgo.common.model.User;
import id.ac.ui.cs.advprog.hiringgo.common.model.UserRole;
import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogDTO;
import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogStatusUpdateDTO;
import id.ac.ui.cs.advprog.hiringgo.periksalog.service.LogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private LogDTO logDTO1;
    private LogDTO logDTO2;
    private LogStatusUpdateDTO updateDTO;

    @BeforeEach
    void setUp() {
        dosen = User.builder()
                .id(1L)
                .fullName("Dosen Test")
                .role(UserRole.DOSEN)
                .build();

        mahasiswa = User.builder()
                .id(2L)
                .fullName("Mahasiswa Test")
                .role(UserRole.MAHASISWA)
                .build();

        logDTO1 = LogDTO.builder()
                .id(1L)
                .judul("Log Test 1")
                .mahasiswaName("Mahasiswa Test")
                .build();

        logDTO2 = LogDTO.builder()
                .id(2L)
                .judul("Log Test 2")
                .mahasiswaName("Mahasiswa Test")
                .build();

        updateDTO = LogStatusUpdateDTO.builder()
                .logId(1L)
                .build();
    }

    @Test
    void getAllLogs_WithDosenRole_ShouldReturnLogs() {
        // Arrange
        when(logService.getAllLogsByDosenId(dosen.getId())).thenReturn(Arrays.asList(logDTO1, logDTO2));

        // Act
        ResponseEntity<List<LogDTO>> response = logController.getAllLogs(dosen);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(logService).getAllLogsByDosenId(dosen.getId());
    }

    @Test
    void getAllLogs_WithNullUser_ShouldReturnForbidden() {
        // Act
        ResponseEntity<List<LogDTO>> response = logController.getAllLogs(null);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
        verify(logService, never()).getAllLogsByDosenId(anyLong());
    }

    @Test
    void getAllLogs_WithNonDosenRole_ShouldReturnForbidden() {
        // Act
        ResponseEntity<List<LogDTO>> response = logController.getAllLogs(mahasiswa);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
        verify(logService, never()).getAllLogsByDosenId(anyLong());
    }

    @Test
    void getAllLogs_WithEmptyList_ShouldReturnNoContent() {
        // Arrange
        when(logService.getAllLogsByDosenId(dosen.getId())).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<List<LogDTO>> response = logController.getAllLogs(dosen);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(logService).getAllLogsByDosenId(dosen.getId());
    }

    @Test
    void updateLogStatus_WithDosenRole_ShouldUpdateAndReturnLog() {
        // Arrange
        when(logService.updateLogStatus(dosen.getId(), updateDTO)).thenReturn(logDTO1);

        // Act
        ResponseEntity<LogDTO> response = logController.updateLogStatus(dosen, updateDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(logDTO1, response.getBody());
        verify(logService).updateLogStatus(dosen.getId(), updateDTO);
    }

    @Test
    void updateLogStatus_WithNullUser_ShouldReturnForbidden() {
        // Act
        ResponseEntity<LogDTO> response = logController.updateLogStatus(null, updateDTO);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
        verify(logService, never()).updateLogStatus(anyLong(), any());
    }

    @Test
    void updateLogStatus_WithNonDosenRole_ShouldReturnForbidden() {
        // Act
        ResponseEntity<LogDTO> response = logController.updateLogStatus(mahasiswa, updateDTO);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
        verify(logService, never()).updateLogStatus(anyLong(), any());
    }

    @Test
    void updateLogStatus_WhenSecurityException_ShouldReturnForbidden() {
        // Arrange
        when(logService.updateLogStatus(dosen.getId(), updateDTO))
                .thenThrow(new SecurityException("Permission denied"));

        // Act
        ResponseEntity<LogDTO> response = logController.updateLogStatus(dosen, updateDTO);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
        verify(logService).updateLogStatus(dosen.getId(), updateDTO);
    }

    @Test
    void updateLogStatus_WhenGenericException_ShouldReturnInternalServerError() {
        // Arrange
        when(logService.updateLogStatus(dosen.getId(), updateDTO))
                .thenThrow(new RuntimeException("Some error"));

        // Act
        ResponseEntity<LogDTO> response = logController.updateLogStatus(dosen, updateDTO);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(logService).updateLogStatus(dosen.getId(), updateDTO);
    }
}