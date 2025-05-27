package id.ac.ui.cs.advprog.hiringgo.daftarLowongan.controller;

import id.ac.ui.cs.advprog.hiringgo.common.ApiResponse;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Slf4j
@CrossOrigin(origins = "http://localhost:8000", allowedHeaders = "*")
@RestController
@RequestMapping("/api/lowongan")
@RequiredArgsConstructor
public class DaftarLowonganController {
    private final DaftarLowonganService daftarLowonganService;
    private final MahasiswaRepository mahasiswaRepository;
    private final CurrentUserProvider currentUserProvider;

    @PostMapping("/{lowonganId}/daftar")
    @AllowedRoles({Role.MAHASISWA})
    public ResponseEntity<ApiResponse<Object>> daftarLowongan(
            @PathVariable String lowonganId,
            @Valid @RequestBody DaftarLowonganRequest request
    ) {
        String mahasiswaId = currentUserProvider.getCurrentUserId();
        String userEmail = currentUserProvider.getCurrentUserEmail();
        String userRole = currentUserProvider.getCurrentUserRole();

        log.info("Starting registration process for mahasiswa: {} to lowongan: {}", mahasiswaId, lowonganId);

        Optional<Mahasiswa> mahasiswaOpt = mahasiswaRepository.findById(mahasiswaId);
        if (mahasiswaOpt.isEmpty()) {
            throw new EntityNotFoundException(
                    Map.of("mahasiswaId", "Mahasiswa dengan id " + mahasiswaId + " tidak ditemukan")
            );
        }

        Mahasiswa mahasiswa = mahasiswaOpt.get();

        daftarLowonganService.registerToLowongan(
                request.getSks(),
                request.getIpk(),
                lowonganId,
                mahasiswa.getId()
        );

        log.info("User with email '{}' and role '{}' successfully registered for lowongan '{}'",
                userEmail, userRole, lowonganId);

        return new ResponseEntity<>(
                new ApiResponse<>("Berhasil mendaftar ke lowongan", null),
                HttpStatus.CREATED
        );
    }

}
