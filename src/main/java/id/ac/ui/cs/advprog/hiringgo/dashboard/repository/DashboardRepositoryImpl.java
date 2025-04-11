package id.ac.ui.cs.advprog.hiringgo.dashboard.repository;

import id.ac.ui.cs.advprog.hiringgo.common.model.Lowongan;
import id.ac.ui.cs.advprog.hiringgo.common.model.LogStatus;
import id.ac.ui.cs.advprog.hiringgo.common.model.UserRole;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardRepositoryImpl implements DashboardRepository {
    @PersistenceContext
    private EntityManager entityManager;

    private static final double RATE_PER_HOUR = 27500.0;

    // Cached query strings for better performance and maintainability
    private static final String COUNT_USERS_BY_ROLE = "SELECT COUNT(u) FROM User u WHERE u.role = :role";
    private static final String COUNT_MATA_KULIAH = "SELECT COUNT(mk) FROM MataKuliah mk";
    private static final String COUNT_LOWONGAN = "SELECT COUNT(l) FROM Lowongan l";
    private static final String COUNT_MATA_KULIAH_BY_DOSEN = "SELECT COUNT(mk) FROM MataKuliah mk JOIN mk.dosenPengampu d WHERE d.id = :dosenId";
    private static final String COUNT_MAHASISWA_BY_DOSEN = "SELECT COUNT(DISTINCT m) FROM Lowongan l JOIN l.mataKuliah mk JOIN mk.dosenPengampu d JOIN l.diterima m WHERE d.id = :dosenId";
    private static final String COUNT_OPEN_LOWONGAN_BY_DOSEN = "SELECT COUNT(l) FROM Lowongan l JOIN l.mataKuliah mk JOIN mk.dosenPengampu d WHERE d.id = :dosenId AND SIZE(l.diterima) < l.jumlahDibutuhkan";
    private static final String COUNT_OPEN_LOWONGAN = "SELECT COUNT(l) FROM Lowongan l WHERE SIZE(l.diterima) < l.jumlahDibutuhkan";
    private static final String COUNT_ACCEPTED_LOWONGAN = "SELECT COUNT(l) FROM Lowongan l JOIN l.diterima m WHERE m.id = :mahasiswaId";
    private static final String COUNT_REJECTED_LOWONGAN = "SELECT COUNT(l) FROM Lowongan l JOIN l.ditolak m WHERE m.id = :mahasiswaId";
    private static final String COUNT_PENDING_LOWONGAN = "SELECT COUNT(l) FROM Lowongan l JOIN l.pendaftar m WHERE m.id = :mahasiswaId AND m NOT MEMBER OF l.diterima AND m NOT MEMBER OF l.ditolak";
    private static final String FIND_LOG_HOURS = "SELECT log.waktuMulai, log.waktuSelesai FROM Log log WHERE log.mahasiswa.id = :mahasiswaId AND log.status = :status";
    private static final String FIND_ACCEPTED_LOWONGAN = "SELECT l FROM Lowongan l JOIN l.diterima m WHERE m.id = :mahasiswaId";

    @Override
    public long countDosenUsers() {
        return executeCountQuery(COUNT_USERS_BY_ROLE, "role", UserRole.DOSEN);
    }

    @Override
    public long countMahasiswaUsers() {
        return executeCountQuery(COUNT_USERS_BY_ROLE, "role", UserRole.MAHASISWA);
    }

    @Override
    public long countMataKuliah() {
        return (long) entityManager.createQuery(COUNT_MATA_KULIAH).getSingleResult();
    }

    @Override
    public long countLowongan() {
        return (long) entityManager.createQuery(COUNT_LOWONGAN).getSingleResult();
    }

    @Override
    public long countMataKuliahByDosenId(Long dosenId) {
        return executeCountQuery(COUNT_MATA_KULIAH_BY_DOSEN, "dosenId", dosenId);
    }

    @Override
    public long countMahasiswaAssistantByDosenId(Long dosenId) {
        return executeCountQuery(COUNT_MAHASISWA_BY_DOSEN, "dosenId", dosenId);
    }

    @Override
    public long countOpenLowonganByDosenId(Long dosenId) {
        return executeCountQuery(COUNT_OPEN_LOWONGAN_BY_DOSEN, "dosenId", dosenId);
    }

    @Override
    public long countOpenLowongan() {
        return (long) entityManager.createQuery(COUNT_OPEN_LOWONGAN).getSingleResult();
    }

    @Override
    public long countAcceptedLowonganByMahasiswaId(Long mahasiswaId) {
        return executeCountQuery(COUNT_ACCEPTED_LOWONGAN, "mahasiswaId", mahasiswaId);
    }

    @Override
    public long countRejectedLowonganByMahasiswaId(Long mahasiswaId) {
        return executeCountQuery(COUNT_REJECTED_LOWONGAN, "mahasiswaId", mahasiswaId);
    }

    @Override
    public long countPendingLowonganByMahasiswaId(Long mahasiswaId) {
        return executeCountQuery(COUNT_PENDING_LOWONGAN, "mahasiswaId", mahasiswaId);
    }

    @Override
    public double calculateTotalLogHoursByMahasiswaId(Long mahasiswaId) {
        List<Object[]> logs = entityManager.createQuery(FIND_LOG_HOURS)
                .setParameter("mahasiswaId", mahasiswaId)
                .setParameter("status", LogStatus.APPROVED)
                .getResultList();

        return calculateTotalHours(logs);
    }

    @Override
    public double calculateTotalInsentifByMahasiswaId(Long mahasiswaId) {
        return calculateTotalLogHoursByMahasiswaId(mahasiswaId) * RATE_PER_HOUR;
    }

    @Override
    public List<Lowongan> findAcceptedLowonganByMahasiswaId(Long mahasiswaId) {
        return entityManager.createQuery(FIND_ACCEPTED_LOWONGAN, Lowongan.class)
                .setParameter("mahasiswaId", mahasiswaId)
                .getResultList();
    }

    // Helper method to execute count queries with a single parameter
    private long executeCountQuery(String queryString, String paramName, Object paramValue) {
        return (long) entityManager.createQuery(queryString)
                .setParameter(paramName, paramValue)
                .getSingleResult();
    }

    // Helper method to calculate total hours from log entries
    private double calculateTotalHours(List<Object[]> logs) {
        double totalHours = 0.0;
        for (Object[] log : logs) {
            LocalTime start = (LocalTime) log[0];
            LocalTime end = (LocalTime) log[1];
            Duration duration = Duration.between(start, end);
            totalHours += duration.toMinutes() / 60.0;
        }
        return totalHours;
    }
}