package id.ac.ui.cs.advprog.hiringgo.dashboardhonor.controller;

import id.ac.ui.cs.advprog.hiringgo.dashboardhonor.model.HonorResponse;
import id.ac.ui.cs.advprog.hiringgo.dashboardhonor.service.HonorService;
import id.ac.ui.cs.advprog.hiringgo.enums.Role;
import id.ac.ui.cs.advprog.hiringgo.model.WebResponse;
import id.ac.ui.cs.advprog.hiringgo.security.JwtUtil;
import id.ac.ui.cs.advprog.hiringgo.security.annotation.AllowedRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class HonorController {

    @Autowired
    private HonorService honorService;

    @Autowired
    private JwtUtil jwtUtil;

    @AllowedRoles({Role.MAHASISWA})
    @GetMapping(
            path = "/mahasiswa/{id}/honors",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<List<HonorResponse>>> getHonors(
            @RequestHeader(name = "Authorization", required = false) String token,
            @PathVariable String id,
            @RequestParam int year,
            @RequestParam int month) {

        if (month < 1 || month > 12) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid month: " + month);
        }

        if (!jwtUtil.extractId(token.substring(7)).equals(id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden");
        }

        List<HonorResponse> honorResponses = honorService.getHonorsByMahasiswaAndPeriod(id, year, month);

        WebResponse<List<HonorResponse>> response = WebResponse.<List<HonorResponse>>builder()
                .data(honorResponses)
                .build();

        return ResponseEntity.ok(response);
    }
}
