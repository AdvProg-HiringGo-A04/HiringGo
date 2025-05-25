package id.ac.ui.cs.advprog.hiringgo.manajemenLog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import id.ac.ui.cs.advprog.hiringgo.entity.Log;

@Repository
public interface LogRepository extends JpaRepository<Log, String> {
    List<Log> findByLowonganIdAndMahasiswaIdOrderByTanggalLogDescWaktuMulaiDesc(String lowonganId, String mahasiswaId);
}