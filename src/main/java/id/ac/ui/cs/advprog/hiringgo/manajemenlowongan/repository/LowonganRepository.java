package id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.repository;

import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.entity.Lowongan;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.entity.PendaftarLowongan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LowonganRepository extends JpaRepository<Lowongan, UUID> {

    boolean existsByMataKuliahAndSemesterAndTahunAjaran(String mataKuliah, String semester, String tahunAjaran);

    @Query("SELECT p FROM PendaftarLowongan p WHERE p.lowongan.id = :lowonganId")
    List<PendaftarLowongan> findPendaftarByLowonganId(UUID lowonganId);

    @Query("SELECT p FROM PendaftarLowongan p WHERE p.id = :pendaftarId")
    Optional<PendaftarLowongan> findPendaftarById(UUID pendaftarId);
}
