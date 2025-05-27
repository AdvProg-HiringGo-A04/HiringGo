package id.ac.ui.cs.advprog.hiringgo.dashboard.controller;

import id.ac.ui.cs.advprog.hiringgo.dashboard.service.DashboardService;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.service.UserService;
import id.ac.ui.cs.advprog.hiringgo.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardControllerTest {

    @Mock
    private DashboardService dashboardService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserService userService;

    @InjectMocks
    private DashboardController dashboardController;

    @Test
    void getDashboard_WithValidToken_ShouldReturnOk() {
        // Arrange
        String token = "Bearer validToken";
        ResponseEntity<String> expectedResponse = ResponseEntity.ok("Dashboard data");

        try (MockedStatic<DashboardResponseHandler> mockedHandler = mockStatic(DashboardResponseHandler.class)) {
            mockedHandler.when(() -> DashboardResponseHandler.handleDashboardRequest(
                            eq(token), eq(jwtUtil), eq(userService), eq(dashboardService), any()))
                    .thenReturn(expectedResponse);

            // Act
            ResponseEntity<?> result = dashboardController.getDashboard(token);

            // Assert
            assertEquals(expectedResponse, result);
            mockedHandler.verify(() -> DashboardResponseHandler.handleDashboardRequest(
                    eq(token), eq(jwtUtil), eq(userService), eq(dashboardService), any()));
        }
    }

    @Test
    void getDashboard_WithNullToken_ShouldReturnUnauthorized() {
        // Arrange
        String token = null;
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Token required");

        try (MockedStatic<DashboardResponseHandler> mockedHandler = mockStatic(DashboardResponseHandler.class)) {
            mockedHandler.when(() -> DashboardResponseHandler.handleDashboardRequest(
                            eq(token), eq(jwtUtil), eq(userService), eq(dashboardService), any()))
                    .thenReturn(expectedResponse);

            // Act
            ResponseEntity<?> result = dashboardController.getDashboard(token);

            // Assert
            assertEquals(expectedResponse, result);
            mockedHandler.verify(() -> DashboardResponseHandler.handleDashboardRequest(
                    eq(token), eq(jwtUtil), eq(userService), eq(dashboardService), any()));
        }
    }

    @Test
    void getDashboard_WithInvalidToken_ShouldReturnUnauthorized() {
        // Arrange
        String token = "InvalidToken";
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Token required");

        try (MockedStatic<DashboardResponseHandler> mockedHandler = mockStatic(DashboardResponseHandler.class)) {
            mockedHandler.when(() -> DashboardResponseHandler.handleDashboardRequest(
                            eq(token), eq(jwtUtil), eq(userService), eq(dashboardService), any()))
                    .thenReturn(expectedResponse);

            // Act
            ResponseEntity<?> result = dashboardController.getDashboard(token);

            // Assert
            assertEquals(expectedResponse, result);
            mockedHandler.verify(() -> DashboardResponseHandler.handleDashboardRequest(
                    eq(token), eq(jwtUtil), eq(userService), eq(dashboardService), any()));
        }
    }
}