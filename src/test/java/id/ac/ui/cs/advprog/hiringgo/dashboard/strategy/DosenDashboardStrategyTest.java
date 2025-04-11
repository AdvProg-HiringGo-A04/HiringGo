// src/test/java/id/ac/ui/cs/advprog/hiringgo/dashboard/strategy/DosenDashboardStrategyTest.java
package id.ac.ui.cs.advprog.hiringgo.dashboard.strategy;

import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.DosenStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.repository.DashboardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void calculateStatistics_ShouldReturnCorrectStatistics() {
        // Arrange
        Long dosenId = 1L;
        when(dashboardRepository.countMataKuliahByDosenId(dosenId)).thenReturn(5L);
        when(dashboardRepository.countMahasiswaAssistantByDosenId(dosenId)).thenReturn(25L);
        when(dashboardRepository.countOpenLowonganByDosenId(dosenId)).thenReturn(3L);

        // Act
        DosenStatisticsDTO result = strategy.calculateStatistics(dosenId);

        // Assert
        assertEquals(5L, result.getTotalMataKuliah());
        assertEquals(25L, result.getTotalMahasiswaAssistant());
        assertEquals(3L, result.getOpenLowonganCount());
    }
}