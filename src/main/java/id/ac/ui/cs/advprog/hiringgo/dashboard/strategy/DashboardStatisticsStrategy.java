package id.ac.ui.cs.advprog.hiringgo.dashboard.strategy;

import java.util.concurrent.CompletableFuture;

public interface DashboardStatisticsStrategy<T> {
    CompletableFuture<T> calculateStatistics(String userId);
}