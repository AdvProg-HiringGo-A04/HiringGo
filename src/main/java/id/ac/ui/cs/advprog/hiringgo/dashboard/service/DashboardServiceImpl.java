package id.ac.ui.cs.advprog.hiringgo.dashboard.service;

import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.AdminStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.DosenStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.MahasiswaStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.repository.DashboardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final DashboardRepository dashboardRepository;
    private final UserValidator userValidator;
    private final StatisticsFactory statisticsFactory;

    @Override
    public Object getStatisticsForUser(User user) {
        userValidator.validateUser(user);
        return statisticsFactory.createStatisticsForUser(user, this);
    }

    @Override
    public AdminStatisticsDTO getAdminStatistics() {
        log.debug("Fetching admin statistics");
        return AdminStatisticsDTO.builder()
                .totalDosen(dashboardRepository.countDosenUsers())
                .totalMahasiswa(dashboardRepository.countMahasiswaUsers())
                .totalMataKuliah(dashboardRepository.countMataKuliah())
                .totalLowongan(dashboardRepository.countLowongan())
                .build();
    }

    @Override
    public DosenStatisticsDTO getDosenStatistics(String dosenId) {
        return DosenStatisticsBuilder.build(dosenId, dashboardRepository, log);
    }

    @Override
    public MahasiswaStatisticsDTO getMahasiswaStatistics(String mahasiswaId) {
        return MahasiswaStatisticsBuilder.build(mahasiswaId, dashboardRepository, log);
    }
}