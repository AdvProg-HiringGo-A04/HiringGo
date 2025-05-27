package id.ac.ui.cs.advprog.hiringgo.daftarLowongan.controller;

import id.ac.ui.cs.advprog.hiringgo.common.ApiResponse;
import id.ac.ui.cs.advprog.hiringgo.daftarLowongan.dto.DaftarLowonganRequest;
import id.ac.ui.cs.advprog.hiringgo.daftarLowongan.exception.EntityNotFoundException;
import id.ac.ui.cs.advprog.hiringgo.daftarLowongan.service.DaftarLowonganService;
import id.ac.ui.cs.advprog.hiringgo.entity.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.repository.MahasiswaRepository;
import id.ac.ui.cs.advprog.hiringgo.security.CurrentUserProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.HttpStatus;
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
    void testDaftarLowonganSuccessfully() {
        String lowonganId = "lowongan123";
        String mahasiswaId = "mahasiswa123";
        DaftarLowonganRequest request = new DaftarLowonganRequest();
        request.setSks(20);
        request.setIpk(3.5);

        Mahasiswa mahasiswa = new Mahasiswa();
        mahasiswa.setId(mahasiswaId);

        // Mocking the current user provider and the mahasiswaRepository
        when(currentUserProvider.getCurrentUserId()).thenReturn(mahasiswaId);
        when(mahasiswaRepository.findById(mahasiswaId)).thenReturn(Optional.of(mahasiswa));

        // Simulate successful registration to lowongan
        doNothing().when(daftarLowonganService).registerToLowongan(eq(request.getSks()), eq(request.getIpk()), eq(lowonganId), eq(mahasiswaId));

        // Call the controller method
        ResponseEntity<ApiResponse<Object>> response = daftarLowonganController.daftarLowongan(lowonganId, request);

        // Assert the response status is CREATED and the message is correct
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Berhasil mendaftar ke lowongan", response.getBody().getMessage());
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

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            daftarLowonganController.daftarLowongan(lowonganId, request);
        });

        assertEquals("Mahasiswa dengan id mahasiswa123 tidak ditemukan", ex.getErrors().get("mahasiswaId"));
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

        doThrow(new RuntimeException("Registration failed")).when(daftarLowonganService).registerToLowongan(eq(request.getSks()), eq(request.getIpk()), eq(lowonganId), eq(mahasiswaId));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            daftarLowonganController.daftarLowongan(lowonganId, request);
        });

        assertEquals("Registration failed", exception.getMessage());

    }

}
