package id.ac.ui.cs.advprog.hiringgo.dashboard.service;

import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.enums.Role;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.AdminStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.DosenStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.MahasiswaStatisticsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatisticsFactoryTest {

    private StatisticsFactory statisticsFactory;

    @Mock
    private DashboardService dashboardService;

    @BeforeEach
    void setUp() {
        statisticsFactory = new StatisticsFactory();
    }

    @Test
    void createStatisticsForUser_WithAdminRole_ShouldReturnAdminStatistics() {
        // Given
        User user = new User();
        user.setId("admin-123");
        user.setRole(Role.ADMIN);
        AdminStatisticsDTO expectedStats = new AdminStatisticsDTO();
        when(dashboardService.getAdminStatistics()).thenReturn(expectedStats);

        // When
        Object result = statisticsFactory.createStatisticsForUser(user, dashboardService);

        // Then
        assertEquals(expectedStats, result);
        verify(dashboardService).getAdminStatistics();
    }

    @Test
    void createStatisticsForUser_WithDosenRole_ShouldReturnDosenStatistics() {
        // Given
        User user = new User();
        user.setId("dosen-123");
        user.setRole(Role.DOSEN);
        DosenStatisticsDTO expectedStats = new DosenStatisticsDTO();
        when(dashboardService.getDosenStatistics("dosen-123")).thenReturn(expectedStats);

        // When
        Object result = statisticsFactory.createStatisticsForUser(user, dashboardService);

        // Then
        assertEquals(expectedStats, result);
        verify(dashboardService).getDosenStatistics("dosen-123");
    }

    @Test
    void createStatisticsForUser_WithMahasiswaRole_ShouldReturnMahasiswaStatistics() {
        // Given
        User user = new User();
        user.setId("mahasiswa-123");
        user.setRole(Role.MAHASISWA);
        MahasiswaStatisticsDTO expectedStats = new MahasiswaStatisticsDTO();
        when(dashboardService.getMahasiswaStatistics("mahasiswa-123")).thenReturn(expectedStats);

        // When
        Object result = statisticsFactory.createStatisticsForUser(user, dashboardService);

        // Then
        assertEquals(expectedStats, result);
        verify(dashboardService).getMahasiswaStatistics("mahasiswa-123");
    }

    @Test
    void createStatisticsForUser_WithUnknownRole_ShouldThrowException() {
        // Given
        User user = new User();
        user.setId("unknown-123");
        // Assuming there's an UNKNOWN role or we can mock it
        user.setRole(null); // This will cause default case

        // When & Then
        assertThrows(NullPointerException.class,
                () -> statisticsFactory.createStatisticsForUser(user, dashboardService));
    }
}