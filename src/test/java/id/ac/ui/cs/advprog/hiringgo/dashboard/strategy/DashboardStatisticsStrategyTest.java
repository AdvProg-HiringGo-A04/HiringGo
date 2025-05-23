package id.ac.ui.cs.advprog.hiringgo.dashboard.strategy;

import org.junit.jupiter.api.Test;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import static org.junit.jupiter.api.Assertions.*;

class DashboardStatisticsStrategyTest {

    @Test
    void interfaceShouldDefineCalculateStatisticsMethod() throws ExecutionException, InterruptedException {
        // This is a compile-time test
        // If the interface doesn't define the calculateStatistics method,
        // the test class won't compile

        // We create a simple implementation to verify the interface
        DashboardStatisticsStrategy<String> testStrategy = new DashboardStatisticsStrategy<String>() {
            @Override
            public CompletableFuture<String> calculateStatistics(String userId) {
                return CompletableFuture.completedFuture("Test result");
            }
        };

        // Verify the implementation works
        CompletableFuture<String> futureResult = testStrategy.calculateStatistics("1L");
        String result = futureResult.get(); // Wait for the async result
        assertEquals("Test result", result);
    }
}