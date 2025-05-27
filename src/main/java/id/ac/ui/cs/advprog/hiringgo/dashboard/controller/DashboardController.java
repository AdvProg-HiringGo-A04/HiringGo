package id.ac.ui.cs.advprog.hiringgo.dashboard.controller;

import id.ac.ui.cs.advprog.hiringgo.dashboard.service.DashboardService;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.service.UserService;
import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        return DashboardResponseHandler.handleDashboardRequest(
                token, jwtUtil, userService, dashboardService, log
        );
    }
}