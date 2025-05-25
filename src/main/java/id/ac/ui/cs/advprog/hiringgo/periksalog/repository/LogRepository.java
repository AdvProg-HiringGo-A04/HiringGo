package id.ac.ui.cs.advprog.hiringgo.periksalog.repository;

import id.ac.ui.cs.advprog.hiringgo.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log, String> {

    @Query("SELECT l FROM Log l " +
            "WHERE EXISTS (SELECT d FROM l.lowongan.mataKuliah.dosenPengampu d WHERE d.id = :dosenId) " +
            "ORDER BY l.tanggalLog DESC, l.waktuMulai DESC")
    List<Log> findAllLogsByDosenId(@Param("dosenId") String dosenId);

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM Log l " +
            "WHERE EXISTS (SELECT d FROM l.lowongan.mataKuliah.dosenPengampu d WHERE d.id = :dosenId) " +
            "AND l.id = :logId")
    boolean isLogOwnedByDosen(@Param("logId") String logId, @Param("dosenId") String dosenId);
}