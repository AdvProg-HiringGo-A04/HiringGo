package id.ac.ui.cs.advprog.hiringgo.dashboard.service;

import id.ac.ui.cs.advprog.hiringgo.entity.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DashboardServiceTest {

    @Test
    void shouldDefineRequiredMethods() {
        // This test verifies that the interface contains all required methods
        Class<?> serviceClass = DashboardService.class;

        // Test getStatisticsForUser method
        assertDoesNotThrow(() -> serviceClass.getMethod("getStatisticsForUser", User.class));

        // Test role-specific methods
        assertDoesNotThrow(() -> serviceClass.getMethod("getAdminStatistics"));
        assertDoesNotThrow(() -> serviceClass.getMethod("getDosenStatistics", String.class));
        assertDoesNotThrow(() -> serviceClass.getMethod("getMahasiswaStatistics", String.class));
    }
}