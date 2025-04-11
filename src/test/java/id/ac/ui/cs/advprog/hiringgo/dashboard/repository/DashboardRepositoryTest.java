package id.ac.ui.cs.advprog.hiringgo.dashboard.repository;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DashboardRepositoryTest {

    @Test
    void shouldDefineRequiredMethods() {
        // This test verifies that the interface contains all required methods
        // It will compile only if all methods are defined in the interface

        // Admin stats methods
        Class<?> repositoryClass = DashboardRepository.class;
        assertDoesNotThrow(() -> repositoryClass.getMethod("countDosenUsers"));
        assertDoesNotThrow(() -> repositoryClass.getMethod("countMahasiswaUsers"));
        assertDoesNotThrow(() -> repositoryClass.getMethod("countMataKuliah"));
        assertDoesNotThrow(() -> repositoryClass.getMethod("countLowongan"));

        // Dosen stats methods
        assertDoesNotThrow(() -> repositoryClass.getMethod("countMataKuliahByDosenId", Long.class));
        assertDoesNotThrow(() -> repositoryClass.getMethod("countMahasiswaAssistantByDosenId", Long.class));
        assertDoesNotThrow(() -> repositoryClass.getMethod("countOpenLowonganByDosenId", Long.class));

        // Mahasiswa stats methods
        assertDoesNotThrow(() -> repositoryClass.getMethod("countOpenLowongan"));
        assertDoesNotThrow(() -> repositoryClass.getMethod("countAcceptedLowonganByMahasiswaId", Long.class));
        assertDoesNotThrow(() -> repositoryClass.getMethod("countRejectedLowonganByMahasiswaId", Long.class));
        assertDoesNotThrow(() -> repositoryClass.getMethod("countPendingLowonganByMahasiswaId", Long.class));
        assertDoesNotThrow(() -> repositoryClass.getMethod("calculateTotalLogHoursByMahasiswaId", Long.class));
        assertDoesNotThrow(() -> repositoryClass.getMethod("calculateTotalInsentifByMahasiswaId", Long.class));
        assertDoesNotThrow(() -> repositoryClass.getMethod("findAcceptedLowonganByMahasiswaId", Long.class));
    }
}