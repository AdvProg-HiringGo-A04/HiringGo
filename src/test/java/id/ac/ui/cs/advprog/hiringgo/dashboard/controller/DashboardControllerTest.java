package id.ac.ui.cs.advprog.hiringgo.dashboard.controller;

import id.ac.ui.cs.advprog.hiringgo.common.model.User;
import id.ac.ui.cs.advprog.hiringgo.common.model.UserRole;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.AdminStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.DosenStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.MahasiswaStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.service.DashboardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DashboardControllerTest {

    @Mock
    private DashboardService dashboardService;

    @InjectMocks
    private DashboardController dashboardController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getDashboard_WhenUserIsAuthenticated_ShouldReturnStatistics() {
        // Arrange
        User adminUser = User.builder()
                .id(1L)
                .role(UserRole.ADMIN)
                .build();

        AdminStatisticsDTO expectedStats = AdminStatisticsDTO.builder()
                .totalDosen(10L)
                .totalMahasiswa(100L)
                .totalMataKuliah(20L)
                .totalLowongan(30L)
                .build();

        when(dashboardService.getStatisticsForUser(adminUser)).thenReturn(expectedStats);

        // Act
        ResponseEntity<?> response = dashboardController.getDashboard(adminUser);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedStats, response.getBody());
    }

    @Test
    void getDashboard_WhenUserIsNull_ShouldReturnUnauthorized() {
        // Act
        ResponseEntity<?> response = dashboardController.getDashboard(null);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(dashboardService, never()).getStatisticsForUser(any());
    }

    @Test
    void getDashboard_WhenServiceThrowsException_ShouldReturnInternalServerError() {
        // Arrange
        User adminUser = User.builder()
                .id(1L)
                .role(UserRole.ADMIN)
                .build();

        when(dashboardService.getStatisticsForUser(adminUser)).thenThrow(new RuntimeException("Test exception"));

        // Act
        ResponseEntity<?> response = dashboardController.getDashboard(adminUser);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void getAdminDashboard_WhenUserIsAdmin_ShouldReturnStatistics() {
        // Arrange
        User adminUser = User.builder()
                .id(1L)
                .role(UserRole.ADMIN)
                .build();

        AdminStatisticsDTO expectedStats = AdminStatisticsDTO.builder()
                .totalDosen(10L)
                .totalMahasiswa(100L)
                .totalMataKuliah(20L)
                .totalLowongan(30L)
                .build();

        when(dashboardService.getAdminStatistics()).thenReturn(expectedStats);

        // Act
        ResponseEntity<AdminStatisticsDTO> response = dashboardController.getAdminDashboard(adminUser);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedStats, response.getBody());
    }

    @Test
    void getAdminDashboard_WhenUserIsNotAdmin_ShouldReturnForbidden() {
        // Arrange
        User dosenUser = User.builder()
                .id(1L)
                .role(UserRole.DOSEN)
                .build();

        // Act
        ResponseEntity<AdminStatisticsDTO> response = dashboardController.getAdminDashboard(dosenUser);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(dashboardService, never()).getAdminStatistics();
    }

    @Test
    void getDosenDashboard_WhenUserIsDosen_ShouldReturnStatistics() {
        // Arrange
        User dosenUser = User.builder()
                .id(1L)
                .role(UserRole.DOSEN)
                .build();

        DosenStatisticsDTO expectedStats = DosenStatisticsDTO.builder()
                .totalMataKuliah(5L)
                .openLowonganCount(10L)
                .totalMahasiswaAssistant(50L)
                .build();

        when(dashboardService.getDosenStatistics(dosenUser.getId())).thenReturn(expectedStats);

        // Act
        ResponseEntity<DosenStatisticsDTO> response = dashboardController.getDosenDashboard(dosenUser);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedStats, response.getBody());
        verify(dashboardService).getDosenStatistics(dosenUser.getId());
    }

    @Test
    void getDosenDashboard_WhenUserIsNotDosen_ShouldReturnForbidden() {
        // Arrange
        User adminUser = User.builder()
                .id(1L)
                .role(UserRole.ADMIN)
                .build();

        // Act
        ResponseEntity<DosenStatisticsDTO> response = dashboardController.getDosenDashboard(adminUser);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(dashboardService, never()).getDosenStatistics(anyLong());
    }

    @Test
    void getDosenDashboard_WhenUserIsNull_ShouldReturnForbidden() {
        // Act
        ResponseEntity<DosenStatisticsDTO> response = dashboardController.getDosenDashboard(null);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(dashboardService, never()).getDosenStatistics(anyLong());
    }

    @Test
    void getMahasiswaDashboard_WhenUserIsMahasiswa_ShouldReturnStatistics() {
        // Arrange
        User mahasiswaUser = User.builder()
                .id(1L)
                .role(UserRole.MAHASISWA)
                .build();

            MahasiswaStatisticsDTO expectedStats = MahasiswaStatisticsDTO.builder()
                    .openLowonganCount(5L)
                    .acceptedLowonganCount(3L)
                    .rejectedLowonganCount(2L)
                    .pendingLowonganCount(1L)
                    .totalLogHours(45.5)
                    .totalInsentif(500000.0)
                    .acceptedLowonganList(new ArrayList<>())
                    .build();

        when(dashboardService.getMahasiswaStatistics(mahasiswaUser.getId())).thenReturn(expectedStats);

        // Act
        ResponseEntity<MahasiswaStatisticsDTO> response = dashboardController.getMahasiswaDashboard(mahasiswaUser);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedStats, response.getBody());
        verify(dashboardService).getMahasiswaStatistics(mahasiswaUser.getId());
    }

    @Test
    void getMahasiswaDashboard_WhenUserIsNotMahasiswa_ShouldReturnForbidden() {
        // Arrange
        User dosenUser = User.builder()
                .id(1L)
                .role(UserRole.DOSEN)
                .build();

        // Act
        ResponseEntity<MahasiswaStatisticsDTO> response = dashboardController.getMahasiswaDashboard(dosenUser);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(dashboardService, never()).getMahasiswaStatistics(anyLong());
    }

    @Test
    void getMahasiswaDashboard_WhenUserIsNull_ShouldReturnForbidden() {
        // Act
        ResponseEntity<MahasiswaStatisticsDTO> response = dashboardController.getMahasiswaDashboard(null);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(dashboardService, never()).getMahasiswaStatistics(anyLong());
    }
}