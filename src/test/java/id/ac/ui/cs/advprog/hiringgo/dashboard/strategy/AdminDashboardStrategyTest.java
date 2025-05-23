package id.ac.ui.cs.advprog.hiringgo.dashboard.strategy;

import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.AdminStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.repository.DashboardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class AdminDashboardStrategyTest {

    @Mock
    private DashboardRepository dashboardRepository;

    private AdminDashboardStrategy strategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        strategy = new AdminDashboardStrategy(dashboardRepository);
    }

    @Test
    void calculateStatistics_ShouldReturnCorrectStatistics() throws ExecutionException, InterruptedException {
        // Arrange
        when(dashboardRepository.countDosenUsers()).thenReturn(10L);
        when(dashboardRepository.countMahasiswaUsers()).thenReturn(100L);
        when(dashboardRepository.countMataKuliah()).thenReturn(20L);
        when(dashboardRepository.countLowongan()).thenReturn(30L);

        // Act
        CompletableFuture<AdminStatisticsDTO> futureResult = strategy.calculateStatistics("1L"); // userId doesn't matter for admin
        AdminStatisticsDTO result = futureResult.get(); // Wait for async result

        // Assert
        assertEquals(10L, result.getTotalDosen());
        assertEquals(100L, result.getTotalMahasiswa());
        assertEquals(20L, result.getTotalMataKuliah());
        assertEquals(30L, result.getTotalLowongan());
    }
}