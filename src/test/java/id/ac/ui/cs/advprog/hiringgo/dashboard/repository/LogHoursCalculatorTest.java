package id.ac.ui.cs.advprog.hiringgo.dashboard.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LogHoursCalculatorTest {

    private LogHoursCalculator calculator;

    @Mock
    private Logger log;

    @BeforeEach
    void setUp() {
        calculator = new LogHoursCalculator();
    }

    @Test
    void calculateTotalHours_WithValidLogs_ShouldCalculateCorrectly() {
        // Given
        List<Object[]> logs = Arrays.asList(
                new Object[]{LocalTime.of(9, 0), LocalTime.of(11, 0)}, // 2 hours
                new Object[]{LocalTime.of(13, 0), LocalTime.of(14, 30)} // 1.5 hours
        );

        // When
        double result = calculator.calculateTotalHours(logs, log);

        // Then
        assertEquals(3.5, result, 0.01);
    }

    @Test
    void calculateTotalHours_WithNullList_ShouldReturnZero() {
        // When
        double result = calculator.calculateTotalHours(null, log);

        // Then
        assertEquals(0.0, result);
    }

    @Test
    void calculateTotalHours_WithEmptyList_ShouldReturnZero() {
        // When
        double result = calculator.calculateTotalHours(Collections.emptyList(), log);

        // Then
        assertEquals(0.0, result);
    }

    @Test
    void calculateTotalHours_WithNullEntry_ShouldSkipEntry() {
        // Given
        List<Object[]> logs = Arrays.asList(
                new Object[]{LocalTime.of(9, 0), LocalTime.of(11, 0)}, // 2 hours
                null, // should be skipped
                new Object[]{LocalTime.of(13, 0), LocalTime.of(14, 0)} // 1 hour
        );

        // When
        double result = calculator.calculateTotalHours(logs, log);

        // Then
        assertEquals(3.0, result, 0.01);
    }

    @Test
    void calculateTotalHours_WithIncompleteEntry_ShouldSkipEntry() {
        // Given
        List<Object[]> logs = Arrays.asList(
                new Object[]{LocalTime.of(9, 0), LocalTime.of(11, 0)}, // 2 hours
                new Object[]{LocalTime.of(13, 0)}, // incomplete entry
                new Object[]{LocalTime.of(14, 0), LocalTime.of(15, 0)} // 1 hour
        );

        // When
        double result = calculator.calculateTotalHours(logs, log);

        // Then
        assertEquals(3.0, result, 0.01);
    }

    @Test
    void calculateTotalHours_WithNullTimeValues_ShouldSkipEntry() {
        // Given
        List<Object[]> logs = Arrays.asList(
                new Object[]{LocalTime.of(9, 0), LocalTime.of(11, 0)}, // 2 hours
                new Object[]{null, LocalTime.of(11, 0)}, // null start time
                new Object[]{LocalTime.of(13, 0), null}, // null end time
                new Object[]{LocalTime.of(14, 0), LocalTime.of(15, 0)} // 1 hour
        );

        // When
        double result = calculator.calculateTotalHours(logs, log);

        // Then
        assertEquals(3.0, result, 0.01);
    }

    @Test
    void calculateTotalHours_WithExceptionInCalculation_ShouldLogAndContinue() {
        // Given - Creating an entry that might cause an exception
        List<Object[]> logs = Arrays.asList(
                new Object[]{LocalTime.of(9, 0), LocalTime.of(11, 0)}, // 2 hours
                new Object[]{"invalid", "invalid"}, // This will cause ClassCastException
                new Object[]{LocalTime.of(14, 0), LocalTime.of(15, 0)} // 1 hour
        );

        // When
        double result = calculator.calculateTotalHours(logs, log);

        // Then
        assertEquals(3.0, result, 0.01);
        verify(log).warn(contains("Error calculating duration for log entry:"), Optional.ofNullable(any()));
    }

    @Test
    void hourlyRate_ShouldBeCorrect() {
        // Then
        assertEquals(27500.0, LogHoursCalculator.HOURLY_RATE);
    }
}