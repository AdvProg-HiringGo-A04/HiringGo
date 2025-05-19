// src/main/java/id/ac/ui/cs/advprog/hiringgo/dashboard/strategy/DosenDashboardStrategy.java
package id.ac.ui.cs.advprog.hiringgo.dashboard.strategy;

import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.DosenStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.repository.DashboardRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DosenDashboardStrategy implements DashboardStatisticsStrategy<DosenStatisticsDTO> {
    private final DashboardRepository dashboardRepository;

    @Override
    public DosenStatisticsDTO calculateStatistics(String dosenId) {
        return DosenStatisticsDTO.builder()
                .totalMataKuliah(dashboardRepository.countMataKuliahByDosenId(dosenId))
                .totalMahasiswaAssistant(dashboardRepository.countMahasiswaAssistantByDosenId(dosenId))
                .openLowonganCount(dashboardRepository.countOpenLowonganByDosenId(dosenId))
                .build();
    }
}