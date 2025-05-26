package id.ac.ui.cs.advprog.hiringgo.manajemenLog.controller;

import id.ac.ui.cs.advprog.hiringgo.common.ApiResponse;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.dto.LogRequest;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.dto.LogResponse;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.service.LogService;
import id.ac.ui.cs.advprog.hiringgo.security.CurrentUserProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LogControllerTest {

    @Mock
    private LogService logService;

    @Mock
    private CurrentUserProvider currentUserProvider;

    private LogController logController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        logController = new LogController(logService, currentUserProvider);
    }

    @Test
    void testGetAllLogsSuccess() {
        String lowonganId = "lowongan123";
        String mahasiswaId = "mahasiswa123";
        LogResponse logResponse = new LogResponse();
        logResponse.setId("log123");
        logResponse.setJudul("Test Log");

        when(currentUserProvider.getCurrentUserId()).thenReturn(mahasiswaId);
        when(logService.getAllLogs(lowonganId, mahasiswaId)).thenReturn(List.of(logResponse));

        ResponseEntity<ApiResponse<List<LogResponse>>> response = logController.getAllLogs(lowonganId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Successfully fetched all logs", response.getBody().getMessage());
        assertEquals(1, response.getBody().getData().size());
    }

    @Test
    void testGetLogByIdSuccess() {
        String logId = "log123";
        String mahasiswaId = "mahasiswa123";
        LogResponse logResponse = new LogResponse();
        logResponse.setId(logId);
        logResponse.setJudul("Test Log");

        when(currentUserProvider.getCurrentUserId()).thenReturn(mahasiswaId);
        when(logService.getLogById(logId, mahasiswaId)).thenReturn(logResponse);

        ResponseEntity<ApiResponse<LogResponse>> response = logController.getLogById(logId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Successfully fetched log", response.getBody().getMessage());
        assertEquals("Test Log", response.getBody().getData().getJudul());
    }

    @Test
    void testCreateLogSuccess() {
        LogRequest logRequest = new LogRequest();
        logRequest.setJudul("Test Log");
        logRequest.setMataKuliahId("mataKuliah123");
        logRequest.setLowonganId("lowongan123");

        String mahasiswaId = "mahasiswa123";
        LogResponse createdLogResponse = new LogResponse();
        createdLogResponse.setId("log123");

        when(currentUserProvider.getCurrentUserId()).thenReturn(mahasiswaId);
        when(logService.createLog(logRequest, mahasiswaId)).thenReturn(createdLogResponse);

        ResponseEntity<ApiResponse<LogResponse>> response = logController.createLog(logRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Successfully created log", response.getBody().getMessage());
        assertEquals("log123", response.getBody().getData().getId());
    }

    @Test
    void testUpdateLogSuccess() {
        String logId = "log123";
        LogRequest logRequest = new LogRequest();
        logRequest.setJudul("Updated Log");

        String mahasiswaId = "mahasiswa123";
        LogResponse updatedLogResponse = new LogResponse();
        updatedLogResponse.setId(logId);

        when(currentUserProvider.getCurrentUserId()).thenReturn(mahasiswaId);
        when(logService.updateLog(logId, logRequest, mahasiswaId)).thenReturn(updatedLogResponse);

        ResponseEntity<ApiResponse<LogResponse>> response = logController.updateLog(logId, logRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Successfully updated log", response.getBody().getMessage());
        assertEquals("log123", response.getBody().getData().getId());
    }

    @Test
    void testDeleteLogSuccess() {
        String lowonganId = "lowongan123";
        String logId = "log123";
        String mahasiswaId = "mahasiswa123";

        when(currentUserProvider.getCurrentUserId()).thenReturn(mahasiswaId);
        doNothing().when(logService).deleteLog(logId, lowonganId, mahasiswaId);

        ResponseEntity<ApiResponse<String>> response = logController.deleteLog(lowonganId, logId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Successfully deleted log", response.getBody().getMessage());
    }

    @Test
    void testGetTotalJamPerBulanSuccess() {
        String lowonganId = "lowongan123";
        String mahasiswaId = "mahasiswa123";

        when(currentUserProvider.getCurrentUserId()).thenReturn(mahasiswaId);
        Map<String, Double> expectedMap = Map.of("totalJam", 40.5);
        when(logService.getTotalJamPerBulan(lowonganId, mahasiswaId)).thenReturn(expectedMap);
        Map<String, Double> result = logController.getTotalJamPerBulan(lowonganId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(40.5, result.get("totalJam"));
    }

    @Test
    void testGetTotalJamPerBulanNoData() {
        String lowonganId = "lowongan123";
        String mahasiswaId = "mahasiswa123";

        when(currentUserProvider.getCurrentUserId()).thenReturn(mahasiswaId);
        when(logService.getTotalJamPerBulan(lowonganId, mahasiswaId)).thenReturn(Map.of());

        Map<String, Double> result = logController.getTotalJamPerBulan(lowonganId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}
