package id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.service;

import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.dto.LowonganForm;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.entity.Lowongan;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.entity.PendaftarLowongan;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.repository.LowonganRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LowonganService {

    private final LowonganRepository lowonganRepository;

    public LowonganService(LowonganRepository lowonganRepository) {
        this.lowonganRepository = lowonganRepository;
    }

    public List<Lowongan> getAllLowongan() {
        return lowonganRepository.findAll();
    }

    public Optional<Lowongan> getLowonganById(UUID id) {
        return lowonganRepository.findById(id);
    }

    @Transactional
    public Lowongan createLowongan(LowonganForm form) {
        boolean exists = lowonganRepository.existsByMataKuliahAndSemesterAndTahunAjaran(
            form.getMataKuliah(), form.getSemester(), form.getTahunAjaran());

        if (exists) {
            throw new IllegalArgumentException("Lowongan dengan kombinasi ini sudah ada!");
        }

        Lowongan lowongan = new Lowongan();
        lowongan.setMataKuliah(form.getMataKuliah());
        lowongan.setSemester(form.getSemester());
        lowongan.setTahunAjaran(form.getTahunAjaran());
        lowongan.setJumlahDibutuhkan(form.getJumlahAsistenDibutuhkan());

        return lowonganRepository.save(lowongan);
    }

    @Transactional
    public Lowongan updateLowongan(UUID id, LowonganForm form) {
        Lowongan lowongan = lowonganRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Lowongan tidak ditemukan"));

        boolean exists = lowonganRepository.existsByMataKuliahAndSemesterAndTahunAjaran(
            form.getMataKuliah(), form.getSemester(), form.getTahunAjaran());

        if (exists && !(lowongan.getMataKuliah().equals(form.getMataKuliah()) &&
                        lowongan.getSemester().equals(form.getSemester()) &&
                        lowongan.getTahunAjaran().equals(form.getTahunAjaran()))) {
            throw new IllegalArgumentException("Lowongan dengan kombinasi ini sudah ada!");
        }

        lowongan.setMataKuliah(form.getMataKuliah());
        lowongan.setSemester(form.getSemester());
        lowongan.setTahunAjaran(form.getTahunAjaran());
        lowongan.setJumlahDibutuhkan(form.getJumlahAsistenDibutuhkan());

        return lowonganRepository.save(lowongan);
    }

    @Transactional
    public boolean deleteLowongan(UUID id) {
        if (lowonganRepository.existsById(id)) {
            lowonganRepository.deleteById(id);
            return true; // Successfully deleted
        }
        return false; // Not found, no deletion
    }

    @Transactional
    public List<PendaftarLowongan> getPendaftarByLowongan(UUID lowonganId) {
        Lowongan lowongan = lowonganRepository.findById(lowonganId)
            .orElseThrow(() -> new IllegalArgumentException("Lowongan tidak ditemukan"));
        return lowongan.getPendaftar();
    }

    @Async
    @Transactional
    public void setStatusPendaftar(UUID lowonganId, String npm, boolean diterima) {
        // Find the lowongan by id
        Lowongan lowongan = lowonganRepository.findById(lowonganId)
                .orElseThrow(() -> new IllegalArgumentException("Lowongan tidak ditemukan"));

        // Find the pendaftar inside the lowongan with matching Mahasiswa NPM
        PendaftarLowongan pendaftar = lowongan.getPendaftar().stream()
                .filter(p -> p.getMahasiswa().getNPM().equals(npm))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Pendaftar tidak ditemukan"));

        // Cek apakah status berubah
        boolean statusLama = pendaftar.isDiterima();
        if (statusLama != diterima) {
            if (diterima) {
                lowongan.setJumlahDiterima(lowongan.getJumlahDiterima() + 1);
            } else {
                lowongan.setJumlahDiterima(lowongan.getJumlahDiterima() - 1);
            }
        }

        // Update status
        pendaftar.setDiterima(diterima);

        // Persist the change (optional if cascade = ALL and dirty checking works)
        lowonganRepository.save(lowongan);
    }
}
