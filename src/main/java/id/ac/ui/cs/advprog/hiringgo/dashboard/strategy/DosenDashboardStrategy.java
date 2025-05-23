package id.ac.ui.cs.advprog.hiringgo.dashboard.strategy;

import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.DosenStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.repository.DashboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class DosenDashboardStrategy implements DashboardStatisticsStrategy<DosenStatisticsDTO> {
    private final DashboardRepository dashboardRepository;

    @Override
    @Async
    public CompletableFuture<DosenStatisticsDTO> calculateStatistics(String dosenId) {
        DosenStatisticsDTO result = DosenStatisticsDTO.builder()
                .totalMataKuliah(dashboardRepository.countMataKuliahByDosenId(dosenId))
                .totalMahasiswaAssistant(dashboardRepository.countMahasiswaAssistantByDosenId(dosenId))
                .openLowonganCount(dashboardRepository.countOpenLowonganByDosenId(dosenId))
                .build();

        return CompletableFuture.completedFuture(result);
    }
}