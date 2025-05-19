package id.ac.ui.cs.advprog.hiringgo.dashboard.repository;

import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.entity.Lowongan;
import java.util.List;

public interface DashboardRepository {
    // Admin stats
    long countDosenUsers();
    long countMahasiswaUsers();
    long countMataKuliah();
    long countLowongan();

    // Dosen stats
    long countMataKuliahByDosenId(String dosenId);
    long countMahasiswaAssistantByDosenId(String dosenId);
    long countOpenLowonganByDosenId(String dosenId);

    // Mahasiswa stats
    long countOpenLowongan();
    long countAcceptedLowonganByMahasiswaId(String mahasiswaId);
    long countRejectedLowonganByMahasiswaId(String mahasiswaId);
    long countPendingLowonganByMahasiswaId(String mahasiswaId);
    double calculateTotalLogHoursByMahasiswaId(String mahasiswaId);
    double calculateTotalInsentifByMahasiswaId(String mahasiswaId);
    List<Lowongan> findAcceptedLowonganByMahasiswaId(String mahasiswaId);
}