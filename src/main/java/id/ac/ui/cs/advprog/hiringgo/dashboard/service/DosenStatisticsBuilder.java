package id.ac.ui.cs.advprog.hiringgo.dashboard.service;

import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.DosenStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.repository.DashboardRepository;
import org.slf4j.Logger;
import org.springframework.util.StringUtils;

public class DosenStatisticsBuilder {

    public static DosenStatisticsDTO build(String dosenId, DashboardRepository repository, Logger log) {
        if (!StringUtils.hasText(dosenId)) {
            log.warn("Attempted to get dosen statistics with null or empty dosenId");
            return createEmptyDosenStatistics();
        }

        log.debug("Fetching dosen statistics for dosenId: {}", dosenId);
        return DosenStatisticsDTO.builder()
                .totalMataKuliah(repository.countMataKuliahByDosenId(dosenId))
                .totalMahasiswaAssistant(repository.countMahasiswaAssistantByDosenId(dosenId))
                .openLowonganCount(repository.countOpenLowonganByDosenId(dosenId))
                .build();
    }

    private static DosenStatisticsDTO createEmptyDosenStatistics() {
        return DosenStatisticsDTO.builder()
                .totalMataKuliah(0L)
                .totalMahasiswaAssistant(0L)
                .openLowonganCount(0L)
                .build();
    }
}