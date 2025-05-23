package id.ac.ui.cs.advprog.hiringgo.periksalog.controller;

import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogDTO;
import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogStatusUpdateDTO;
import id.ac.ui.cs.advprog.hiringgo.periksalog.service.LogService;
import id.ac.ui.cs.advprog.hiringgo.model.WebResponse;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
        dosen.setRole("DOSEN");

        mahasiswa = new User();
        mahasiswa.setId("mahasiswa-123");
        mahasiswa.setEmail("mahasiswa@test.com");
        mahasiswa.setRole("MAHASISWA");

        logDTO1 = LogDTO.builder()
                .id("log-1")
                .judul("Log Test 1")
                .mahasiswaName("Mahasiswa Test")
                .build();

        logDTO2 = LogDTO.builder()
                .id("log-2")
                .judul("Log Test 2")
                .mahasiswaName("Mahasiswa Test")
                .build();

        updateDTO = LogStatusUpdateDTO.builder()
                .logId("log-1")
                .build();
    }

    @Test
    void getAllLogs_WithDosenRole_ShouldReturnLogs() throws ExecutionException, InterruptedException, TimeoutException {
        // Arrange
        List<LogDTO> expectedLogs = Arrays.asList(logDTO1, logDTO2);
        when(logService.getAllLogsByDosenIdAsync(dosen.getId()))
                .thenReturn(CompletableFuture.completedFuture(expectedLogs));

        // Act
        CompletableFuture<ResponseEntity<WebResponse<List<LogDTO>>>> futureResponse =
                logController.getAllLogs(dosen);
        ResponseEntity<WebResponse<List<LogDTO>>> response =
                futureResponse.get(5, TimeUnit.SECONDS);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());
        assertEquals(2, response.getBody().getData().size());
        assertNull(response.getBody().getErrors());
        verify(logService).getAllLogsByDosenIdAsync(dosen.getId());
    }

    @Test
    void getAllLogs_WithNullUser_ShouldReturnForbidden() throws ExecutionException, InterruptedException, TimeoutException {
        // Act
        CompletableFuture<ResponseEntity<WebResponse<List<LogDTO>>>> futureResponse =
                logController.getAllLogs(null);
        ResponseEntity<WebResponse<List<LogDTO>>> response =
                futureResponse.get(5, TimeUnit.SECONDS);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertTrue(response.getBody().getErrors().contains("Access forbidden"));
        verify(logService, never()).getAllLogsByDosenIdAsync(anyString());
    }

    @Test
    void getAllLogs_WithNonDosenRole_ShouldReturnForbidden() throws ExecutionException, InterruptedException, TimeoutException {
        // Act
        CompletableFuture<ResponseEntity<WebResponse<List<LogDTO>>>> futureResponse =
                logController.getAllLogs(mahasiswa);
        ResponseEntity<WebResponse<List<LogDTO>>> response =
                futureResponse.get(5, TimeUnit.SECONDS);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertTrue(response.getBody().getErrors().contains("Access forbidden"));
        verify(logService, never()).getAllLogsByDosenIdAsync(anyString());
    }

    @Test
    void getAllLogs_WithEmptyList_ShouldReturnOkWithEmptyList() throws ExecutionException, InterruptedException, TimeoutException {
        // Arrange
        when(logService.getAllLogsByDosenIdAsync(dosen.getId()))
                .thenReturn(CompletableFuture.completedFuture(new ArrayList<>()));

        // Act
        CompletableFuture<ResponseEntity<WebResponse<List<LogDTO>>>> futureResponse =
                logController.getAllLogs(dosen);
        ResponseEntity<WebResponse<List<LogDTO>>> response =
                futureResponse.get(5, TimeUnit.SECONDS);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());
        assertTrue(response.getBody().getData().isEmpty());
        verify(logService).getAllLogsByDosenIdAsync(dosen.getId());
    }

    @Test
    void getAllLogs_WhenServiceThrowsException_ShouldReturnInternalServerError() throws ExecutionException, InterruptedException, TimeoutException {
        // Arrange
        CompletableFuture<List<LogDTO>> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new RuntimeException("Service error"));
        when(logService.getAllLogsByDosenIdAsync(dosen.getId())).thenReturn(failedFuture);

        // Act
        CompletableFuture<ResponseEntity<WebResponse<List<LogDTO>>>> futureResponse =
                logController.getAllLogs(dosen);
        ResponseEntity<WebResponse<List<LogDTO>>> response =
                futureResponse.get(5, TimeUnit.SECONDS);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertTrue(response.getBody().getErrors().contains("Service error"));
        verify(logService).getAllLogsByDosenIdAsync(dosen.getId());
    }

    @Test
    void updateLogStatus_WithDosenRole_ShouldUpdateAndReturnLog() throws ExecutionException, InterruptedException, TimeoutException {
        // Arrange
        String logId = "log-1";
        when(logService.updateLogStatusAsync(dosen.getId(), updateDTO))
                .thenReturn(CompletableFuture.completedFuture(logDTO1));

        // Act
        CompletableFuture<ResponseEntity<WebResponse<LogDTO>>> futureResponse =
                logController.updateLogStatus(dosen, logId, updateDTO);
        ResponseEntity<WebResponse<LogDTO>> response =
                futureResponse.get(5, TimeUnit.SECONDS);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());
        assertEquals(logDTO1, response.getBody().getData());
        assertNull(response.getBody().getErrors());
        verify(logService).updateLogStatusAsync(dosen.getId(), updateDTO);
    }

    @Test
    void updateLogStatus_WithNullUser_ShouldReturnForbidden() throws ExecutionException, InterruptedException, TimeoutException {
        // Act
        String logId = "log-1";
        CompletableFuture<ResponseEntity<WebResponse<LogDTO>>> futureResponse =
                logController.updateLogStatus(null, logId, updateDTO);
        ResponseEntity<WebResponse<LogDTO>> response =
                futureResponse.get(5, TimeUnit.SECONDS);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertTrue(response.getBody().getErrors().contains("Access forbidden"));
        verify(logService, never()).updateLogStatusAsync(anyString(), any());
    }

    @Test
    void updateLogStatus_WithNonDosenRole_ShouldReturnForbidden() throws ExecutionException, InterruptedException, TimeoutException {
        // Act
        String logId = "log-1";
        CompletableFuture<ResponseEntity<WebResponse<LogDTO>>> futureResponse =
                logController.updateLogStatus(mahasiswa, logId, updateDTO);
        ResponseEntity<WebResponse<LogDTO>> response =
                futureResponse.get(5, TimeUnit.SECONDS);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertTrue(response.getBody().getErrors().contains("Access forbidden"));
        verify(logService, never()).updateLogStatusAsync(anyString(), any());
    }

    @Test
    void updateLogStatus_WhenSecurityException_ShouldReturnForbidden() throws ExecutionException, InterruptedException, TimeoutException {
        // Arrange
        String logId = "log-1";
        CompletableFuture<LogDTO> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new SecurityException("Permission denied"));
        when(logService.updateLogStatusAsync(dosen.getId(), updateDTO)).thenReturn(failedFuture);

        // Act
        CompletableFuture<ResponseEntity<WebResponse<LogDTO>>> futureResponse =
                logController.updateLogStatus(dosen, logId, updateDTO);
        ResponseEntity<WebResponse<LogDTO>> response =
                futureResponse.get(5, TimeUnit.SECONDS);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertEquals("Permission denied", response.getBody().getErrors());
        verify(logService).updateLogStatusAsync(dosen.getId(), updateDTO);
    }

    @Test
    void updateLogStatus_WhenGenericException_ShouldReturnInternalServerError() throws ExecutionException, InterruptedException, TimeoutException {
        // Arrange
        String logId = "log-1";
        CompletableFuture<LogDTO> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new RuntimeException("Some error"));
        when(logService.updateLogStatusAsync(dosen.getId(), updateDTO)).thenReturn(failedFuture);

        // Act
        CompletableFuture<ResponseEntity<WebResponse<LogDTO>>> futureResponse =
                logController.updateLogStatus(dosen, logId, updateDTO);
        ResponseEntity<WebResponse<LogDTO>> response =
                futureResponse.get(5, TimeUnit.SECONDS);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertTrue(response.getBody().getErrors().contains("Some error"));
        verify(logService).updateLogStatusAsync(dosen.getId(), updateDTO);
    }

    // Helper method for testing async operations without timeout
    @Test
    void getAllLogs_AsyncBehavior_ShouldCompleteImmediately() {
        // Arrange
        List<LogDTO> expectedLogs = Arrays.asList(logDTO1, logDTO2);
        when(logService.getAllLogsByDosenIdAsync(dosen.getId()))
                .thenReturn(CompletableFuture.completedFuture(expectedLogs));

        // Act
        CompletableFuture<ResponseEntity<WebResponse<List<LogDTO>>>> futureResponse =
                logController.getAllLogs(dosen);

        // Assert
        assertTrue(futureResponse.isDone(), "Future should complete immediately for valid dosen");
        assertFalse(futureResponse.isCancelled());
        assertFalse(futureResponse.isCompletedExceptionally());
    }

    @Test
    void updateLogStatus_AsyncBehavior_ShouldCompleteImmediately() {
        // Arrange
        String logId = "log-1";
        when(logService.updateLogStatusAsync(dosen.getId(), updateDTO))
                .thenReturn(CompletableFuture.completedFuture(logDTO1));

        // Act
        CompletableFuture<ResponseEntity<WebResponse<LogDTO>>> futureResponse =
                logController.updateLogStatus(dosen, logId, updateDTO);

        // Assert
        assertTrue(futureResponse.isDone(), "Future should complete immediately for valid update");
        assertFalse(futureResponse.isCancelled());
        assertFalse(futureResponse.isCompletedExceptionally());
    }
}