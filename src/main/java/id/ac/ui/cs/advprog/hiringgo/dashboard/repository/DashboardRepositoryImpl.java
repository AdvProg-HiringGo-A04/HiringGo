package id.ac.ui.cs.advprog.hiringgo.dashboard.repository;

import id.ac.ui.cs.advprog.hiringgo.entity.Lowongan;
import id.ac.ui.cs.advprog.hiringgo.enums.Role;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.enums.StatusLog;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardRepositoryImpl implements DashboardRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final QueryConstants queryConstants = new QueryConstants();
    private final QueryExecutor queryExecutor;
    private final LogHoursCalculator logHoursCalculator;

    public DashboardRepositoryImpl() {
        this.queryExecutor = new QueryExecutor();
        this.logHoursCalculator = new LogHoursCalculator();
    }

    @Override
    public long countDosenUsers() {
        return queryExecutor.executeCountQuery(
                entityManager, queryConstants.COUNT_USERS_BY_ROLE, "role", Role.DOSEN, log
        );
    }

    @Override
    public long countMahasiswaUsers() {
        return queryExecutor.executeCountQuery(
                entityManager, queryConstants.COUNT_USERS_BY_ROLE, "role", Role.MAHASISWA, log
        );
    }

    @Override
    public long countMataKuliah() {
        return queryExecutor.executeSimpleCountQuery(
                entityManager, queryConstants.COUNT_MATA_KULIAH, log
        );
    }

    @Override
    public long countLowongan() {
        return queryExecutor.executeSimpleCountQuery(
                entityManager, queryConstants.COUNT_LOWONGAN, log
        );
    }

    @Override
    public long countMataKuliahByDosenId(String dosenId) {
        if (!StringUtils.hasText(dosenId)) {
            log.warn("Attempted to count mata kuliah with null or empty dosenId");
            return 0L;
        }
        return queryExecutor.executeCountQuery(
                entityManager, queryConstants.COUNT_MATA_KULIAH_BY_DOSEN, "dosenId", dosenId, log
        );
    }

    @Override
    public long countMahasiswaAssistantByDosenId(String dosenId) {
        if (!StringUtils.hasText(dosenId)) {
            log.warn("Attempted to count mahasiswa assistant with null or empty dosenId");
            return 0L;
        }
        return queryExecutor.executeCountQuery(
                entityManager, queryConstants.COUNT_MAHASISWA_BY_DOSEN, "dosenId", dosenId, log
        );
    }

    @Override
    public long countOpenLowonganByDosenId(String dosenId) {
        if (!StringUtils.hasText(dosenId)) {
            log.warn("Attempted to count open lowongan with null or empty dosenId");
            return 0L;
        }
        return queryExecutor.executeCountQuery(
                entityManager, queryConstants.COUNT_OPEN_LOWONGAN_BY_DOSEN, "dosenId", dosenId, log
        );
    }

    @Override
    public long countOpenLowongan() {
        return queryExecutor.executeSimpleCountQuery(
                entityManager, queryConstants.COUNT_OPEN_LOWONGAN, log
        );
    }

    @Override
    public long countAcceptedLowonganByMahasiswaId(String mahasiswaId) {
        if (!StringUtils.hasText(mahasiswaId)) {
            log.warn("Attempted to count accepted lowongan with null or empty mahasiswaId");
            return 0L;
        }
        return queryExecutor.executeCountQuery(
                entityManager, queryConstants.COUNT_ACCEPTED_LOWONGAN, "mahasiswaId", mahasiswaId, log
        );
    }

    @Override
    public long countRejectedLowonganByMahasiswaId(String mahasiswaId) {
        if (!StringUtils.hasText(mahasiswaId)) {
            log.warn("Attempted to count rejected lowongan with null or empty mahasiswaId");
            return 0L;
        }
        return queryExecutor.executeCountQuery(
                entityManager, queryConstants.COUNT_REJECTED_LOWONGAN, "mahasiswaId", mahasiswaId, log
        );
    }

    @Override
    public long countPendingLowonganByMahasiswaId(String mahasiswaId) {
        if (!StringUtils.hasText(mahasiswaId)) {
            log.warn("Attempted to count pending lowongan with null or empty mahasiswaId");
            return 0L;
        }
        return queryExecutor.executeCountQuery(
                entityManager, queryConstants.COUNT_PENDING_LOWONGAN, "mahasiswaId", mahasiswaId, log
        );
    }

    @Override
    public double calculateTotalLogHoursByMahasiswaId(String mahasiswaId) {
        if (!StringUtils.hasText(mahasiswaId)) {
            log.warn("Attempted to calculate log hours with null or empty mahasiswaId");
            return 0.0;
        }

        try {
            @SuppressWarnings("unchecked")
            List<Object[]> logs = entityManager.createQuery(queryConstants.FIND_LOG_HOURS)
                    .setParameter("mahasiswaId", mahasiswaId)
                    .setParameter("status", StatusLog.DITERIMA)
                    .getResultList();

            return logHoursCalculator.calculateTotalHours(logs, log);
        } catch (Exception e) {
            log.error("Error calculating total log hours for mahasiswa {}: {}", mahasiswaId, e.getMessage());
            return 0.0;
        }
    }

    @Override
    public double calculateTotalInsentifByMahasiswaId(String mahasiswaId) {
        return calculateTotalLogHoursByMahasiswaId(mahasiswaId) * LogHoursCalculator.HOURLY_RATE;
    }

    @Override
    public List<Lowongan> findAcceptedLowonganByMahasiswaId(String mahasiswaId) {
        if (!StringUtils.hasText(mahasiswaId)) {
            log.warn("Attempted to find accepted lowongan DTO with null or empty mahasiswaId");
            return Collections.emptyList();
        }

        try {
            return entityManager.createQuery(queryConstants.FIND_ACCEPTED_LOWONGAN, Lowongan.class)
                    .setParameter("mahasiswaId", mahasiswaId)
                    .getResultList();
        } catch (Exception e) {
            log.error("Error finding accepted lowongan for mahasiswa {}: {}", mahasiswaId, e.getMessage());
            return Collections.emptyList();
        }
    }
}
