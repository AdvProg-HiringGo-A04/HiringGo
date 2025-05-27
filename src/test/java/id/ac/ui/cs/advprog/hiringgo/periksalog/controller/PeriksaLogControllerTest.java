package id.ac.ui.cs.advprog.hiringgo.periksalog.controller;

import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.enums.Role;
import id.ac.ui.cs.advprog.hiringgo.model.WebResponse;
import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogDTO;
import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogStatusUpdateDTO;
import id.ac.ui.cs.advprog.hiringgo.periksalog.service.AuthenticationService;
import id.ac.ui.cs.advprog.hiringgo.periksalog.service.PeriksaLogService;
import id.ac.ui.cs.advprog.hiringgo.periksalog.util.ResponseBuilder;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.enums.StatusLog;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.enums.TipeKategori;
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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PeriksaLogControllerTest {

    @Mock
    private PeriksaLogService logService;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private ResponseBuilder responseBuilder;

    @InjectMocks
    private PeriksaLogController controller;

    private User dosenUser;
    private User mahasiswaUser;
    private LogDTO logDTO;
    private LogStatusUpdateDTO statusUpdateDTO;
    private String token = "Bearer valid-token";

    @BeforeEach
    void setUp() {
        dosenUser = new User();
        dosenUser.setId("dosen-1");
        dosenUser.setRole(Role.DOSEN);

        mahasiswaUser = new User();
        mahasiswaUser.setId("mahasiswa-1");
        mahasiswaUser.setRole(Role.MAHASISWA);

        logDTO = LogDTO.builder()
                .id("log-1")
                .judul("Test Log")
                .kategori(TipeKategori.ASISTENSI)
                .waktuMulai(LocalTime.of(9, 0))
                .waktuSelesai(LocalTime.of(11, 0))
                .tanggalLog(LocalDate.now())
                .status(StatusLog.DIPROSES)
                .mahasiswaName("Test Student")
                .mataKuliahName("Test Course")
                .mataKuliahCode("CS001")
                .durationInHours(2.0)
                .build();

        statusUpdateDTO = LogStatusUpdateDTO.builder()
                .logId("log-1")
                .status(StatusLog.DITERIMA)
                .build();
    }

    // Async Tests
    @Test
    void getAllLogsAsync_Success() throws Exception {
        List<LogDTO> logs = Arrays.asList(logDTO);
        when(authenticationService.authenticateUser(token)).thenReturn(dosenUser);
        when(authenticationService.isDosenUser(dosenUser)).thenReturn(true);
        when(responseBuilder.getUserIdSafely(dosenUser)).thenReturn("dosen-1");
        when(logService.getAllLogsByDosenIdAsync("dosen-1")).thenReturn(CompletableFuture.completedFuture(logs));
        when(responseBuilder.createSuccessResponse(logs)).thenReturn(ResponseEntity.ok(WebResponse.<List<LogDTO>>builder().data(logs).build()));

        CompletableFuture<ResponseEntity<WebResponse<List<LogDTO>>>> result = controller.getAllLogsAsync(token);
        ResponseEntity<WebResponse<List<LogDTO>>> response = result.get();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(authenticationService).authenticateUser(token);
        verify(authenticationService).isDosenUser(dosenUser);
        verify(logService).getAllLogsByDosenIdAsync("dosen-1");
    }

    @SuppressWarnings("unchecked")
    @Test
    void getAllLogsAsync_Forbidden_NonDosenUser() throws Exception {
        when(authenticationService.authenticateUser(token)).thenReturn(mahasiswaUser);
        when(authenticationService.isDosenUser(mahasiswaUser)).thenReturn(false);
        when(responseBuilder.getUserIdSafely(mahasiswaUser)).thenReturn("mahasiswa-1");
        when(responseBuilder.getUserRoleSafely(mahasiswaUser)).thenReturn("MAHASISWA");
        ResponseEntity<WebResponse<List<LogDTO>>> forbiddenResponse = ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(WebResponse.<List<LogDTO>>builder().errors("Access forbidden: Only lecturers can access logs").build());
        when(responseBuilder.createForbiddenResponse("Access forbidden: Only lecturers can access logs")).thenReturn((ResponseEntity) forbiddenResponse);

        CompletableFuture<ResponseEntity<WebResponse<List<LogDTO>>>> result = controller.getAllLogsAsync(token);
        ResponseEntity<WebResponse<List<LogDTO>>> response = result.get();

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(authenticationService).authenticateUser(token);
        verify(authenticationService).isDosenUser(mahasiswaUser);
        verify(responseBuilder).createForbiddenResponse("Access forbidden: Only lecturers can access logs");
    }

    @SuppressWarnings("unchecked")
    @Test
    void getAllLogsAsync_AuthenticationError() throws Exception {
        when(authenticationService.authenticateUser(token)).thenThrow(new IllegalArgumentException("Invalid token"));
        ResponseEntity<WebResponse<List<LogDTO>>> unauthorizedResponse = ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(WebResponse.<List<LogDTO>>builder().errors("Invalid token").build());
        when(responseBuilder.createUnauthorizedResponse("Invalid token")).thenReturn((ResponseEntity) unauthorizedResponse);

        CompletableFuture<ResponseEntity<WebResponse<List<LogDTO>>>> result = controller.getAllLogsAsync(token);
        ResponseEntity<WebResponse<List<LogDTO>>> response = result.get();

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(authenticationService).authenticateUser(token);
        verify(responseBuilder).createUnauthorizedResponse("Invalid token");
    }

    @Test
    void updateLogStatusAsync_Success() throws Exception {
        when(authenticationService.authenticateUser(token)).thenReturn(dosenUser);
        when(authenticationService.isDosenUser(dosenUser)).thenReturn(true);
        when(responseBuilder.getUserIdSafely(dosenUser)).thenReturn("dosen-1");
        when(logService.updateLogStatusAsync("dosen-1", statusUpdateDTO)).thenReturn(CompletableFuture.completedFuture(logDTO));
        when(responseBuilder.createSuccessResponse(logDTO)).thenReturn(ResponseEntity.ok(WebResponse.<LogDTO>builder().data(logDTO).build()));

        CompletableFuture<ResponseEntity<WebResponse<LogDTO>>> result = controller.updateLogStatusAsync(token, "log-1", statusUpdateDTO);
        ResponseEntity<WebResponse<LogDTO>> response = result.get();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("log-1", statusUpdateDTO.getLogId());
        verify(authenticationService).authenticateUser(token);
        verify(authenticationService).isDosenUser(dosenUser);
        verify(logService).updateLogStatusAsync("dosen-1", statusUpdateDTO);
    }

    @SuppressWarnings("unchecked")
    @Test
    void updateLogStatusAsync_Forbidden_NonDosenUser() throws Exception {
        when(authenticationService.authenticateUser(token)).thenReturn(mahasiswaUser);
        when(authenticationService.isDosenUser(mahasiswaUser)).thenReturn(false);
        when(responseBuilder.getUserIdSafely(mahasiswaUser)).thenReturn("mahasiswa-1");
        when(responseBuilder.getUserRoleSafely(mahasiswaUser)).thenReturn("MAHASISWA");
        ResponseEntity<WebResponse<LogDTO>> forbiddenResponse = ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(WebResponse.<LogDTO>builder().errors("Access forbidden: Only lecturers can update log status").build());
        when(responseBuilder.createForbiddenResponse("Access forbidden: Only lecturers can update log status")).thenReturn((ResponseEntity) forbiddenResponse);

        CompletableFuture<ResponseEntity<WebResponse<LogDTO>>> result = controller.updateLogStatusAsync(token, "log-1", statusUpdateDTO);
        ResponseEntity<WebResponse<LogDTO>> response = result.get();

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(authenticationService).authenticateUser(token);
        verify(authenticationService).isDosenUser(mahasiswaUser);
        verify(responseBuilder).createForbiddenResponse("Access forbidden: Only lecturers can update log status");
    }

    @SuppressWarnings("unchecked")
    @Test
    void updateLogStatusAsync_AuthenticationError() throws Exception {
        when(authenticationService.authenticateUser(token)).thenThrow(new IllegalArgumentException("Invalid token"));
        ResponseEntity<WebResponse<LogDTO>> unauthorizedResponse = ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(WebResponse.<LogDTO>builder().errors("Invalid token").build());
        when(responseBuilder.createUnauthorizedResponse("Invalid token")).thenReturn((ResponseEntity) unauthorizedResponse);

        CompletableFuture<ResponseEntity<WebResponse<LogDTO>>> result = controller.updateLogStatusAsync(token, "log-1", statusUpdateDTO);
        ResponseEntity<WebResponse<LogDTO>> response = result.get();

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(authenticationService).authenticateUser(token);
        verify(responseBuilder).createUnauthorizedResponse("Invalid token");
    }

    // Synchronous Tests
    @Test
    void getAllLogs_Success() {
        List<LogDTO> logs = Arrays.asList(logDTO);
        when(authenticationService.authenticateUser(token)).thenReturn(dosenUser);
        when(authenticationService.isDosenUser(dosenUser)).thenReturn(true);
        when(responseBuilder.getUserIdSafely(dosenUser)).thenReturn("dosen-1");
        when(logService.getAllLogsByDosenId("dosen-1")).thenReturn(logs);
        when(responseBuilder.createSuccessResponse(logs)).thenReturn(ResponseEntity.ok(WebResponse.<List<LogDTO>>builder().data(logs).build()));

        ResponseEntity<WebResponse<List<LogDTO>>> response = controller.getAllLogs(token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(authenticationService).authenticateUser(token);
        verify(authenticationService).isDosenUser(dosenUser);
        verify(logService).getAllLogsByDosenId("dosen-1");
    }

    @SuppressWarnings("unchecked")
    @Test
    void getAllLogs_Forbidden_NonDosenUser() {
        when(authenticationService.authenticateUser(token)).thenReturn(mahasiswaUser);
        when(authenticationService.isDosenUser(mahasiswaUser)).thenReturn(false);
        when(responseBuilder.getUserIdSafely(mahasiswaUser)).thenReturn("mahasiswa-1");
        when(responseBuilder.getUserRoleSafely(mahasiswaUser)).thenReturn("MAHASISWA");
        ResponseEntity<WebResponse<List<LogDTO>>> forbiddenResponse = ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(WebResponse.<List<LogDTO>>builder().errors("Access forbidden: Only lecturers can access logs").build());
        when(responseBuilder.createForbiddenResponse("Access forbidden: Only lecturers can access logs")).thenReturn((ResponseEntity) forbiddenResponse);

        ResponseEntity<WebResponse<List<LogDTO>>> response = controller.getAllLogs(token);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(authenticationService).authenticateUser(token);
        verify(authenticationService).isDosenUser(mahasiswaUser);
        verify(responseBuilder).createForbiddenResponse("Access forbidden: Only lecturers can access logs");
    }

    @SuppressWarnings("unchecked")
    @Test
    void getAllLogs_AuthenticationError() {
        when(authenticationService.authenticateUser(token)).thenThrow(new IllegalArgumentException("Invalid token"));
        ResponseEntity<WebResponse<List<LogDTO>>> unauthorizedResponse = ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(WebResponse.<List<LogDTO>>builder().errors("Invalid token").build());
        when(responseBuilder.createUnauthorizedResponse("Invalid token")).thenReturn((ResponseEntity) unauthorizedResponse);

        ResponseEntity<WebResponse<List<LogDTO>>> response = controller.getAllLogs(token);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(authenticationService).authenticateUser(token);
        verify(responseBuilder).createUnauthorizedResponse("Invalid token");
    }

    @Test
    void getAllLogs_GenericException() {
        when(authenticationService.authenticateUser(token)).thenThrow(new RuntimeException("Database error"));
        ResponseEntity<WebResponse<List<LogDTO>>> errorResponse = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(WebResponse.<List<LogDTO>>builder().errors("An error occurred while fetching logs").build());
        when(responseBuilder.handleException(any(RuntimeException.class))).thenReturn(errorResponse);

        ResponseEntity<WebResponse<List<LogDTO>>> response = controller.getAllLogs(token);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(authenticationService).authenticateUser(token);
        verify(responseBuilder).handleException(any(RuntimeException.class));
    }

    @Test
    void updateLogStatus_Success() {
        when(authenticationService.authenticateUser(token)).thenReturn(dosenUser);
        when(authenticationService.isDosenUser(dosenUser)).thenReturn(true);
        when(responseBuilder.getUserIdSafely(dosenUser)).thenReturn("dosen-1");
        when(logService.updateLogStatus("dosen-1", statusUpdateDTO)).thenReturn(logDTO);
        when(responseBuilder.createSuccessResponse(logDTO)).thenReturn(ResponseEntity.ok(WebResponse.<LogDTO>builder().data(logDTO).build()));

        ResponseEntity<WebResponse<LogDTO>> response = controller.updateLogStatus(token, "log-1", statusUpdateDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("log-1", statusUpdateDTO.getLogId());
        verify(authenticationService).authenticateUser(token);
        verify(authenticationService).isDosenUser(dosenUser);
        verify(logService).updateLogStatus("dosen-1", statusUpdateDTO);
    }

    @SuppressWarnings("unchecked")
    @Test
    void updateLogStatus_Forbidden_NonDosenUser() {
        when(authenticationService.authenticateUser(token)).thenReturn(mahasiswaUser);
        when(authenticationService.isDosenUser(mahasiswaUser)).thenReturn(false);
        when(responseBuilder.getUserIdSafely(mahasiswaUser)).thenReturn("mahasiswa-1");
        when(responseBuilder.getUserRoleSafely(mahasiswaUser)).thenReturn("MAHASISWA");
        ResponseEntity<WebResponse<LogDTO>> forbiddenResponse = ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(WebResponse.<LogDTO>builder().errors("Access forbidden: Only lecturers can update log status").build());
        when(responseBuilder.createForbiddenResponse("Access forbidden: Only lecturers can update log status")).thenReturn((ResponseEntity) forbiddenResponse);

        ResponseEntity<WebResponse<LogDTO>> response = controller.updateLogStatus(token, "log-1", statusUpdateDTO);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(authenticationService).authenticateUser(token);
        verify(authenticationService).isDosenUser(mahasiswaUser);
        verify(responseBuilder).createForbiddenResponse("Access forbidden: Only lecturers can update log status");
    }

    @SuppressWarnings("unchecked")
    @Test
    void updateLogStatus_AuthenticationError() {
        when(authenticationService.authenticateUser(token)).thenThrow(new IllegalArgumentException("Invalid token"));
        ResponseEntity<WebResponse<LogDTO>> unauthorizedResponse = ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(WebResponse.<LogDTO>builder().errors("Invalid token").build());
        when(responseBuilder.createUnauthorizedResponse("Invalid token")).thenReturn((ResponseEntity) unauthorizedResponse);

        ResponseEntity<WebResponse<LogDTO>> response = controller.updateLogStatus(token, "log-1", statusUpdateDTO);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(authenticationService).authenticateUser(token);
        verify(responseBuilder).createUnauthorizedResponse("Invalid token");
    }

    @Test
    void updateLogStatus_SecurityException() {
        when(authenticationService.authenticateUser(token)).thenReturn(dosenUser);
        when(authenticationService.isDosenUser(dosenUser)).thenReturn(true);
        when(responseBuilder.getUserIdSafely(dosenUser)).thenReturn("dosen-1");
        when(logService.updateLogStatus("dosen-1", statusUpdateDTO)).thenThrow(new SecurityException("Permission denied"));
        ResponseEntity<WebResponse<LogDTO>> securityResponse = ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(WebResponse.<LogDTO>builder().errors("Permission denied").build());
        when(responseBuilder.createSecurityExceptionResponse("Permission denied")).thenReturn(securityResponse);

        ResponseEntity<WebResponse<LogDTO>> response = controller.updateLogStatus(token, "log-1", statusUpdateDTO);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(responseBuilder).createSecurityExceptionResponse("Permission denied");
    }

    @Test
    void updateLogStatus_GenericException() {
        when(authenticationService.authenticateUser(token)).thenReturn(dosenUser);
        when(authenticationService.isDosenUser(dosenUser)).thenReturn(true);
        when(responseBuilder.getUserIdSafely(dosenUser)).thenReturn("dosen-1");
        when(logService.updateLogStatus("dosen-1", statusUpdateDTO)).thenThrow(new RuntimeException("Database error"));
        when(responseBuilder.getErrorMessage(any(RuntimeException.class), eq("An error occurred while updating log status")))
                .thenReturn("An error occurred while updating log status: Database error");
        ResponseEntity<WebResponse<LogDTO>> errorResponse = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(WebResponse.<LogDTO>builder().errors("An error occurred while updating log status: Database error").build());
        when(responseBuilder.createInternalServerErrorResponseForLog("An error occurred while updating log status: Database error"))
                .thenReturn(errorResponse);

        ResponseEntity<WebResponse<LogDTO>> response = controller.updateLogStatus(token, "log-1", statusUpdateDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(responseBuilder).createInternalServerErrorResponseForLog("An error occurred while updating log status: Database error");
    }

    @Test
    void handleAsyncException_GenericException() throws Exception {
        List<LogDTO> logs = Arrays.asList(logDTO);
        CompletableFuture<List<LogDTO>> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new RuntimeException("Async error"));

        when(authenticationService.authenticateUser(token)).thenReturn(dosenUser);
        when(authenticationService.isDosenUser(dosenUser)).thenReturn(true);
        when(responseBuilder.getUserIdSafely(dosenUser)).thenReturn("dosen-1");
        when(logService.getAllLogsByDosenIdAsync("dosen-1")).thenReturn(failedFuture);
        when(responseBuilder.getErrorMessage(any(Throwable.class), eq("An error occurred while fetching logs")))
                .thenReturn("An error occurred while fetching logs: Async error");
        ResponseEntity<WebResponse<List<LogDTO>>> errorResponse = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(WebResponse.<List<LogDTO>>builder().errors("An error occurred while fetching logs: Async error").build());
        when(responseBuilder.createInternalServerErrorResponse("An error occurred while fetching logs: Async error"))
                .thenReturn(errorResponse);

        CompletableFuture<ResponseEntity<WebResponse<List<LogDTO>>>> result = controller.getAllLogsAsync(token);
        ResponseEntity<WebResponse<List<LogDTO>>> response = result.get();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}