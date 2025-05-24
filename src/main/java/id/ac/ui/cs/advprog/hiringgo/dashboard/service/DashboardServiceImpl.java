package id.ac.ui.cs.advprog.hiringgo.dashboard.service;

import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.AdminStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.DosenStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.LowonganDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.MahasiswaStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.repository.DashboardRepository;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.entity.Lowongan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final DashboardRepository dashboardRepository;

    @Override
    public Object getStatisticsForUser(User user) {
        validateUser(user);

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
        if (!StringUtils.hasText(dosenId)) {
            log.warn("Attempted to get dosen statistics with null or empty dosenId");
            return createEmptyDosenStatistics();
        }

        log.debug("Fetching dosen statistics for dosenId: {}", dosenId);
        return DosenStatisticsDTO.builder()
                .totalMataKuliah(dashboardRepository.countMataKuliahByDosenId(dosenId))
                .totalMahasiswaAssistant(dashboardRepository.countMahasiswaAssistantByDosenId(dosenId))
                .openLowonganCount(dashboardRepository.countOpenLowonganByDosenId(dosenId))
                .build();
    }

    @Override
    public MahasiswaStatisticsDTO getMahasiswaStatistics(String mahasiswaId) {
        if (!StringUtils.hasText(mahasiswaId)) {
            log.warn("Attempted to get mahasiswa statistics with null or empty mahasiswaId");
            return createEmptyMahasiswaStatistics();
        }

        log.debug("Fetching mahasiswa statistics for mahasiswaId: {}", mahasiswaId);

        List<Lowongan> acceptedLowongan = dashboardRepository.findAcceptedLowonganByMahasiswaId(mahasiswaId);
        List<LowonganDTO> lowonganDTOList = convertLowonganListToDTO(acceptedLowongan);

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

    private void validateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (user.getRole() == null) {
            throw new IllegalArgumentException("User role cannot be null");
        }
    }

    private DosenStatisticsDTO createEmptyDosenStatistics() {
        return DosenStatisticsDTO.builder()
                .totalMataKuliah(0L)
                .totalMahasiswaAssistant(0L)
                .openLowonganCount(0L)
                .build();
    }

    private MahasiswaStatisticsDTO createEmptyMahasiswaStatistics() {
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

    private List<LowonganDTO> convertLowonganListToDTO(List<Lowongan> lowonganList) {
        if (lowonganList == null || lowonganList.isEmpty()) {
            return Collections.emptyList();
        }

        return lowonganList.stream()
                .map(this::convertLowonganToDTO)
                .filter(dto -> dto != null) // Filter out any null DTOs from conversion errors
                .toList();
    }

    private LowonganDTO convertLowonganToDTO(Lowongan lowongan) {
        if (lowongan == null) {
            log.warn("Attempted to convert null Lowongan to DTO");
            return null;
        }

        try {
            String tahunAjaranStr = lowongan.getTahunAjaran();
            int tahunAjaran = 0;

            if (StringUtils.hasText(tahunAjaranStr) && tahunAjaranStr.contains("/")) {
                tahunAjaran = Integer.parseInt(tahunAjaranStr.split("/")[0]);
            }

            return LowonganDTO.builder()
                    .id(lowongan.getId() != null ? lowongan.getId().toString() : "")
                    .mataKuliahName(lowongan.getMataKuliah() != null ? lowongan.getMataKuliah() : "")
                    .tahunAjaran(tahunAjaran)
                    .semester(lowongan.getSemester() != null ? lowongan.getSemester() : "")
                    .build();
        } catch (Exception e) {
            log.error("Error converting Lowongan to DTO: {}", e.getMessage());
            return null;
        }
    }
}