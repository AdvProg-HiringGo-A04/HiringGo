package id.ac.ui.cs.advprog.hiringgo.dashboard.service;

import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.entity.Lowongan;
import id.ac.ui.cs.advprog.hiringgo.entity.Role;
import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.AdminStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.DosenStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.LowonganDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.MahasiswaStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.repository.DashboardRepository;
import id.ac.ui.cs.advprog.hiringgo.dashboard.strategy.AdminDashboardStrategy;
import id.ac.ui.cs.advprog.hiringgo.dashboard.strategy.DashboardStatisticsStrategy;
import id.ac.ui.cs.advprog.hiringgo.dashboard.strategy.DashboardStrategyFactory;
import id.ac.ui.cs.advprog.hiringgo.dashboard.strategy.DosenDashboardStrategy;
import id.ac.ui.cs.advprog.hiringgo.dashboard.strategy.MahasiswaDashboardStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class DashboardServiceImplTest {

    @Mock
    private DashboardRepository dashboardRepository;

    @Mock
    private DashboardStrategyFactory dashboardStrategyFactory;

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    private User adminUser;
    private User dosenUser;
    private User mahasiswaUser;

    @Mock
    private AdminDashboardStrategy adminStrategy;

    @Mock
    private DosenDashboardStrategy dosenStrategy;

    @Mock
    private MahasiswaDashboardStrategy mahasiswaStrategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create mock User objects
        adminUser = createMockUser("1", Role.ADMIN);
        dosenUser = createMockUser("2", Role.DOSEN);
        mahasiswaUser = createMockUser("3", Role.MAHASISWA);
    }

    private User createMockUser(String id, Role role) {
        User user = new User();
        user.setId(id);
        user.setEmail("user" + id + "@example.com");
        user.setRole(role);
        user.setPassword("password");
        return user;
    }

    @Test
    void getAdminStatistics_ShouldReturnCorrectStatistics() {
        // Arrange
        when(dashboardRepository.countDosenUsers()).thenReturn(10L);
        when(dashboardRepository.countMahasiswaUsers()).thenReturn(100L);
        when(dashboardRepository.countMataKuliah()).thenReturn(20L);
        when(dashboardRepository.countLowongan()).thenReturn(30L);

        // Act
        AdminStatisticsDTO result = dashboardService.getAdminStatistics();

        // Assert
        assertEquals(10L, result.getTotalDosen());
        assertEquals(100L, result.getTotalMahasiswa());
        assertEquals(20L, result.getTotalMataKuliah());
        assertEquals(30L, result.getTotalLowongan());
    }

    @Test
    void getDosenStatistics_ShouldReturnCorrectStatistics() {
        // Arrange
        String dosenId = "2";
        when(dashboardRepository.countMataKuliahByDosenId(dosenId)).thenReturn(5L);
        when(dashboardRepository.countMahasiswaAssistantByDosenId(dosenId)).thenReturn(15L);
        when(dashboardRepository.countOpenLowonganByDosenId(dosenId)).thenReturn(3L);

        // Act
        DosenStatisticsDTO result = dashboardService.getDosenStatistics(dosenId);

        // Assert
        assertEquals(5L, result.getTotalMataKuliah());
        assertEquals(15L, result.getTotalMahasiswaAssistant());
        assertEquals(3L, result.getOpenLowonganCount());
    }

    @Test
    void getMahasiswaStatistics_ShouldReturnCorrectStatistics() {
        // Arrange
        String mahasiswaId = "3";

        List<Lowongan> acceptedLowongans = Arrays.asList(
                createLowongan(UUID.randomUUID(), "Algoritma dan Struktur Data", "CSCM601"),
                createLowongan(UUID.randomUUID(), "Pemrograman Berbasis Objek", "CSCM602")
        );

        when(dashboardRepository.countOpenLowongan()).thenReturn(10L);
        when(dashboardRepository.countAcceptedLowonganByMahasiswaId(mahasiswaId)).thenReturn(2L);
        when(dashboardRepository.countRejectedLowonganByMahasiswaId(mahasiswaId)).thenReturn(1L);
        when(dashboardRepository.countPendingLowonganByMahasiswaId(mahasiswaId)).thenReturn(3L);
        when(dashboardRepository.calculateTotalLogHoursByMahasiswaId(mahasiswaId)).thenReturn(45.5);
        when(dashboardRepository.calculateTotalInsentifByMahasiswaId(mahasiswaId)).thenReturn(1500000.0);
        when(dashboardRepository.findAcceptedLowonganByMahasiswaId(mahasiswaId)).thenReturn(acceptedLowongans);

        // Act
        MahasiswaStatisticsDTO result = dashboardService.getMahasiswaStatistics(mahasiswaId);

        // Assert
        assertEquals(10L, result.getOpenLowonganCount());
        assertEquals(2L, result.getAcceptedLowonganCount());
        assertEquals(1L, result.getRejectedLowonganCount());
        assertEquals(3L, result.getPendingLowonganCount());
        assertEquals(45.5, result.getTotalLogHours());
        assertEquals(1500000.0, result.getTotalInsentif());
        assertEquals(2, result.getAcceptedLowonganList().size());
    }

    @Test
    void getStatisticsForUser_WhenUserIsAdmin_ShouldReturnAdminStatistics() throws Exception {
        // Arrange
        AdminStatisticsDTO expectedStats = AdminStatisticsDTO.builder()
                .totalDosen(10L)
                .totalMahasiswa(100L)
                .totalMataKuliah(20L)
                .totalLowongan(30L)
                .build();

        // Set up the adminStrategy mock to return a CompletableFuture
        when(adminStrategy.calculateStatistics(adminUser.getId()))
                .thenReturn(CompletableFuture.completedFuture(expectedStats));

        // Mock the factory to return our mock strategy
        when(dashboardStrategyFactory.getStrategy(adminUser)).thenReturn((DashboardStatisticsStrategy) adminStrategy);

        // Act
        CompletableFuture<Object> futureResult = dashboardService.getStatisticsForUser(adminUser);
        Object result = futureResult.get(); // Get the actual result from CompletableFuture

        // Assert
        verify(dashboardStrategyFactory).getStrategy(adminUser);
        verify(adminStrategy).calculateStatistics(adminUser.getId());

        assertTrue(result instanceof AdminStatisticsDTO);
        AdminStatisticsDTO stats = (AdminStatisticsDTO) result;
        assertEquals(10L, stats.getTotalDosen());
        assertEquals(100L, stats.getTotalMahasiswa());
        assertEquals(20L, stats.getTotalMataKuliah());
        assertEquals(30L, stats.getTotalLowongan());
    }

    @Test
    void getStatisticsForUser_WhenUserIsDosen_ShouldReturnDosenStatistics() throws ExecutionException, InterruptedException {
        // Arrange
        DosenStatisticsDTO expectedStats = DosenStatisticsDTO.builder()
                .totalMataKuliah(5L)
                .totalMahasiswaAssistant(15L)
                .openLowonganCount(3L)
                .build();

        // Set up the dosenStrategy mock to return a CompletableFuture
        when(dosenStrategy.calculateStatistics(dosenUser.getId()))
                .thenReturn(CompletableFuture.completedFuture(expectedStats));

        // Mock the factory to return our mock strategy
        when(dashboardStrategyFactory.getStrategy(dosenUser)).thenReturn((DashboardStatisticsStrategy) dosenStrategy);

        // Act
        Object result = dashboardService.getStatisticsForUser(dosenUser).get();

        // Assert
        verify(dashboardStrategyFactory).getStrategy(dosenUser);
        verify(dosenStrategy).calculateStatistics(dosenUser.getId());

        assertTrue(result instanceof DosenStatisticsDTO);
        DosenStatisticsDTO stats = (DosenStatisticsDTO) result;
        assertEquals(5L, stats.getTotalMataKuliah());
        assertEquals(15L, stats.getTotalMahasiswaAssistant());
        assertEquals(3L, stats.getOpenLowonganCount());
    }

    @Test
    void getStatisticsForUser_WhenUserIsMahasiswa_ShouldReturnMahasiswaStatistics() throws Exception {
        // Arrange
        MahasiswaStatisticsDTO expectedStats = MahasiswaStatisticsDTO.builder()
                .openLowonganCount(10L)
                .acceptedLowonganCount(2L)
                .rejectedLowonganCount(1L)
                .pendingLowonganCount(3L)
                .totalLogHours(45.5)
                .totalInsentif(1500000.0)
                .acceptedLowonganList(Arrays.asList(
                        LowonganDTO.builder().id(UUID.randomUUID().toString()).mataKuliahName("Test Course").build()
                ))
                .build();

        // Set up the mahasiswaStrategy mock to return a CompletableFuture
        when(mahasiswaStrategy.calculateStatistics(mahasiswaUser.getId()))
                .thenReturn(CompletableFuture.completedFuture(expectedStats));

        // Mock the factory to return our mock strategy
        when(dashboardStrategyFactory.getStrategy(mahasiswaUser)).thenReturn((DashboardStatisticsStrategy) mahasiswaStrategy);

        // Act
        CompletableFuture<Object> futureResult = dashboardService.getStatisticsForUser(mahasiswaUser);
        Object result = futureResult.get(); // Get the actual result from CompletableFuture

        // Assert
        verify(dashboardStrategyFactory).getStrategy(mahasiswaUser);
        verify(mahasiswaStrategy).calculateStatistics(mahasiswaUser.getId());

        assertTrue(result instanceof MahasiswaStatisticsDTO);
        MahasiswaStatisticsDTO stats = (MahasiswaStatisticsDTO) result;
        assertEquals(10L, stats.getOpenLowonganCount());
        assertEquals(2L, stats.getAcceptedLowonganCount());
        assertEquals(1L, stats.getRejectedLowonganCount());
        assertEquals(3L, stats.getPendingLowonganCount());
    }

    private Lowongan createLowongan(UUID id, String mataKuliahName, String mataKuliahCode) {
        return Lowongan.builder()
                .id(id)
                .mataKuliah(mataKuliahName)
                .tahunAjaran("2024/2025")
                .semester("Ganjil")
                .build();
    }
}