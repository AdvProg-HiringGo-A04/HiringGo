package id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.controller;

import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.dto.LowonganForm;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.entity.Lowongan;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.entity.PendaftarLowongan;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.service.LowonganService;
import id.ac.ui.cs.advprog.hiringgo.model.WebResponse;
import id.ac.ui.cs.advprog.hiringgo.security.JwtUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.Optional;

@RestController
public class LowonganController {

    @Autowired
    private LowonganService lowonganService;

    @Autowired
    private Validator validator;

    @Autowired
    private JwtUtil jwtUtil;

    private void roleRequired(String token) {
        if (token == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        token = token.substring(7);

        if (!jwtUtil.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        String role = jwtUtil.extractRole(token);

        Set<String> allowedRoles = Set.of("ADMIN", "DOSEN");
        if (!allowedRoles.contains(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden");
        }
    }

    @GetMapping(
            path = "/lowongan",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<List<Lowongan>>> getAllLowongan(
            @RequestHeader(name = "Authorization", required = false) String token
    ) {
        roleRequired(token);
        List<Lowongan> lowonganList = lowonganService.getAllLowongan();
        WebResponse<List<Lowongan>> response = WebResponse.<List<Lowongan>>builder()
                .data(lowonganList)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping(
            path = "/lowongan/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<Lowongan>> getLowonganById(
            @PathVariable("id") String id,
            @RequestHeader(name = "Authorization", required = false) String token
    ) {
        roleRequired(token);
        Optional<Lowongan> lowongan = lowonganService.getLowonganById(id);

        if (lowongan.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(WebResponse.<Lowongan>builder()
                            .errors("Lowongan not found")
                            .build());
        }

        return ResponseEntity.ok(
                WebResponse.<Lowongan>builder()
                        .data(lowongan.get())
                        .build()
        );
    }

    @PostMapping(
            path = "/lowongan",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<Void>> createLowongan(
            @RequestBody(required = false) LowonganForm form,
            @RequestHeader(name = "Authorization", required = false) String token
    ) {
        roleRequired(token);

        if (form == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body is missing or invalid");
        }

        Set<ConstraintViolation<LowonganForm>> violations = validator.validate(form);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        Lowongan created = lowonganService.createLowongan(form);

        WebResponse<Void> response = WebResponse.<Void>builder().build();

        return ResponseEntity
                .created(URI.create("/lowongan/" + created.getId()))
                .body(response);
    }

    @PutMapping(
            path = "/lowongan/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<Lowongan>> updateLowongan(
            @PathVariable("id") String id,
            @RequestBody(required = false) LowonganForm form,
            @RequestHeader(name = "Authorization", required = false) String token
    ) {
        roleRequired(token);

        if (form == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body is missing or invalid");
        }

        Set<ConstraintViolation<LowonganForm>> violations = validator.validate(form);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        Lowongan updated = lowonganService.updateLowongan(id, form);
        if (updated == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lowongan not found");
        }

        WebResponse<Lowongan> response = WebResponse.<Lowongan>builder()
                .data(updated)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(
            path = "/lowongan/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<Void>> deleteLowongan(
            @PathVariable("id") String id,
            @RequestHeader(name = "Authorization", required = false) String token
    ) {
        roleRequired(token);

        boolean deleted = lowonganService.deleteLowongan(id);
        if (!deleted) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lowongan not found");
        }

        WebResponse<Void> response = WebResponse.<Void>builder().build();
        return ResponseEntity.ok(response); // or .noContent().build() if you prefer 204
    }

    @GetMapping(
            path = "/lowongan/{id}/pendaftar",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<List<PendaftarLowongan>>> getPendaftar(
            @PathVariable("id") String lowonganId,
            @RequestHeader(name = "Authorization", required = false) String token
    ) {
        roleRequired(token);

        List<PendaftarLowongan> pendaftarList = lowonganService.getPendaftarByLowongan(lowonganId);
        WebResponse<List<PendaftarLowongan>> response = WebResponse.<List<PendaftarLowongan>>builder()
                .data(pendaftarList)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping(
            path = "/lowongan/{id}/pendaftar/{npm}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<Void>> setStatusPendaftar(
            @PathVariable("id") String lowonganId,
            @PathVariable("npm") String npm,
            @RequestParam("diterima") boolean diterima,
            @RequestHeader(name = "Authorization", required = false) String token
    ) {
        roleRequired(token);

        try {
            lowonganService.setStatusPendaftar(lowonganId, npm, diterima);
            WebResponse<Void> response = WebResponse.<Void>builder().build();
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pendaftar not found");
        }
    }
}
