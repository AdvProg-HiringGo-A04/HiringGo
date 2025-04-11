package id.ac.ui.cs.advprog.hiringgo.dashboard.repository;

import id.ac.ui.cs.advprog.hiringgo.common.model.Lowongan;
import id.ac.ui.cs.advprog.hiringgo.common.model.LogStatus;
import id.ac.ui.cs.advprog.hiringgo.common.model.UserRole;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DashboardRepositoryImpl implements DashboardRepository {
    @PersistenceContext
    private EntityManager entityManager;

    private static final double RATE_PER_HOUR = 27500.0;

    @Override
    public long countDosenUsers() {
        return (long) entityManager.createQuery(
                        "SELECT COUNT(u) FROM User u WHERE u.role = :role")
                .setParameter("role", UserRole.DOSEN)
                .getSingleResult();
    }

    @Override
    public long countMahasiswaUsers() {
        return (long) entityManager.createQuery(
                        "SELECT COUNT(u) FROM User u WHERE u.role = :role")
                .setParameter("role", UserRole.MAHASISWA)
                .getSingleResult();
    }

    @Override
    public long countMataKuliah() {
        return (long) entityManager.createQuery(
                        "SELECT COUNT(mk) FROM MataKuliah mk")
                .getSingleResult();
    }

    @Override
    public long countLowongan() {
        return (long) entityManager.createQuery(
                        "SELECT COUNT(l) FROM Lowongan l")
                .getSingleResult();
    }

    @Override
    public long countMataKuliahByDosenId(Long dosenId) {
        return (long) entityManager.createQuery(
                        "SELECT COUNT(mk) FROM MataKuliah mk JOIN mk.dosenPengampu d WHERE d.id = :dosenId")
                .setParameter("dosenId", dosenId)
                .getSingleResult();
    }

    @Override
    public long countMahasiswaAssistantByDosenId(Long dosenId) {
        return (long) entityManager.createQuery(
                        "SELECT COUNT(DISTINCT m) FROM Lowongan l JOIN l.mataKuliah mk JOIN mk.dosenPengampu d JOIN l.diterima m WHERE d.id = :dosenId")
                .setParameter("dosenId", dosenId)
                .getSingleResult();
    }

    @Override
    public long countOpenLowonganByDosenId(Long dosenId) {
        return (long) entityManager.createQuery(
                        "SELECT COUNT(l) FROM Lowongan l JOIN l.mataKuliah mk JOIN mk.dosenPengampu d WHERE d.id = :dosenId AND SIZE(l.diterima) < l.jumlahDibutuhkan")
                .setParameter("dosenId", dosenId)
                .getSingleResult();
    }

    @Override
    public long countOpenLowongan() {
        return (long) entityManager.createQuery(
                        "SELECT COUNT(l) FROM Lowongan l WHERE SIZE(l.diterima) < l.jumlahDibutuhkan")
                .getSingleResult();
    }

    @Override
    public long countAcceptedLowonganByMahasiswaId(Long mahasiswaId) {
        return (long) entityManager.createQuery(
                        "SELECT COUNT(l) FROM Lowongan l JOIN l.diterima m WHERE m.id = :mahasiswaId")
                .setParameter("mahasiswaId", mahasiswaId)
                .getSingleResult();
    }

    @Override
    public long countRejectedLowonganByMahasiswaId(Long mahasiswaId) {
        return (long) entityManager.createQuery(
                        "SELECT COUNT(l) FROM Lowongan l JOIN l.ditolak m WHERE m.id = :mahasiswaId")
                .setParameter("mahasiswaId", mahasiswaId)
                .getSingleResult();
    }

    @Override
    public long countPendingLowonganByMahasiswaId(Long mahasiswaId) {
        return (long) entityManager.createQuery(
                        "SELECT COUNT(l) FROM Lowongan l JOIN l.pendaftar m WHERE m.id = :mahasiswaId AND m NOT MEMBER OF l.diterima AND m NOT MEMBER OF l.ditolak")
                .setParameter("mahasiswaId", mahasiswaId)
                .getSingleResult();
    }

    @Override
    public double calculateTotalLogHoursByMahasiswaId(Long mahasiswaId) {
        List<Object[]> logs = entityManager.createQuery(
                        "SELECT log.waktuMulai, log.waktuSelesai FROM Log log WHERE log.mahasiswa.id = :mahasiswaId AND log.status = :status")
                .setParameter("mahasiswaId", mahasiswaId)
                .setParameter("status", LogStatus.APPROVED)
                .getResultList();

        double totalHours = 0.0;
        for (Object[] log : logs) {
            LocalTime start = (LocalTime) log[0];
            LocalTime end = (LocalTime) log[1];
            Duration duration = Duration.between(start, end);
            totalHours += duration.toMinutes() / 60.0;
        }

        return totalHours;
    }

    @Override
    public double calculateTotalInsentifByMahasiswaId(Long mahasiswaId) {
        return calculateTotalLogHoursByMahasiswaId(mahasiswaId) * RATE_PER_HOUR;
    }

    @Override
    public List<Lowongan> findAcceptedLowonganByMahasiswaId(Long mahasiswaId) {
        return entityManager.createQuery(
                        "SELECT l FROM Lowongan l JOIN l.diterima m WHERE m.id = :mahasiswaId", Lowongan.class)
                .setParameter("mahasiswaId", mahasiswaId)
                .getResultList();
    }
}