package id.ac.ui.cs.advprog.hiringgo.periksalog.service;

import id.ac.ui.cs.advprog.hiringgo.entity.MataKuliah;
import id.ac.ui.cs.advprog.hiringgo.repository.MataKuliahRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MataKuliahServiceTest {

    @Mock
    private MataKuliahRepository mataKuliahRepository;

    @InjectMocks
    private MataKuliahService mataKuliahService;

    private MataKuliah mataKuliah;

    @BeforeEach
    void setUp() {
        mataKuliah = new MataKuliah();
        mataKuliah.setKodeMataKuliah("CS-001");
        mataKuliah.setNama("Advanced Programming");
    }

    @Test
    void testGetMataKuliahByKode_WhenFound_ShouldReturnMataKuliah() {
        // Arrange
        when(mataKuliahRepository.findByKodeMataKuliah("CS-001"))
                .thenReturn(Optional.of(mataKuliah));

        // Act
        MataKuliah result = mataKuliahService.getMataKuliahByKode("CS-001");

        // Assert
        assertNotNull(result);
        assertEquals("CS-001", result.getKodeMataKuliah());
        assertEquals("Advanced Programming", result.getNama());
        verify(mataKuliahRepository).findByKodeMataKuliah("CS-001");
    }

    @Test
    void testGetMataKuliahByKode_WhenNotFound_ShouldThrowException() {
        // Arrange
        when(mataKuliahRepository.findByKodeMataKuliah("INVALID"))
                .thenReturn(Optional.empty());

        // Act & Assert
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> mataKuliahService.getMataKuliahByKode("INVALID")
        );

        assertEquals("Mata kuliah tidak ditemukan", exception.getMessage());
        verify(mataKuliahRepository).findByKodeMataKuliah("INVALID");
    }
}
