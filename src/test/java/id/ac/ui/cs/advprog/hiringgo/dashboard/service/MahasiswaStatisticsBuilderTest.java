package id.ac.ui.cs.advprog.hiringgo.dashboard.service;

import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.LowonganDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.MahasiswaStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.repository.DashboardRepository;
import id.ac.ui.cs.advprog.hiringgo.entity.Lowongan;
import id.ac.ui.cs.advprog.hiringgo.entity.MataKuliah;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MahasiswaStatisticsBuilderTest {

    @Mock
    private DashboardRepository repository;

    @Mock
    private Logger log;

    @Test
    void build_WithValidMahasiswaId_ShouldReturnStatistics() {
        // Given
        String mahasiswaId = "mahasiswa-123";
        Lowongan lowongan = createMockLowongan();
        List<Lowongan> acceptedLowongan = Arrays.asList(lowongan);

        when(repository.findAcceptedLowonganByMahasiswaId(mahasiswaId)).thenReturn(acceptedLowongan);
        when(repository.countOpenLowongan()).thenReturn(10L);
        when(repository.countAcceptedLowonganByMahasiswaId(mahasiswaId)).thenReturn(2L);
        when(repository.countRejectedLowonganByMahasiswaId(mahasiswaId)).thenReturn(1L);
        when(repository.countPendingLowonganByMahasiswaId(mahasiswaId)).thenReturn(3L);
        when(repository.calculateTotalLogHoursByMahasiswaId(mahasiswaId)).thenReturn(40.5);
        when(repository.calculateTotalInsentifByMahasiswaId(mahasiswaId)).thenReturn(1113750.0);

        // When
        MahasiswaStatisticsDTO result = MahasiswaStatisticsBuilder.build(mahasiswaId, repository, log);

        // Then
        assertNotNull(result);
        assertEquals(10L, result.getOpenLowonganCount());
        assertEquals(2L, result.getAcceptedLowonganCount());
        assertEquals(1L, result.getRejectedLowonganCount());
        assertEquals(3L, result.getPendingLowonganCount());
        assertEquals(40.5, result.getTotalLogHours());
        assertEquals(1113750.0, result.getTotalInsentif());
        assertNotNull(result.getAcceptedLowonganList());
        assertEquals(1, result.getAcceptedLowonganList().size());
    }

    @Test
    void build_WithNullMahasiswaId_ShouldReturnEmptyStatistics() {
        // When
        MahasiswaStatisticsDTO result = MahasiswaStatisticsBuilder.build(null, repository, log);

        // Then
        assertNotNull(result);
        assertEquals(0L, result.getOpenLowonganCount());
        assertEquals(0L, result.getAcceptedLowonganCount());
        assertEquals(0L, result.getRejectedLowonganCount());
        assertEquals(0L, result.getPendingLowonganCount());
        assertEquals(0.0, result.getTotalLogHours());
        assertEquals(0.0, result.getTotalInsentif());
        assertTrue(result.getAcceptedLowonganList().isEmpty());
        verify(log).warn("Attempted to get mahasiswa statistics with null or empty mahasiswaId");
        verifyNoInteractions(repository);
    }

    @Test
    void build_WithEmptyMahasiswaId_ShouldReturnEmptyStatistics() {
        // When
        MahasiswaStatisticsDTO result = MahasiswaStatisticsBuilder.build("", repository, log);

        // Then
        assertNotNull(result);
        assertEquals(0L, result.getOpenLowonganCount());
        assertEquals(0L, result.getAcceptedLowonganCount());
        assertEquals(0L, result.getRejectedLowonganCount());
        assertEquals(0L, result.getPendingLowonganCount());
        assertEquals(0.0, result.getTotalLogHours());
        assertEquals(0.0, result.getTotalInsentif());
        assertTrue(result.getAcceptedLowonganList().isEmpty());
        verify(log).warn("Attempted to get mahasiswa statistics with null or empty mahasiswaId");
        verifyNoInteractions(repository);
    }

    private Lowongan createMockLowongan() {
        Lowongan lowongan = new Lowongan();
        lowongan.setId("lowongan-123");
        lowongan.setTahunAjaran("2023/2024");
        lowongan.setSemester("Ganjil");

        MataKuliah mataKuliah = new MataKuliah();
        mataKuliah.setNamaMataKuliah("Advanced Programming");
        lowongan.setMataKuliah(mataKuliah);

        return lowongan;
    }
}