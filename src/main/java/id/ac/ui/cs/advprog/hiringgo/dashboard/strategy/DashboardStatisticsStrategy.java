// src/main/java/id/ac/ui/cs/advprog/hiringgo/dashboard/strategy/DashboardStatisticsStrategy.java
package id.ac.ui.cs.advprog.hiringgo.dashboard.strategy;

// Strategy interface for Dashboard statistics calculation
public interface DashboardStatisticsStrategy<T> {
    T calculateStatistics(Long userId);
}