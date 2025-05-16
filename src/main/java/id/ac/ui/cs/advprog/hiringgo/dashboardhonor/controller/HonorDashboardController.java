package id.ac.ui.cs.advprog.hiringgo.dashboardhonor.controller;

import id.ac.ui.cs.advprog.hiringgo.model.WebResponse;
import id.ac.ui.cs.advprog.hiringgo.dashboardhonor.model.DashboardHonorDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboardhonor.model.Log;
import id.ac.ui.cs.advprog.hiringgo.dashboardhonor.service.HonorDashboardService;
import id.ac.ui.cs.advprog.hiringgo.security.JwtUtilImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(
        path = "/api/students/{studentId}/honor",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class HonorDashboardController {

    private final HonorDashboardService service;
    private final JwtUtilImpl jwtUtil;

    public HonorDashboardController(HonorDashboardService service, JwtUtilImpl jwtUtil) {
        this.service = service;
        this.jwtUtil = jwtUtil;
    }

    private void authorize(String token, String requiredRole) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        String jwt = token.substring(7);
        String role = jwtUtil.extractRole(jwt);
        if (!requiredRole.equals(role)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
    }

    @GetMapping("/logs")
    public ResponseEntity<WebResponse<Map<String, List<Log>>>> getLogs(
            @RequestHeader(name = "Authorization", required = false) String token,
            @PathVariable String studentId,
            @RequestParam int year,
            @RequestParam int month
    ) {
        authorize(token, "STUDENT");

        YearMonth period = YearMonth.of(year, month);
        Map<String, List<Log>> data = service.getLogsForMahasiswaByPeriod(studentId, period);

        WebResponse<Map<String, List<Log>>> response =
                new WebResponse<>(data, null);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<WebResponse<Map<String, BigDecimal>>> getSummary(
            @RequestHeader(name = "Authorization", required = false) String token,
            @PathVariable String studentId,
            @RequestParam int year,
            @RequestParam int month
    ) {
        authorize(token, "STUDENT");

        YearMonth period = YearMonth.of(year, month);
        Map<String, BigDecimal> data = service.calculateHonor(studentId, period);

        WebResponse<Map<String, BigDecimal>> response =
                new WebResponse<>(data, null);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/table")
    public ResponseEntity<WebResponse<List<DashboardHonorDTO>>> getTable(
            @RequestHeader(name = "Authorization", required = false) String token,
            @PathVariable String studentId,
            @RequestParam int year,
            @RequestParam int month
    ) {
        authorize(token, "STUDENT");

        YearMonth period = YearMonth.of(year, month);
        List<DashboardHonorDTO> data = service.getHonorTable(studentId, period);

        WebResponse<List<DashboardHonorDTO>> response =
                new WebResponse<>(data, null);

        return ResponseEntity.ok(response);
    }
}
