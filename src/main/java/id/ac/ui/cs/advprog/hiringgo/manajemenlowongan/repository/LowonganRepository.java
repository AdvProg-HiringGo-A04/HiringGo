package id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.repository;

import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.entity.Lowongan;
import id.ac.ui.cs.advprog.hiringgo.entity.MataKuliah;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.entity.PendaftarLowongan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LowonganRepository extends JpaRepository<Lowongan, String> {

    boolean existsByMataKuliahAndSemesterAndTahunAjaran(MataKuliah mataKuliah, String semester, String tahunAjaran);

    @Query(value = "SELECT * FROM mahasiswa_pendaftar_lowongan WHERE lowongan_id = :id", nativeQuery = true)
    List<PendaftarLowongan> findPendaftarByLowonganId(String lowonganId);

    @Query(value = "SELECT * FROM mahasiswa_pendaftar_lowongan WHERE lowongan_id = :id", nativeQuery = true)
    Optional<PendaftarLowongan> findPendaftarById(String pendaftarId);
}
