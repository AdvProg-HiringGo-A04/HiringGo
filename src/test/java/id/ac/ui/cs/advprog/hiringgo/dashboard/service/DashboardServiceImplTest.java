package id.ac.ui.cs.advprog.hiringgo.dashboard.service;

import id.ac.ui.cs.advprog.hiringgo.common.model.Lowongan;
import id.ac.ui.cs.advprog.hiringgo.common.model.MataKuliah;
import id.ac.ui.cs.advprog.hiringgo.common.model.User;
import id.ac.ui.cs.advprog.hiringgo.common.model.UserRole;
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
import org.mockito.Spy;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
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

    // Create real strategy instances for testing
    private AdminDashboardStrategy adminStrategy;
    private DosenDashboardStrategy dosenStrategy;
    private MahasiswaDashboardStrategy mahasiswaStrategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        adminUser = User.builder()
                .id(1L)
                .role(UserRole.ADMIN)
                .build();

        dosenUser = User.builder()
                .id(2L)
                .role(UserRole.DOSEN)
                .build();

        mahasiswaUser = User.builder()
                .id(3L)
                .role(UserRole.MAHASISWA)
                .build();

        // Initialize the real strategy instances
        adminStrategy = new AdminDashboardStrategy(dashboardRepository);
        dosenStrategy = new DosenDashboardStrategy(dashboardRepository);
        mahasiswaStrategy = new MahasiswaDashboardStrategy(dashboardRepository);
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
        Long dosenId = 2L;
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
        Long mahasiswaId = 3L;

        List<Lowongan> acceptedLowongans = Arrays.asList(
                createLowongan(1L, "Algoritma dan Struktur Data", "CSCM601"),
                createLowongan(2L, "Pemrograman Berbasis Objek", "CSCM602")
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

        LowonganDTO firstLowongan = result.getAcceptedLowonganList().get(0);
        assertEquals(1L, firstLowongan.getId());
        assertEquals("Algoritma dan Struktur Data", firstLowongan.getMataKuliahName());
        assertEquals("CSCM601", firstLowongan.getMataKuliahCode());
    }

    @Test
    void getStatisticsForUser_WhenUserIsAdmin_ShouldReturnAdminStatistics() {
        // Arrange
        when(dashboardRepository.countDosenUsers()).thenReturn(10L);
        when(dashboardRepository.countMahasiswaUsers()).thenReturn(100L);
        when(dashboardRepository.countMataKuliah()).thenReturn(20L);
        when(dashboardRepository.countLowongan()).thenReturn(30L);

        // Use doReturn().when() syntax for stubbing
        doReturn(adminStrategy).when(dashboardStrategyFactory).getStrategy(adminUser);

        // Act
        Object result = dashboardService.getStatisticsForUser(adminUser);

        // Assert
        verify(dashboardStrategyFactory).getStrategy(adminUser);
        assertTrue(result instanceof AdminStatisticsDTO);
        AdminStatisticsDTO stats = (AdminStatisticsDTO) result;
        assertEquals(10L, stats.getTotalDosen());
        assertEquals(100L, stats.getTotalMahasiswa());
        assertEquals(20L, stats.getTotalMataKuliah());
        assertEquals(30L, stats.getTotalLowongan());
    }

    @Test
    void getStatisticsForUser_WhenUserIsDosen_ShouldReturnDosenStatistics() {
        // Arrange
        Long dosenId = 2L;
        when(dashboardRepository.countMataKuliahByDosenId(dosenId)).thenReturn(5L);
        when(dashboardRepository.countMahasiswaAssistantByDosenId(dosenId)).thenReturn(15L);
        when(dashboardRepository.countOpenLowonganByDosenId(dosenId)).thenReturn(3L);

        // Use doReturn().when() syntax for stubbing
        doReturn(dosenStrategy).when(dashboardStrategyFactory).getStrategy(dosenUser);

        // Act
        Object result = dashboardService.getStatisticsForUser(dosenUser);

        // Assert
        verify(dashboardStrategyFactory).getStrategy(dosenUser);
        assertTrue(result instanceof DosenStatisticsDTO);
        DosenStatisticsDTO stats = (DosenStatisticsDTO) result;
        assertEquals(5L, stats.getTotalMataKuliah());
        assertEquals(15L, stats.getTotalMahasiswaAssistant());
        assertEquals(3L, stats.getOpenLowonganCount());
    }

    @Test
    void getStatisticsForUser_WhenUserIsMahasiswa_ShouldReturnMahasiswaStatistics() {
        // Arrange
        Long mahasiswaId = 3L;

        List<Lowongan> acceptedLowongans = Arrays.asList(
                createLowongan(1L, "Algoritma dan Struktur Data", "CSCM601")
        );

        when(dashboardRepository.countOpenLowongan()).thenReturn(10L);
        when(dashboardRepository.countAcceptedLowonganByMahasiswaId(mahasiswaId)).thenReturn(2L);
        when(dashboardRepository.countRejectedLowonganByMahasiswaId(mahasiswaId)).thenReturn(1L);
        when(dashboardRepository.countPendingLowonganByMahasiswaId(mahasiswaId)).thenReturn(3L);
        when(dashboardRepository.calculateTotalLogHoursByMahasiswaId(mahasiswaId)).thenReturn(45.5);
        when(dashboardRepository.calculateTotalInsentifByMahasiswaId(mahasiswaId)).thenReturn(1500000.0);
        when(dashboardRepository.findAcceptedLowonganByMahasiswaId(mahasiswaId)).thenReturn(acceptedLowongans);

        // Use doReturn().when() syntax for stubbing
        doReturn(mahasiswaStrategy).when(dashboardStrategyFactory).getStrategy(mahasiswaUser);

        // Act
        Object result = dashboardService.getStatisticsForUser(mahasiswaUser);

        // Assert
        verify(dashboardStrategyFactory).getStrategy(mahasiswaUser);
        assertTrue(result instanceof MahasiswaStatisticsDTO);
        MahasiswaStatisticsDTO stats = (MahasiswaStatisticsDTO) result;
        assertEquals(10L, stats.getOpenLowonganCount());
        assertEquals(2L, stats.getAcceptedLowonganCount());
    }

    private Lowongan createLowongan(Long id, String mataKuliahName, String mataKuliahCode) {
        MataKuliah mataKuliah = MataKuliah.builder()
                .id(id * 10)  // Just to make them different
                .name(mataKuliahName)
                .code(mataKuliahCode)
                .build();

        return Lowongan.builder()
                .id(id)
                .mataKuliah(mataKuliah)
                .tahunAjaran(2024)  // Changed from String to int
                .semester("Ganjil")
                .build();
    }

    // Helper method to reduce verbosity in tests
    private void assertTrue(boolean condition) {
        org.junit.jupiter.api.Assertions.assertTrue(condition);
    }
}