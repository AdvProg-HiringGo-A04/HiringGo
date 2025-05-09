package id.ac.ui.cs.advprog.hiringgo.manajemenLog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import id.ac.ui.cs.advprog.hiringgo.manajemenLog.model.Log;

@Repository
public interface LogRepository extends JpaRepository<Log, String> {
    List<Log> findByMataKuliahIdAndMahasiswaIdOrderByTanggalLogDescWaktuMulaiDesc(String mataKuliahId, String mahasiswaId);
    boolean existsByMataKuliahIdAndMahasiswaId(String courseId, String studentId);
}
