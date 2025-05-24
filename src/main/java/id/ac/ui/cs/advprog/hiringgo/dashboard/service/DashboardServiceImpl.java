package id.ac.ui.cs.advprog.hiringgo.dashboard.service;

import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.entity.Role;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.AdminStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.DosenStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.MahasiswaStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.repository.DashboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final DashboardRepository dashboardRepository;

    @Override
    public Object getStatisticsForUser(User user) {
        if (user == null || user.getRole() == null) {
            throw new IllegalArgumentException("User and user role cannot be null");
        }

        switch (user.getRole()) {
            case ADMIN:
                return getAdminStatistics();
            case DOSEN:
                return getDosenStatistics(user.getId());
            case MAHASISWA:
                return getMahasiswaStatistics(user.getId());
            default:
                throw new IllegalArgumentException("Unknown user role: " + user.getRole());
        }
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
    public DosenStatisticsDTO getDosenStatistics(String dosenId) {
        return DosenStatisticsDTO.builder()
                .totalMataKuliah(dashboardRepository.countMataKuliahByDosenId(dosenId))
                .totalMahasiswaAssistant(dashboardRepository.countMahasiswaAssistantByDosenId(dosenId))
                .openLowonganCount(dashboardRepository.countOpenLowonganByDosenId(dosenId))
                .build();
    }

    @Override
    public MahasiswaStatisticsDTO getMahasiswaStatistics(String mahasiswaId) {
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
                                    .id(lowongan.getId().toString())
                                    .mataKuliahName(lowongan.getMataKuliah())
                                    .tahunAjaran(Integer.parseInt(lowongan.getTahunAjaran().split("/")[0]))
                                    .semester(lowongan.getSemester())
                                    .build();
                        })
                        .toList())
                .build();
    }
}