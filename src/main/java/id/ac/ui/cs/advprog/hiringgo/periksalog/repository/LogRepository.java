package id.ac.ui.cs.advprog.hiringgo.periksalog.repository;

import id.ac.ui.cs.advprog.hiringgo.common.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {

    @Query("SELECT l FROM Log l JOIN l.lowongan low JOIN low.mataKuliah mk JOIN mk.dosenPengampu d " +
            "WHERE d.id = :dosenId ORDER BY l.tanggalLog DESC, l.waktuMulai DESC")
    List<Log> findAllLogsByDosenId(@Param("dosenId") Long dosenId);

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM Log l JOIN l.lowongan low " +
            "JOIN low.mataKuliah mk JOIN mk.dosenPengampu d WHERE l.id = :logId AND d.id = :dosenId")
    boolean isLogOwnedByDosen(@Param("logId") Long logId, @Param("dosenId") Long dosenId);
}