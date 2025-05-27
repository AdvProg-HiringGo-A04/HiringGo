package id.ac.ui.cs.advprog.hiringgo.dashboard.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QueryConstantsTest {

    private QueryConstants queryConstants;

    @BeforeEach
    void setUp() {
        queryConstants = new QueryConstants();
    }

    @Test
    void countUsersByRole_ShouldHaveCorrectQuery() {
        // Act & Assert
        assertEquals("SELECT COUNT(u) FROM User u WHERE u.role = :role",
                queryConstants.COUNT_USERS_BY_ROLE);
    }

    @Test
    void countMataKuliah_ShouldHaveCorrectQuery() {
        // Act & Assert
        assertEquals("SELECT COUNT(mk) FROM MataKuliah mk",
                queryConstants.COUNT_MATA_KULIAH);
    }

    @Test
    void countLowongan_ShouldHaveCorrectQuery() {
        // Act & Assert
        assertEquals("SELECT COUNT(l) FROM Lowongan l",
                queryConstants.COUNT_LOWONGAN);
    }

    @Test
    void countMataKuliahByDosen_ShouldHaveCorrectQuery() {
        // Act & Assert
        String expectedQuery = "SELECT COUNT(mk) FROM MataKuliah mk JOIN mk.dosenPengampu d WHERE d.id = :dosenId";
        assertEquals(expectedQuery, queryConstants.COUNT_MATA_KULIAH_BY_DOSEN);
    }

    @Test
    void countMahasiswaByDosen_ShouldHaveCorrectQuery() {
        // Act & Assert
        String expectedQuery = "SELECT COUNT(DISTINCT pl.mahasiswa) FROM PendaftarLowongan pl " +
                "WHERE pl.lowongan.id IN (SELECT l.id FROM Lowongan l " +
                "WHERE l.mataKuliah.kodeMataKuliah IN (SELECT mk.kodeMataKuliah FROM MataKuliah mk " +
                "JOIN mk.dosenPengampu d WHERE d.id = :dosenId)) AND pl.diterima = true";
        assertEquals(expectedQuery, queryConstants.COUNT_MAHASISWA_BY_DOSEN);
    }

    @Test
    void countOpenLowonganByDosen_ShouldHaveCorrectQuery() {
        // Act & Assert
        String expectedQuery = "SELECT COUNT(l) FROM Lowongan l " +
                "WHERE l.mataKuliah.kodeMataKuliah IN (SELECT mk.kodeMataKuliah FROM MataKuliah mk " +
                "JOIN mk.dosenPengampu d WHERE d.id = :dosenId) " +
                "AND l.jumlahDibutuhkan > " +
                "(SELECT COUNT(pl) FROM PendaftarLowongan pl WHERE pl.lowongan.id = l.id AND pl.diterima = true)";
        assertEquals(expectedQuery, queryConstants.COUNT_OPEN_LOWONGAN_BY_DOSEN);
    }

    @Test
    void countOpenLowongan_ShouldHaveCorrectQuery() {
        // Act & Assert
        String expectedQuery = "SELECT COUNT(l) FROM Lowongan l " +
                "WHERE l.jumlahDibutuhkan > " +
                "(SELECT COUNT(pl) FROM PendaftarLowongan pl WHERE pl.lowongan.id = l.id AND pl.diterima = true)";
        assertEquals(expectedQuery, queryConstants.COUNT_OPEN_LOWONGAN);
    }

    @Test
    void countAcceptedLowongan_ShouldHaveCorrectQuery() {
        // Act & Assert
        String expectedQuery = "SELECT COUNT(pl) FROM PendaftarLowongan pl " +
                "WHERE pl.mahasiswa.id = :mahasiswaId AND pl.diterima = true";
        assertEquals(expectedQuery, queryConstants.COUNT_ACCEPTED_LOWONGAN);
    }

    @Test
    void countRejectedLowongan_ShouldHaveCorrectQuery() {
        // Act & Assert
        String expectedQuery = "SELECT COUNT(pl) FROM PendaftarLowongan pl " +
                "WHERE pl.mahasiswa.id = :mahasiswaId AND pl.diterima = false AND pl.diterima IS NOT NULL";
        assertEquals(expectedQuery, queryConstants.COUNT_REJECTED_LOWONGAN);
    }

    @Test
    void countPendingLowongan_ShouldHaveCorrectQuery() {
        // Act & Assert
        String expectedQuery = "SELECT COUNT(pl) FROM PendaftarLowongan pl " +
                "WHERE pl.mahasiswa.id = :mahasiswaId AND pl.diterima IS NULL";
        assertEquals(expectedQuery, queryConstants.COUNT_PENDING_LOWONGAN);
    }

    @Test
    void findLogHours_ShouldHaveCorrectQuery() {
        // Act & Assert
        String expectedQuery = "SELECT log.waktuMulai, log.waktuSelesai FROM Log log " +
                "WHERE log.mahasiswa.id = :mahasiswaId AND log.status = :status";
        assertEquals(expectedQuery, queryConstants.FIND_LOG_HOURS);
    }

    @Test
    void findAcceptedLowongan_ShouldHaveCorrectQuery() {
        // Act & Assert
        String expectedQuery = "SELECT l FROM Lowongan l " +
                "JOIN PendaftarLowongan pl ON pl.lowongan.id = l.id " +
                "WHERE pl.mahasiswa.id = :mahasiswaId AND pl.diterima = true";
        assertEquals(expectedQuery, queryConstants.FIND_ACCEPTED_LOWONGAN);
    }

    @Test
    void allQueriesAreNotNull() {
        // Act & Assert
        assertNotNull(queryConstants.COUNT_USERS_BY_ROLE);
        assertNotNull(queryConstants.COUNT_MATA_KULIAH);
        assertNotNull(queryConstants.COUNT_LOWONGAN);
        assertNotNull(queryConstants.COUNT_MATA_KULIAH_BY_DOSEN);
        assertNotNull(queryConstants.COUNT_MAHASISWA_BY_DOSEN);
        assertNotNull(queryConstants.COUNT_OPEN_LOWONGAN_BY_DOSEN);
        assertNotNull(queryConstants.COUNT_OPEN_LOWONGAN);
        assertNotNull(queryConstants.COUNT_ACCEPTED_LOWONGAN);
        assertNotNull(queryConstants.COUNT_REJECTED_LOWONGAN);
        assertNotNull(queryConstants.COUNT_PENDING_LOWONGAN);
        assertNotNull(queryConstants.FIND_LOG_HOURS);
        assertNotNull(queryConstants.FIND_ACCEPTED_LOWONGAN);
    }

    @Test
    void allQueriesAreNotEmpty() {
        // Act & Assert
        assertFalse(queryConstants.COUNT_USERS_BY_ROLE.isEmpty());
        assertFalse(queryConstants.COUNT_MATA_KULIAH.isEmpty());
        assertFalse(queryConstants.COUNT_LOWONGAN.isEmpty());
        assertFalse(queryConstants.COUNT_MATA_KULIAH_BY_DOSEN.isEmpty());
        assertFalse(queryConstants.COUNT_MAHASISWA_BY_DOSEN.isEmpty());
        assertFalse(queryConstants.COUNT_OPEN_LOWONGAN_BY_DOSEN.isEmpty());
        assertFalse(queryConstants.COUNT_OPEN_LOWONGAN.isEmpty());
        assertFalse(queryConstants.COUNT_ACCEPTED_LOWONGAN.isEmpty());
        assertFalse(queryConstants.COUNT_REJECTED_LOWONGAN.isEmpty());
        assertFalse(queryConstants.COUNT_PENDING_LOWONGAN.isEmpty());
        assertFalse(queryConstants.FIND_LOG_HOURS.isEmpty());
        assertFalse(queryConstants.FIND_ACCEPTED_LOWONGAN.isEmpty());
    }

    @Test
    void parameterizedQueries_ContainCorrectParameters() {
        // Act & Assert
        assertTrue(queryConstants.COUNT_USERS_BY_ROLE.contains(":role"));
        assertTrue(queryConstants.COUNT_MATA_KULIAH_BY_DOSEN.contains(":dosenId"));
        assertTrue(queryConstants.COUNT_MAHASISWA_BY_DOSEN.contains(":dosenId"));
        assertTrue(queryConstants.COUNT_OPEN_LOWONGAN_BY_DOSEN.contains(":dosenId"));
        assertTrue(queryConstants.COUNT_ACCEPTED_LOWONGAN.contains(":mahasiswaId"));
        assertTrue(queryConstants.COUNT_REJECTED_LOWONGAN.contains(":mahasiswaId"));
        assertTrue(queryConstants.COUNT_PENDING_LOWONGAN.contains(":mahasiswaId"));
        assertTrue(queryConstants.FIND_LOG_HOURS.contains(":mahasiswaId"));
        assertTrue(queryConstants.FIND_LOG_HOURS.contains(":status"));
        assertTrue(queryConstants.FIND_ACCEPTED_LOWONGAN.contains(":mahasiswaId"));
    }

    @Test
    void countQueries_StartWithSelectCount() {
        // Act & Assert
        assertTrue(queryConstants.COUNT_USERS_BY_ROLE.startsWith("SELECT COUNT("));
        assertTrue(queryConstants.COUNT_MATA_KULIAH.startsWith("SELECT COUNT("));
        assertTrue(queryConstants.COUNT_LOWONGAN.startsWith("SELECT COUNT("));
        assertTrue(queryConstants.COUNT_MATA_KULIAH_BY_DOSEN.startsWith("SELECT COUNT("));
        assertTrue(queryConstants.COUNT_MAHASISWA_BY_DOSEN.startsWith("SELECT COUNT("));
        assertTrue(queryConstants.COUNT_OPEN_LOWONGAN_BY_DOSEN.startsWith("SELECT COUNT("));
        assertTrue(queryConstants.COUNT_OPEN_LOWONGAN.startsWith("SELECT COUNT("));
        assertTrue(queryConstants.COUNT_ACCEPTED_LOWONGAN.startsWith("SELECT COUNT("));
        assertTrue(queryConstants.COUNT_REJECTED_LOWONGAN.startsWith("SELECT COUNT("));
        assertTrue(queryConstants.COUNT_PENDING_LOWONGAN.startsWith("SELECT COUNT("));
    }

    @Test
    void findQueries_StartWithSelect() {
        // Act & Assert
        assertTrue(queryConstants.FIND_LOG_HOURS.startsWith("SELECT "));
        assertTrue(queryConstants.FIND_ACCEPTED_LOWONGAN.startsWith("SELECT "));
    }

    @Test
    void constructor_CreatesInstance() {
        // Act
        QueryConstants newQueryConstants = new QueryConstants();

        // Assert
        assertNotNull(newQueryConstants);
        assertNotNull(newQueryConstants.COUNT_USERS_BY_ROLE);
        assertNotNull(newQueryConstants.COUNT_MATA_KULIAH);
        assertNotNull(newQueryConstants.COUNT_LOWONGAN);
    }
}