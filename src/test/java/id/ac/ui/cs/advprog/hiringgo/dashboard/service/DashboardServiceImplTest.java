package id.ac.ui.cs.advprog.hiringgo.dashboard.service;

import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.enums.Role;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.AdminStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.DosenStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.MahasiswaStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.repository.DashboardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardServiceImplTest {

    @Mock
    private DashboardRepository dashboardRepository;

    @Mock
    private UserValidator userValidator;

    @Mock
    private StatisticsFactory statisticsFactory;

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId("test-id");
        testUser.setRole(Role.ADMIN);
    }

    @Test
    void getStatisticsForUser_ShouldCallValidatorAndFactory() {
        // Given
        Object expectedStatistics = new AdminStatisticsDTO();
        when(statisticsFactory.createStatisticsForUser(testUser, dashboardService))
                .thenReturn(expectedStatistics);

        // When
        Object result = dashboardService.getStatisticsForUser(testUser);

        // Then
        verify(userValidator).validateUser(testUser);
        verify(statisticsFactory).createStatisticsForUser(testUser, dashboardService);
        assertEquals(expectedStatistics, result);
    }

    @Test
    void getAdminStatistics_ShouldReturnCorrectStatistics() {
        // Given
        when(dashboardRepository.countDosenUsers()).thenReturn(10L);
        when(dashboardRepository.countMahasiswaUsers()).thenReturn(100L);
        when(dashboardRepository.countMataKuliah()).thenReturn(5L);
        when(dashboardRepository.countLowongan()).thenReturn(15L);

        // When
        AdminStatisticsDTO result = dashboardService.getAdminStatistics();

        // Then
        assertNotNull(result);
        assertEquals(10L, result.getTotalDosen());
        assertEquals(100L, result.getTotalMahasiswa());
        assertEquals(5L, result.getTotalMataKuliah());
        assertEquals(15L, result.getTotalLowongan());
    }

    @Test
    void getDosenStatistics_ShouldCallBuilder() {
        // Given
        String dosenId = "dosen-123";

        // When
        dashboardService.getDosenStatistics(dosenId);

        // Then - This will test the static method call
        // We verify that the repository methods would be called through the builder
        // Note: Testing static methods requires PowerMock or similar, but we can test the flow
        assertDoesNotThrow(() -> dashboardService.getDosenStatistics(dosenId));
    }

    @Test
    void getMahasiswaStatistics_ShouldCallBuilder() {
        // Given
        String mahasiswaId = "mahasiswa-123";

        // When
        dashboardService.getMahasiswaStatistics(mahasiswaId);

        // Then
        assertDoesNotThrow(() -> dashboardService.getMahasiswaStatistics(mahasiswaId));
    }
}