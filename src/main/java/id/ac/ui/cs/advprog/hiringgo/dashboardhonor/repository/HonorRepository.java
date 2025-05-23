package id.ac.ui.cs.advprog.hiringgo.dashboardhonor.repository;

import id.ac.ui.cs.advprog.hiringgo.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HonorRepository extends JpaRepository<Log, String> {
    @Query("SELECT l FROM Log l " +
            "WHERE l.mahasiswa.id = :mahasiswaId " +
            "  AND l.tanggalLog BETWEEN :startDate AND :endDate " +
            "ORDER BY l.tanggalLog ASC")
    List<Log> findLogsByMahasiswaAndPeriod(
            @Param("mahasiswaId") String mahasiswaId,
            @Param("startDate") LocalDate    startDate,
            @Param("endDate")   LocalDate    endDate
    );
}
