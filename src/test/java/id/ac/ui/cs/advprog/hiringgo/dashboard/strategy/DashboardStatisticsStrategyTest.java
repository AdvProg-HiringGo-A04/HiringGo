// src/test/java/id/ac/ui/cs/advprog/hiringgo/dashboard/strategy/DashboardStatisticsStrategyTest.java
package id.ac.ui.cs.advprog.hiringgo.dashboard.strategy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DashboardStatisticsStrategyTest {

    @Test
    void interfaceShouldDefineCalculateStatisticsMethod() {
        // This is a compile-time test
        // If the interface doesn't define the calculateStatistics method,
        // the test class won't compile

        // We create a simple implementation to verify the interface
        DashboardStatisticsStrategy<String> testStrategy = new DashboardStatisticsStrategy<String>() {
            @Override
            public String calculateStatistics(String userId) {
                return "Test result";
            }
        };

        // Verify the implementation works
        String result = testStrategy.calculateStatistics("1L");
        assertEquals("Test result", result);
    }
}