package id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.controller;

import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.dto.LowonganForm;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.entity.Lowongan;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.entity.PendaftarLowongan;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.service.LowonganService;
import id.ac.ui.cs.advprog.hiringgo.model.WebResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/lowongan")
public class LowonganController {

    @Autowired
    private LowonganService lowonganService;

    @Autowired
    private Validator validator;

    @GetMapping
    public ResponseEntity<WebResponse<List<Lowongan>>> getAllLowongan() {
        List<Lowongan> lowonganList = lowonganService.getAllLowongan();
        WebResponse<List<Lowongan>> response = WebResponse.<List<Lowongan>>builder()
                .data(lowonganList)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<WebResponse<Void>> createLowongan(@RequestBody(required = false) LowonganForm form) {
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
                .created(URI.create("/api/lowongan/" + created.getId()))
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WebResponse<Lowongan>> updateLowongan(@PathVariable UUID id, @RequestBody(required = false) LowonganForm form) {
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

    @DeleteMapping("/{id}")
    public ResponseEntity<WebResponse<Void>> deleteLowongan(@PathVariable UUID id) {
        boolean deleted = lowonganService.deleteLowongan(id);
        if (!deleted) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lowongan not found");
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/pendaftar")
    public ResponseEntity<WebResponse<List<PendaftarLowongan>>> getPendaftar(@PathVariable UUID id) {
        List<PendaftarLowongan> pendaftarList = lowonganService.getPendaftarByLowongan(id);
        WebResponse<List<PendaftarLowongan>> response = WebResponse.<List<PendaftarLowongan>>builder()
                .data(pendaftarList)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/pendaftar/{npm}/status")
    public ResponseEntity<WebResponse<Void>> setStatusPendaftar(@PathVariable UUID id, @RequestParam boolean diterima) {
        try {
            lowonganService.setStatusPendaftar(id, diterima);
            WebResponse<Void> response = WebResponse.<Void>builder().build();
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pendaftar not found");
        }
    }
}
