package id.ac.ui.cs.advprog.hiringgo.dashboard.repository;

import id.ac.ui.cs.advprog.hiringgo.enums.Role;
import id.ac.ui.cs.advprog.hiringgo.entity.Lowongan;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.enums.StatusLog;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class DashboardRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query query;

    @Mock
    private TypedQuery<Lowongan> typedQuery;

    @InjectMocks
    private DashboardRepositoryImpl dashboardRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void countDosenUsers_ShouldReturnCorrectCount() {
        // Arrange
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(eq("role"), eq(Role.DOSEN))).thenReturn(query);
        when(query.getSingleResult()).thenReturn(10L);

        // Act
        long result = dashboardRepository.countDosenUsers();

        // Assert
        assertEquals(10L, result);
    }

    @Test
    void countDosenUsers_WhenExceptionThrown_ShouldReturnZero() {
        // Arrange
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(eq("role"), eq(Role.DOSEN))).thenReturn(query);
        when(query.getSingleResult()).thenThrow(new RuntimeException("Database error"));

        // Act
        long result = dashboardRepository.countDosenUsers();

        // Assert
        assertEquals(0L, result);
    }

    @Test
    void countDosenUsers_WhenResultIsNull_ShouldReturnZero() {
        // Arrange
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(eq("role"), eq(Role.DOSEN))).thenReturn(query);
        when(query.getSingleResult()).thenReturn(null);

        // Act
        long result = dashboardRepository.countDosenUsers();

        // Assert
        assertEquals(0L, result);
    }

    @Test
    void countMahasiswaUsers_ShouldReturnCorrectCount() {
        // Arrange
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(eq("role"), eq(Role.MAHASISWA))).thenReturn(query);
        when(query.getSingleResult()).thenReturn(100L);

        // Act
        long result = dashboardRepository.countMahasiswaUsers();

        // Assert
        assertEquals(100L, result);
    }

    @Test
    void countMahasiswaUsers_WhenExceptionThrown_ShouldReturnZero() {
        // Arrange
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(eq("role"), eq(Role.MAHASISWA))).thenReturn(query);
        when(query.getSingleResult()).thenThrow(new RuntimeException("Database error"));

        // Act
        long result = dashboardRepository.countMahasiswaUsers();

        // Assert
        assertEquals(0L, result);
    }

    @Test
    void countMataKuliah_ShouldReturnCorrectCount() {
        // Arrange
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(20L);

        // Act
        long result = dashboardRepository.countMataKuliah();

        // Assert
        assertEquals(20L, result);
    }

    @Test
    void countMataKuliah_WhenExceptionThrown_ShouldReturnZero() {
        // Arrange
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.getSingleResult()).thenThrow(new RuntimeException("Database error"));

        // Act
        long result = dashboardRepository.countMataKuliah();

        // Assert
        assertEquals(0L, result);
    }

    @Test
    void countMataKuliah_WhenResultIsNull_ShouldReturnZero() {
        // Arrange
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(null);

        // Act
        long result = dashboardRepository.countMataKuliah();

        // Assert
        assertEquals(0L, result);
    }

    @Test
    void countLowongan_ShouldReturnCorrectCount() {
        // Arrange
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(30L);

        // Act
        long result = dashboardRepository.countLowongan();

        // Assert
        assertEquals(30L, result);
    }

    @Test
    void countLowongan_WhenExceptionThrown_ShouldReturnZero() {
        // Arrange
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.getSingleResult()).thenThrow(new RuntimeException("Database error"));

        // Act
        long result = dashboardRepository.countLowongan();

        // Assert
        assertEquals(0L, result);
    }

    @Test
    void countMataKuliahByDosenId_ShouldReturnCorrectCount() {
        // Arrange
        String dosenId = "1";
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(eq("dosenId"), eq(dosenId))).thenReturn(query);
        when(query.getSingleResult()).thenReturn(5L);

        // Act
        long result = dashboardRepository.countMataKuliahByDosenId(dosenId);

        // Assert
        assertEquals(5L, result);
    }

    @Test
    void countMataKuliahByDosenId_WhenDosenIdIsNull_ShouldReturnZero() {
        // Act
        long result = dashboardRepository.countMataKuliahByDosenId(null);

        // Assert
        assertEquals(0L, result);
    }

    @Test
    void countMataKuliahByDosenId_WhenDosenIdIsEmpty_ShouldReturnZero() {
        // Act
        long result = dashboardRepository.countMataKuliahByDosenId("");

        // Assert
        assertEquals(0L, result);
    }

    @Test
    void countMataKuliahByDosenId_WhenDosenIdIsBlank_ShouldReturnZero() {
        // Act
        long result = dashboardRepository.countMataKuliahByDosenId("   ");

        // Assert
        assertEquals(0L, result);
    }

    @Test
    void countMataKuliahByDosenId_WhenExceptionThrown_ShouldReturnZero() {
        // Arrange
        String dosenId = "1";
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(eq("dosenId"), eq(dosenId))).thenReturn(query);
        when(query.getSingleResult()).thenThrow(new RuntimeException("Database error"));

        // Act
        long result = dashboardRepository.countMataKuliahByDosenId(dosenId);

        // Assert
        assertEquals(0L, result);
    }

    @Test
    void countMahasiswaAssistantByDosenId_ShouldReturnCorrectCount() {
        // Arrange
        String dosenId = "1";
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(eq("dosenId"), eq(dosenId))).thenReturn(query);
        when(query.getSingleResult()).thenReturn(15L);

        // Act
        long result = dashboardRepository.countMahasiswaAssistantByDosenId(dosenId);

        // Assert
        assertEquals(15L, result);
    }

    @Test
    void countMahasiswaAssistantByDosenId_WhenDosenIdIsNull_ShouldReturnZero() {
        // Act
        long result = dashboardRepository.countMahasiswaAssistantByDosenId(null);

        // Assert
        assertEquals(0L, result);
    }

    @Test
    void countMahasiswaAssistantByDosenId_WhenDosenIdIsEmpty_ShouldReturnZero() {
        // Act
        long result = dashboardRepository.countMahasiswaAssistantByDosenId("");

        // Assert
        assertEquals(0L, result);
    }

    @Test
    void countOpenLowonganByDosenId_ShouldReturnCorrectCount() {
        // Arrange
        String dosenId = "1";
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(eq("dosenId"), eq(dosenId))).thenReturn(query);
        when(query.getSingleResult()).thenReturn(3L);

        // Act
        long result = dashboardRepository.countOpenLowonganByDosenId(dosenId);

        // Assert
        assertEquals(3L, result);
    }

    @Test
    void countOpenLowonganByDosenId_WhenDosenIdIsNull_ShouldReturnZero() {
        // Act
        long result = dashboardRepository.countOpenLowonganByDosenId(null);

        // Assert
        assertEquals(0L, result);
    }

    @Test
    void countOpenLowonganByDosenId_WhenDosenIdIsEmpty_ShouldReturnZero() {
        // Act
        long result = dashboardRepository.countOpenLowonganByDosenId("");

        // Assert
        assertEquals(0L, result);
    }

    @Test
    void countOpenLowongan_ShouldReturnCorrectCount() {
        // Arrange
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(8L);

        // Act
        long result = dashboardRepository.countOpenLowongan();

        // Assert
        assertEquals(8L, result);
    }

    @Test
    void countOpenLowongan_WhenExceptionThrown_ShouldReturnZero() {
        // Arrange
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.getSingleResult()).thenThrow(new RuntimeException("Database error"));

        // Act
        long result = dashboardRepository.countOpenLowongan();

        // Assert
        assertEquals(0L, result);
    }

    @Test
    void countAcceptedLowonganByMahasiswaId_ShouldReturnCorrectCount() {
        // Arrange
        String mahasiswaId = "1";
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(eq("mahasiswaId"), eq(mahasiswaId))).thenReturn(query);
        when(query.getSingleResult()).thenReturn(5L);

        // Act
        long result = dashboardRepository.countAcceptedLowonganByMahasiswaId(mahasiswaId);

        // Assert
        assertEquals(5L, result);
    }

    @Test
    void countAcceptedLowonganByMahasiswaId_WhenMahasiswaIdIsNull_ShouldReturnZero() {
        // Act
        long result = dashboardRepository.countAcceptedLowonganByMahasiswaId(null);

        // Assert
        assertEquals(0L, result);
    }

    @Test
    void countAcceptedLowonganByMahasiswaId_WhenMahasiswaIdIsEmpty_ShouldReturnZero() {
        // Act
        long result = dashboardRepository.countAcceptedLowonganByMahasiswaId("");

        // Assert
        assertEquals(0L, result);
    }

    @Test
    void countRejectedLowonganByMahasiswaId_ShouldReturnCorrectCount() {
        // Arrange
        String mahasiswaId = "1";
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(eq("mahasiswaId"), eq(mahasiswaId))).thenReturn(query);
        when(query.getSingleResult()).thenReturn(2L);

        // Act
        long result = dashboardRepository.countRejectedLowonganByMahasiswaId(mahasiswaId);

        // Assert
        assertEquals(2L, result);
    }

    @Test
    void countRejectedLowonganByMahasiswaId_WhenMahasiswaIdIsNull_ShouldReturnZero() {
        // Act
        long result = dashboardRepository.countRejectedLowonganByMahasiswaId(null);

        // Assert
        assertEquals(0L, result);
    }

    @Test
    void countRejectedLowonganByMahasiswaId_WhenMahasiswaIdIsEmpty_ShouldReturnZero() {
        // Act
        long result = dashboardRepository.countRejectedLowonganByMahasiswaId("");

        // Assert
        assertEquals(0L, result);
    }

    @Test
    void countPendingLowonganByMahasiswaId_ShouldReturnCorrectCount() {
        // Arrange
        String mahasiswaId = "1";
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(eq("mahasiswaId"), eq(mahasiswaId))).thenReturn(query);
        when(query.getSingleResult()).thenReturn(3L);

        // Act
        long result = dashboardRepository.countPendingLowonganByMahasiswaId(mahasiswaId);

        // Assert
        assertEquals(3L, result);
    }

    @Test
    void countPendingLowonganByMahasiswaId_WhenMahasiswaIdIsNull_ShouldReturnZero() {
        // Act
        long result = dashboardRepository.countPendingLowonganByMahasiswaId(null);

        // Assert
        assertEquals(0L, result);
    }

    @Test
    void countPendingLowonganByMahasiswaId_WhenMahasiswaIdIsEmpty_ShouldReturnZero() {
        // Act
        long result = dashboardRepository.countPendingLowonganByMahasiswaId("");

        // Assert
        assertEquals(0L, result);
    }

    @Test
    void calculateTotalLogHoursByMahasiswaId_ShouldReturnCorrectHours() {
        // Arrange
        String mahasiswaId = "1";

        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(eq("mahasiswaId"), eq(mahasiswaId))).thenReturn(query);
        when(query.setParameter(eq("status"), eq(StatusLog.DITERIMA))).thenReturn(query);

        // Create log time data - 2 hours total
        LocalTime start1 = LocalTime.of(10, 0);
        LocalTime end1 = LocalTime.of(11, 0);
        LocalTime start2 = LocalTime.of(14, 0);
        LocalTime end2 = LocalTime.of(15, 0);

        List<Object[]> logs = Arrays.asList(
                new Object[]{start1, end1},
                new Object[]{start2, end2}
        );

        when(query.getResultList()).thenReturn(logs);

        // Act
        double result = dashboardRepository.calculateTotalLogHoursByMahasiswaId(mahasiswaId);

        // Assert
        assertEquals(2.0, result, 0.01);
    }

    @Test
    void calculateTotalLogHoursByMahasiswaId_WhenMahasiswaIdIsNull_ShouldReturnZero() {
        // Act
        double result = dashboardRepository.calculateTotalLogHoursByMahasiswaId(null);

        // Assert
        assertEquals(0.0, result);
    }

    @Test
    void calculateTotalLogHoursByMahasiswaId_WhenMahasiswaIdIsEmpty_ShouldReturnZero() {
        // Act
        double result = dashboardRepository.calculateTotalLogHoursByMahasiswaId("");

        // Assert
        assertEquals(0.0, result);
    }

    @Test
    void calculateTotalLogHoursByMahasiswaId_WhenExceptionThrown_ShouldReturnZero() {
        // Arrange
        String mahasiswaId = "1";
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(eq("mahasiswaId"), eq(mahasiswaId))).thenReturn(query);
        when(query.setParameter(eq("status"), eq(StatusLog.DITERIMA))).thenReturn(query);
        when(query.getResultList()).thenThrow(new RuntimeException("Database error"));

        // Act
        double result = dashboardRepository.calculateTotalLogHoursByMahasiswaId(mahasiswaId);

        // Assert
        assertEquals(0.0, result);
    }

    @Test
    void calculateTotalLogHoursByMahasiswaId_WhenLogsAreEmpty_ShouldReturnZero() {
        // Arrange
        String mahasiswaId = "1";
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(eq("mahasiswaId"), eq(mahasiswaId))).thenReturn(query);
        when(query.setParameter(eq("status"), eq(StatusLog.DITERIMA))).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.emptyList());

        // Act
        double result = dashboardRepository.calculateTotalLogHoursByMahasiswaId(mahasiswaId);

        // Assert
        assertEquals(0.0, result);
    }

    @Test
    void calculateTotalLogHoursByMahasiswaId_WhenLogsContainNullEntries_ShouldHandleGracefully() {
        // Arrange
        String mahasiswaId = "1";
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(eq("mahasiswaId"), eq(mahasiswaId))).thenReturn(query);
        when(query.setParameter(eq("status"), eq(StatusLog.DITERIMA))).thenReturn(query);

        LocalTime start = LocalTime.of(10, 0);
        LocalTime end = LocalTime.of(11, 0);

        List<Object[]> logs = Arrays.asList(
                new Object[]{start, end},  // Valid entry
                null,                      // Null entry
                new Object[]{null, end},   // Entry with null start
                new Object[]{start, null}, // Entry with null end
                new Object[]{start}        // Entry with insufficient data
        );

        when(query.getResultList()).thenReturn(logs);

        // Act
        double result = dashboardRepository.calculateTotalLogHoursByMahasiswaId(mahasiswaId);

        // Assert
        assertEquals(1.0, result, 0.01); // Only the first valid entry should be counted
    }

    @Test
    void calculateTotalLogHoursByMahasiswaId_WhenLogEntryThrowsException_ShouldContinueProcessing() {
        // Arrange
        String mahasiswaId = "1";
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(eq("mahasiswaId"), eq(mahasiswaId))).thenReturn(query);
        when(query.setParameter(eq("status"), eq(StatusLog.DITERIMA))).thenReturn(query);

        LocalTime start = LocalTime.of(10, 0);
        LocalTime end = LocalTime.of(11, 0);

        List<Object[]> logs = Arrays.asList(
                new Object[]{start, end},     // Valid entry
                new Object[]{"invalid", end}  // Invalid entry that will cause exception
        );

        when(query.getResultList()).thenReturn(logs);

        // Act
        double result = dashboardRepository.calculateTotalLogHoursByMahasiswaId(mahasiswaId);

        // Assert
        assertEquals(1.0, result, 0.01); // Only the valid entry should be counted
    }

    @Test
    void calculateTotalInsentifByMahasiswaId_ShouldReturnCorrectAmount() {
        // Arrange
        String mahasiswaId = "1";
        double logHours = 2.0;
        double expectedInsentif = logHours * 27500.0; // Based on HOURLY_RATE

        // Mock the calculateTotalLogHoursByMahasiswaId method to return 2.0 hours
        DashboardRepositoryImpl spyRepository = org.mockito.Mockito.spy(dashboardRepository);
        org.mockito.Mockito.doReturn(logHours).when(spyRepository).calculateTotalLogHoursByMahasiswaId(mahasiswaId);

        // Act
        double result = spyRepository.calculateTotalInsentifByMahasiswaId(mahasiswaId);

        // Assert
        assertEquals(expectedInsentif, result, 0.01);
    }

    @Test
    void findAcceptedLowonganByMahasiswaId_ShouldReturnCorrectList() {
        // Arrange
        String mahasiswaId = "1";
        List<Lowongan> expectedLowongans = new ArrayList<>();
        Lowongan lowongan = new Lowongan();
        expectedLowongans.add(lowongan);

        when(entityManager.createQuery(anyString(), eq(Lowongan.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(eq("mahasiswaId"), eq(mahasiswaId))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedLowongans);

        // Act
        List<Lowongan> result = dashboardRepository.findAcceptedLowonganByMahasiswaId(mahasiswaId);

        // Assert
        assertEquals(expectedLowongans.size(), result.size());
        assertEquals(expectedLowongans, result);
    }

    @Test
    void findAcceptedLowonganByMahasiswaId_WhenMahasiswaIdIsNull_ShouldReturnEmptyList() {
        // Act
        List<Lowongan> result = dashboardRepository.findAcceptedLowonganByMahasiswaId(null);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void findAcceptedLowonganByMahasiswaId_WhenMahasiswaIdIsEmpty_ShouldReturnEmptyList() {
        // Act
        List<Lowongan> result = dashboardRepository.findAcceptedLowonganByMahasiswaId("");

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void findAcceptedLowonganByMahasiswaId_WhenExceptionThrown_ShouldReturnEmptyList() {
        // Arrange
        String mahasiswaId = "1";
        when(entityManager.createQuery(anyString(), eq(Lowongan.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(eq("mahasiswaId"), eq(mahasiswaId))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenThrow(new RuntimeException("Database error"));

        // Act
        List<Lowongan> result = dashboardRepository.findAcceptedLowonganByMahasiswaId(mahasiswaId);

        // Assert
        assertEquals(0, result.size());
    }
}