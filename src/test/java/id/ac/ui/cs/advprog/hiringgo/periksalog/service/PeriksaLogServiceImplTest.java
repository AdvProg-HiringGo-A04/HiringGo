package id.ac.ui.cs.advprog.hiringgo.periksalog.service;

import id.ac.ui.cs.advprog.hiringgo.entity.Log;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.enums.StatusLog;
import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogDTO;
import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogStatusUpdateDTO;
import id.ac.ui.cs.advprog.hiringgo.repository.LogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class PeriksaLogServiceImplTest {

    @Mock
    private LogRepository logRepository;

    @Mock
    private ValidationService validationService;

    @Mock
    private LogConverterService logConverterService;

    @InjectMocks
    private PeriksaLogServiceImpl periksaLogService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllLogsByDosenIdAsync() throws ExecutionException, InterruptedException {
        // Arrange
        String dosenId = "dosen-123";
        Log log1 = new Log();
        log1.setId("log-1");
        Log log2 = new Log();
        log2.setId("log-2");
        List<Log> logs = Arrays.asList(log1, log2);

        LogDTO logDTO1 = LogDTO.builder().id("log-1").build();
        LogDTO logDTO2 = LogDTO.builder().id("log-2").build();

        when(logRepository.findAllLogsByDosenId(dosenId)).thenReturn(logs);
        when(logConverterService.convertToDTO(log1)).thenReturn(logDTO1);
        when(logConverterService.convertToDTO(log2)).thenReturn(logDTO2);

        // Act
        CompletableFuture<List<LogDTO>> result = periksaLogService.getAllLogsByDosenIdAsync(dosenId);
        List<LogDTO> logDTOs = result.get();

        // Assert
        assertNotNull(logDTOs);
        assertEquals(2, logDTOs.size());
        assertEquals("log-1", logDTOs.get(0).getId());
        assertEquals("log-2", logDTOs.get(1).getId());

        verify(validationService).validateDosenId(dosenId);
        verify(logRepository).findAllLogsByDosenId(dosenId);
        verify(logConverterService, times(2)).convertToDTO(any(Log.class));
    }

    @Test
    void testGetAllLogsByDosenIdAsyncWithException() {
        // Arrange
        String dosenId = "dosen-123";
        doThrow(new IllegalArgumentException("Invalid dosen ID")).when(validationService).validateDosenId(dosenId);

        // Act & Assert
        CompletableFuture<List<LogDTO>> result = periksaLogService.getAllLogsByDosenIdAsync(dosenId);

        ExecutionException exception = assertThrows(ExecutionException.class, result::get);
        assertTrue(exception.getCause() instanceof RuntimeException);
        assertTrue(exception.getCause().getMessage().contains("Failed to fetch logs"));
    }

    @Test
    void testUpdateLogStatusAsync() throws ExecutionException, InterruptedException {
        // Arrange
        String dosenId = "dosen-123";
        String logId = "log-123";
        LogStatusUpdateDTO updateDTO = LogStatusUpdateDTO.builder()
                .logId(logId)
                .status(StatusLog.DITERIMA)
                .build();

        Log log = new Log();
        log.setId(logId);
        log.setStatus(StatusLog.DIPROSES);

        LogDTO expectedDTO = LogDTO.builder().id(logId).status(StatusLog.DITERIMA).build();

        when(logRepository.findById(logId)).thenReturn(Optional.of(log));
        when(logRepository.save(any(Log.class))).thenReturn(log);
        when(logConverterService.convertToDTO(any(Log.class))).thenReturn(expectedDTO);

        // Act
        CompletableFuture<LogDTO> result = periksaLogService.updateLogStatusAsync(dosenId, updateDTO);
        LogDTO actualDTO = result.get();

        // Assert
        assertNotNull(actualDTO);
        assertEquals(logId, actualDTO.getId());
        assertEquals(StatusLog.DITERIMA, actualDTO.getStatus());

        verify(validationService).validateInputs(dosenId, updateDTO);
        verify(validationService).validateDosenOwnership(logId, dosenId, periksaLogService);
        verify(logRepository).findById(logId);
        verify(logRepository).save(any(Log.class));
        verify(logConverterService).convertToDTO(any(Log.class));
    }

    @Test
    void testUpdateLogStatusAsyncWithSecurityException() {
        // Arrange
        String dosenId = "dosen-123";
        String logId = "log-123";
        LogStatusUpdateDTO updateDTO = LogStatusUpdateDTO.builder()
                .logId(logId)
                .status(StatusLog.DITERIMA)
                .build();

        doThrow(new SecurityException("Access denied"))
                .when(validationService).validateDosenOwnership(logId, dosenId, periksaLogService);

        // Act & Assert
        CompletableFuture<LogDTO> result = periksaLogService.updateLogStatusAsync(dosenId, updateDTO);

        ExecutionException exception = assertThrows(ExecutionException.class, result::get);
        assertTrue(exception.getCause() instanceof SecurityException);
        assertEquals("Access denied", exception.getCause().getMessage());
    }

    @Test
    void testUpdateLogStatusAsyncWithOtherException() {
        // Arrange
        String dosenId = "dosen-123";
        String logId = "log-123";
        LogStatusUpdateDTO updateDTO = LogStatusUpdateDTO.builder()
                .logId(logId)
                .status(StatusLog.DITERIMA)
                .build();

        doThrow(new RuntimeException("Database error"))
                .when(validationService).validateInputs(dosenId, updateDTO);

        // Act & Assert
        CompletableFuture<LogDTO> result = periksaLogService.updateLogStatusAsync(dosenId, updateDTO);

        ExecutionException exception = assertThrows(ExecutionException.class, result::get);
        assertTrue(exception.getCause() instanceof RuntimeException);
        assertTrue(exception.getCause().getMessage().contains("Failed to update log status"));
    }

    @Test
    void testIsLogOwnedByDosenAsync() throws ExecutionException, InterruptedException {
        // Arrange
        String logId = "log-123";
        String dosenId = "dosen-123";

        when(logRepository.isLogOwnedByDosen(logId, dosenId)).thenReturn(true);

        // Act
        CompletableFuture<Boolean> result = periksaLogService.isLogOwnedByDosenAsync(logId, dosenId);
        Boolean isOwned = result.get();

        // Assert
        assertTrue(isOwned);
        verify(validationService).validateLogId(logId);
        verify(validationService).validateDosenId(dosenId);
        verify(logRepository).isLogOwnedByDosen(logId, dosenId);
    }

    @Test
    void testIsLogOwnedByDosenAsyncWithException() throws ExecutionException, InterruptedException {
        // Arrange
        String logId = "log-123";
        String dosenId = "dosen-123";

        doThrow(new RuntimeException("Database error"))
                .when(validationService).validateLogId(logId);

        // Act
        CompletableFuture<Boolean> result = periksaLogService.isLogOwnedByDosenAsync(logId, dosenId);
        Boolean isOwned = result.get();

        // Assert
        assertFalse(isOwned); // Should return false on exception
    }

    @Test
    void testGetAllLogsByDosenId() {
        // Arrange
        String dosenId = "dosen-123";
        Log log1 = new Log();
        log1.setId("log-1");
        Log log2 = new Log();
        log2.setId("log-2");
        List<Log> logs = Arrays.asList(log1, log2);

        LogDTO logDTO1 = LogDTO.builder().id("log-1").build();
        LogDTO logDTO2 = LogDTO.builder().id("log-2").build();

        when(logRepository.findAllLogsByDosenId(dosenId)).thenReturn(logs);
        when(logConverterService.convertToDTO(log1)).thenReturn(logDTO1);
        when(logConverterService.convertToDTO(log2)).thenReturn(logDTO2);

        // Act
        List<LogDTO> result = periksaLogService.getAllLogsByDosenId(dosenId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("log-1", result.get(0).getId());
        assertEquals("log-2", result.get(1).getId());

        verify(validationService).validateDosenId(dosenId);
        verify(logRepository).findAllLogsByDosenId(dosenId);
        verify(logConverterService, times(2)).convertToDTO(any(Log.class));
    }

    @Test
    void testUpdateLogStatus() {
        // Arrange
        String dosenId = "dosen-123";
        String logId = "log-123";
        LogStatusUpdateDTO updateDTO = LogStatusUpdateDTO.builder()
                .logId(logId)
                .status(StatusLog.DITERIMA)
                .build();

        Log log = new Log();
        log.setId(logId);
        log.setStatus(StatusLog.DIPROSES);

        LogDTO expectedDTO = LogDTO.builder().id(logId).status(StatusLog.DITERIMA).build();

        when(logRepository.findById(logId)).thenReturn(Optional.of(log));
        when(logRepository.save(any(Log.class))).thenReturn(log);
        when(logConverterService.convertToDTO(any(Log.class))).thenReturn(expectedDTO);

        // Act
        LogDTO result = periksaLogService.updateLogStatus(dosenId, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(logId, result.getId());
        assertEquals(StatusLog.DITERIMA, result.getStatus());

        verify(validationService).validateInputs(dosenId, updateDTO);
        verify(validationService).validateDosenOwnership(logId, dosenId, periksaLogService);
        verify(logRepository).findById(logId);
        verify(logRepository).save(any(Log.class));
        verify(logConverterService).convertToDTO(any(Log.class));
    }

    @Test
    void testUpdateLogStatusWithLogNotFound() {
        // Arrange
        String dosenId = "dosen-123";
        String logId = "log-123";
        LogStatusUpdateDTO updateDTO = LogStatusUpdateDTO.builder()
                .logId(logId)
                .status(StatusLog.DITERIMA)
                .build();

        when(logRepository.findById(logId)).thenReturn(Optional.empty());

        // Act & Assert
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> periksaLogService.updateLogStatus(dosenId, updateDTO));

        assertTrue(exception.getMessage().contains("Log not found with ID: " + logId));
        verify(validationService).validateInputs(dosenId, updateDTO);
        verify(validationService).validateDosenOwnership(logId, dosenId, periksaLogService);
        verify(logRepository).findById(logId);
        verify(logRepository, never()).save(any(Log.class));
    }

    @Test
    void testUpdateLogStatusWithSecurityException() {
        // Arrange
        String dosenId = "dosen-123";
        String logId = "log-123";
        LogStatusUpdateDTO updateDTO = LogStatusUpdateDTO.builder()
                .logId(logId)
                .status(StatusLog.DITERIMA)
                .build();

        doThrow(new SecurityException("Access denied"))
                .when(validationService).validateDosenOwnership(logId, dosenId, periksaLogService);

        // Act & Assert
        SecurityException exception = assertThrows(SecurityException.class,
                () -> periksaLogService.updateLogStatus(dosenId, updateDTO));

        assertEquals("Access denied", exception.getMessage());
        verify(validationService).validateInputs(dosenId, updateDTO);
        verify(validationService).validateDosenOwnership(logId, dosenId, periksaLogService);
        verify(logRepository, never()).findById(anyString());
    }

    @Test
    void testIsLogOwnedByDosen() {
        // Arrange
        String logId = "log-123";
        String dosenId = "dosen-123";

        when(logRepository.isLogOwnedByDosen(logId, dosenId)).thenReturn(true);

        // Act
        boolean result = periksaLogService.isLogOwnedByDosen(logId, dosenId);

        // Assert
        assertTrue(result);
        verify(validationService).validateLogId(logId);
        verify(validationService).validateDosenId(dosenId);
        verify(logRepository).isLogOwnedByDosen(logId, dosenId);
    }

    @Test
    void testIsLogOwnedByDosenReturnsFalse() {
        // Arrange
        String logId = "log-123";
        String dosenId = "dosen-123";

        when(logRepository.isLogOwnedByDosen(logId, dosenId)).thenReturn(false);

        // Act
        boolean result = periksaLogService.isLogOwnedByDosen(logId, dosenId);

        // Assert
        assertFalse(result);
        verify(validationService).validateLogId(logId);
        verify(validationService).validateDosenId(dosenId);
        verify(logRepository).isLogOwnedByDosen(logId, dosenId);
    }

    @Test
    void testUpdateLogFieldsUpdatesStatusAndDate() {
        // Arrange
        String logId = "log-123";
        LogStatusUpdateDTO updateDTO = LogStatusUpdateDTO.builder()
                .logId(logId)
                .status(StatusLog.DITERIMA)
                .build();

        Log log = new Log();
        log.setId(logId);
        log.setStatus(StatusLog.DIPROSES);
        LocalDate originalDate = log.getUpdatedAt();

        when(logRepository.findById(logId)).thenReturn(Optional.of(log));
        when(logRepository.save(any(Log.class))).thenReturn(log);
        when(logConverterService.convertToDTO(any(Log.class))).thenReturn(LogDTO.builder().build());

        // Act
        periksaLogService.updateLogStatus("dosen-123", updateDTO);

        // Assert
        assertEquals(StatusLog.DITERIMA, log.getStatus());
        assertNotEquals(originalDate, log.getUpdatedAt());
        assertEquals(LocalDate.now(), log.getUpdatedAt());
    }
}