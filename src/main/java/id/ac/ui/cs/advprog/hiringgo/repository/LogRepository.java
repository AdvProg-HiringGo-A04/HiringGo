package id.ac.ui.cs.advprog.hiringgo.repository;

import id.ac.ui.cs.advprog.hiringgo.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

public interface LogRepository extends JpaRepository<Log, String> {

    List<Log> findByMahasiswaId(String mahasiswaId);
    List<Log> findByTanggalLogBetweenAndMahasiswaIdOrderByMataKuliahNamaMataKuliahAsc(LocalDate tanggalLog, LocalDate tanggalLog2, String mahasiswa_id);
    List<Log> findByLowonganIdAndMahasiswaIdOrderByTanggalLogDescWaktuMulaiDesc(String lowonganId, String mahasiswaId);
}