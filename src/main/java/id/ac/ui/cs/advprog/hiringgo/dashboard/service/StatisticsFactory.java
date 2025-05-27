package id.ac.ui.cs.advprog.hiringgo.dashboard.service;

import id.ac.ui.cs.advprog.hiringgo.entity.User;
import org.springframework.stereotype.Component;

@Component
public class StatisticsFactory {

    public Object createStatisticsForUser(User user, DashboardService dashboardService) {
        switch (user.getRole()) {
            case ADMIN:
                return dashboardService.getAdminStatistics();
            case DOSEN:
                return dashboardService.getDosenStatistics(user.getId());
            case MAHASISWA:
                return dashboardService.getMahasiswaStatistics(user.getId());
            default:
                throw new IllegalArgumentException("Unknown user role: " + user.getRole());
        }
    }
}