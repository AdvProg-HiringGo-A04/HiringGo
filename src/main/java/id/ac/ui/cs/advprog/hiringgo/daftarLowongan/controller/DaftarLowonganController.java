package id.ac.ui.cs.advprog.hiringgo.daftarLowongan.controller;

import id.ac.ui.cs.advprog.hiringgo.common.ApiResponse;
import id.ac.ui.cs.advprog.hiringgo.daftarLowongan.command.DaftarLowonganCommand;
import id.ac.ui.cs.advprog.hiringgo.daftarLowongan.dto.DaftarLowonganRequest;
import id.ac.ui.cs.advprog.hiringgo.daftarLowongan.exception.EntityNotFoundException;
import id.ac.ui.cs.advprog.hiringgo.daftarLowongan.service.DaftarLowonganService;
import id.ac.ui.cs.advprog.hiringgo.entity.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.enums.Role;
import id.ac.ui.cs.advprog.hiringgo.repository.MahasiswaRepository;

import id.ac.ui.cs.advprog.hiringgo.security.CurrentUserProvider;
import id.ac.ui.cs.advprog.hiringgo.security.annotation.AllowedRoles;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@CrossOrigin(origins = "http://localhost:8000", allowedHeaders = "*")
@RestController
@RequestMapping("/api/lowongan")
@RequiredArgsConstructor
public class DaftarLowonganController {
    private final DaftarLowonganService daftarLowonganService;
    private final MahasiswaRepository mahasiswaRepository;
    private final CurrentUserProvider currentUserProvider;

    @AllowedRoles({Role.MAHASISWA})
    @PostMapping("/{lowonganId}/daftar")
    public ResponseEntity<ApiResponse<String>> daftarLowongan(
            @PathVariable String lowonganId,
            @Valid @RequestBody DaftarLowonganRequest request
    ) {

        String mahasiswaId = currentUserProvider.getCurrentUserId();

        // Cari mahasiswa berdasarkan NPM
        Optional<Mahasiswa> mahasiswaOpt = mahasiswaRepository.findById(mahasiswaId);
        if (mahasiswaOpt.isEmpty()) {
            throw new EntityNotFoundException(
                    Map.of("mahasiswaId", "Mahasiswa dengan id " + mahasiswaId + " tidak ditemukan")
            );
        }

        Mahasiswa mahasiswa = mahasiswaOpt.get();

        DaftarLowonganCommand command = new DaftarLowonganCommand(
                request.getSks(),
                request.getIpk(),
                lowonganId,
                mahasiswa.getId()
        );

        daftarLowonganService.execute(command);

        log.info("User with email '{}' and role '{}' registered for lowongan '{}'",
                currentUserProvider.getCurrentUserEmail(),
                currentUserProvider.getCurrentUserRole(),
                lowonganId);

        return new ResponseEntity<ApiResponse<String>>(
                new ApiResponse<>("Berhasil mendaftar ke lowongan", null),
                HttpStatus.CREATED
        );

    }
}
