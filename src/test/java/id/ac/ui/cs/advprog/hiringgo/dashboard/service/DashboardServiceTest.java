package id.ac.ui.cs.advprog.hiringgo.dashboard.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DashboardServiceTest {

    @Test
    void shouldDefineRequiredMethods() {
        // This test verifies that the interface contains all required methods

        Class<?> serviceClass = DashboardService.class;

        // Test getStatisticsForUser method
        assertDoesNotThrow(() -> serviceClass.getMethod("getStatisticsForUser", id.ac.ui.cs.advprog.hiringgo.common.model.User.class));

        // Test role-specific methods
        assertDoesNotThrow(() -> serviceClass.getMethod("getAdminStatistics"));
        assertDoesNotThrow(() -> serviceClass.getMethod("getDosenStatistics", Long.class));
        assertDoesNotThrow(() -> serviceClass.getMethod("getMahasiswaStatistics", Long.class));
    }
}