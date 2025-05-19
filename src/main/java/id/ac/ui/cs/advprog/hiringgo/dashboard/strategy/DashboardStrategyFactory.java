// src/main/java/id/ac/ui/cs/advprog/hiringgo/dashboard/strategy/DashboardStrategyFactory.java
package id.ac.ui.cs.advprog.hiringgo.dashboard.strategy;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Users;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Role;
import id.ac.ui.cs.advprog.hiringgo.dashboard.repository.DashboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DashboardStrategyFactory {
    private final DashboardRepository dashboardRepository;

    public DashboardStatisticsStrategy<?> getStrategy(Users user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        if (user.getRole() == null) {
            throw new IllegalArgumentException("User role cannot be null");
        }

        switch (user.getRole()) {
            case ADMIN:
                return new AdminDashboardStrategy(dashboardRepository);
            case DOSEN:
                return new DosenDashboardStrategy(dashboardRepository);
            case MAHASISWA:
                return new MahasiswaDashboardStrategy(dashboardRepository);
            default:
                throw new IllegalArgumentException("Unknown user role: " + user.getRole());
        }
    }
}