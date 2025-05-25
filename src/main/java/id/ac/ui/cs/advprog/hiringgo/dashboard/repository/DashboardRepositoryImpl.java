package id.ac.ui.cs.advprog.hiringgo.dashboard.repository;

import id.ac.ui.cs.advprog.hiringgo.entity.Lowongan;
import id.ac.ui.cs.advprog.hiringgo.enums.Role;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.model.enums.StatusLog;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardRepositoryImpl implements DashboardRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private static final double HOURLY_RATE = 27500.0;
    private static final double MINUTES_PER_HOUR = 60.0;

    private static final class Queries {
        static final String COUNT_USERS_BY_ROLE =
                "SELECT COUNT(u) FROM User u WHERE u.role = :role";
        static final String COUNT_MATA_KULIAH =
                "SELECT COUNT(mk) FROM MataKuliah mk";
        static final String COUNT_LOWONGAN =
                "SELECT COUNT(l) FROM Lowongan l";
        static final String COUNT_MATA_KULIAH_BY_DOSEN =
                "SELECT COUNT(mk) FROM MataKuliah mk JOIN mk.dosenPengampu d WHERE d.id = :dosenId";
        static final String COUNT_MAHASISWA_BY_DOSEN =
                "SELECT COUNT(DISTINCT pl.mahasiswa) FROM PendaftarLowongan pl " +
                        "WHERE pl.lowongan.id IN (SELECT l.id FROM Lowongan l " +
                        "WHERE l.mataKuliah IN (SELECT mk.id FROM MataKuliah mk " +
                        "JOIN mk.dosenPengampu d WHERE d.id = :dosenId)) AND pl.diterima = true";
        static final String COUNT_OPEN_LOWONGAN_BY_DOSEN =
                "SELECT COUNT(l) FROM Lowongan l WHERE l.jumlahDiterima < l.jumlahDibutuhkan " +
                        "AND l.mataKuliah IN (SELECT mk.id FROM MataKuliah mk " +
                        "JOIN mk.dosenPengampu d WHERE d.id = :dosenId)";
        static final String COUNT_OPEN_LOWONGAN =
                "SELECT COUNT(l) FROM Lowongan l WHERE l.jumlahDiterima < l.jumlahDibutuhkan";
        static final String COUNT_ACCEPTED_LOWONGAN =
                "SELECT COUNT(pl) FROM PendaftarLowongan pl " +
                        "WHERE pl.mahasiswa.id = :mahasiswaId AND pl.diterima = true";
        static final String COUNT_REJECTED_LOWONGAN =
                "SELECT COUNT(pl) FROM PendaftarLowongan pl " +
                        "WHERE pl.mahasiswa.id = :mahasiswaId AND pl.diterima = false AND pl.diterima IS NOT NULL";
        static final String COUNT_PENDING_LOWONGAN =
                "SELECT COUNT(pl) FROM PendaftarLowongan pl " +
                        "WHERE pl.mahasiswa.id = :mahasiswaId AND pl.diterima IS NULL";
        static final String FIND_LOG_HOURS =
                "SELECT log.waktuMulai, log.waktuSelesai FROM Log log " +
                        "WHERE log.mahasiswaId = :mahasiswaId AND log.status = :status";
        static final String FIND_ACCEPTED_LOWONGAN =
                "SELECT pl.lowongan FROM PendaftarLowongan pl " +
                        "WHERE pl.mahasiswa.id = :mahasiswaId AND pl.diterima = true";
    }

    @Override
    public long countDosenUsers() {
        return executeCountQuery(Queries.COUNT_USERS_BY_ROLE, "role", Role.DOSEN);
    }

    @Override
    public long countMahasiswaUsers() {
        return executeCountQuery(Queries.COUNT_USERS_BY_ROLE, "role", Role.MAHASISWA);
    }

    @Override
    public long countMataKuliah() {
        return executeSimpleCountQuery(Queries.COUNT_MATA_KULIAH);
    }

    @Override
    public long countLowongan() {
        return executeSimpleCountQuery(Queries.COUNT_LOWONGAN);
    }

    @Override
    public long countMataKuliahByDosenId(String dosenId) {
        if (!StringUtils.hasText(dosenId)) {
            log.warn("Attempted to count mata kuliah with null or empty dosenId");
            return 0L;
        }
        return executeCountQuery(Queries.COUNT_MATA_KULIAH_BY_DOSEN, "dosenId", dosenId);
    }

    @Override
    public long countMahasiswaAssistantByDosenId(String dosenId) {
        if (!StringUtils.hasText(dosenId)) {
            log.warn("Attempted to count mahasiswa assistant with null or empty dosenId");
            return 0L;
        }
        return executeCountQuery(Queries.COUNT_MAHASISWA_BY_DOSEN, "dosenId", dosenId);
    }

    @Override
    public long countOpenLowonganByDosenId(String dosenId) {
        if (!StringUtils.hasText(dosenId)) {
            log.warn("Attempted to count open lowongan with null or empty dosenId");
            return 0L;
        }
        return executeCountQuery(Queries.COUNT_OPEN_LOWONGAN_BY_DOSEN, "dosenId", dosenId);
    }

    @Override
    public long countOpenLowongan() {
        return executeSimpleCountQuery(Queries.COUNT_OPEN_LOWONGAN);
    }

    @Override
    public long countAcceptedLowonganByMahasiswaId(String mahasiswaId) {
        if (!StringUtils.hasText(mahasiswaId)) {
            log.warn("Attempted to count accepted lowongan with null or empty mahasiswaId");
            return 0L;
        }
        return executeCountQuery(Queries.COUNT_ACCEPTED_LOWONGAN, "mahasiswaId", mahasiswaId);
    }

    @Override
    public long countRejectedLowonganByMahasiswaId(String mahasiswaId) {
        if (!StringUtils.hasText(mahasiswaId)) {
            log.warn("Attempted to count rejected lowongan with null or empty mahasiswaId");
            return 0L;
        }
        return executeCountQuery(Queries.COUNT_REJECTED_LOWONGAN, "mahasiswaId", mahasiswaId);
    }

    @Override
    public long countPendingLowonganByMahasiswaId(String mahasiswaId) {
        if (!StringUtils.hasText(mahasiswaId)) {
            log.warn("Attempted to count pending lowongan with null or empty mahasiswaId");
            return 0L;
        }
        return executeCountQuery(Queries.COUNT_PENDING_LOWONGAN, "mahasiswaId", mahasiswaId);
    }

    @Override
    public double calculateTotalLogHoursByMahasiswaId(String mahasiswaId) {
        if (!StringUtils.hasText(mahasiswaId)) {
            log.warn("Attempted to calculate log hours with null or empty mahasiswaId");
            return 0.0;
        }

        try {
            @SuppressWarnings("unchecked")
            List<Object[]> logs = entityManager.createQuery(Queries.FIND_LOG_HOURS)
                    .setParameter("mahasiswaId", mahasiswaId)
                    .setParameter("status", StatusLog.DITERIMA)
                    .getResultList();

            return calculateTotalHours(logs);
        } catch (Exception e) {
            log.error("Error calculating total log hours for mahasiswa {}: {}", mahasiswaId, e.getMessage());
            return 0.0;
        }
    }

    @Override
    public double calculateTotalInsentifByMahasiswaId(String mahasiswaId) {
        return calculateTotalLogHoursByMahasiswaId(mahasiswaId) * HOURLY_RATE;
    }

    @Override
    public List<Lowongan> findAcceptedLowonganByMahasiswaId(String mahasiswaId) {
        if (!StringUtils.hasText(mahasiswaId)) {
            log.warn("Attempted to find accepted lowongan with null or empty mahasiswaId");
            return Collections.emptyList();
        }

        try {
            return entityManager.createQuery(Queries.FIND_ACCEPTED_LOWONGAN, Lowongan.class)
                    .setParameter("mahasiswaId", mahasiswaId)
                    .getResultList();
        } catch (Exception e) {
            log.error("Error finding accepted lowongan for mahasiswa {}: {}", mahasiswaId, e.getMessage());
            return Collections.emptyList();
        }
    }

    private long executeCountQuery(String queryString, String paramName, Object paramValue) {
        try {
            Object result = entityManager.createQuery(queryString)
                    .setParameter(paramName, paramValue)
                    .getSingleResult();
            return result != null ? (Long) result : 0L;
        } catch (Exception e) {
            log.error("Error executing count query: {}", e.getMessage());
            return 0L;
        }
    }

    private long executeSimpleCountQuery(String queryString) {
        try {
            Object result = entityManager.createQuery(queryString).getSingleResult();
            return result != null ? (Long) result : 0L;
        } catch (Exception e) {
            log.error("Error executing simple count query: {}", e.getMessage());
            return 0L;
        }
    }

    private double calculateTotalHours(List<Object[]> logs) {
        if (logs == null || logs.isEmpty()) {
            return 0.0;
        }

        double totalHours = 0.0;
        for (Object[] entry : logs) {
            if (entry != null && entry.length >= 2 && entry[0] != null && entry[1] != null) {
                try {
                    LocalTime start = (LocalTime) entry[0];
                    LocalTime end = (LocalTime) entry[1];
                    Duration duration = Duration.between(start, end);
                    totalHours += duration.toMinutes() / MINUTES_PER_HOUR;
                } catch (Exception e) {
                    log.warn("Error calculating duration for log entry: {}", e.getMessage());
                }
            }
        }
        return totalHours;
    }
}