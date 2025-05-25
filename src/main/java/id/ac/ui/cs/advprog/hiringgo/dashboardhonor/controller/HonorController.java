package id.ac.ui.cs.advprog.hiringgo.dashboardhonor.controller;

import id.ac.ui.cs.advprog.hiringgo.dashboardhonor.model.HonorResponse;
import id.ac.ui.cs.advprog.hiringgo.entity.Log;
import id.ac.ui.cs.advprog.hiringgo.model.WebResponse;
import id.ac.ui.cs.advprog.hiringgo.repository.LogRepository;
import id.ac.ui.cs.advprog.hiringgo.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class HonorController {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private void authorize(String token, String requiredRole, String id) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        String jwt = token.substring(7);
        String role = jwtUtil.extractRole(jwt);
        if (!requiredRole.equals(role)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        if (!jwtUtil.extractId(jwt).equals(id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden");
        }
    }

    @GetMapping("/mahasiswa/{id}/honors")
    public ResponseEntity<WebResponse<List<HonorResponse>>> getHonors(
            @RequestHeader(name = "Authorization", required = false) String token,
            @PathVariable String id,
            @RequestParam int year,
            @RequestParam int month) {

        authorize(token, "MAHASISWA", id);

        if (month < 1 || month > 12) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid month");
        }

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<Log> logList = logRepository.findByTanggalLogBetweenAndMahasiswaIdOrderByMataKuliahNamaMataKuliahAsc(startDate, endDate, id);

        double honorPerJam = 27500;

        Map<String, List<Log>> groupedLogs = logList.stream()
                .collect(Collectors.groupingBy(log ->
                        log.getMahasiswa().getId() + "_" +
                                log.getMataKuliah().getKodeMataKuliah() + "_" +
                                log.getStatus()
                ));

        List<HonorResponse> honorResponses = new ArrayList<>();

        for (Map.Entry<String, List<Log>> entry : groupedLogs.entrySet()) {
            List<Log> logs = entry.getValue();
            if (logs.isEmpty()) continue;

            Log firstLog = logs.getFirst();

            double totalJam = logs.stream()
                    .mapToDouble(log -> Duration.between(log.getWaktuMulai(), log.getWaktuSelesai()).toMinutes() / 60.0)
                    .sum();

            HonorResponse response = HonorResponse.builder()
                    .tanggalAwal(startDate)
                    .tanggalAkhir(endDate)
                    .mahasiswa(firstLog.getMahasiswa())
                    .mataKuliah(firstLog.getMataKuliah())
                    .totalJam(totalJam)
                    .honorPerJam(honorPerJam)
                    .totalPembayaran(totalJam * honorPerJam)
                    .status(firstLog.getStatus())
                    .build();

            honorResponses.add(response);
        }

        WebResponse<List<HonorResponse>> response = WebResponse.<List<HonorResponse>>builder()
                .data(honorResponses)
                .build();

        return ResponseEntity.ok(response);
    }
}
