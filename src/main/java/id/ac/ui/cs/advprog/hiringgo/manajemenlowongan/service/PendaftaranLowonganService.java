package id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.service;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.dto.PendaftaranLowonganForm;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.entity.Lowongan;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.entity.PendaftarLowongan;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.repository.LowonganRepository;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.repository.PendaftarLowonganRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PendaftaranLowonganService {

    private final LowonganRepository lowonganRepo;
    private final PendaftarLowonganRepository pendaftarRepo;

    public PendaftaranLowonganService(LowonganRepository lowonganRepo,
                                       PendaftarLowonganRepository pendaftarRepo) {
        this.lowonganRepo = lowonganRepo;
        this.pendaftarRepo = pendaftarRepo;
    }

    @Transactional
    public void daftar(Mahasiswa mahasiswa, PendaftaranLowonganForm form) {
        if (form.getIpk() < 0 || form.getIpk() > 4) {
            throw new IllegalArgumentException("IPK harus antara 0 dan 4");
        }

        if (form.getJumlahSks() < 0 || form.getJumlahSks() > 24) {
            throw new IllegalArgumentException("Jumlah SKS tidak valid");
        }

        UUID lowonganId = form.getLowonganId();
        Lowongan lowongan = lowonganRepo.findById(lowonganId)
            .orElseThrow(() -> new IllegalArgumentException("Lowongan tidak ditemukan"));

        boolean alreadyRegistered = pendaftarRepo.existsByMahasiswaAndLowongan(mahasiswa, lowongan);
        if (alreadyRegistered) {
            throw new IllegalStateException("Mahasiswa sudah mendaftar lowongan ini");
        }

        PendaftarLowongan pendaftar = PendaftarLowongan.builder()
            .lowongan(lowongan)
            .mahasiswa(mahasiswa)
            .jumlahSks(form.getJumlahSks())
            .ipk(form.getIpk())
            .diterima(false)
            .build();

        pendaftarRepo.save(pendaftar);
    }
}
