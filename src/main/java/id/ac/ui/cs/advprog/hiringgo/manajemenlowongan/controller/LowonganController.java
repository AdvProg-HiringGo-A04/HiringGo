package id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.controller;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.dto.PendaftaranLowonganForm;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.entity.Lowongan;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.service.PendaftaranLowonganService;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.repository.LowonganRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/lowongan")
public class LowonganController {

    private final LowonganRepository lowonganRepository;
    private final PendaftaranLowonganService pendaftaranService;

    @GetMapping
    public String listLowongan(Model model) {
        model.addAttribute("lowonganList", lowonganRepository.findAll());
        return "lowongan/list"; // Create this HTML later
    }

    @GetMapping("/{id}")
    public String detailLowongan(@PathVariable UUID id, Model model) {
        Lowongan lowongan = lowonganRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Lowongan tidak ditemukan"));
        model.addAttribute("lowongan", lowongan);
        model.addAttribute("form", new PendaftaranLowonganForm());
        return "lowongan/detail"; // Create this HTML later
    }

    @PostMapping("/{id}/daftar")
    public String daftarLowongan(@PathVariable UUID id,
                                 @ModelAttribute("form") PendaftaranLowonganForm form,
                                 @AuthenticationPrincipal Mahasiswa mahasiswa,
                                 Model model) {
        try {
            form.setLowonganId(id);
            pendaftaranService.daftar(mahasiswa, form);
            return "redirect:/lowongan/" + id + "?success";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("lowongan", lowonganRepository.findById(id).orElse(null));
            return "lowongan/detail";
        }
    }
}
