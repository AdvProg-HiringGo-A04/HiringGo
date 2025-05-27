package id.ac.ui.cs.advprog.hiringgo.dashboard.controller;

import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.enums.Role;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.AdminStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.DosenStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.MahasiswaStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.service.DashboardService;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.service.UserService;
import id.ac.ui.cs.advprog.hiringgo.security.JwtUtil;
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

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserService userService;

    @InjectMocks
    private DashboardController dashboardController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getDashboard_WhenUserIsAdmin_ShouldReturnAdminStatistics() {
        // Arrange
        String token = "Bearer valid.jwt.token";
        String userId = "1";
        String userRole = "ADMIN";
        User adminUser = createMockUser(userId, Role.ADMIN);
        AdminStatisticsDTO expectedStats = new AdminStatisticsDTO(10L, 100L, 20L, 30L);

        when(jwtUtil.extractId("valid.jwt.token")).thenReturn(userId);
        when(jwtUtil.extractRole("valid.jwt.token")).thenReturn(userRole);
        when(userService.getUserById(userId)).thenReturn(adminUser);
        when(dashboardService.getStatisticsForUser(adminUser)).thenReturn(expectedStats);

        // Act
        ResponseEntity<?> response = dashboardController.getDashboard(token);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedStats, response.getBody());
    }

    @Test
    void getDashboard_WhenUserIsDosen_ShouldReturnDosenStatistics() {
        // Arrange
        String token = "Bearer valid.jwt.token";
        String userId = "1";
        String userRole = "DOSEN";
        User dosenUser = createMockUser(userId, Role.DOSEN);
        DosenStatisticsDTO expectedStats = new DosenStatisticsDTO(5L, 10L, 50L);

        when(jwtUtil.extractId("valid.jwt.token")).thenReturn(userId);
        when(jwtUtil.extractRole("valid.jwt.token")).thenReturn(userRole);
        when(userService.getUserById(userId)).thenReturn(dosenUser);
        when(dashboardService.getStatisticsForUser(dosenUser)).thenReturn(expectedStats);

        // Act
        ResponseEntity<?> response = dashboardController.getDashboard(token);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedStats, response.getBody());
    }

    @Test
    void getDashboard_WhenUserIsMahasiswa_ShouldReturnMahasiswaStatistics() {
        // Arrange
        String token = "Bearer valid.jwt.token";
        String userId = "1";
        String userRole = "MAHASISWA";
        User mahasiswaUser = createMockUser(userId, Role.MAHASISWA);
        MahasiswaStatisticsDTO expectedStats = new MahasiswaStatisticsDTO(
                5L, 3L, 2L, 1L, 45.5, 500000.0, new ArrayList<>()
        );

        when(jwtUtil.extractId("valid.jwt.token")).thenReturn(userId);
        when(jwtUtil.extractRole("valid.jwt.token")).thenReturn(userRole);
        when(userService.getUserById(userId)).thenReturn(mahasiswaUser);
        when(dashboardService.getStatisticsForUser(mahasiswaUser)).thenReturn(expectedStats);

        // Act
        ResponseEntity<?> response = dashboardController.getDashboard(token);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedStats, response.getBody());
    }

    @Test
    void getDashboard_WhenTokenIsNull_ShouldReturnUnauthorized() {
        // Act
        ResponseEntity<?> response = dashboardController.getDashboard(null);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Token required", response.getBody());
        verify(dashboardService, never()).getStatisticsForUser(any());
        verify(jwtUtil, never()).extractId(any());
        verify(userService, never()).getUserById(any());
    }

    @Test
    void getDashboard_WhenTokenDoesNotStartWithBearer_ShouldReturnUnauthorized() {
        // Act
        ResponseEntity<?> response = dashboardController.getDashboard("InvalidToken");

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Token required", response.getBody());
        verify(dashboardService, never()).getStatisticsForUser(any());
        verify(jwtUtil, never()).extractId(any());
        verify(userService, never()).getUserById(any());
    }

    @Test
    void getDashboard_WhenUserIdIsNull_ShouldReturnUnauthorized() {
        // Arrange
        String token = "Bearer valid.jwt.token";
        when(jwtUtil.extractId("valid.jwt.token")).thenReturn(null);
        when(jwtUtil.extractRole("valid.jwt.token")).thenReturn("ADMIN");

        // Act
        ResponseEntity<?> response = dashboardController.getDashboard(token);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid token", response.getBody());
        verify(userService, never()).getUserById(any());
        verify(dashboardService, never()).getStatisticsForUser(any());
    }

    @Test
    void getDashboard_WhenUserRoleIsNull_ShouldReturnUnauthorized() {
        // Arrange
        String token = "Bearer valid.jwt.token";
        when(jwtUtil.extractId("valid.jwt.token")).thenReturn("1");
        when(jwtUtil.extractRole("valid.jwt.token")).thenReturn(null);

        // Act
        ResponseEntity<?> response = dashboardController.getDashboard(token);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid token", response.getBody());
        verify(userService, never()).getUserById(any());
        verify(dashboardService, never()).getStatisticsForUser(any());
    }

    @Test
    void getDashboard_WhenServiceThrowsIllegalArgument_ShouldReturnBadRequest() {
        // Arrange
        String token = "Bearer valid.jwt.token";
        String userId = "1";
        String userRole = "ADMIN";
        User user = createMockUser(userId, Role.ADMIN);

        when(jwtUtil.extractId("valid.jwt.token")).thenReturn(userId);
        when(jwtUtil.extractRole("valid.jwt.token")).thenReturn(userRole);
        when(userService.getUserById(userId)).thenReturn(user);
        when(dashboardService.getStatisticsForUser(user)).thenThrow(new IllegalArgumentException("Invalid user"));

        // Act
        ResponseEntity<?> response = dashboardController.getDashboard(token);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid request: Invalid user", response.getBody());
    }

    @Test
    void getDashboard_WhenServiceThrowsException_ShouldReturnInternalServerError() {
        // Arrange
        String token = "Bearer valid.jwt.token";
        String userId = "1";
        String userRole = "ADMIN";
        User user = createMockUser(userId, Role.ADMIN);

        when(jwtUtil.extractId("valid.jwt.token")).thenReturn(userId);
        when(jwtUtil.extractRole("valid.jwt.token")).thenReturn(userRole);
        when(userService.getUserById(userId)).thenReturn(user);
        when(dashboardService.getStatisticsForUser(user)).thenThrow(new RuntimeException("Test exception"));

        // Act
        ResponseEntity<?> response = dashboardController.getDashboard(token);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to retrieve dashboard", response.getBody());
    }

    @Test
    void getDashboard_WhenJwtUtilThrowsException_ShouldReturnInternalServerError() {
        // Arrange
        String token = "Bearer invalid.jwt.token";
        when(jwtUtil.extractId("invalid.jwt.token")).thenThrow(new RuntimeException("JWT parsing error"));

        // Act
        ResponseEntity<?> response = dashboardController.getDashboard(token);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to retrieve dashboard", response.getBody());
        verify(userService, never()).getUserById(any());
        verify(dashboardService, never()).getStatisticsForUser(any());
    }

    @Test
    void getDashboard_WhenUserServiceThrowsException_ShouldReturnInternalServerError() {
        // Arrange
        String token = "Bearer valid.jwt.token";
        String userId = "1";
        String userRole = "ADMIN";

        when(jwtUtil.extractId("valid.jwt.token")).thenReturn(userId);
        when(jwtUtil.extractRole("valid.jwt.token")).thenReturn(userRole);
        when(userService.getUserById(userId)).thenThrow(new RuntimeException("User service error"));

        // Act
        ResponseEntity<?> response = dashboardController.getDashboard(token);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to retrieve dashboard", response.getBody());
        verify(dashboardService, never()).getStatisticsForUser(any());
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