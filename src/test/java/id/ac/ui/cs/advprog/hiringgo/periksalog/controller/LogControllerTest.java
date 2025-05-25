package id.ac.ui.cs.advprog.hiringgo.periksalog.controller;

import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.enums.Role;
import id.ac.ui.cs.advprog.hiringgo.entity.Log;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

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
        dosen = new User();
        dosen.setId("dosen-123");
        dosen.setEmail("dosen@test.com");
        dosen.setRole(Role.DOSEN);

        mahasiswa = new User();
        mahasiswa.setId("mahasiswa-123");
        mahasiswa.setEmail("mahasiswa@test.com");
        mahasiswa.setRole(Role.MAHASISWA);

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

    // ==================== SYNCHRONOUS TESTS ====================

    @Test
    void getAllLogs_WithDosenRole_ShouldReturnLogs() {
        List<LogDTO> expectedLogs = Arrays.asList(logDTO1, logDTO2);
        when(logService.getAllLogsByDosenId(dosen.getId())).thenReturn(expectedLogs);

        ResponseEntity<WebResponse<List<LogDTO>>> response = logController.getAllLogs(dosen);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());
        assertEquals(2, response.getBody().getData().size());
        assertNull(response.getBody().getErrors());
        verify(logService).getAllLogsByDosenId(dosen.getId());
    }

    @Test
    void getAllLogs_WithNullUser_ShouldReturnForbidden() {
        ResponseEntity<WebResponse<List<LogDTO>>> response = logController.getAllLogs(null);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertTrue(response.getBody().getErrors().contains("Access forbidden"));
        verify(logService, never()).getAllLogsByDosenId(anyString());
    }

    @Test
    void getAllLogs_WithNonDosenRole_ShouldReturnForbidden() {
        ResponseEntity<WebResponse<List<LogDTO>>> response = logController.getAllLogs(mahasiswa);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertTrue(response.getBody().getErrors().contains("Access forbidden"));
        verify(logService, never()).getAllLogsByDosenId(anyString());
    }

    @Test
    void getAllLogs_WithEmptyList_ShouldReturnOkWithEmptyList() {
        when(logService.getAllLogsByDosenId(dosen.getId())).thenReturn(new ArrayList<>());

        ResponseEntity<WebResponse<List<LogDTO>>> response = logController.getAllLogs(dosen);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());
        assertTrue(response.getBody().getData().isEmpty());
        verify(logService).getAllLogsByDosenId(dosen.getId());
    }

    @Test
    void getAllLogs_WhenServiceThrowsException_ShouldReturnInternalServerError() {
        when(logService.getAllLogsByDosenId(dosen.getId()))
                .thenThrow(new RuntimeException("Service error"));

        ResponseEntity<WebResponse<List<LogDTO>>> response = logController.getAllLogs(dosen);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertTrue(response.getBody().getErrors().contains("Service error"));
        verify(logService).getAllLogsByDosenId(dosen.getId());
    }

    @Test
    void getAllLogs_WhenServiceReturnsNull_ShouldHandleGracefully() {
        when(logService.getAllLogsByDosenId(dosen.getId())).thenReturn(null);

        ResponseEntity<WebResponse<List<LogDTO>>> response = logController.getAllLogs(dosen);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        verify(logService).getAllLogsByDosenId(dosen.getId());
    }

    @Test
    void getAllLogs_WhenServiceThrowsExceptionWithNullMessage_ShouldReturnDefaultMessage() {
        when(logService.getAllLogsByDosenId(dosen.getId()))
                .thenThrow(new RuntimeException((String) null));

        ResponseEntity<WebResponse<List<LogDTO>>> response = logController.getAllLogs(dosen);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertEquals("An error occurred while fetching logs", response.getBody().getErrors());
        verify(logService).getAllLogsByDosenId(dosen.getId());
    }

    @Test
    void updateLogStatus_WithDosenRole_ShouldUpdateAndReturnLog() {
        String logId = "log-1";
        when(logService.updateLogStatus(dosen.getId(), updateDTO)).thenReturn(logDTO1);

        ResponseEntity<WebResponse<LogDTO>> response =
                logController.updateLogStatus(dosen, logId, updateDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());
        assertEquals(logDTO1, response.getBody().getData());
        assertNull(response.getBody().getErrors());
        assertEquals(logId, updateDTO.getLogId());
        verify(logService).updateLogStatus(dosen.getId(), updateDTO);
    }

    @Test
    void updateLogStatus_WithNullUser_ShouldReturnForbidden() {
        String logId = "log-1";
        ResponseEntity<WebResponse<LogDTO>> response =
                logController.updateLogStatus(null, logId, updateDTO);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertTrue(response.getBody().getErrors().contains("Access forbidden"));
        verify(logService, never()).updateLogStatus(anyString(), any());
    }

    @Test
    void updateLogStatus_WithNonDosenRole_ShouldReturnForbidden() {
        String logId = "log-1";
        ResponseEntity<WebResponse<LogDTO>> response =
                logController.updateLogStatus(mahasiswa, logId, updateDTO);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertTrue(response.getBody().getErrors().contains("Access forbidden"));
        verify(logService, never()).updateLogStatus(anyString(), any());
    }

    @Test
    void updateLogStatus_WhenSecurityException_ShouldReturnForbidden() {
        String logId = "log-1";
        when(logService.updateLogStatus(dosen.getId(), updateDTO))
                .thenThrow(new SecurityException("Permission denied"));

        ResponseEntity<WebResponse<LogDTO>> response =
                logController.updateLogStatus(dosen, logId, updateDTO);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertEquals("Permission denied", response.getBody().getErrors());
        verify(logService).updateLogStatus(dosen.getId(), updateDTO);
    }

    @Test
    void updateLogStatus_WhenGenericException_ShouldReturnInternalServerError() {
        String logId = "log-1";
        when(logService.updateLogStatus(dosen.getId(), updateDTO))
                .thenThrow(new RuntimeException("Some error"));

        ResponseEntity<WebResponse<LogDTO>> response =
                logController.updateLogStatus(dosen, logId, updateDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertTrue(response.getBody().getErrors().contains("Some error"));
        verify(logService).updateLogStatus(dosen.getId(), updateDTO);
    }

    @Test
    void updateLogStatus_WhenExceptionWithNullMessage_ShouldReturnDefaultMessage() {
        String logId = "log-1";
        when(logService.updateLogStatus(dosen.getId(), updateDTO))
                .thenThrow(new RuntimeException((String) null));

        ResponseEntity<WebResponse<LogDTO>> response =
                logController.updateLogStatus(dosen, logId, updateDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertEquals("An error occurred while updating log status", response.getBody().getErrors());
        verify(logService).updateLogStatus(dosen.getId(), updateDTO);
    }

    @Test
    void updateLogStatus_ShouldSetLogIdFromPathVariable() {
        String logId = "different-log-id";
        LogStatusUpdateDTO dtoWithoutId = LogStatusUpdateDTO.builder()
                .status(StatusLog.DITERIMA)
                .build();
        when(logService.updateLogStatus(dosen.getId(), dtoWithoutId)).thenReturn(logDTO1);

        logController.updateLogStatus(dosen, logId, dtoWithoutId);

        assertEquals(logId, dtoWithoutId.getLogId());
        verify(logService).updateLogStatus(dosen.getId(), dtoWithoutId);
    }

    // ==================== ASYNC TESTS ====================

    @Test
    void getAllLogsAsync_WithDosenRole_ShouldReturnLogs() {
        List<LogDTO> expectedLogs = Arrays.asList(logDTO1, logDTO2);
        when(logService.getAllLogsByDosenIdAsync(dosen.getId()))
                .thenReturn(CompletableFuture.completedFuture(expectedLogs));

        CompletableFuture<ResponseEntity<WebResponse<List<LogDTO>>>> futureResponse =
                logController.getAllLogsAsync(dosen);
        ResponseEntity<WebResponse<List<LogDTO>>> response = futureResponse.join();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());
        assertEquals(2, response.getBody().getData().size());
        assertNull(response.getBody().getErrors());
        verify(logService).getAllLogsByDosenIdAsync(dosen.getId());
    }

    @Test
    void getAllLogsAsync_WithNullUser_ShouldReturnForbidden() {
        CompletableFuture<ResponseEntity<WebResponse<List<LogDTO>>>> futureResponse =
                logController.getAllLogsAsync(null);
        ResponseEntity<WebResponse<List<LogDTO>>> response = futureResponse.join();

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertTrue(response.getBody().getErrors().contains("Access forbidden"));
        verify(logService, never()).getAllLogsByDosenIdAsync(anyString());
    }

    @Test
    void getAllLogsAsync_WithNonDosenRole_ShouldReturnForbidden() {
        CompletableFuture<ResponseEntity<WebResponse<List<LogDTO>>>> futureResponse =
                logController.getAllLogsAsync(mahasiswa);
        ResponseEntity<WebResponse<List<LogDTO>>> response = futureResponse.join();

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertTrue(response.getBody().getErrors().contains("Access forbidden"));
        verify(logService, never()).getAllLogsByDosenIdAsync(anyString());
    }

    @Test
    void getAllLogsAsync_WhenServiceThrowsException_ShouldReturnInternalServerError() {
        CompletableFuture<List<LogDTO>> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new RuntimeException("Async service error"));
        when(logService.getAllLogsByDosenIdAsync(dosen.getId())).thenReturn(failedFuture);

        CompletableFuture<ResponseEntity<WebResponse<List<LogDTO>>>> futureResponse =
                logController.getAllLogsAsync(dosen);
        ResponseEntity<WebResponse<List<LogDTO>>> response = futureResponse.join();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertTrue(response.getBody().getErrors().contains("Async service error"));
        verify(logService).getAllLogsByDosenIdAsync(dosen.getId());
    }

    @Test
    void getAllLogsAsync_WhenServiceThrowsExceptionWithNullMessage_ShouldReturnDefaultMessage() {
        CompletableFuture<List<LogDTO>> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new RuntimeException((String) null));
        when(logService.getAllLogsByDosenIdAsync(dosen.getId())).thenReturn(failedFuture);

        CompletableFuture<ResponseEntity<WebResponse<List<LogDTO>>>> futureResponse =
                logController.getAllLogsAsync(dosen);
        ResponseEntity<WebResponse<List<LogDTO>>> response = futureResponse.join();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertEquals("An error occurred while fetching logs: java.lang.RuntimeException", response.getBody().getErrors());
        verify(logService).getAllLogsByDosenIdAsync(dosen.getId());
    }

    @Test
    void getAllLogsAsync_WithEmptyList_ShouldReturnOkWithEmptyList() {
        when(logService.getAllLogsByDosenIdAsync(dosen.getId()))
                .thenReturn(CompletableFuture.completedFuture(new ArrayList<>()));

        CompletableFuture<ResponseEntity<WebResponse<List<LogDTO>>>> futureResponse =
                logController.getAllLogsAsync(dosen);
        ResponseEntity<WebResponse<List<LogDTO>>> response = futureResponse.join();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());
        assertTrue(response.getBody().getData().isEmpty());
        verify(logService).getAllLogsByDosenIdAsync(dosen.getId());
    }

    @Test
    void getAllLogsAsync_WhenServiceReturnsNull_ShouldHandleGracefully() {
        when(logService.getAllLogsByDosenIdAsync(dosen.getId()))
                .thenReturn(CompletableFuture.completedFuture(null));

        CompletableFuture<ResponseEntity<WebResponse<List<LogDTO>>>> futureResponse =
                logController.getAllLogsAsync(dosen);
        ResponseEntity<WebResponse<List<LogDTO>>> response = futureResponse.join();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        verify(logService).getAllLogsByDosenIdAsync(dosen.getId());
    }

    @Test
    void updateLogStatusAsync_WithDosenRole_ShouldUpdateAndReturnLog() {
        String logId = "log-1";
        when(logService.updateLogStatusAsync(dosen.getId(), updateDTO))
                .thenReturn(CompletableFuture.completedFuture(logDTO1));

        CompletableFuture<ResponseEntity<WebResponse<LogDTO>>> futureResponse =
                logController.updateLogStatusAsync(dosen, logId, updateDTO);
        ResponseEntity<WebResponse<LogDTO>> response = futureResponse.join();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());
        assertEquals(logDTO1, response.getBody().getData());
        assertNull(response.getBody().getErrors());
        assertEquals(logId, updateDTO.getLogId());
        verify(logService).updateLogStatusAsync(dosen.getId(), updateDTO);
    }

    @Test
    void updateLogStatusAsync_WithNullUser_ShouldReturnForbidden() {
        String logId = "log-1";
        CompletableFuture<ResponseEntity<WebResponse<LogDTO>>> futureResponse =
                logController.updateLogStatusAsync(null, logId, updateDTO);
        ResponseEntity<WebResponse<LogDTO>> response = futureResponse.join();

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertTrue(response.getBody().getErrors().contains("Access forbidden"));
        verify(logService, never()).updateLogStatusAsync(anyString(), any());
    }

    @Test
    void updateLogStatusAsync_WithNonDosenRole_ShouldReturnForbidden() {
        String logId = "log-1";
        CompletableFuture<ResponseEntity<WebResponse<LogDTO>>> futureResponse =
                logController.updateLogStatusAsync(mahasiswa, logId, updateDTO);
        ResponseEntity<WebResponse<LogDTO>> response = futureResponse.join();

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertTrue(response.getBody().getErrors().contains("Access forbidden"));
        verify(logService, never()).updateLogStatusAsync(anyString(), any());
    }

    @Test
    void updateLogStatusAsync_WhenSecurityException_ShouldReturnForbidden() {
        String logId = "log-1";
        CompletableFuture<LogDTO> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new SecurityException("Async permission denied"));
        when(logService.updateLogStatusAsync(dosen.getId(), updateDTO)).thenReturn(failedFuture);

        CompletableFuture<ResponseEntity<WebResponse<LogDTO>>> futureResponse =
                logController.updateLogStatusAsync(dosen, logId, updateDTO);
        ResponseEntity<WebResponse<LogDTO>> response = futureResponse.join();

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertEquals("Async permission denied", response.getBody().getErrors());
        verify(logService).updateLogStatusAsync(dosen.getId(), updateDTO);
    }

    @Test
    void updateLogStatusAsync_WhenGenericException_ShouldReturnInternalServerError() {
        String logId = "log-1";
        CompletableFuture<LogDTO> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new RuntimeException("Async service error"));
        when(logService.updateLogStatusAsync(dosen.getId(), updateDTO)).thenReturn(failedFuture);

        CompletableFuture<ResponseEntity<WebResponse<LogDTO>>> futureResponse =
                logController.updateLogStatusAsync(dosen, logId, updateDTO);
        ResponseEntity<WebResponse<LogDTO>> response = futureResponse.join();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertTrue(response.getBody().getErrors().contains("Async service error"));
        verify(logService).updateLogStatusAsync(dosen.getId(), updateDTO);
    }

    @Test
    void updateLogStatusAsync_WhenExceptionWithNullMessage_ShouldReturnDefaultMessage() {
        String logId = "log-1";
        CompletableFuture<LogDTO> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new RuntimeException((String) null));
        when(logService.updateLogStatusAsync(dosen.getId(), updateDTO)).thenReturn(failedFuture);

        CompletableFuture<ResponseEntity<WebResponse<LogDTO>>> futureResponse =
                logController.updateLogStatusAsync(dosen, logId, updateDTO);
        ResponseEntity<WebResponse<LogDTO>> response = futureResponse.join();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertEquals("An error occurred while updating log status: java.lang.RuntimeException", response.getBody().getErrors());
        verify(logService).updateLogStatusAsync(dosen.getId(), updateDTO);
    }

    @Test
    void updateLogStatusAsync_ShouldSetLogIdFromPathVariable() {
        String logId = "different-async-log-id";
        LogStatusUpdateDTO dtoWithoutId = LogStatusUpdateDTO.builder()
                .status(StatusLog.DITERIMA)
                .build();
        when(logService.updateLogStatusAsync(dosen.getId(), dtoWithoutId))
                .thenReturn(CompletableFuture.completedFuture(logDTO1));

        logController.updateLogStatusAsync(dosen, logId, dtoWithoutId);

        assertEquals(logId, dtoWithoutId.getLogId());
        verify(logService).updateLogStatusAsync(dosen.getId(), dtoWithoutId);
    }

    // ==================== UTILITY METHOD TESTS ====================

    @Test
    void getUserIdSafely_WithNullUser_ShouldReturnUnknown() {
        // This is tested indirectly through the logging in other methods
        // but we can verify behavior through the forbidden responses with null user
        CompletableFuture<ResponseEntity<WebResponse<List<LogDTO>>>> futureResponse =
                logController.getAllLogsAsync(null);
        ResponseEntity<WebResponse<List<LogDTO>>> response = futureResponse.join();

        // The method should handle null user gracefully (which it does in the log messages)
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void getUserRoleSafely_WithNullUser_ShouldReturnUnknown() {
        // This is tested indirectly through the logging in other methods
        // Similar to getUserIdSafely, we verify through forbidden responses
        ResponseEntity<WebResponse<List<LogDTO>>> response = logController.getAllLogs(null);

        // The method should handle null user gracefully (which it does in the log messages)
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}