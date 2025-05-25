package id.ac.ui.cs.advprog.hiringgo.dashboard.controller;

import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.enums.Role;
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
    void getDashboard_WhenUserIsAdmin_ShouldReturnAdminStatistics() {
        // Arrange
        User adminUser = createMockUser("1", Role.ADMIN);
        AdminStatisticsDTO expectedStats = new AdminStatisticsDTO(10L, 100L, 20L, 30L);

        when(dashboardService.getStatisticsForUser(adminUser)).thenReturn(expectedStats);

        // Act
        ResponseEntity<?> response = dashboardController.getDashboard(adminUser);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedStats, response.getBody());
    }

    @Test
    void getDashboard_WhenUserIsDosen_ShouldReturnDosenStatistics() {
        // Arrange
        User dosenUser = createMockUser("1", Role.DOSEN);
        DosenStatisticsDTO expectedStats = new DosenStatisticsDTO(5L, 10L, 50L);

        when(dashboardService.getStatisticsForUser(dosenUser)).thenReturn(expectedStats);

        // Act
        ResponseEntity<?> response = dashboardController.getDashboard(dosenUser);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedStats, response.getBody());
    }

    @Test
    void getDashboard_WhenUserIsMahasiswa_ShouldReturnMahasiswaStatistics() {
        // Arrange
        User mahasiswaUser = createMockUser("1", Role.MAHASISWA);
        MahasiswaStatisticsDTO expectedStats = new MahasiswaStatisticsDTO(
                5L, 3L, 2L, 1L, 45.5, 500000.0, new ArrayList<>()
        );

        when(dashboardService.getStatisticsForUser(mahasiswaUser)).thenReturn(expectedStats);

        // Act
        ResponseEntity<?> response = dashboardController.getDashboard(mahasiswaUser);

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
    void getDashboard_WhenServiceThrowsIllegalArgument_ShouldReturnBadRequest() {
        // Arrange
        User user = createMockUser("1", Role.ADMIN);
        when(dashboardService.getStatisticsForUser(user)).thenThrow(new IllegalArgumentException("Invalid user"));

        // Act
        ResponseEntity<?> response = dashboardController.getDashboard(user);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getDashboard_WhenServiceThrowsException_ShouldReturnInternalServerError() {
        // Arrange
        User user = createMockUser("1", Role.ADMIN);
        when(dashboardService.getStatisticsForUser(user)).thenThrow(new RuntimeException("Test exception"));

        // Act
        ResponseEntity<?> response = dashboardController.getDashboard(user);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // Helper method to create mock User entities
    private User createMockUser(String id, Role role) {
        User user = new User();
        user.setId(id);
        user.setEmail("user@example.com");
        user.setRole(role);
        user.setPassword("password");
        return user;
    }
}