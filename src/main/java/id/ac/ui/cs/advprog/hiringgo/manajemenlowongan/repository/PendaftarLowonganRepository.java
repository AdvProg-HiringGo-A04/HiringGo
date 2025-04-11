package id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.repository;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.entity.Lowongan;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.entity.PendaftarLowongan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

public interface PendaftarLowonganRepository extends JpaRepository<PendaftarLowongan, UUID> {
    List<PendaftarLowongan> findByMahasiswa(Mahasiswa mahasiswa);
    boolean existsByMahasiswaAndLowongan(Mahasiswa m, Lowongan l);
}
