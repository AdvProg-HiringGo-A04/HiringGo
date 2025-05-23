package id.ac.ui.cs.advprog.hiringgo.dashboard.strategy;

import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.entity.Lowongan;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.LowonganDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.MahasiswaStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.repository.DashboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MahasiswaDashboardStrategy implements DashboardStatisticsStrategy<MahasiswaStatisticsDTO> {
    private final DashboardRepository dashboardRepository;

    @Override
    @Async
    public CompletableFuture<MahasiswaStatisticsDTO> calculateStatistics(String mahasiswaId) {
        List<Lowongan> acceptedLowongan = dashboardRepository.findAcceptedLowonganByMahasiswaId(mahasiswaId);

        List<LowonganDTO> lowonganDTOList = acceptedLowongan.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        MahasiswaStatisticsDTO result = MahasiswaStatisticsDTO.builder()
                .openLowonganCount(dashboardRepository.countOpenLowongan())
                .acceptedLowonganCount(dashboardRepository.countAcceptedLowonganByMahasiswaId(mahasiswaId))
                .rejectedLowonganCount(dashboardRepository.countRejectedLowonganByMahasiswaId(mahasiswaId))
                .pendingLowonganCount(dashboardRepository.countPendingLowonganByMahasiswaId(mahasiswaId))
                .totalLogHours(dashboardRepository.calculateTotalLogHoursByMahasiswaId(mahasiswaId))
                .totalInsentif(dashboardRepository.calculateTotalInsentifByMahasiswaId(mahasiswaId))
                .acceptedLowonganList(lowonganDTOList)
                .build();

        return CompletableFuture.completedFuture(result);
    }

    private LowonganDTO convertToDTO(Lowongan lowongan) {
        return LowonganDTO.builder()
                .id(lowongan.getId().toString())
                .mataKuliahName(lowongan.getMataKuliah())
                .tahunAjaran(Integer.parseInt(lowongan.getTahunAjaran().split("/")[0]))
                .semester(lowongan.getSemester())
                .build();
    }
}