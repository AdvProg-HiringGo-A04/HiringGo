// src/main/java/id/ac/ui/cs/advprog/hiringgo/dashboard/strategy/AdminDashboardStrategy.java
package id.ac.ui.cs.advprog.hiringgo.dashboard.strategy;

import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.AdminStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.repository.DashboardRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AdminDashboardStrategy implements DashboardStatisticsStrategy<AdminStatisticsDTO> {
    private final DashboardRepository dashboardRepository;

    @Override
    public AdminStatisticsDTO calculateStatistics(Long userId) {
        return AdminStatisticsDTO.builder()
                .totalDosen(dashboardRepository.countDosenUsers())
                .totalMahasiswa(dashboardRepository.countMahasiswaUsers())
                .totalMataKuliah(dashboardRepository.countMataKuliah())
                .totalLowongan(dashboardRepository.countLowongan())
                .build();
    }
}