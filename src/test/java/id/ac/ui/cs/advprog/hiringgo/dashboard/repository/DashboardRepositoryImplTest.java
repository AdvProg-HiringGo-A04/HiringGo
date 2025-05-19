package id.ac.ui.cs.advprog.hiringgo.dashboard.repository;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Role;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.entity.Lowongan;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.model.enums.StatusLog;
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
    void calculateTotalInsentifByMahasiswaId_ShouldReturnCorrectAmount() {
        // Arrange
        String mahasiswaId = "1";
        double logHours = 2.0;
        double expectedInsentif = logHours * 27500.0; // Based on RATE_PER_HOUR

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
}