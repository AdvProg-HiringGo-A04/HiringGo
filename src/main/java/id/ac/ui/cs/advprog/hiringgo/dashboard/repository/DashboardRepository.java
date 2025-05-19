package id.ac.ui.cs.advprog.hiringgo.dashboard.repository;

import id.ac.ui.cs.advprog.hiringgo.common.model.Lowongan;
import java.util.List;

public interface DashboardRepository {
    // Admin stats0
    long countDosenUsers();
    long countMahasiswaUsers();
    long countMataKuliah();
    long countLowongan();

    // Dosen stats
    long countMataKuliahByDosenId(Long dosenId);
    long countMahasiswaAssistantByDosenId(Long dosenId);
    long countOpenLowonganByDosenId(Long dosenId);

    // Mahasiswa stats
    long countOpenLowongan();
    long countAcceptedLowonganByMahasiswaId(Long mahasiswaId);
    long countRejectedLowonganByMahasiswaId(Long mahasiswaId);
    long countPendingLowonganByMahasiswaId(Long mahasiswaId);
    double calculateTotalLogHoursByMahasiswaId(Long mahasiswaId);
    double calculateTotalInsentifByMahasiswaId(Long mahasiswaId);
    List<Lowongan> findAcceptedLowonganByMahasiswaId(Long mahasiswaId);
}