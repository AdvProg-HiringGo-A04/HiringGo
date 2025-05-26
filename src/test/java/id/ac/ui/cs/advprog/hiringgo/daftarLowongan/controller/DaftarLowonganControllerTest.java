package id.ac.ui.cs.advprog.hiringgo.daftarLowongan.controller;

import id.ac.ui.cs.advprog.hiringgo.common.ApiResponse;
import id.ac.ui.cs.advprog.hiringgo.daftarLowongan.command.DaftarLowonganCommand;
import id.ac.ui.cs.advprog.hiringgo.daftarLowongan.dto.DaftarLowonganRequest;
import id.ac.ui.cs.advprog.hiringgo.daftarLowongan.exception.EntityNotFoundException;
import id.ac.ui.cs.advprog.hiringgo.daftarLowongan.service.DaftarLowonganService;
import id.ac.ui.cs.advprog.hiringgo.entity.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.repository.MahasiswaRepository;
import id.ac.ui.cs.advprog.hiringgo.security.CurrentUserProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DaftarLowonganControllerTest {

    @Mock
    private DaftarLowonganService daftarLowonganService;

    @Mock
    private MahasiswaRepository mahasiswaRepository;

    @Mock
    private CurrentUserProvider currentUserProvider;

    private DaftarLowonganController daftarLowonganController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        daftarLowonganController = new DaftarLowonganController(daftarLowonganService, mahasiswaRepository, currentUserProvider);
    }

    @Test
    void testDaftarLowonganMahasiswaNotFound() {
        String lowonganId = "lowongan123";
        String mahasiswaId = "mahasiswa123";
        DaftarLowonganRequest request = new DaftarLowonganRequest();
        request.setSks(20);
        request.setIpk(3.5);

        when(currentUserProvider.getCurrentUserId()).thenReturn(mahasiswaId);
        when(mahasiswaRepository.findById(mahasiswaId)).thenReturn(Optional.empty());

        CompletableFuture<ResponseEntity<ApiResponse<Object>>> responseFuture = daftarLowonganController.daftarLowongan(lowonganId, request);
        Throwable exception = assertThrows(RuntimeException.class, responseFuture::join);

        assertInstanceOf(EntityNotFoundException.class, exception.getCause());
        assertEquals("Data tidak ditemukan", exception.getCause().getMessage());
    }

    @Test
    void testDaftarLowonganExceptionHandling() {
        String lowonganId = "lowongan123";
        String mahasiswaId = "mahasiswa123";
        DaftarLowonganRequest request = new DaftarLowonganRequest();
        request.setSks(20);
        request.setIpk(3.5);

        Mahasiswa mahasiswa = new Mahasiswa();
        mahasiswa.setId(mahasiswaId);

        when(currentUserProvider.getCurrentUserId()).thenReturn(mahasiswaId);
        when(mahasiswaRepository.findById(mahasiswaId)).thenReturn(Optional.of(mahasiswa));

        DaftarLowonganCommand command = new DaftarLowonganCommand(20, 3.5, lowonganId, mahasiswaId);
        CompletableFuture<Void> failedFuture = CompletableFuture.failedFuture(new RuntimeException("Registration failed"));
        when(daftarLowonganService.executeAsync(command)).thenReturn(failedFuture);

        CompletableFuture<ResponseEntity<ApiResponse<Object>>> responseFuture = daftarLowonganController.daftarLowongan(lowonganId, request);

        Throwable exception = assertThrows(RuntimeException.class, responseFuture::join);

        assertTrue(exception.getMessage().contains("Registration failed"));
    }

}
