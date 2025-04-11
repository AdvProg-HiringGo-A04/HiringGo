package id.ac.ui.cs.advprog.hiringgo.dashboard.service;

import id.ac.ui.cs.advprog.hiringgo.common.model.User;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.AdminStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.DosenStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.MahasiswaStatisticsDTO;

public interface DashboardService {
    Object getStatisticsForUser(User user);
    AdminStatisticsDTO getAdminStatistics();
    DosenStatisticsDTO getDosenStatistics(Long dosenId);
    MahasiswaStatisticsDTO getMahasiswaStatistics(Long mahasiswaId);
}