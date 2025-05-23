package id.ac.ui.cs.advprog.hiringgo.dashboard.strategy;

import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.DosenStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.repository.DashboardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class DosenDashboardStrategyTest {

    @Mock
    private DashboardRepository dashboardRepository;

    private DosenDashboardStrategy strategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        strategy = new DosenDashboardStrategy(dashboardRepository);
    }

    @Test
    void calculateStatistics_ShouldReturnCorrectStatistics() throws ExecutionException, InterruptedException {
        // Arrange
        String dosenId = "1";
        when(dashboardRepository.countMataKuliahByDosenId(dosenId)).thenReturn(5L);
        when(dashboardRepository.countMahasiswaAssistantByDosenId(dosenId)).thenReturn(10L);
        when(dashboardRepository.countOpenLowonganByDosenId(dosenId)).thenReturn(3L);

        // Act
        CompletableFuture<DosenStatisticsDTO> futureResult = strategy.calculateStatistics(dosenId);
        DosenStatisticsDTO result = futureResult.get(); // Wait for async result

        // Assert
        assertEquals(5L, result.getTotalMataKuliah());
        assertEquals(10L, result.getTotalMahasiswaAssistant());
        assertEquals(3L, result.getOpenLowonganCount());
    }
}