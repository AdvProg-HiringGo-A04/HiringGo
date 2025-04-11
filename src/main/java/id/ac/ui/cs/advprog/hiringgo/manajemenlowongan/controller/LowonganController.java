package id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.controller;

import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.dto.LowonganForm;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.entity.Lowongan;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.entity.PendaftarLowongan;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.service.LowonganService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/lowongan")
public class LowonganController {

    private final LowonganService lowonganService;

    public LowonganController(LowonganService lowonganService) {
        this.lowonganService = lowonganService;
    }

    @GetMapping
    public List<Lowongan> getAllLowongan() {
        return lowonganService.getAllLowongan();
    }

    @PostMapping("/create")
    public String createLowongan(@ModelAttribute LowonganForm form) {
        lowonganService.createLowongan(form);
        return "redirect:/lowongan";
    }

    @PutMapping("/{id}")
    public ResponseEntity<Lowongan> updateLowongan(@PathVariable UUID id, @RequestBody LowonganForm form) {
        return ResponseEntity.ok(lowonganService.updateLowongan(id, form));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLowongan(@PathVariable UUID id) {
        lowonganService.deleteLowongan(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/pendaftar")
    public List<PendaftarLowongan> getPendaftar(@PathVariable UUID id) {
        return lowonganService.getPendaftarByLowongan(id);
    }

    @PostMapping("/pendaftar/{id}/status")
    public ResponseEntity<Void> setStatusPendaftar(@PathVariable UUID id, @RequestParam boolean diterima) {
        lowonganService.setStatusPendaftar(id, diterima);
        return ResponseEntity.ok().build();
    }
}
