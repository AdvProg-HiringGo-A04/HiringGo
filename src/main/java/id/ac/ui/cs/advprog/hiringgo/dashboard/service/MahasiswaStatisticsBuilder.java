package id.ac.ui.cs.advprog.hiringgo.dashboard.service;

import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.LowonganDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.MahasiswaStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.repository.DashboardRepository;
import id.ac.ui.cs.advprog.hiringgo.entity.Lowongan;
import org.slf4j.Logger;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

public class MahasiswaStatisticsBuilder {

    public static MahasiswaStatisticsDTO build(String mahasiswaId, DashboardRepository repository, Logger log) {
        if (!StringUtils.hasText(mahasiswaId)) {
            log.warn("Attempted to get mahasiswa statistics with null or empty mahasiswaId");
            return createEmptyMahasiswaStatistics();
        }

        log.debug("Fetching mahasiswa statistics for mahasiswaId: {}", mahasiswaId);

        List<Lowongan> acceptedLowongan = repository.findAcceptedLowonganByMahasiswaId(mahasiswaId);
        List<LowonganDTO> lowonganDTOList = LowonganConverter.convertListToDTO(acceptedLowongan, log);

        return MahasiswaStatisticsDTO.builder()
                .openLowonganCount(repository.countOpenLowongan())
                .acceptedLowonganCount(repository.countAcceptedLowonganByMahasiswaId(mahasiswaId))
                .rejectedLowonganCount(repository.countRejectedLowonganByMahasiswaId(mahasiswaId))
                .pendingLowonganCount(repository.countPendingLowonganByMahasiswaId(mahasiswaId))
                .totalLogHours(repository.calculateTotalLogHoursByMahasiswaId(mahasiswaId))
                .totalInsentif(repository.calculateTotalInsentifByMahasiswaId(mahasiswaId))
                .acceptedLowonganList(lowonganDTOList)
                .build();
    }

    private static MahasiswaStatisticsDTO createEmptyMahasiswaStatistics() {
        return MahasiswaStatisticsDTO.builder()
                .openLowonganCount(0L)
                .acceptedLowonganCount(0L)
                .rejectedLowonganCount(0L)
                .pendingLowonganCount(0L)
                .totalLogHours(0.0)
                .totalInsentif(0.0)
                .acceptedLowonganList(Collections.emptyList())
                .build();
    }
}