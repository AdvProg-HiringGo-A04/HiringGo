package id.ac.ui.cs.advprog.hiringgo.dashboard.controller;

import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<?> getDashboard(@AuthenticationPrincipal User user) {
        if (user == null) {
            log.warn("Unauthorized access attempt to dashboard");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("User not authenticated");
        }

        try {
            Object statistics = dashboardService.getStatisticsForUser(user);
            log.debug("Dashboard statistics retrieved for user role: {}", user.getRole());
            return ResponseEntity.ok(statistics);
        } catch (IllegalArgumentException e) {
            log.error("Invalid argument when getting dashboard for user {}: {}", user.getId(), e.getMessage());
            return ResponseEntity.badRequest()
                    .body("Invalid request: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error when getting dashboard for user {}: {}", user.getId(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve dashboard");
        }
    }
}