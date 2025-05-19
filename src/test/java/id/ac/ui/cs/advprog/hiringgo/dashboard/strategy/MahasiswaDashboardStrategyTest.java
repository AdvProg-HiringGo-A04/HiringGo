package id.ac.ui.cs.advprog.hiringgo.dashboard.strategy;

import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.entity.Lowongan;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.LowonganDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.MahasiswaStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.repository.DashboardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class MahasiswaDashboardStrategyTest {

    @Mock
    private DashboardRepository dashboardRepository;

    private MahasiswaDashboardStrategy strategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        strategy = new MahasiswaDashboardStrategy(dashboardRepository);
    }

    @Test
    void calculateStatistics_ShouldReturnCorrectStatistics() {
        // Arrange
        String mahasiswaId = "1";

        // Mock repository methods
        when(dashboardRepository.countOpenLowongan()).thenReturn(5L);
        when(dashboardRepository.countAcceptedLowonganByMahasiswaId(mahasiswaId)).thenReturn(2L);
        when(dashboardRepository.countRejectedLowonganByMahasiswaId(mahasiswaId)).thenReturn(1L);
        when(dashboardRepository.countPendingLowonganByMahasiswaId(mahasiswaId)).thenReturn(3L);
        when(dashboardRepository.calculateTotalLogHoursByMahasiswaId(mahasiswaId)).thenReturn(25.5);
        when(dashboardRepository.calculateTotalInsentifByMahasiswaId(mahasiswaId)).thenReturn(701250.0); // 25.5 * 27500

        // Create mock data for lowongan
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        Lowongan l1 = Lowongan.builder()
                .id(id1)
                .mataKuliah("Advanced Programming")
                .tahunAjaran("2023/2024")
                .semester("GANJIL")
                .build();

        Lowongan l2 = Lowongan.builder()
                .id(id2)
                .mataKuliah("Web Programming")
                .tahunAjaran("2023/2024")
                .semester("GENAP")
                .build();

        List<Lowongan> acceptedLowongan = Arrays.asList(l1, l2);
        when(dashboardRepository.findAcceptedLowonganByMahasiswaId(mahasiswaId)).thenReturn(acceptedLowongan);

        // Act
        MahasiswaStatisticsDTO result = strategy.calculateStatistics(mahasiswaId);

        // Assert
        assertEquals(5L, result.getOpenLowonganCount());
        assertEquals(2L, result.getAcceptedLowonganCount());
        assertEquals(1L, result.getRejectedLowonganCount());
        assertEquals(3L, result.getPendingLowonganCount());
        assertEquals(25.5, result.getTotalLogHours());
        assertEquals(701250.0, result.getTotalInsentif());

        // Verify lowongan DTO list
        assertEquals(2, result.getAcceptedLowonganList().size());

        LowonganDTO dto1 = result.getAcceptedLowonganList().get(0);
        assertEquals(id1.toString(), dto1.getId());
        assertEquals("Advanced Programming", dto1.getMataKuliahName());
        // Note: mataKuliahCode is not available in the new lowongan model
        assertEquals(2023, dto1.getTahunAjaran());
        assertEquals("GANJIL", dto1.getSemester());

        LowonganDTO dto2 = result.getAcceptedLowonganList().get(1);
        assertEquals(id2.toString(), dto2.getId());
        assertEquals("Web Programming", dto2.getMataKuliahName());
        // Note: mataKuliahCode is not available in the new lowongan model
        assertEquals(2023, dto2.getTahunAjaran());
        assertEquals("GENAP", dto2.getSemester());
    }
}