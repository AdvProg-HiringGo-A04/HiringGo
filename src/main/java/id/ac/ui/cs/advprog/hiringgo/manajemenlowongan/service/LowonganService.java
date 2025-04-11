package id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.service;

import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.dto.LowonganForm;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.entity.Lowongan;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.entity.PendaftarLowongan;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.repository.LowonganRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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
    public void deleteLowongan(UUID id) {
        lowonganRepository.deleteById(id);
    }

    public List<PendaftarLowongan> getPendaftarByLowongan(UUID lowonganId) {
        Lowongan lowongan = lowonganRepository.findById(lowonganId)
            .orElseThrow(() -> new IllegalArgumentException("Lowongan tidak ditemukan"));
        return lowongan.getPendaftar(); // assumes you have a `getPendaftar()` method
    }

    @Transactional
    public void setStatusPendaftar(UUID pendaftarId, boolean diterima) {
        Optional<Lowongan> lowonganOpt = lowonganRepository.findAll().stream()
            .filter(l -> l.getPendaftar().stream().anyMatch(p -> p.getId().equals(pendaftarId)))
            .findFirst();

        if (lowonganOpt.isEmpty()) {
            throw new IllegalArgumentException("Pendaftar tidak ditemukan");
        }

        PendaftarLowongan pendaftar = lowonganOpt.get().getPendaftar().stream()
            .filter(p -> p.getId().equals(pendaftarId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Pendaftar tidak ditemukan"));

        pendaftar.setDiterima(diterima);
        lowonganRepository.save(lowonganOpt.get());
    }
}
