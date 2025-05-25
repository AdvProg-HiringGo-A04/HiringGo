package id.ac.ui.cs.advprog.hiringgo.dashboard.repository;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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
        assertDoesNotThrow(() -> repositoryClass.getMethod("countMataKuliahByDosenId", String.class));
        assertDoesNotThrow(() -> repositoryClass.getMethod("countMahasiswaAssistantByDosenId", String.class));
        assertDoesNotThrow(() -> repositoryClass.getMethod("countOpenLowonganByDosenId", String.class));

        // Mahasiswa stats methods
        assertDoesNotThrow(() -> repositoryClass.getMethod("countOpenLowongan"));
        assertDoesNotThrow(() -> repositoryClass.getMethod("countAcceptedLowonganByMahasiswaId", String.class));
        assertDoesNotThrow(() -> repositoryClass.getMethod("countRejectedLowonganByMahasiswaId", String.class));
        assertDoesNotThrow(() -> repositoryClass.getMethod("countPendingLowonganByMahasiswaId", String.class));
        assertDoesNotThrow(() -> repositoryClass.getMethod("calculateTotalLogHoursByMahasiswaId", String.class));
        assertDoesNotThrow(() -> repositoryClass.getMethod("calculateTotalInsentifByMahasiswaId", String.class));
        assertDoesNotThrow(() -> repositoryClass.getMethod("findAcceptedLowonganByMahasiswaId", String.class));
    }
}