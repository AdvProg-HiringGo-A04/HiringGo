package id.ac.ui.cs.advprog.hiringgo.dashboard.strategy;

import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.AdminStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.repository.DashboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class AdminDashboardStrategy implements DashboardStatisticsStrategy<AdminStatisticsDTO> {
    private final DashboardRepository dashboardRepository;

    @Override
    @Async
    public CompletableFuture<AdminStatisticsDTO> calculateStatistics(String userId) {
        AdminStatisticsDTO result = AdminStatisticsDTO.builder()
                .totalDosen(dashboardRepository.countDosenUsers())
                .totalMahasiswa(dashboardRepository.countMahasiswaUsers())
                .totalMataKuliah(dashboardRepository.countMataKuliah())
                .totalLowongan(dashboardRepository.countLowongan())
                .build();

        return CompletableFuture.completedFuture(result);
    }
}