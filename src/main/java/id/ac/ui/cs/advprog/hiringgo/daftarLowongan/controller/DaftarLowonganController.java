package id.ac.ui.cs.advprog.hiringgo.daftarLowongan.controller;

import id.ac.ui.cs.advprog.hiringgo.daftarLowongan.command.DaftarLowonganCommand;
import id.ac.ui.cs.advprog.hiringgo.daftarLowongan.dto.DaftarLowonganRequest;
import id.ac.ui.cs.advprog.hiringgo.daftarLowongan.exception.EntityNotFoundException;
import id.ac.ui.cs.advprog.hiringgo.daftarLowongan.service.DaftarLowonganService;
import id.ac.ui.cs.advprog.hiringgo.entity.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.repository.MahasiswaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/lowongan")
public class DaftarLowonganController {

    @Autowired
    private DaftarLowonganService daftarLowonganService;

    @Autowired
    private MahasiswaRepository mahasiswaRepository;

    @PostMapping("/{lowonganId}/daftar")
    public ResponseEntity<String> daftarLowongan(
            @PathVariable String lowonganId,
            @RequestBody DaftarLowonganRequest request,
            @RequestParam String npm
    ) {

        // Cari mahasiswa berdasarkan NPM
        Optional<Mahasiswa> mahasiswaOpt = mahasiswaRepository.findByNPM(npm);
        if (mahasiswaOpt.isEmpty()) {
            throw new EntityNotFoundException(
                    Map.of("mahasiswaId", "Mahasiswa dengan NPM " + npm + " tidak ditemukan")
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
        return ResponseEntity.ok("Berhasil mendaftar ke lowongan");
    }
}
