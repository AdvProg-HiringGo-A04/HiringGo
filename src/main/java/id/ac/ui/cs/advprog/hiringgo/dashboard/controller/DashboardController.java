package id.ac.ui.cs.advprog.hiringgo.dashboard.controller;
import id.ac.ui.cs.advprog.hiringgo.common.model.User;
import id.ac.ui.cs.advprog.hiringgo.common.model.UserRole;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.AdminStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.DosenStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.MahasiswaStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<?> getDashboard(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        try {
            Object statistics = dashboardService.getStatisticsForUser(user);
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve dashboard: " + e.getMessage());
        }
    }

    // These endpoints could be used for testing or direct access to specific statistics
    @GetMapping("/admin")
    public ResponseEntity<AdminStatisticsDTO> getAdminDashboard(@AuthenticationPrincipal User user) {
        if (user == null || user.getRole() != UserRole.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        return ResponseEntity.ok(dashboardService.getAdminStatistics());
    }

    @GetMapping("/dosen")
    public ResponseEntity<DosenStatisticsDTO> getDosenDashboard(@AuthenticationPrincipal User user) {
        if (user == null || user.getRole() != UserRole.DOSEN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        return ResponseEntity.ok(dashboardService.getDosenStatistics(user.getId()));
    }

    @GetMapping("/mahasiswa")
    public ResponseEntity<MahasiswaStatisticsDTO> getMahasiswaDashboard(@AuthenticationPrincipal User user) {
        if (user == null || user.getRole() != UserRole.MAHASISWA) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        return ResponseEntity.ok(dashboardService.getMahasiswaStatistics(user.getId()));
    }
}