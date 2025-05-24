package id.ac.ui.cs.advprog.hiringgo.dashboard.service;

import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.entity.Lowongan;
import id.ac.ui.cs.advprog.hiringgo.entity.Role;
import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.AdminStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.DosenStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.MahasiswaStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.repository.DashboardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class DashboardServiceImplTest {

    @Mock
    private DashboardRepository dashboardRepository;

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    private User adminUser;
    private User dosenUser;
    private User mahasiswaUser;

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
                createLowongan(UUID.randomUUID(), "Algoritma dan Struktur Data"),
                createLowongan(UUID.randomUUID(), "Pemrograman Berbasis Objek")
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
    void getStatisticsForUser_WhenUserIsAdmin_ShouldReturnAdminStatistics() {
        // Arrange
        when(dashboardRepository.countDosenUsers()).thenReturn(10L);
        when(dashboardRepository.countMahasiswaUsers()).thenReturn(100L);
        when(dashboardRepository.countMataKuliah()).thenReturn(20L);
        when(dashboardRepository.countLowongan()).thenReturn(30L);

        // Act
        Object result = dashboardService.getStatisticsForUser(adminUser);

        // Assert
        assertTrue(result instanceof AdminStatisticsDTO);
        AdminStatisticsDTO stats = (AdminStatisticsDTO) result;
        assertEquals(10L, stats.getTotalDosen());
        assertEquals(100L, stats.getTotalMahasiswa());
        assertEquals(20L, stats.getTotalMataKuliah());
        assertEquals(30L, stats.getTotalLowongan());

        // Verify repository calls
        verify(dashboardRepository).countDosenUsers();
        verify(dashboardRepository).countMahasiswaUsers();
        verify(dashboardRepository).countMataKuliah();
        verify(dashboardRepository).countLowongan();
    }

    @Test
    void getStatisticsForUser_WhenUserIsDosen_ShouldReturnDosenStatistics() {
        // Arrange
        when(dashboardRepository.countMataKuliahByDosenId(dosenUser.getId())).thenReturn(5L);
        when(dashboardRepository.countMahasiswaAssistantByDosenId(dosenUser.getId())).thenReturn(15L);
        when(dashboardRepository.countOpenLowonganByDosenId(dosenUser.getId())).thenReturn(3L);

        // Act
        Object result = dashboardService.getStatisticsForUser(dosenUser);

        // Assert
        assertTrue(result instanceof DosenStatisticsDTO);
        DosenStatisticsDTO stats = (DosenStatisticsDTO) result;
        assertEquals(5L, stats.getTotalMataKuliah());
        assertEquals(15L, stats.getTotalMahasiswaAssistant());
        assertEquals(3L, stats.getOpenLowonganCount());

        // Verify repository calls
        verify(dashboardRepository).countMataKuliahByDosenId(dosenUser.getId());
        verify(dashboardRepository).countMahasiswaAssistantByDosenId(dosenUser.getId());
        verify(dashboardRepository).countOpenLowonganByDosenId(dosenUser.getId());
    }

    @Test
    void getStatisticsForUser_WhenUserIsMahasiswa_ShouldReturnMahasiswaStatistics() {
        // Arrange
        List<Lowongan> acceptedLowongans = Arrays.asList(
                createLowongan(UUID.randomUUID(), "Test Course")
        );

        when(dashboardRepository.countOpenLowongan()).thenReturn(10L);
        when(dashboardRepository.countAcceptedLowonganByMahasiswaId(mahasiswaUser.getId())).thenReturn(2L);
        when(dashboardRepository.countRejectedLowonganByMahasiswaId(mahasiswaUser.getId())).thenReturn(1L);
        when(dashboardRepository.countPendingLowonganByMahasiswaId(mahasiswaUser.getId())).thenReturn(3L);
        when(dashboardRepository.calculateTotalLogHoursByMahasiswaId(mahasiswaUser.getId())).thenReturn(45.5);
        when(dashboardRepository.calculateTotalInsentifByMahasiswaId(mahasiswaUser.getId())).thenReturn(1500000.0);
        when(dashboardRepository.findAcceptedLowonganByMahasiswaId(mahasiswaUser.getId())).thenReturn(acceptedLowongans);

        // Act
        Object result = dashboardService.getStatisticsForUser(mahasiswaUser);

        // Assert
        assertTrue(result instanceof MahasiswaStatisticsDTO);
        MahasiswaStatisticsDTO stats = (MahasiswaStatisticsDTO) result;
        assertEquals(10L, stats.getOpenLowonganCount());
        assertEquals(2L, stats.getAcceptedLowonganCount());
        assertEquals(1L, stats.getRejectedLowonganCount());
        assertEquals(3L, stats.getPendingLowonganCount());
        assertEquals(45.5, stats.getTotalLogHours());
        assertEquals(1500000.0, stats.getTotalInsentif());
        assertEquals(1, stats.getAcceptedLowonganList().size());

        // Verify repository calls
        verify(dashboardRepository).countOpenLowongan();
        verify(dashboardRepository).countAcceptedLowonganByMahasiswaId(mahasiswaUser.getId());
        verify(dashboardRepository).countRejectedLowonganByMahasiswaId(mahasiswaUser.getId());
        verify(dashboardRepository).countPendingLowonganByMahasiswaId(mahasiswaUser.getId());
        verify(dashboardRepository).calculateTotalLogHoursByMahasiswaId(mahasiswaUser.getId());
        verify(dashboardRepository).calculateTotalInsentifByMahasiswaId(mahasiswaUser.getId());
        verify(dashboardRepository).findAcceptedLowonganByMahasiswaId(mahasiswaUser.getId());
    }

    @Test
    void getStatisticsForUser_WhenUserIsNull_ShouldThrowException() {
        // Act & Assert
        try {
            dashboardService.getStatisticsForUser(null);
        } catch (IllegalArgumentException e) {
            assertEquals("User and user role cannot be null", e.getMessage());
        }
    }

    @Test
    void getStatisticsForUser_WhenUserRoleIsNull_ShouldThrowException() {
        // Arrange
        User userWithNullRole = new User();
        userWithNullRole.setId("1");
        userWithNullRole.setRole(null);

        // Act & Assert
        try {
            dashboardService.getStatisticsForUser(userWithNullRole);
        } catch (IllegalArgumentException e) {
            assertEquals("User and user role cannot be null", e.getMessage());
        }
    }

    private Lowongan createLowongan(UUID id, String mataKuliahName) {
        return Lowongan.builder()
                .id(id)
                .mataKuliah(mataKuliahName)
                .tahunAjaran("2024/2025")
                .semester("Ganjil")
                .build();
    }
}