package id.ac.ui.cs.advprog.hiringgo.repository;

import id.ac.ui.cs.advprog.hiringgo.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LogRepository extends JpaRepository<Log, String> {

    @Query(value = "SELECT l.* FROM log l " +
            "JOIN lowongan low ON low.id = l.lowongan_id " +
            "JOIN mata_kuliah mk ON mk.kode_mata_kuliah = low.kode_mata_kuliah " +
            "JOIN mengampu_mata_kuliah mmp ON mmp.kode_mata_kuliah = mk.kode_mata_kuliah " +
            "WHERE mmp.id = :dosenId " +
            "ORDER BY l.tanggal_log DESC, l.waktu_mulai DESC",
            nativeQuery = true)
    List<Log> findAllLogsByDosenId(@Param("dosenId") String dosenId);

    @Query(value = "SELECT CASE WHEN COUNT(l.id) > 0 THEN true ELSE false END FROM log l " +
            "JOIN lowongan low ON low.id = l.lowongan_id " +
            "JOIN mata_kuliah mk ON mk.kode_mata_kuliah = low.kode_mata_kuliah " +
            "JOIN mengampu_mata_kuliah mmp ON mmp.kode_mata_kuliah = mk.kode_mata_kuliah " +
            "WHERE mmp.id = :dosenId AND l.id = :logId",
            nativeQuery = true)
    boolean isLogOwnedByDosen(@Param("logId") String logId, @Param("dosenId") String dosenId);

    List<Log> findByMahasiswaId(String mahasiswaId);

    List<Log> findByTanggalLogBetweenAndMahasiswaIdOrderByLowonganMataKuliahNamaMataKuliahAsc(LocalDate tanggalLog, LocalDate tanggalLog2, String mahasiswa_id);
    List<Log> findByLowonganIdAndMahasiswaIdOrderByTanggalLogDescWaktuMulaiDesc(String lowonganId, String mahasiswaId);
}