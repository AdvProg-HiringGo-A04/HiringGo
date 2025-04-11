package id.ac.ui.cs.advprog.hiringgo.dashboard.service;

import id.ac.ui.cs.advprog.hiringgo.common.model.User;
import id.ac.ui.cs.advprog.hiringgo.common.model.UserRole;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.AdminStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.repository.DashboardRepository;
import id.ac.ui.cs.advprog.hiringgo.dashboard.strategy.AdminDashboardStrategy;
import id.ac.ui.cs.advprog.hiringgo.dashboard.strategy.DashboardStatisticsStrategy;
import id.ac.ui.cs.advprog.hiringgo.dashboard.strategy.DashboardStrategyFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class DashboardServiceImplTest {

    @Mock
    private DashboardRepository dashboardRepository;

    @Mock
    private DashboardStrategyFactory dashboardStrategyFactory;

    @Mock
    private DashboardStatisticsStrategy<AdminStatisticsDTO> adminStrategy;

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAdminStatistics_ShouldReturnCorrectStatistics() {
        // Arrange
        when(dashboardRepository.countDosenUsers()).thenReturn(10L);
        when(dashboardRepository.countMahasiswaUsers()).thenReturn(100L);
        when(dashboardRepository.countMataKuliah()).thenReturn(20L);
        when(dashboardRepository.countLowongan()).thenReturn(30L);

        // Act
        AdminStatisticsDTO result = dashboardService.getAdminStatistics();

        // Assert
        assertEquals(10L, result.getTotalDosen());
        assertEquals(100L, result.getTotalMahasiswa());
        assertEquals(20L, result.getTotalMataKuliah());
        assertEquals(30L, result.getTotalLowongan());
    }

    @Test
    void getStatisticsForUser_WhenUserIsAdmin_ShouldReturnAdminStatistics() {
        // Arrange
        User adminUser = User.builder()
                .id(1L)
                .role(UserRole.ADMIN)
                .build();

        AdminStatisticsDTO expectedStats = AdminStatisticsDTO.builder()
                .totalDosen(10L)
                .totalMahasiswa(100L)
                .totalMataKuliah(20L)
                .totalLowongan(30L)
                .build();

        when(dashboardStrategyFactory.getStrategy(adminUser)).thenReturn(adminStrategy);
        when(adminStrategy.calculateStatistics(adminUser.getId())).thenReturn(expectedStats);

        // Act
        Object result = dashboardService.getStatisticsForUser(adminUser);

        // Assert
        assertEquals(expectedStats, result);
    }
}