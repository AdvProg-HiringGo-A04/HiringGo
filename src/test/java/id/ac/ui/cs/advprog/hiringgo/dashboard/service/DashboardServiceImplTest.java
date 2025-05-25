package id.ac.ui.cs.advprog.hiringgo.dashboard.service;

import id.ac.ui.cs.advprog.hiringgo.entity.Lowongan;
import id.ac.ui.cs.advprog.hiringgo.entity.MataKuliah;
import id.ac.ui.cs.advprog.hiringgo.enums.Role;
import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.AdminStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.DosenStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.LowonganDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.MahasiswaStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.repository.DashboardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
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
    void getDosenStatistics_WithNullDosenId_ShouldReturnEmptyStatistics() {
        // Act
        DosenStatisticsDTO result = dashboardService.getDosenStatistics(null);

        // Assert
        assertEquals(0L, result.getTotalMataKuliah());
        assertEquals(0L, result.getTotalMahasiswaAssistant());
        assertEquals(0L, result.getOpenLowonganCount());

        // Verify no repository calls were made
        verifyNoInteractions(dashboardRepository);
    }

    @Test
    void getDosenStatistics_WithEmptyDosenId_ShouldReturnEmptyStatistics() {
        // Act
        DosenStatisticsDTO result = dashboardService.getDosenStatistics("");

        // Assert
        assertEquals(0L, result.getTotalMataKuliah());
        assertEquals(0L, result.getTotalMahasiswaAssistant());
        assertEquals(0L, result.getOpenLowonganCount());

        // Verify no repository calls were made
        verifyNoInteractions(dashboardRepository);
    }

    @Test
    void getDosenStatistics_WithBlankDosenId_ShouldReturnEmptyStatistics() {
        // Act
        DosenStatisticsDTO result = dashboardService.getDosenStatistics("   ");

        // Assert
        assertEquals(0L, result.getTotalMataKuliah());
        assertEquals(0L, result.getTotalMahasiswaAssistant());
        assertEquals(0L, result.getOpenLowonganCount());

        // Verify no repository calls were made
        verifyNoInteractions(dashboardRepository);
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
    void getMahasiswaStatistics_WithNullMahasiswaId_ShouldReturnEmptyStatistics() {
        // Act
        MahasiswaStatisticsDTO result = dashboardService.getMahasiswaStatistics(null);

        // Assert
        assertEquals(0L, result.getOpenLowonganCount());
        assertEquals(0L, result.getAcceptedLowonganCount());
        assertEquals(0L, result.getRejectedLowonganCount());
        assertEquals(0L, result.getPendingLowonganCount());
        assertEquals(0.0, result.getTotalLogHours());
        assertEquals(0.0, result.getTotalInsentif());
        assertTrue(result.getAcceptedLowonganList().isEmpty());

        // Verify no repository calls were made
        verifyNoInteractions(dashboardRepository);
    }

    @Test
    void getMahasiswaStatistics_WithEmptyMahasiswaId_ShouldReturnEmptyStatistics() {
        // Act
        MahasiswaStatisticsDTO result = dashboardService.getMahasiswaStatistics("");

        // Assert
        assertEquals(0L, result.getOpenLowonganCount());
        assertEquals(0L, result.getAcceptedLowonganCount());
        assertEquals(0L, result.getRejectedLowonganCount());
        assertEquals(0L, result.getPendingLowonganCount());
        assertEquals(0.0, result.getTotalLogHours());
        assertEquals(0.0, result.getTotalInsentif());
        assertTrue(result.getAcceptedLowonganList().isEmpty());

        // Verify no repository calls were made
        verifyNoInteractions(dashboardRepository);
    }

    @Test
    void getMahasiswaStatistics_WithEmptyLowonganList_ShouldReturnCorrectStatistics() {
        // Arrange
        String mahasiswaId = "3";

        when(dashboardRepository.countOpenLowongan()).thenReturn(10L);
        when(dashboardRepository.countAcceptedLowonganByMahasiswaId(mahasiswaId)).thenReturn(0L);
        when(dashboardRepository.countRejectedLowonganByMahasiswaId(mahasiswaId)).thenReturn(1L);
        when(dashboardRepository.countPendingLowonganByMahasiswaId(mahasiswaId)).thenReturn(3L);
        when(dashboardRepository.calculateTotalLogHoursByMahasiswaId(mahasiswaId)).thenReturn(0.0);
        when(dashboardRepository.calculateTotalInsentifByMahasiswaId(mahasiswaId)).thenReturn(0.0);
        when(dashboardRepository.findAcceptedLowonganByMahasiswaId(mahasiswaId)).thenReturn(Collections.emptyList());

        // Act
        MahasiswaStatisticsDTO result = dashboardService.getMahasiswaStatistics(mahasiswaId);

        // Assert
        assertEquals(10L, result.getOpenLowonganCount());
        assertEquals(0L, result.getAcceptedLowonganCount());
        assertEquals(1L, result.getRejectedLowonganCount());
        assertEquals(3L, result.getPendingLowonganCount());
        assertEquals(0.0, result.getTotalLogHours());
        assertEquals(0.0, result.getTotalInsentif());
        assertTrue(result.getAcceptedLowonganList().isEmpty());
    }

    @Test
    void getMahasiswaStatistics_WithNullLowonganList_ShouldReturnCorrectStatistics() {
        // Arrange
        String mahasiswaId = "3";

        when(dashboardRepository.countOpenLowongan()).thenReturn(10L);
        when(dashboardRepository.countAcceptedLowonganByMahasiswaId(mahasiswaId)).thenReturn(0L);
        when(dashboardRepository.countRejectedLowonganByMahasiswaId(mahasiswaId)).thenReturn(1L);
        when(dashboardRepository.countPendingLowonganByMahasiswaId(mahasiswaId)).thenReturn(3L);
        when(dashboardRepository.calculateTotalLogHoursByMahasiswaId(mahasiswaId)).thenReturn(0.0);
        when(dashboardRepository.calculateTotalInsentifByMahasiswaId(mahasiswaId)).thenReturn(0.0);
        when(dashboardRepository.findAcceptedLowonganByMahasiswaId(mahasiswaId)).thenReturn(null);

        // Act
        MahasiswaStatisticsDTO result = dashboardService.getMahasiswaStatistics(mahasiswaId);

        // Assert
        assertEquals(10L, result.getOpenLowonganCount());
        assertEquals(0L, result.getAcceptedLowonganCount());
        assertEquals(1L, result.getRejectedLowonganCount());
        assertEquals(3L, result.getPendingLowonganCount());
        assertEquals(0.0, result.getTotalLogHours());
        assertEquals(0.0, result.getTotalInsentif());
        assertTrue(result.getAcceptedLowonganList().isEmpty());
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
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> dashboardService.getStatisticsForUser(null));
        assertEquals("User cannot be null", exception.getMessage());
    }

    @Test
    void getStatisticsForUser_WhenUserRoleIsNull_ShouldThrowException() {
        // Arrange
        User userWithNullRole = new User();
        userWithNullRole.setId("1");
        userWithNullRole.setRole(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> dashboardService.getStatisticsForUser(userWithNullRole));
        assertEquals("User role cannot be null", exception.getMessage());
    }

    @Test
    void convertLowonganToDTO_WithNullLowongan_ShouldLogWarningAndReturnNull() {
        // This tests the private method indirectly through getMahasiswaStatistics
        String mahasiswaId = "3";

        // Create a list with one null Lowongan
        List<Lowongan> lowonganListWithNull = Arrays.asList((Lowongan) null);

        when(dashboardRepository.countOpenLowongan()).thenReturn(10L);
        when(dashboardRepository.countAcceptedLowonganByMahasiswaId(mahasiswaId)).thenReturn(1L);
        when(dashboardRepository.countRejectedLowonganByMahasiswaId(mahasiswaId)).thenReturn(0L);
        when(dashboardRepository.countPendingLowonganByMahasiswaId(mahasiswaId)).thenReturn(0L);
        when(dashboardRepository.calculateTotalLogHoursByMahasiswaId(mahasiswaId)).thenReturn(0.0);
        when(dashboardRepository.calculateTotalInsentifByMahasiswaId(mahasiswaId)).thenReturn(0.0);
        when(dashboardRepository.findAcceptedLowonganByMahasiswaId(mahasiswaId)).thenReturn(lowonganListWithNull);

        // Act
        MahasiswaStatisticsDTO result = dashboardService.getMahasiswaStatistics(mahasiswaId);

        // Assert - the null lowongan should be filtered out
        assertTrue(result.getAcceptedLowonganList().isEmpty());
    }

    @Test
    void convertLowonganToDTO_WithValidLowongan_ShouldReturnCorrectDTO() {
        // Arrange
        String mahasiswaId = "3";
        Lowongan lowongan = createLowonganWithCompleteData();
        List<Lowongan> lowonganList = Arrays.asList(lowongan);

        when(dashboardRepository.countOpenLowongan()).thenReturn(10L);
        when(dashboardRepository.countAcceptedLowonganByMahasiswaId(mahasiswaId)).thenReturn(1L);
        when(dashboardRepository.countRejectedLowonganByMahasiswaId(mahasiswaId)).thenReturn(0L);
        when(dashboardRepository.countPendingLowonganByMahasiswaId(mahasiswaId)).thenReturn(0L);
        when(dashboardRepository.calculateTotalLogHoursByMahasiswaId(mahasiswaId)).thenReturn(0.0);
        when(dashboardRepository.calculateTotalInsentifByMahasiswaId(mahasiswaId)).thenReturn(0.0);
        when(dashboardRepository.findAcceptedLowonganByMahasiswaId(mahasiswaId)).thenReturn(lowonganList);

        // Act
        MahasiswaStatisticsDTO result = dashboardService.getMahasiswaStatistics(mahasiswaId);

        // Assert
        assertEquals(1, result.getAcceptedLowonganList().size());
        LowonganDTO dto = result.getAcceptedLowonganList().get(0);
        assertEquals("test-id", dto.getId());
        assertEquals(2024, dto.getTahunAjaran());
        assertEquals("Ganjil", dto.getSemester());
    }

    @Test
    void convertLowonganToDTO_WithLowonganWithoutSlashInTahunAjaran_ShouldSetTahunAjaranToZero() {
        // Arrange
        String mahasiswaId = "3";
        Lowongan lowongan = createLowonganWithInvalidTahunAjaran();
        List<Lowongan> lowonganList = Arrays.asList(lowongan);

        when(dashboardRepository.countOpenLowongan()).thenReturn(10L);
        when(dashboardRepository.countAcceptedLowonganByMahasiswaId(mahasiswaId)).thenReturn(1L);
        when(dashboardRepository.countRejectedLowonganByMahasiswaId(mahasiswaId)).thenReturn(0L);
        when(dashboardRepository.countPendingLowonganByMahasiswaId(mahasiswaId)).thenReturn(0L);
        when(dashboardRepository.calculateTotalLogHoursByMahasiswaId(mahasiswaId)).thenReturn(0.0);
        when(dashboardRepository.calculateTotalInsentifByMahasiswaId(mahasiswaId)).thenReturn(0.0);
        when(dashboardRepository.findAcceptedLowonganByMahasiswaId(mahasiswaId)).thenReturn(lowonganList);

        // Act
        MahasiswaStatisticsDTO result = dashboardService.getMahasiswaStatistics(mahasiswaId);

        // Assert
        assertEquals(1, result.getAcceptedLowonganList().size());
        LowonganDTO dto = result.getAcceptedLowonganList().get(0);
        assertEquals(0, dto.getTahunAjaran()); // Should be 0 for invalid format
    }

    @Test
    void convertLowonganToDTO_WithNullFields_ShouldHandleGracefully() {
        // Arrange
        String mahasiswaId = "3";
        Lowongan lowongan = createLowonganWithNullFields();
        List<Lowongan> lowonganList = Arrays.asList(lowongan);

        when(dashboardRepository.countOpenLowongan()).thenReturn(10L);
        when(dashboardRepository.countAcceptedLowonganByMahasiswaId(mahasiswaId)).thenReturn(1L);
        when(dashboardRepository.countRejectedLowonganByMahasiswaId(mahasiswaId)).thenReturn(0L);
        when(dashboardRepository.countPendingLowonganByMahasiswaId(mahasiswaId)).thenReturn(0L);
        when(dashboardRepository.calculateTotalLogHoursByMahasiswaId(mahasiswaId)).thenReturn(0.0);
        when(dashboardRepository.calculateTotalInsentifByMahasiswaId(mahasiswaId)).thenReturn(0.0);
        when(dashboardRepository.findAcceptedLowonganByMahasiswaId(mahasiswaId)).thenReturn(lowonganList);

        // Act
        MahasiswaStatisticsDTO result = dashboardService.getMahasiswaStatistics(mahasiswaId);

        // Assert
        assertEquals(1, result.getAcceptedLowonganList().size());
        LowonganDTO dto = result.getAcceptedLowonganList().get(0);
        assertEquals("", dto.getId()); // Should be empty string for null ID
        assertEquals("", dto.getMataKuliahName()); // Should be empty string for null MataKuliah
        assertEquals(0, dto.getTahunAjaran()); // Should be 0 for null tahunAjaran
        assertEquals("", dto.getSemester()); // Should be empty string for null semester
    }

    private Lowongan createLowongan(UUID id, String mataKuliahName) {
        MataKuliah mataKuliah = MataKuliah.builder()
                .namaMataKuliah(mataKuliahName)
                .kodeMataKuliah("MK-" + mataKuliahName.substring(0, 3).toUpperCase())
                .build();

        return Lowongan.builder()
                .id(id.toString())
                .mataKuliah(mataKuliah)
                .tahunAjaran("2024/2025")
                .semester("Ganjil")
                .build();
    }

    private Lowongan createLowonganWithCompleteData() {
        MataKuliah mataKuliah = MataKuliah.builder()
                .namaMataKuliah("Test Course")
                .kodeMataKuliah("TEST")
                .build();

        return Lowongan.builder()
                .id("test-id")
                .mataKuliah(mataKuliah)
                .tahunAjaran("2024/2025")
                .semester("Ganjil")
                .build();
    }

    private Lowongan createLowonganWithInvalidTahunAjaran() {
        MataKuliah mataKuliah = MataKuliah.builder()
                .namaMataKuliah("Test Course")
                .kodeMataKuliah("TEST")
                .build();

        return Lowongan.builder()
                .id("test-id")
                .mataKuliah(mataKuliah)
                .tahunAjaran("2024") // No slash
                .semester("Ganjil")
                .build();
    }

    private Lowongan createLowonganWithNullFields() {
        return Lowongan.builder()
                .id(null)
                .mataKuliah(null)
                .tahunAjaran(null)
                .semester(null)
                .build();
    }
}