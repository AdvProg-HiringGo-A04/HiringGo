package id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.service;

import id.ac.ui.cs.advprog.hiringgo.entity.MataKuliah;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.dto.LowonganForm;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.entity.Lowongan;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.entity.PendaftarLowongan;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.repository.LowonganRepository;
import id.ac.ui.cs.advprog.hiringgo.matakuliah.service.MataKuliahService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LowonganService {

    private final LowonganRepository lowonganRepository;

    @Autowired
    private MataKuliahService mataKuliahService;

    public LowonganService(LowonganRepository lowonganRepository, MataKuliahService mataKuliahService) {
        this.mataKuliahService = mataKuliahService;
        this.lowonganRepository = lowonganRepository;
    }

    public List<Lowongan> getAllLowongan() {
        return lowonganRepository.findAll();
    }

    public Optional<Lowongan> getLowonganById(String id) {
        return lowonganRepository.findById(id);
    }

    @Transactional
    public Lowongan createLowongan(LowonganForm form) {
        MataKuliah mk = mataKuliahService.findByKode(form.getKodeMataKuliah());
        boolean exists = lowonganRepository.existsByMataKuliahAndSemesterAndTahunAjaran(
            mk, form.getSemester(), form.getTahunAjaran());

        if (exists) {
            throw new IllegalArgumentException("Lowongan dengan kombinasi ini sudah ada!");
        }
        Lowongan lowongan = new Lowongan();
        
        lowongan.setId(UUID.randomUUID().toString());
        lowongan.setMataKuliah(mk);
        lowongan.setSemester(form.getSemester());
        lowongan.setTahunAjaran(form.getTahunAjaran());
        lowongan.setJumlahDibutuhkan(form.getJumlahAsistenDibutuhkan());

        return lowonganRepository.save(lowongan);
    }

    @Transactional
    public Lowongan updateLowongan(String id, LowonganForm form) {
        Lowongan lowongan = lowonganRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Lowongan tidak ditemukan"));

        MataKuliah mk = mataKuliahService.findByKode(form.getKodeMataKuliah());
        boolean exists = lowonganRepository.existsByMataKuliahAndSemesterAndTahunAjaran(
            mk, form.getSemester(), form.getTahunAjaran());

        

        if (exists && !(lowongan.getMataKuliah().equals(mk) &&
                        lowongan.getSemester().equals(form.getSemester()) &&
                        lowongan.getTahunAjaran().equals(form.getTahunAjaran()))) {
            throw new IllegalArgumentException("Lowongan dengan kombinasi ini sudah ada!");
        }

        lowongan.setMataKuliah(mk);
        lowongan.setSemester(form.getSemester());
        lowongan.setTahunAjaran(form.getTahunAjaran());
        lowongan.setJumlahDibutuhkan(form.getJumlahAsistenDibutuhkan());

        return lowonganRepository.save(lowongan);
    }

    @Transactional
    public boolean deleteLowongan(String id) {
        if (lowonganRepository.existsById(id)) {
            lowonganRepository.deleteById(id);
            return true; // Successfully deleted
        }
        return false; // Not found, no deletion
    }

    @Transactional
    public List<PendaftarLowongan> getPendaftarByLowongan(String lowonganId) {
        Lowongan lowongan = lowonganRepository.findById(lowonganId)
            .orElseThrow(() -> new IllegalArgumentException("Lowongan tidak ditemukan"));
        return lowongan.getPendaftar();
    }

    @Async
    @Transactional
    public void setStatusPendaftar(String lowonganId, String npm, boolean diterima) {
        // Find the lowongan by id
        Lowongan lowongan = lowonganRepository.findById(lowonganId)
                .orElseThrow(() -> new IllegalArgumentException("Lowongan tidak ditemukan"));

        // Find the pendaftar inside the lowongan with matching Mahasiswa NPM
        PendaftarLowongan pendaftar = lowongan.getPendaftar().stream()
                .filter(p -> p.getMahasiswa().getNPM().equals(npm))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Pendaftar tidak ditemukan"));

        // Update status
        pendaftar.setDiterima(diterima);
        
        // Persist the change (optional if cascade = ALL and dirty checking works)
        lowonganRepository.save(lowongan);
    }
}
