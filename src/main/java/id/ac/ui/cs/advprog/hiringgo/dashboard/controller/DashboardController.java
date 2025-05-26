package id.ac.ui.cs.advprog.hiringgo.dashboard.controller;

import id.ac.ui.cs.advprog.hiringgo.dashboard.service.DashboardService;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.service.UserService;
import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getDashboard(
            @RequestHeader(name = "Authorization", required = false) String token) {

        if (token == null || !token.startsWith("Bearer ")) {
            log.warn("Unauthorized access attempt to dashboard - no token provided");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token required");
        }

        try {
            String jwt = token.substring(7);

            String userId = jwtUtil.extractId(jwt);
            String userRole = jwtUtil.extractRole(jwt);

            if (userId == null || userRole == null) {
                log.warn("Invalid token - missing user ID or role");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid token");
            }
            User user = userService.getUserById(userId);

            log.debug("Dashboard request from user: {} with role: {}", userId, userRole);

            Object statistics = dashboardService.getStatisticsForUser(user);

            log.debug("Dashboard statistics retrieved for user role: {}", userRole);
            return ResponseEntity.ok(statistics);

        } catch (IllegalArgumentException e) {
            log.error("Invalid argument when getting dashboard: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body("Invalid request: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error when getting dashboard: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve dashboard");
        }
    }
}