package id.ac.ui.cs.advprog.hiringgo.dashboard.service;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Users;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.AdminStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.DosenStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.MahasiswaStatisticsDTO;

public interface DashboardService {
    Object getStatisticsForUser(Users user);
    AdminStatisticsDTO getAdminStatistics();
    DosenStatisticsDTO getDosenStatistics(String dosenId);
    MahasiswaStatisticsDTO getMahasiswaStatistics(String mahasiswaId);
}