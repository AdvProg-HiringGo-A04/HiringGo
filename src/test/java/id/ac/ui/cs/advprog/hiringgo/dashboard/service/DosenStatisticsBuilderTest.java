package id.ac.ui.cs.advprog.hiringgo.dashboard.service;

import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.DosenStatisticsDTO;
import id.ac.ui.cs.advprog.hiringgo.dashboard.repository.DashboardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DosenStatisticsBuilderTest {

    @Mock
    private DashboardRepository repository;

    @Mock
    private Logger log;

    @Test
    void build_WithValidDosenId_ShouldReturnStatistics() {
        // Given
        String dosenId = "dosen-123";
        when(repository.countMataKuliahByDosenId(dosenId)).thenReturn(3L);
        when(repository.countMahasiswaAssistantByDosenId(dosenId)).thenReturn(5L);
        when(repository.countOpenLowonganByDosenId(dosenId)).thenReturn(2L);

        // When
        DosenStatisticsDTO result = DosenStatisticsBuilder.build(dosenId, repository, log);

        // Then
        assertNotNull(result);
        assertEquals(3L, result.getTotalMataKuliah());
        assertEquals(5L, result.getTotalMahasiswaAssistant());
        assertEquals(2L, result.getOpenLowonganCount());
        verify(repository).countMataKuliahByDosenId(dosenId);
        verify(repository).countMahasiswaAssistantByDosenId(dosenId);
        verify(repository).countOpenLowonganByDosenId(dosenId);
    }

    @Test
    void build_WithNullDosenId_ShouldReturnEmptyStatistics() {
        // When
        DosenStatisticsDTO result = DosenStatisticsBuilder.build(null, repository, log);

        // Then
        assertNotNull(result);
        assertEquals(0L, result.getTotalMataKuliah());
        assertEquals(0L, result.getTotalMahasiswaAssistant());
        assertEquals(0L, result.getOpenLowonganCount());
        verify(log).warn("Attempted to get dosen statistics with null or empty dosenId");
        verifyNoInteractions(repository);
    }

    @Test
    void build_WithEmptyDosenId_ShouldReturnEmptyStatistics() {
        // When
        DosenStatisticsDTO result = DosenStatisticsBuilder.build("", repository, log);

        // Then
        assertNotNull(result);
        assertEquals(0L, result.getTotalMataKuliah());
        assertEquals(0L, result.getTotalMahasiswaAssistant());
        assertEquals(0L, result.getOpenLowonganCount());
        verify(log).warn("Attempted to get dosen statistics with null or empty dosenId");
        verifyNoInteractions(repository);
    }

    @Test
    void build_WithWhitespaceDosenId_ShouldReturnEmptyStatistics() {
        // When
        DosenStatisticsDTO result = DosenStatisticsBuilder.build("   ", repository, log);

        // Then
        assertNotNull(result);
        assertEquals(0L, result.getTotalMataKuliah());
        assertEquals(0L, result.getTotalMahasiswaAssistant());
        assertEquals(0L, result.getOpenLowonganCount());
        verify(log).warn("Attempted to get dosen statistics with null or empty dosenId");
        verifyNoInteractions(repository);
    }
}
