package id.ac.ui.cs.advprog.hiringgo.dashboard.service;

import id.ac.ui.cs.advprog.hiringgo.common.model.User;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.AdminStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.DosenStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.MahasiswaStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.repository.DashboardRepository;
import id.ac.ui.cs.advprog.hiringgo.dashboard.strategy.DashboardStatisticsStrategy;
import id.ac.ui.cs.advprog.hiringgo.dashboard.strategy.DashboardStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final DashboardRepository dashboardRepository;
    private final DashboardStrategyFactory dashboardStrategyFactory;

    @Override
    public Object getStatisticsForUser(User user) {
        DashboardStatisticsStrategy<?> strategy = dashboardStrategyFactory.getStrategy(user);
        return strategy.calculateStatistics(user.getId());
    }

    @Override
    public AdminStatisticsDTO getAdminStatistics() {
        return AdminStatisticsDTO.builder()
                .totalDosen(dashboardRepository.countDosenUsers())
                .totalMahasiswa(dashboardRepository.countMahasiswaUsers())
                .totalMataKuliah(dashboardRepository.countMataKuliah())
                .totalLowongan(dashboardRepository.countLowongan())
                .build();
    }

    @Override
    public DosenStatisticsDTO getDosenStatistics(Long dosenId) {
        return DosenStatisticsDTO.builder()
                .totalMataKuliah(dashboardRepository.countMataKuliahByDosenId(dosenId))
                .totalMahasiswaAssistant(dashboardRepository.countMahasiswaAssistantByDosenId(dosenId))
                .openLowonganCount(dashboardRepository.countOpenLowonganByDosenId(dosenId))
                .build();
    }

    @Override
    public MahasiswaStatisticsDTO getMahasiswaStatistics(Long mahasiswaId) {
        return MahasiswaStatisticsDTO.builder()
                .openLowonganCount(dashboardRepository.countOpenLowongan())
                .acceptedLowonganCount(dashboardRepository.countAcceptedLowonganByMahasiswaId(mahasiswaId))
                .rejectedLowonganCount(dashboardRepository.countRejectedLowonganByMahasiswaId(mahasiswaId))
                .pendingLowonganCount(dashboardRepository.countPendingLowonganByMahasiswaId(mahasiswaId))
                .totalLogHours(dashboardRepository.calculateTotalLogHoursByMahasiswaId(mahasiswaId))
                .totalInsentif(dashboardRepository.calculateTotalInsentifByMahasiswaId(mahasiswaId))
                .acceptedLowonganList(dashboardRepository.findAcceptedLowonganByMahasiswaId(mahasiswaId)
                        .stream()
                        .map(lowongan -> {
                            return id.ac.ui.cs.advprog.hiringgo.dashboard.dto.LowonganDTO.builder()
                                    .id(lowongan.getId())
                                    .mataKuliahName(lowongan.getMataKuliah().getName())
                                    .mataKuliahCode(lowongan.getMataKuliah().getCode())
                                    .tahunAjaran(lowongan.getTahunAjaran())
                                    .semester(lowongan.getSemester())
                                    .build();
                        })
                        .toList())
                .build();
    }
}