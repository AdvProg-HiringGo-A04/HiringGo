package id.ac.ui.cs.advprog.hiringgo.dashboard.repository;

import id.ac.ui.cs.advprog.hiringgo.entity.Lowongan;
import id.ac.ui.cs.advprog.hiringgo.enums.Role;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.enums.StatusLog;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query query;

    @Mock
    private TypedQuery<Lowongan> typedQuery;

    @Mock
    private QueryExecutor queryExecutor;

    @Mock
    private LogHoursCalculator logHoursCalculator;

    @InjectMocks
    private DashboardRepositoryImpl dashboardRepository;

    @BeforeEach
    void setUp() {
        // Reset the mocks and create a new instance for each test
        dashboardRepository = new DashboardRepositoryImpl();
        // Use reflection to inject mocked dependencies
        try {
            // Inject entityManager using reflection
            Field entityManagerField = DashboardRepositoryImpl.class.getDeclaredField("entityManager");
            entityManagerField.setAccessible(true);
            entityManagerField.set(dashboardRepository, entityManager);

            Field queryExecutorField = DashboardRepositoryImpl.class.getDeclaredField("queryExecutor");
            queryExecutorField.setAccessible(true);
            queryExecutorField.set(dashboardRepository, queryExecutor);

            Field logHoursCalculatorField = DashboardRepositoryImpl.class.getDeclaredField("logHoursCalculator");
            logHoursCalculatorField.setAccessible(true);
            logHoursCalculatorField.set(dashboardRepository, logHoursCalculator);
        } catch (Exception e) {
            // Fallback - create instance without mocked dependencies for integration-like tests
            fail("Failed to set up test dependencies: " + e.getMessage());
        }
    }

    @Test
    void countDosenUsers_ShouldReturnCorrectCount() {
        // Arrange
        long expectedCount = 5L;
        when(queryExecutor.executeCountQuery(eq(entityManager), anyString(), eq("role"), eq(Role.DOSEN), any()))
                .thenReturn(expectedCount);

        // Act
        long result = dashboardRepository.countDosenUsers();

        // Assert
        assertEquals(expectedCount, result);
        verify(queryExecutor).executeCountQuery(eq(entityManager), anyString(), eq("role"), eq(Role.DOSEN), any());
    }

    @Test
    void countMahasiswaUsers_ShouldReturnCorrectCount() {
        // Arrange
        long expectedCount = 10L;
        when(queryExecutor.executeCountQuery(eq(entityManager), anyString(), eq("role"), eq(Role.MAHASISWA), any()))
                .thenReturn(expectedCount);

        // Act
        long result = dashboardRepository.countMahasiswaUsers();

        // Assert
        assertEquals(expectedCount, result);
        verify(queryExecutor).executeCountQuery(eq(entityManager), anyString(), eq("role"), eq(Role.MAHASISWA), any());
    }

    @Test
    void countMataKuliah_ShouldReturnCorrectCount() {
        // Arrange
        long expectedCount = 15L;
        when(queryExecutor.executeSimpleCountQuery(eq(entityManager), anyString(), any()))
                .thenReturn(expectedCount);

        // Act
        long result = dashboardRepository.countMataKuliah();

        // Assert
        assertEquals(expectedCount, result);
        verify(queryExecutor).executeSimpleCountQuery(eq(entityManager), anyString(), any());
    }

    @Test
    void countLowongan_ShouldReturnCorrectCount() {
        // Arrange
        long expectedCount = 8L;
        when(queryExecutor.executeSimpleCountQuery(eq(entityManager), anyString(), any()))
                .thenReturn(expectedCount);

        // Act
        long result = dashboardRepository.countLowongan();

        // Assert
        assertEquals(expectedCount, result);
        verify(queryExecutor).executeSimpleCountQuery(eq(entityManager), anyString(), any());
    }

    @Test
    void countMataKuliahByDosenId_WithValidId_ShouldReturnCorrectCount() {
        // Arrange
        String dosenId = "dosen123";
        long expectedCount = 3L;
        when(queryExecutor.executeCountQuery(eq(entityManager), anyString(), eq("dosenId"), eq(dosenId), any()))
                .thenReturn(expectedCount);

        // Act
        long result = dashboardRepository.countMataKuliahByDosenId(dosenId);

        // Assert
        assertEquals(expectedCount, result);
        verify(queryExecutor).executeCountQuery(eq(entityManager), anyString(), eq("dosenId"), eq(dosenId), any());
    }

    @Test
    void countMataKuliahByDosenId_WithNullId_ShouldReturnZero() {
        // Act
        long result = dashboardRepository.countMataKuliahByDosenId(null);

        // Assert
        assertEquals(0L, result);
        verifyNoInteractions(queryExecutor);
    }

    @Test
    void countMataKuliahByDosenId_WithEmptyId_ShouldReturnZero() {
        // Act
        long result = dashboardRepository.countMataKuliahByDosenId("");

        // Assert
        assertEquals(0L, result);
        verifyNoInteractions(queryExecutor);
    }

    @Test
    void countMataKuliahByDosenId_WithBlankId_ShouldReturnZero() {
        // Act
        long result = dashboardRepository.countMataKuliahByDosenId("   ");

        // Assert
        assertEquals(0L, result);
        verifyNoInteractions(queryExecutor);
    }

    @Test
    void countMahasiswaAssistantByDosenId_WithValidId_ShouldReturnCorrectCount() {
        // Arrange
        String dosenId = "dosen123";
        long expectedCount = 7L;
        when(queryExecutor.executeCountQuery(eq(entityManager), anyString(), eq("dosenId"), eq(dosenId), any()))
                .thenReturn(expectedCount);

        // Act
        long result = dashboardRepository.countMahasiswaAssistantByDosenId(dosenId);

        // Assert
        assertEquals(expectedCount, result);
        verify(queryExecutor).executeCountQuery(eq(entityManager), anyString(), eq("dosenId"), eq(dosenId), any());
    }

    @Test
    void countMahasiswaAssistantByDosenId_WithNullId_ShouldReturnZero() {
        // Act
        long result = dashboardRepository.countMahasiswaAssistantByDosenId(null);

        // Assert
        assertEquals(0L, result);
        verifyNoInteractions(queryExecutor);
    }

    @Test
    void countOpenLowonganByDosenId_WithValidId_ShouldReturnCorrectCount() {
        // Arrange
        String dosenId = "dosen123";
        long expectedCount = 2L;
        when(queryExecutor.executeCountQuery(eq(entityManager), anyString(), eq("dosenId"), eq(dosenId), any()))
                .thenReturn(expectedCount);

        // Act
        long result = dashboardRepository.countOpenLowonganByDosenId(dosenId);

        // Assert
        assertEquals(expectedCount, result);
        verify(queryExecutor).executeCountQuery(eq(entityManager), anyString(), eq("dosenId"), eq(dosenId), any());
    }

    @Test
    void countOpenLowonganByDosenId_WithNullId_ShouldReturnZero() {
        // Act
        long result = dashboardRepository.countOpenLowonganByDosenId(null);

        // Assert
        assertEquals(0L, result);
        verifyNoInteractions(queryExecutor);
    }

    @Test
    void countOpenLowongan_ShouldReturnCorrectCount() {
        // Arrange
        long expectedCount = 4L;
        when(queryExecutor.executeSimpleCountQuery(eq(entityManager), anyString(), any()))
                .thenReturn(expectedCount);

        // Act
        long result = dashboardRepository.countOpenLowongan();

        // Assert
        assertEquals(expectedCount, result);
        verify(queryExecutor).executeSimpleCountQuery(eq(entityManager), anyString(), any());
    }

    @Test
    void countAcceptedLowonganByMahasiswaId_WithValidId_ShouldReturnCorrectCount() {
        // Arrange
        String mahasiswaId = "mahasiswa123";
        long expectedCount = 3L;
        when(queryExecutor.executeCountQuery(eq(entityManager), anyString(), eq("mahasiswaId"), eq(mahasiswaId), any()))
                .thenReturn(expectedCount);

        // Act
        long result = dashboardRepository.countAcceptedLowonganByMahasiswaId(mahasiswaId);

        // Assert
        assertEquals(expectedCount, result);
        verify(queryExecutor).executeCountQuery(eq(entityManager), anyString(), eq("mahasiswaId"), eq(mahasiswaId), any());
    }

    @Test
    void countAcceptedLowonganByMahasiswaId_WithNullId_ShouldReturnZero() {
        // Act
        long result = dashboardRepository.countAcceptedLowonganByMahasiswaId(null);

        // Assert
        assertEquals(0L, result);
        verifyNoInteractions(queryExecutor);
    }

    @Test
    void countRejectedLowonganByMahasiswaId_WithValidId_ShouldReturnCorrectCount() {
        // Arrange
        String mahasiswaId = "mahasiswa123";
        long expectedCount = 1L;
        when(queryExecutor.executeCountQuery(eq(entityManager), anyString(), eq("mahasiswaId"), eq(mahasiswaId), any()))
                .thenReturn(expectedCount);

        // Act
        long result = dashboardRepository.countRejectedLowonganByMahasiswaId(mahasiswaId);

        // Assert
        assertEquals(expectedCount, result);
        verify(queryExecutor).executeCountQuery(eq(entityManager), anyString(), eq("mahasiswaId"), eq(mahasiswaId), any());
    }

    @Test
    void countRejectedLowonganByMahasiswaId_WithNullId_ShouldReturnZero() {
        // Act
        long result = dashboardRepository.countRejectedLowonganByMahasiswaId(null);

        // Assert
        assertEquals(0L, result);
        verifyNoInteractions(queryExecutor);
    }

    @Test
    void countPendingLowonganByMahasiswaId_WithValidId_ShouldReturnCorrectCount() {
        // Arrange
        String mahasiswaId = "mahasiswa123";
        long expectedCount = 2L;
        when(queryExecutor.executeCountQuery(eq(entityManager), anyString(), eq("mahasiswaId"), eq(mahasiswaId), any()))
                .thenReturn(expectedCount);

        // Act
        long result = dashboardRepository.countPendingLowonganByMahasiswaId(mahasiswaId);

        // Assert
        assertEquals(expectedCount, result);
        verify(queryExecutor).executeCountQuery(eq(entityManager), anyString(), eq("mahasiswaId"), eq(mahasiswaId), any());
    }

    @Test
    void countPendingLowonganByMahasiswaId_WithNullId_ShouldReturnZero() {
        // Act
        long result = dashboardRepository.countPendingLowonganByMahasiswaId(null);

        // Assert
        assertEquals(0L, result);
        verifyNoInteractions(queryExecutor);
    }

    @Test
    void calculateTotalLogHoursByMahasiswaId_WithValidId_ShouldReturnCorrectHours() {
        // Arrange
        String mahasiswaId = "mahasiswa123";
        LocalDateTime start1 = LocalDateTime.of(2024, 1, 1, 9, 0);
        LocalDateTime end1 = LocalDateTime.of(2024, 1, 1, 11, 0);
        LocalDateTime start2 = LocalDateTime.of(2024, 1, 2, 14, 0);
        LocalDateTime end2 = LocalDateTime.of(2024, 1, 2, 16, 30);

        List<Object[]> mockLogs = Arrays.asList(
                new Object[]{start1, end1},
                new Object[]{start2, end2}
        );

        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter("mahasiswaId", mahasiswaId)).thenReturn(query);
        when(query.setParameter("status", StatusLog.DITERIMA)).thenReturn(query);
        when(query.getResultList()).thenReturn(mockLogs);
        when(logHoursCalculator.calculateTotalHours(eq(mockLogs), any())).thenReturn(4.5);

        // Act
        double result = dashboardRepository.calculateTotalLogHoursByMahasiswaId(mahasiswaId);

        // Assert
        assertEquals(4.5, result, 0.01);
        verify(entityManager).createQuery(anyString());
        verify(logHoursCalculator).calculateTotalHours(eq(mockLogs), any());
    }

    @Test
    void calculateTotalLogHoursByMahasiswaId_WithNullId_ShouldReturnZero() {
        // Act
        double result = dashboardRepository.calculateTotalLogHoursByMahasiswaId(null);

        // Assert
        assertEquals(0.0, result, 0.01);
        verifyNoInteractions(entityManager);
    }

    @Test
    void calculateTotalLogHoursByMahasiswaId_WithEmptyId_ShouldReturnZero() {
        // Act
        double result = dashboardRepository.calculateTotalLogHoursByMahasiswaId("");

        // Assert
        assertEquals(0.0, result, 0.01);
        verifyNoInteractions(entityManager);
    }

    @Test
    void calculateTotalLogHoursByMahasiswaId_WithException_ShouldReturnZero() {
        // Arrange
        String mahasiswaId = "mahasiswa123";
        when(entityManager.createQuery(anyString())).thenThrow(new RuntimeException("Database error"));

        // Act
        double result = dashboardRepository.calculateTotalLogHoursByMahasiswaId(mahasiswaId);

        // Assert
        assertEquals(0.0, result, 0.01);
        verify(entityManager).createQuery(anyString());
    }

    @Test
    void calculateTotalInsentifByMahasiswaId_WithValidId_ShouldReturnCorrectAmount() {
        // Arrange
        String mahasiswaId = "mahasiswa123";
        double totalHours = 10.0;
        double expectedInsentif = totalHours * LogHoursCalculator.HOURLY_RATE;

        // Mock the calculateTotalLogHoursByMahasiswaId method behavior
        List<Object[]> mockLogs = Arrays.asList(
                new Object[]{LocalDateTime.now(), LocalDateTime.now().plusHours(5)},
                new Object[]{LocalDateTime.now(), LocalDateTime.now().plusHours(5)}
        );

        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter("mahasiswaId", mahasiswaId)).thenReturn(query);
        when(query.setParameter("status", StatusLog.DITERIMA)).thenReturn(query);
        when(query.getResultList()).thenReturn(mockLogs);
        when(logHoursCalculator.calculateTotalHours(eq(mockLogs), any())).thenReturn(totalHours);

        // Act
        double result = dashboardRepository.calculateTotalInsentifByMahasiswaId(mahasiswaId);

        // Assert
        assertEquals(expectedInsentif, result, 0.01);
    }

    @Test
    void calculateTotalInsentifByMahasiswaId_WithNullId_ShouldReturnZero() {
        // Act
        double result = dashboardRepository.calculateTotalInsentifByMahasiswaId(null);

        // Assert
        assertEquals(0.0, result, 0.01);
    }

    @Test
    void findAcceptedLowonganByMahasiswaId_WithValidId_ShouldReturnLowonganList() {
        // Arrange
        String mahasiswaId = "mahasiswa123";
        Lowongan lowongan1 = new Lowongan();
        lowongan1.setId("lowongan1");
        Lowongan lowongan2 = new Lowongan();
        lowongan2.setId("lowongan2");
        List<Lowongan> expectedLowongan = Arrays.asList(lowongan1, lowongan2);

        when(entityManager.createQuery(anyString(), eq(Lowongan.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter("mahasiswaId", mahasiswaId)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedLowongan);

        // Act
        List<Lowongan> result = dashboardRepository.findAcceptedLowonganByMahasiswaId(mahasiswaId);

        // Assert
        assertEquals(expectedLowongan, result);
        assertEquals(2, result.size());
        verify(entityManager).createQuery(anyString(), eq(Lowongan.class));
        verify(typedQuery).setParameter("mahasiswaId", mahasiswaId);
        verify(typedQuery).getResultList();
    }

    @Test
    void findAcceptedLowonganByMahasiswaId_WithNullId_ShouldReturnEmptyList() {
        // Act
        List<Lowongan> result = dashboardRepository.findAcceptedLowonganByMahasiswaId(null);

        // Assert
        assertTrue(result.isEmpty());
        verifyNoInteractions(entityManager);
    }

    @Test
    void findAcceptedLowonganByMahasiswaId_WithEmptyId_ShouldReturnEmptyList() {
        // Act
        List<Lowongan> result = dashboardRepository.findAcceptedLowonganByMahasiswaId("");

        // Assert
        assertTrue(result.isEmpty());
        verifyNoInteractions(entityManager);
    }

    @Test
    void findAcceptedLowonganByMahasiswaId_WithBlankId_ShouldReturnEmptyList() {
        // Act
        List<Lowongan> result = dashboardRepository.findAcceptedLowonganByMahasiswaId("   ");

        // Assert
        assertTrue(result.isEmpty());
        verifyNoInteractions(entityManager);
    }

    @Test
    void findAcceptedLowonganByMahasiswaId_WithException_ShouldReturnEmptyList() {
        // Arrange
        String mahasiswaId = "mahasiswa123";
        when(entityManager.createQuery(anyString(), eq(Lowongan.class)))
                .thenThrow(new RuntimeException("Database error"));

        // Act
        List<Lowongan> result = dashboardRepository.findAcceptedLowonganByMahasiswaId(mahasiswaId);

        // Assert
        assertTrue(result.isEmpty());
        verify(entityManager).createQuery(anyString(), eq(Lowongan.class));
    }

    @Test
    void constructor_ShouldInitializeQueryExecutorAndLogHoursCalculator() {
        // Act
        DashboardRepositoryImpl newRepository = new DashboardRepositoryImpl();

        // Assert
        assertNotNull(newRepository);
        // The constructor initializes queryExecutor and logHoursCalculator
        // This test ensures the constructor completes without errors
    }
}