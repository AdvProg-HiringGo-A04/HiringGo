package id.ac.ui.cs.advprog.hiringgo.daftarLowongan.service;

import id.ac.ui.cs.advprog.hiringgo.daftarLowongan.command.DaftarLowonganCommand;
import id.ac.ui.cs.advprog.hiringgo.daftarLowongan.exception.InvalidDataException;
import id.ac.ui.cs.advprog.hiringgo.daftarLowongan.exception.EntityNotFoundException;
import id.ac.ui.cs.advprog.hiringgo.entity.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.entity.Lowongan;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.entity.PendaftarLowongan;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.repository.LowonganRepository;
import id.ac.ui.cs.advprog.hiringgo.repository.MahasiswaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DaftarLowonganService {
    private final LowonganRepository lowonganRepository;
    private final MahasiswaRepository mahasiswaRepository;

    @Transactional
    public void execute(DaftarLowonganCommand command) {
        if (command.getIpk() < 0 || command.getIpk() > 4) {
            Map<String, String> errors = new HashMap<>();
            errors.put("ipk", "IPK harus antara 0 dan 4");
            throw new InvalidDataException(errors);
        }

        if (command.getSks() < 0) {
            Map<String, String> errors = new HashMap<>();
            errors.put("sks", "SKS tidak boleh negatif");
            throw new InvalidDataException(errors);
        }

        // Ambil lowongan
        String lowonganId = command.getLowonganId();
        Lowongan lowongan = lowonganRepository.findById(lowonganId)
                .orElseThrow(() -> new EntityNotFoundException(Map.of("lowonganId", "Lowongan tidak ditemukan")));

        // Ambil mahasiswa
        String mahasiswaId = command.getMahasiswaId();
        Mahasiswa mahasiswa = mahasiswaRepository.findById(mahasiswaId)
                .orElseThrow(() -> new EntityNotFoundException(Map.of("mahasiswaId", "Mahasiswa tidak ditemukan")));


        // Cek mahasiswa sudah daftar
        boolean sudahDaftar = lowongan.getPendaftar().stream()
                .anyMatch(p -> p.getMahasiswa().getId().equals(mahasiswa.getId()));

        if (sudahDaftar) {
            throw new IllegalStateException("Mahasiswa sudah mendaftar ke lowongan ini");
        }

        // Buat pendaftar baru
        PendaftarLowongan pendaftar = PendaftarLowongan.builder()
                .id(UUID.randomUUID().toString())
                .lowongan(lowongan)
                .mahasiswa(mahasiswa)
                .jumlahSks(command.getSks())
                .ipk(command.getIpk())
                .diterima(false) // default
                .build();

        lowongan.getPendaftar().add(pendaftar);
        lowongan.setJumlahPendaftar(lowongan.getJumlahPendaftar() + 1);
        lowonganRepository.save(lowongan);
    }
}
