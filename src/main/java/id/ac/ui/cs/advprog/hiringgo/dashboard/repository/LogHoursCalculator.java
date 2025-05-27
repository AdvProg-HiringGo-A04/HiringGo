package id.ac.ui.cs.advprog.hiringgo.dashboard.repository;

import org.slf4j.Logger;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

public class LogHoursCalculator {

    public static final double HOURLY_RATE = 27500.0;
    private static final double MINUTES_PER_HOUR = 60.0;

    public double calculateTotalHours(List<Object[]> logs, Logger log) {
        if (logs == null || logs.isEmpty()) {
            return 0.0;
        }

        double totalHours = 0.0;
        for (Object[] entry : logs) {
            if (entry != null && entry.length >= 2 && entry[0] != null && entry[1] != null) {
                try {
                    LocalTime start = (LocalTime) entry[0];
                    LocalTime end = (LocalTime) entry[1];
                    Duration duration = Duration.between(start, end);
                    totalHours += duration.toMinutes() / MINUTES_PER_HOUR;
                } catch (Exception e) {
                    log.warn("Error calculating duration for log entry: {}", e.getMessage());
                }
            }
        }
        return totalHours;
    }
}