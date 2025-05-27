package id.ac.ui.cs.advprog.hiringgo.dashboard.controller;

import id.ac.ui.cs.advprog.hiringgo.dashboard.service.DashboardService;
import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.enums.Role;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.service.UserService;
import id.ac.ui.cs.advprog.hiringgo.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardResponseHandlerTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserService userService;

    @Mock
    private DashboardService dashboardService;

    @Mock
    private Logger log;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId("user123");
        mockUser.setRole(Role.MAHASISWA);
    }

    @Test
    void handleDashboardRequest_WithValidToken_ShouldReturnDashboardData() {
        // Arrange
        String token = "Bearer validJwtToken";
        String jwt = "validJwtToken";
        Map<String, Object> expectedStatistics = new HashMap<>();
        expectedStatistics.put("totalApplications", 5);

        when(jwtUtil.extractId(jwt)).thenReturn("user123");
        when(jwtUtil.extractRole(jwt)).thenReturn("MAHASISWA");
        when(userService.getUserById("user123")).thenReturn(mockUser);
        when(dashboardService.getStatisticsForUser(mockUser)).thenReturn(expectedStatistics);

        // Act
        ResponseEntity<?> result = DashboardResponseHandler.handleDashboardRequest(
                token, jwtUtil, userService, dashboardService, log);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedStatistics, result.getBody());
        verify(jwtUtil).extractId(jwt);
        verify(jwtUtil).extractRole(jwt);
        verify(userService).getUserById("user123");
        verify(dashboardService).getStatisticsForUser(mockUser);
    }

    @Test
    void handleDashboardRequest_WithNullToken_ShouldReturnUnauthorized() {
        // Act
        ResponseEntity<?> result = DashboardResponseHandler.handleDashboardRequest(
                null, jwtUtil, userService, dashboardService, log);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Token required", result.getBody());
        verify(log).warn("Unauthorized access attempt to dashboard - no token provided");
        verifyNoInteractions(jwtUtil, userService, dashboardService);
    }

    @Test
    void handleDashboardRequest_WithEmptyToken_ShouldReturnUnauthorized() {
        // Act
        ResponseEntity<?> result = DashboardResponseHandler.handleDashboardRequest(
                "", jwtUtil, userService, dashboardService, log);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Token required", result.getBody());
        verify(log).warn("Unauthorized access attempt to dashboard - no token provided");
        verifyNoInteractions(jwtUtil, userService, dashboardService);
    }

    @Test
    void handleDashboardRequest_WithTokenNotStartingWithBearer_ShouldReturnUnauthorized() {
        // Act
        ResponseEntity<?> result = DashboardResponseHandler.handleDashboardRequest(
                "InvalidToken", jwtUtil, userService, dashboardService, log);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Token required", result.getBody());
        verify(log).warn("Unauthorized access attempt to dashboard - no token provided");
        verifyNoInteractions(jwtUtil, userService, dashboardService);
    }

    @Test
    void handleDashboardRequest_WithNullUserId_ShouldReturnUnauthorized() {
        // Arrange
        String token = "Bearer invalidJwtToken";
        String jwt = "invalidJwtToken";

        when(jwtUtil.extractId(jwt)).thenReturn(null);
        when(jwtUtil.extractRole(jwt)).thenReturn("MAHASISWA");

        // Act
        ResponseEntity<?> result = DashboardResponseHandler.handleDashboardRequest(
                token, jwtUtil, userService, dashboardService, log);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Invalid token", result.getBody());
        verify(log).warn("Invalid token - missing user ID or role");
        verifyNoInteractions(userService, dashboardService);
    }

    @Test
    void handleDashboardRequest_WithNullUserRole_ShouldReturnUnauthorized() {
        // Arrange
        String token = "Bearer invalidJwtToken";
        String jwt = "invalidJwtToken";

        when(jwtUtil.extractId(jwt)).thenReturn("user123");
        when(jwtUtil.extractRole(jwt)).thenReturn(null);

        // Act
        ResponseEntity<?> result = DashboardResponseHandler.handleDashboardRequest(
                token, jwtUtil, userService, dashboardService, log);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Invalid token", result.getBody());
        verify(log).warn("Invalid token - missing user ID or role");
        verifyNoInteractions(userService, dashboardService);
    }

    @Test
    void handleDashboardRequest_WithBothNullUserIdAndRole_ShouldReturnUnauthorized() {
        // Arrange
        String token = "Bearer invalidJwtToken";
        String jwt = "invalidJwtToken";

        when(jwtUtil.extractId(jwt)).thenReturn(null);
        when(jwtUtil.extractRole(jwt)).thenReturn(null);

        // Act
        ResponseEntity<?> result = DashboardResponseHandler.handleDashboardRequest(
                token, jwtUtil, userService, dashboardService, log);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Invalid token", result.getBody());
        verify(log).warn("Invalid token - missing user ID or role");
        verifyNoInteractions(userService, dashboardService);
    }

    @Test
    void handleDashboardRequest_WithIllegalArgumentException_ShouldReturnBadRequest() {
        // Arrange
        String token = "Bearer validJwtToken";
        String jwt = "validJwtToken";
        String errorMessage = "Invalid argument";

        when(jwtUtil.extractId(jwt)).thenThrow(new IllegalArgumentException(errorMessage));

        // Act
        ResponseEntity<?> result = DashboardResponseHandler.handleDashboardRequest(
                token, jwtUtil, userService, dashboardService, log);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Invalid request: " + errorMessage, result.getBody());
        verify(log).error("Invalid argument when getting dashboard: {}", errorMessage);
    }

    @Test
    void handleDashboardRequest_WithUnexpectedException_ShouldReturnInternalServerError() {
        // Arrange
        String token = "Bearer validJwtToken";
        String jwt = "validJwtToken";
        RuntimeException exception = new RuntimeException("Unexpected error");

        when(jwtUtil.extractId(jwt)).thenThrow(exception);

        // Act
        ResponseEntity<?> result = DashboardResponseHandler.handleDashboardRequest(
                token, jwtUtil, userService, dashboardService, log);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals("Failed to retrieve dashboard", result.getBody());
        verify(log).error(eq("Unexpected error when getting dashboard: {}"), eq("Unexpected error"), eq(exception));
    }

    @Test
    void handleDashboardRequest_WithValidTokenAndDebugLogging_ShouldLogDebugMessages() {
        // Arrange
        String token = "Bearer validJwtToken";
        String jwt = "validJwtToken";
        String userId = "user123";
        String userRole = "DOSEN";
        Map<String, Object> expectedStatistics = new HashMap<>();

        mockUser.setRole(Role.DOSEN);

        when(jwtUtil.extractId(jwt)).thenReturn(userId);
        when(jwtUtil.extractRole(jwt)).thenReturn(userRole);
        when(userService.getUserById(userId)).thenReturn(mockUser);
        when(dashboardService.getStatisticsForUser(mockUser)).thenReturn(expectedStatistics);

        // Act
        ResponseEntity<?> result = DashboardResponseHandler.handleDashboardRequest(
                token, jwtUtil, userService, dashboardService, log);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(log).debug("Dashboard request from user: {} with role: {}", userId, userRole);
        verify(log).debug("Dashboard statistics retrieved for user role: {}", mockUser.getRole());
    }

    @Test
    void handleDashboardRequest_WithShortBearerToken_ShouldReturnUnauthorized() {
        // Arrange - Token shorter than "Bearer " (7 characters)
        String token = "Bear";

        // Act
        ResponseEntity<?> result = DashboardResponseHandler.handleDashboardRequest(
                token, jwtUtil, userService, dashboardService, log);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Token required", result.getBody());
        verify(log).warn("Unauthorized access attempt to dashboard - no token provided");
        verifyNoInteractions(jwtUtil, userService, dashboardService);
    }

    @Test
    void handleDashboardRequest_WithExactBearerLength_ShouldProcessEmptyJwt() {
        // Arrange - Token exactly "Bearer " (7 characters) - results in empty JWT
        String token = "Bearer ";
        String jwt = "";

        when(jwtUtil.extractId(jwt)).thenReturn(null);
        when(jwtUtil.extractRole(jwt)).thenReturn(null);

        // Act
        ResponseEntity<?> result = DashboardResponseHandler.handleDashboardRequest(
                token, jwtUtil, userService, dashboardService, log);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Invalid token", result.getBody());
        verify(log).warn("Invalid token - missing user ID or role");
        verify(jwtUtil).extractId(jwt);
        verify(jwtUtil).extractRole(jwt);
    }
}