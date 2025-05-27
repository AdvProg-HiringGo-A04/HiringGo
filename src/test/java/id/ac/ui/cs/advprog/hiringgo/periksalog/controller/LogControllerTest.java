package id.ac.ui.cs.advprog.hiringgo.periksalog.controller;

import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.enums.Role;
import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogDTO;
import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogStatusUpdateDTO;
import id.ac.ui.cs.advprog.hiringgo.model.WebResponse;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.enums.TipeKategori;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.enums.StatusLog;
import id.ac.ui.cs.advprog.hiringgo.periksalog.service.PeriksaLogService;
import id.ac.ui.cs.advprog.hiringgo.security.JwtUtil;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.service.UserService;

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
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LogControllerTest {

    @Mock
    private PeriksaLogService logService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserService userService;

    @InjectMocks
    private PeriksaLogController logController;

    private User dosen;
    private User mahasiswa;
    private LogDTO logDTO1;
    private LogDTO logDTO2;
    private LogStatusUpdateDTO updateDTO;

    // JWT tokens for testing
    private static final String VALID_DOSEN_TOKEN = "Bearer valid_dosen_token";
    private static final String VALID_MAHASISWA_TOKEN = "Bearer valid_mahasiswa_token";
    private static final String INVALID_TOKEN = "Bearer invalid_token";
    private static final String NOT_A_BEARER_TOKEN = "NotBearer token";
    private static final String NULL_TOKEN = null;

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

        // We're not setting up token mocks in setUp anymore to avoid unnecessary stubbing
    }

    // Helper method to set up authentication mocks
    private void setupAuthMock(String token, String userId, String role, User user) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            when(jwtUtil.extractId(jwt)).thenReturn(userId);
            when(jwtUtil.extractRole(jwt)).thenReturn(role);
            if (userId != null && role != null) {
                when(userService.getUserById(userId)).thenReturn(user);
            }
        }
    }

    // ==================== SYNCHRONOUS TESTS ====================

    @Test
    void getAllLogs_WithDosenToken_ShouldReturnLogs() {
        // Setup auth mock specifically for this test
        setupAuthMock(VALID_DOSEN_TOKEN, dosen.getId(), "DOSEN", dosen);

        List<LogDTO> expectedLogs = Arrays.asList(logDTO1, logDTO2);
        when(logService.getAllLogsByDosenId(dosen.getId())).thenReturn(expectedLogs);

        ResponseEntity<WebResponse<List<LogDTO>>> response = logController.getAllLogs(VALID_DOSEN_TOKEN);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());
        assertEquals(2, response.getBody().getData().size());
        assertNull(response.getBody().getErrors());
        verify(logService).getAllLogsByDosenId(dosen.getId());
        verify(jwtUtil).extractId("valid_dosen_token");
        verify(jwtUtil).extractRole("valid_dosen_token");
    }

    @Test
    void getAllLogs_WithNullToken_ShouldReturnUnauthorized() {
        ResponseEntity<WebResponse<List<LogDTO>>> response = logController.getAllLogs(NULL_TOKEN);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertEquals("Token required", response.getBody().getErrors());
        verify(logService, never()).getAllLogsByDosenId(anyString());
    }

    @Test
    void getAllLogs_WithNonBearerToken_ShouldReturnUnauthorized() {
        ResponseEntity<WebResponse<List<LogDTO>>> response = logController.getAllLogs(NOT_A_BEARER_TOKEN);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertEquals("Token required", response.getBody().getErrors());
        verify(logService, never()).getAllLogsByDosenId(anyString());
    }

    @Test
    void getAllLogs_WithInvalidToken_ShouldReturnUnauthorized() {
        // Setup invalid token mock
        setupAuthMock(INVALID_TOKEN, null, null, null);

        ResponseEntity<WebResponse<List<LogDTO>>> response = logController.getAllLogs(INVALID_TOKEN);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertEquals("Invalid token", response.getBody().getErrors());
        verify(logService, never()).getAllLogsByDosenId(anyString());
    }

    @Test
    void getAllLogs_WithMahasiswaToken_ShouldReturnForbidden() {
        // Setup mahasiswa token mock
        setupAuthMock(VALID_MAHASISWA_TOKEN, mahasiswa.getId(), "MAHASISWA", mahasiswa);

        ResponseEntity<WebResponse<List<LogDTO>>> response = logController.getAllLogs(VALID_MAHASISWA_TOKEN);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertTrue(response.getBody().getErrors().contains("Access forbidden"));
        verify(logService, never()).getAllLogsByDosenId(anyString());
    }

    @Test
    void getAllLogs_WithEmptyList_ShouldReturnOkWithEmptyList() {
        // Setup auth mock
        setupAuthMock(VALID_DOSEN_TOKEN, dosen.getId(), "DOSEN", dosen);

        when(logService.getAllLogsByDosenId(dosen.getId())).thenReturn(new ArrayList<>());

        ResponseEntity<WebResponse<List<LogDTO>>> response = logController.getAllLogs(VALID_DOSEN_TOKEN);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());
        assertTrue(response.getBody().getData().isEmpty());
        verify(logService).getAllLogsByDosenId(dosen.getId());
    }

    @Test
    void getAllLogs_WhenServiceThrowsException_ShouldReturnInternalServerError() {
        // Setup auth mock
        setupAuthMock(VALID_DOSEN_TOKEN, dosen.getId(), "DOSEN", dosen);

        when(logService.getAllLogsByDosenId(dosen.getId()))
                .thenThrow(new RuntimeException("Service error"));

        ResponseEntity<WebResponse<List<LogDTO>>> response = logController.getAllLogs(VALID_DOSEN_TOKEN);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertTrue(response.getBody().getErrors().contains("Service error"));
        verify(logService).getAllLogsByDosenId(dosen.getId());
    }

    @Test
    void getAllLogs_WhenServiceReturnsNull_ShouldHandleGracefully() {
        // Setup auth mock
        setupAuthMock(VALID_DOSEN_TOKEN, dosen.getId(), "DOSEN", dosen);

        when(logService.getAllLogsByDosenId(dosen.getId())).thenReturn(null);

        ResponseEntity<WebResponse<List<LogDTO>>> response = logController.getAllLogs(VALID_DOSEN_TOKEN);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        verify(logService).getAllLogsByDosenId(dosen.getId());
    }

    @Test
    void getAllLogs_WhenServiceThrowsExceptionWithNullMessage_ShouldReturnDefaultMessage() {
        // Setup auth mock
        setupAuthMock(VALID_DOSEN_TOKEN, dosen.getId(), "DOSEN", dosen);

        when(logService.getAllLogsByDosenId(dosen.getId()))
                .thenThrow(new RuntimeException((String) null));

        ResponseEntity<WebResponse<List<LogDTO>>> response = logController.getAllLogs(VALID_DOSEN_TOKEN);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertEquals("An error occurred while fetching logs", response.getBody().getErrors());
        verify(logService).getAllLogsByDosenId(dosen.getId());
    }

    @Test
    void updateLogStatus_WithDosenToken_ShouldUpdateAndReturnLog() {
        // Setup auth mock
        setupAuthMock(VALID_DOSEN_TOKEN, dosen.getId(), "DOSEN", dosen);

        String logId = "log-1";
        when(logService.updateLogStatus(dosen.getId(), updateDTO)).thenReturn(logDTO1);

        ResponseEntity<WebResponse<LogDTO>> response =
                logController.updateLogStatus(VALID_DOSEN_TOKEN, logId, updateDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());
        assertEquals(logDTO1, response.getBody().getData());
        assertNull(response.getBody().getErrors());
        assertEquals(logId, updateDTO.getLogId());
        verify(logService).updateLogStatus(dosen.getId(), updateDTO);
    }

    @Test
    void updateLogStatus_WithNullToken_ShouldReturnUnauthorized() {
        String logId = "log-1";
        ResponseEntity<WebResponse<LogDTO>> response =
                logController.updateLogStatus(NULL_TOKEN, logId, updateDTO);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertEquals("Token required", response.getBody().getErrors());
        verify(logService, never()).updateLogStatus(anyString(), any());
    }

    @Test
    void updateLogStatus_WithMahasiswaToken_ShouldReturnForbidden() {
        // Setup auth mock
        setupAuthMock(VALID_MAHASISWA_TOKEN, mahasiswa.getId(), "MAHASISWA", mahasiswa);

        String logId = "log-1";
        ResponseEntity<WebResponse<LogDTO>> response =
                logController.updateLogStatus(VALID_MAHASISWA_TOKEN, logId, updateDTO);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertTrue(response.getBody().getErrors().contains("Access forbidden"));
        verify(logService, never()).updateLogStatus(anyString(), any());
    }

    @Test
    void updateLogStatus_WhenSecurityException_ShouldReturnForbidden() {
        // Setup auth mock
        setupAuthMock(VALID_DOSEN_TOKEN, dosen.getId(), "DOSEN", dosen);

        String logId = "log-1";
        when(logService.updateLogStatus(dosen.getId(), updateDTO))
                .thenThrow(new SecurityException("Permission denied"));

        ResponseEntity<WebResponse<LogDTO>> response =
                logController.updateLogStatus(VALID_DOSEN_TOKEN, logId, updateDTO);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertEquals("Permission denied", response.getBody().getErrors());
        verify(logService).updateLogStatus(dosen.getId(), updateDTO);
    }

    @Test
    void updateLogStatus_WhenGenericException_ShouldReturnInternalServerError() {
        // Setup auth mock
        setupAuthMock(VALID_DOSEN_TOKEN, dosen.getId(), "DOSEN", dosen);

        String logId = "log-1";
        when(logService.updateLogStatus(dosen.getId(), updateDTO))
                .thenThrow(new RuntimeException("Some error"));

        ResponseEntity<WebResponse<LogDTO>> response =
                logController.updateLogStatus(VALID_DOSEN_TOKEN, logId, updateDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertTrue(response.getBody().getErrors().contains("Some error"));
        verify(logService).updateLogStatus(dosen.getId(), updateDTO);
    }

    @Test
    void updateLogStatus_WhenExceptionWithNullMessage_ShouldReturnDefaultMessage() {
        // Setup auth mock
        setupAuthMock(VALID_DOSEN_TOKEN, dosen.getId(), "DOSEN", dosen);

        String logId = "log-1";
        when(logService.updateLogStatus(dosen.getId(), updateDTO))
                .thenThrow(new RuntimeException((String) null));

        ResponseEntity<WebResponse<LogDTO>> response =
                logController.updateLogStatus(VALID_DOSEN_TOKEN, logId, updateDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertEquals("An error occurred while updating log status", response.getBody().getErrors());
        verify(logService).updateLogStatus(dosen.getId(), updateDTO);
    }

    @Test
    void updateLogStatus_ShouldSetLogIdFromPathVariable() {
        // Setup auth mock
        setupAuthMock(VALID_DOSEN_TOKEN, dosen.getId(), "DOSEN", dosen);

        String logId = "different-log-id";
        LogStatusUpdateDTO dtoWithoutId = LogStatusUpdateDTO.builder()
                .status(StatusLog.DITERIMA)
                .build();
        when(logService.updateLogStatus(dosen.getId(), dtoWithoutId)).thenReturn(logDTO1);

        logController.updateLogStatus(VALID_DOSEN_TOKEN, logId, dtoWithoutId);

        assertEquals(logId, dtoWithoutId.getLogId());
        verify(logService).updateLogStatus(dosen.getId(), dtoWithoutId);
    }

    // ==================== ASYNC TESTS ====================

    @Test
    void getAllLogsAsync_WithDosenToken_ShouldReturnLogs() {
        // Setup auth mock
        setupAuthMock(VALID_DOSEN_TOKEN, dosen.getId(), "DOSEN", dosen);

        List<LogDTO> expectedLogs = Arrays.asList(logDTO1, logDTO2);
        when(logService.getAllLogsByDosenIdAsync(dosen.getId()))
                .thenReturn(CompletableFuture.completedFuture(expectedLogs));

        CompletableFuture<ResponseEntity<WebResponse<List<LogDTO>>>> futureResponse =
                logController.getAllLogsAsync(VALID_DOSEN_TOKEN);
        ResponseEntity<WebResponse<List<LogDTO>>> response = futureResponse.join();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());
        assertEquals(2, response.getBody().getData().size());
        assertNull(response.getBody().getErrors());
        verify(logService).getAllLogsByDosenIdAsync(dosen.getId());
    }

    @Test
    void getAllLogsAsync_WithNullToken_ShouldReturnUnauthorized() {
        CompletableFuture<ResponseEntity<WebResponse<List<LogDTO>>>> futureResponse =
                logController.getAllLogsAsync(NULL_TOKEN);
        ResponseEntity<WebResponse<List<LogDTO>>> response = futureResponse.join();

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertEquals("Token required", response.getBody().getErrors());
        verify(logService, never()).getAllLogsByDosenIdAsync(anyString());
    }

    @Test
    void getAllLogsAsync_WithMahasiswaToken_ShouldReturnForbidden() {
        // Setup auth mock
        setupAuthMock(VALID_MAHASISWA_TOKEN, mahasiswa.getId(), "MAHASISWA", mahasiswa);

        CompletableFuture<ResponseEntity<WebResponse<List<LogDTO>>>> futureResponse =
                logController.getAllLogsAsync(VALID_MAHASISWA_TOKEN);
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
        // Setup auth mock
        setupAuthMock(VALID_DOSEN_TOKEN, dosen.getId(), "DOSEN", dosen);

        CompletableFuture<List<LogDTO>> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new RuntimeException("Async service error"));
        when(logService.getAllLogsByDosenIdAsync(dosen.getId())).thenReturn(failedFuture);

        CompletableFuture<ResponseEntity<WebResponse<List<LogDTO>>>> futureResponse =
                logController.getAllLogsAsync(VALID_DOSEN_TOKEN);
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
        // Setup auth mock
        setupAuthMock(VALID_DOSEN_TOKEN, dosen.getId(), "DOSEN", dosen);

        CompletableFuture<List<LogDTO>> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new RuntimeException((String) null));
        when(logService.getAllLogsByDosenIdAsync(dosen.getId())).thenReturn(failedFuture);

        CompletableFuture<ResponseEntity<WebResponse<List<LogDTO>>>> futureResponse =
                logController.getAllLogsAsync(VALID_DOSEN_TOKEN);
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
        // Setup auth mock
        setupAuthMock(VALID_DOSEN_TOKEN, dosen.getId(), "DOSEN", dosen);

        when(logService.getAllLogsByDosenIdAsync(dosen.getId()))
                .thenReturn(CompletableFuture.completedFuture(new ArrayList<>()));

        CompletableFuture<ResponseEntity<WebResponse<List<LogDTO>>>> futureResponse =
                logController.getAllLogsAsync(VALID_DOSEN_TOKEN);
        ResponseEntity<WebResponse<List<LogDTO>>> response = futureResponse.join();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());
        assertTrue(response.getBody().getData().isEmpty());
        verify(logService).getAllLogsByDosenIdAsync(dosen.getId());
    }

    @Test
    void getAllLogsAsync_WhenServiceReturnsNull_ShouldHandleGracefully() {
        // Setup auth mock
        setupAuthMock(VALID_DOSEN_TOKEN, dosen.getId(), "DOSEN", dosen);

        when(logService.getAllLogsByDosenIdAsync(dosen.getId()))
                .thenReturn(CompletableFuture.completedFuture(null));

        CompletableFuture<ResponseEntity<WebResponse<List<LogDTO>>>> futureResponse =
                logController.getAllLogsAsync(VALID_DOSEN_TOKEN);
        ResponseEntity<WebResponse<List<LogDTO>>> response = futureResponse.join();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        verify(logService).getAllLogsByDosenIdAsync(dosen.getId());
    }

    @Test
    void updateLogStatusAsync_WithDosenToken_ShouldUpdateAndReturnLog() {
        // Setup auth mock
        setupAuthMock(VALID_DOSEN_TOKEN, dosen.getId(), "DOSEN", dosen);

        String logId = "log-1";
        when(logService.updateLogStatusAsync(dosen.getId(), updateDTO))
                .thenReturn(CompletableFuture.completedFuture(logDTO1));

        CompletableFuture<ResponseEntity<WebResponse<LogDTO>>> futureResponse =
                logController.updateLogStatusAsync(VALID_DOSEN_TOKEN, logId, updateDTO);
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
    void updateLogStatusAsync_WithNullToken_ShouldReturnUnauthorized() {
        String logId = "log-1";
        CompletableFuture<ResponseEntity<WebResponse<LogDTO>>> futureResponse =
                logController.updateLogStatusAsync(NULL_TOKEN, logId, updateDTO);
        ResponseEntity<WebResponse<LogDTO>> response = futureResponse.join();

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getErrors());
        assertEquals("Token required", response.getBody().getErrors());
        verify(logService, never()).updateLogStatusAsync(anyString(), any());
    }

    @Test
    void updateLogStatusAsync_WithMahasiswaToken_ShouldReturnForbidden() {
        // Setup auth mock
        setupAuthMock(VALID_MAHASISWA_TOKEN, mahasiswa.getId(), "MAHASISWA", mahasiswa);

        String logId = "log-1";
        CompletableFuture<ResponseEntity<WebResponse<LogDTO>>> futureResponse =
                logController.updateLogStatusAsync(VALID_MAHASISWA_TOKEN, logId, updateDTO);
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
        // Setup auth mock
        setupAuthMock(VALID_DOSEN_TOKEN, dosen.getId(), "DOSEN", dosen);

        String logId = "log-1";
        CompletableFuture<LogDTO> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new SecurityException("Async permission denied"));
        when(logService.updateLogStatusAsync(dosen.getId(), updateDTO)).thenReturn(failedFuture);

        CompletableFuture<ResponseEntity<WebResponse<LogDTO>>> futureResponse =
                logController.updateLogStatusAsync(VALID_DOSEN_TOKEN, logId, updateDTO);
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
        // Setup auth mock
        setupAuthMock(VALID_DOSEN_TOKEN, dosen.getId(), "DOSEN", dosen);

        String logId = "log-1";
        CompletableFuture<LogDTO> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new RuntimeException("Async service error"));
        when(logService.updateLogStatusAsync(dosen.getId(), updateDTO)).thenReturn(failedFuture);

        CompletableFuture<ResponseEntity<WebResponse<LogDTO>>> futureResponse =
                logController.updateLogStatusAsync(VALID_DOSEN_TOKEN, logId, updateDTO);
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
        // Setup auth mock
        setupAuthMock(VALID_DOSEN_TOKEN, dosen.getId(), "DOSEN", dosen);

        String logId = "log-1";
        CompletableFuture<LogDTO> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new RuntimeException((String) null));
        when(logService.updateLogStatusAsync(dosen.getId(), updateDTO)).thenReturn(failedFuture);

        CompletableFuture<ResponseEntity<WebResponse<LogDTO>>> futureResponse =
                logController.updateLogStatusAsync(VALID_DOSEN_TOKEN, logId, updateDTO);
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
        // Setup auth mock
        setupAuthMock(VALID_DOSEN_TOKEN, dosen.getId(), "DOSEN", dosen);

        String logId = "different-async-log-id";
        LogStatusUpdateDTO dtoWithoutId = LogStatusUpdateDTO.builder()
                .status(StatusLog.DITERIMA)
                .build();
        when(logService.updateLogStatusAsync(dosen.getId(), dtoWithoutId))
                .thenReturn(CompletableFuture.completedFuture(logDTO1));

        logController.updateLogStatusAsync(VALID_DOSEN_TOKEN, logId, dtoWithoutId);

        assertEquals(logId, dtoWithoutId.getLogId());
        verify(logService).updateLogStatusAsync(dosen.getId(), dtoWithoutId);
    }

    @Test
    void authenticateUser_WhenUserNotFound_ShouldThrowException() {
        // Setup a scenario where the token is valid but user is not found
        String validTokenWithoutUser = "Bearer valid_token_without_user";
        when(jwtUtil.extractId("valid_token_without_user")).thenReturn("non-existent-id");
        when(jwtUtil.extractRole("valid_token_without_user")).thenReturn("DOSEN");
        when(userService.getUserById("non-existent-id")).thenThrow(new RuntimeException("User not found"));

        ResponseEntity<WebResponse<List<LogDTO>>> response = logController.getAllLogs(validTokenWithoutUser);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid token", response.getBody().getErrors());
    }

    @Test
    void authenticateUser_WhenUserServiceFailure_ShouldHandleGracefully() {
        // Setup a scenario where the token is valid but user service has internal error
        String validTokenWithServiceError = "Bearer valid_token_with_service_error";
        when(jwtUtil.extractId("valid_token_with_service_error")).thenReturn("valid-id");
        when(jwtUtil.extractRole("valid_token_with_service_error")).thenReturn("DOSEN");
        when(userService.getUserById("valid-id")).thenThrow(new RuntimeException("Database connection error"));

        ResponseEntity<WebResponse<List<LogDTO>>> response = logController.getAllLogs(validTokenWithServiceError);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid token", response.getBody().getErrors());
    }
}