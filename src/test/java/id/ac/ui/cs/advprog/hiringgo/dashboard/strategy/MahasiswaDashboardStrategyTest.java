// src/test/java/id/ac/ui/cs/advprog/hiringgo/dashboard/strategy/MahasiswaDashboardStrategyTest.java
package id.ac.ui.cs.advprog.hiringgo.dashboard.strategy;

import id.ac.ui.cs.advprog.hiringgo.common.model.Lowongan;
import id.ac.ui.cs.advprog.hiringgo.common.model.MataKuliah;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.LowonganDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.MahasiswaStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.repository.DashboardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

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
        Long mahasiswaId = 1L;

        // Mock repository methods
        when(dashboardRepository.countOpenLowongan()).thenReturn(5L);
        when(dashboardRepository.countAcceptedLowonganByMahasiswaId(mahasiswaId)).thenReturn(2L);
        when(dashboardRepository.countRejectedLowonganByMahasiswaId(mahasiswaId)).thenReturn(1L);
        when(dashboardRepository.countPendingLowonganByMahasiswaId(mahasiswaId)).thenReturn(3L);
        when(dashboardRepository.calculateTotalLogHoursByMahasiswaId(mahasiswaId)).thenReturn(25.5);
        when(dashboardRepository.calculateTotalInsentifByMahasiswaId(mahasiswaId)).thenReturn(701250.0); // 25.5 * 27500

        // Create mock data for lowongan
        MataKuliah mk1 = MataKuliah.builder().id(1L).name("Advanced Programming").code("CSUI-ADVPROG").build();
        MataKuliah mk2 = MataKuliah.builder().id(2L).name("Web Programming").code("CSUI-WEBPROG").build();

        Lowongan l1 = Lowongan.builder().id(1L).mataKuliah(mk1).tahunAjaran(2023).semester("GANJIL").build();
        Lowongan l2 = Lowongan.builder().id(2L).mataKuliah(mk2).tahunAjaran(2023).semester("GENAP").build();

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
        assertEquals(1L, dto1.getId());
        assertEquals("Advanced Programming", dto1.getMataKuliahName());
        assertEquals("CSUI-ADVPROG", dto1.getMataKuliahCode());
        assertEquals(2023, dto1.getTahunAjaran());
        assertEquals("GANJIL", dto1.getSemester());

        LowonganDTO dto2 = result.getAcceptedLowonganList().get(1);
        assertEquals(2L, dto2.getId());
        assertEquals("Web Programming", dto2.getMataKuliahName());
        assertEquals("CSUI-WEBPROG", dto2.getMataKuliahCode());
        assertEquals(2023, dto2.getTahunAjaran());
        assertEquals("GENAP", dto2.getSemester());
    }
}