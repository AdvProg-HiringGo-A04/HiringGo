// src/main/java/id/ac/ui/cs/advprog/hiringgo/dashboard/strategy/MahasiswaDashboardStrategy.java
package id.ac.ui.cs.advprog.hiringgo.dashboard.strategy;

import id.ac.ui.cs.advprog.hiringgo.common.model.Lowongan;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.LowonganDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.MahasiswaStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.repository.DashboardRepository;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MahasiswaDashboardStrategy implements DashboardStatisticsStrategy<MahasiswaStatisticsDTO> {
    private final DashboardRepository dashboardRepository;

    @Override
    public MahasiswaStatisticsDTO calculateStatistics(Long mahasiswaId) {
        List<Lowongan> acceptedLowongan = dashboardRepository.findAcceptedLowonganByMahasiswaId(mahasiswaId);

        List<LowonganDTO> lowonganDTOList = acceptedLowongan.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return MahasiswaStatisticsDTO.builder()
                .openLowonganCount(dashboardRepository.countOpenLowongan())
                .acceptedLowonganCount(dashboardRepository.countAcceptedLowonganByMahasiswaId(mahasiswaId))
                .rejectedLowonganCount(dashboardRepository.countRejectedLowonganByMahasiswaId(mahasiswaId))
                .pendingLowonganCount(dashboardRepository.countPendingLowonganByMahasiswaId(mahasiswaId))
                .totalLogHours(dashboardRepository.calculateTotalLogHoursByMahasiswaId(mahasiswaId))
                .totalInsentif(dashboardRepository.calculateTotalInsentifByMahasiswaId(mahasiswaId))
                .acceptedLowonganList(lowonganDTOList)
                .build();
    }

    private LowonganDTO convertToDTO(Lowongan lowongan) {
        return LowonganDTO.builder()
                .id(lowongan.getId())
                .mataKuliahName(lowongan.getMataKuliah().getName())
                .mataKuliahCode(lowongan.getMataKuliah().getCode())
                .tahunAjaran(lowongan.getTahunAjaran())
                .semester(lowongan.getSemester())
                .build();
    }
}