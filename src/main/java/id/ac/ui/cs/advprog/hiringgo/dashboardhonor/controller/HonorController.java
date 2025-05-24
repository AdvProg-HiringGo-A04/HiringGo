package id.ac.ui.cs.advprog.hiringgo.dashboardhonor.controller;

import id.ac.ui.cs.advprog.hiringgo.dashboardhonor.model.HonorResponse;
import id.ac.ui.cs.advprog.hiringgo.dashboardhonor.service.HonorService;
import id.ac.ui.cs.advprog.hiringgo.model.WebResponse;
import id.ac.ui.cs.advprog.hiringgo.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class HonorController {

    @Autowired
    private HonorService honorService;

    @Autowired
    private JwtUtil jwtUtil;

    private void authorize(String token, String requiredRole, String id) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token required");
        }

        try {
            String jwt = token.substring(7);
            String role = jwtUtil.extractRole(jwt);
            String tokenId = jwtUtil.extractId(jwt);

            if (!requiredRole.equals(role)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "Role mismatch. Required: " + requiredRole + ", Got: " + role);
            }

            if (!tokenId.equals(id)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "User ID mismatch. Token ID: " + tokenId + ", Requested: " + id);
            }
        } catch (Exception e) {
            if (e instanceof ResponseStatusException) {
                throw e;
            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token: " + e.getMessage());
        }
    }

    @GetMapping("/mahasiswa/{id}/honors")
    public ResponseEntity<WebResponse<List<HonorResponse>>> getHonors(
            @RequestHeader(name = "Authorization", required = false) String token,
            @PathVariable String id,
            @RequestParam int year,
            @RequestParam int month) {

        try {
            if (month < 1 || month > 12) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid month: " + month);
            }

            authorize(token, "MAHASISWA", id);

            List<HonorResponse> honorResponses = honorService.getHonorsByMahasiswaAndPeriod(id, year, month);

            WebResponse<List<HonorResponse>> response = WebResponse.<List<HonorResponse>>builder()
                    .data(honorResponses)
                    .build();

            return ResponseEntity.ok(response);

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unexpected error: " + e.getMessage());
        }
    }
}