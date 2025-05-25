package id.ac.ui.cs.advprog.hiringgo.manajemenLog.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import id.ac.ui.cs.advprog.hiringgo.entity.Lowongan;
import id.ac.ui.cs.advprog.hiringgo.entity.MataKuliah;
import id.ac.ui.cs.advprog.hiringgo.entity.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.dto.LogRequest;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.dto.LogResponse;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.exception.InvalidLogException;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.exception.LogNotFoundException;
import id.ac.ui.cs.advprog.hiringgo.entity.Log;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.model.enums.StatusLog;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.model.enums.TipeKategori;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.repository.LogRepository;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.validation.LogValidator;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.validation.LogValidatorFactory;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.repository.AsdosMataKuliahRepository;
import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.repository.LowonganRepository;
import id.ac.ui.cs.advprog.hiringgo.repository.MahasiswaRepository;
import id.ac.ui.cs.advprog.hiringgo.repository.MataKuliahRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class LogServiceTest {
    @Mock
    private LogRepository logRepository;

    @Mock
    private AsdosMataKuliahRepository asdosRepository;

    @Mock
    private LogValidatorFactory validatorFactory;

    @Mock
    private MahasiswaRepository mahasiswaRepository;

    @Mock
    private MataKuliahRepository mataKuliahRepository;

    @Mock
    private LowonganRepository lowonganRepository;

    @InjectMocks
    private LogServiceImpl logService;

    private LogRequest request;
    private Log log;
    private MataKuliah mataKuliah;
    private Mahasiswa mahasiswa;
    private Lowongan lowongan;
    private final String mahasiswaId = "mahasiswa1";
    private final String mataKuliahId = "matakuliah1";
    private final String lowonganId = "lowongan1";

    @BeforeEach
    void setUp() {
        // Create mock entities
        mataKuliah = new MataKuliah();
        mataKuliah.setKodeMataKuliah(mataKuliahId);

        mahasiswa = new Mahasiswa();
        mahasiswa.setId(mahasiswaId);

        lowongan = new Lowongan();
        lowongan.setId(lowonganId);

        request = LogRequest.builder()
                .judul("judul")
                .keterangan("asistensi")
                .kategori(TipeKategori.ASISTENSI)
                .waktuMulai(LocalTime.of(9, 0))
                .waktuSelesai(LocalTime.of(11, 0))
                .tanggalLog(LocalDate.now())
                .pesan("halo")
                .mataKuliahId(mataKuliahId)
                .lowonganId(lowonganId)
                .build();

        log = Log.builder()
                .id("log1")
                .judul("judul")
                .keterangan("asistensi")
                .kategori(TipeKategori.ASISTENSI)
                .waktuMulai(LocalTime.of(9, 0))
                .waktuSelesai(LocalTime.of(11, 0))
                .tanggalLog(LocalDate.now())
                .status(StatusLog.DIPROSES)
                .mataKuliah(mataKuliah)  // Use entity object
                .mahasiswa(mahasiswa)    // Use entity object
                .lowongan(lowongan)      // Use entity object
                .createdAt(LocalDate.now())
                .build();

        // Mock repository calls
        when(asdosRepository.existsByMahasiswaIdAndLowonganId(anyString(), anyString())).thenReturn(true);
        when(logRepository.findByLowonganIdAndMahasiswaIdOrderByTanggalLogDescWaktuMulaiDesc(lowonganId, mahasiswaId))
                .thenReturn(Collections.singletonList(log));
        when(mataKuliahRepository.findById(mataKuliahId)).thenReturn(Optional.of(mataKuliah));
        when(mahasiswaRepository.findById(mahasiswaId)).thenReturn(Optional.of(mahasiswa));
        when(lowonganRepository.findById(lowonganId)).thenReturn(Optional.of(lowongan));
        when(validatorFactory.createValidators()).thenReturn(Collections.emptyList());
    }

    @Test
    void testGetAllLogs() {
        List<LogResponse> results = logService.getAllLogs(lowonganId, mahasiswaId);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(log.getJudul(), results.get(0).getJudul());
        verify(logRepository).findByLowonganIdAndMahasiswaIdOrderByTanggalLogDescWaktuMulaiDesc(lowonganId, mahasiswaId);
    }

    @Test
    void testGetLogById() {
        when(logRepository.findById("log1")).thenReturn(Optional.of(log));

        LogResponse result = logService.getLogById("log1", mahasiswaId);

        assertNotNull(result);
        assertEquals(log.getJudul(), result.getJudul());
        verify(logRepository).findById("log1");
    }

    @Test
    void testGetLogById_LogNotFound() {
        when(logRepository.findById("log999")).thenReturn(Optional.empty());

        assertThrows(LogNotFoundException.class, () -> logService.getLogById("log999", mahasiswaId));
    }

    @Test
    void testGetLogById_UnauthorizedAccess() {
        when(logRepository.findById("log1")).thenReturn(Optional.of(log));

        assertThrows(LogNotFoundException.class, () -> logService.getLogById("log1", "unauthorized_mahasiswa"));
    }

    @Test
    void testCreateLog() {
        when(logRepository.save(any(Log.class))).thenReturn(log);

        LogResponse result = logService.createLog(request, mahasiswaId);

        assertNotNull(result);
        assertEquals(log.getJudul(), result.getJudul());
        assertEquals(StatusLog.DIPROSES, result.getStatus());
        verify(logRepository, times(1)).save(any(Log.class));
        verify(mataKuliahRepository).findById(mataKuliahId);
        verify(mahasiswaRepository).findById(mahasiswaId);
        verify(lowonganRepository).findById(lowonganId);
    }

    @Test
    void testCreateLog_InvalidLog() {
        // Mock validator to return validation errors
        LogValidator mockValidator = mock(LogValidator.class);
        when(mockValidator.validate(any(LogRequest.class)))
                .thenReturn(Map.of("waktuSelesai", "Waktu selesai harus setelah waktu mulai"));
        when(validatorFactory.createValidators()).thenReturn(List.of(mockValidator));

        LogRequest invalidRequest = LogRequest.builder()
                .judul("judul")
                .kategori(TipeKategori.ASISTENSI)
                .waktuMulai(LocalTime.of(11, 0))
                .waktuSelesai(LocalTime.of(9, 0))
                .tanggalLog(LocalDate.now())
                .mataKuliahId(mataKuliahId)
                .lowonganId(lowonganId)
                .build();

        assertThrows(InvalidLogException.class, () -> logService.createLog(invalidRequest, mahasiswaId));
        verify(logRepository, never()).save(any(Log.class));
    }

    @Test
    void testCreateLog_NotEnrolled() {
        when(asdosRepository.existsByMahasiswaIdAndLowonganId(mahasiswaId, lowonganId)).thenReturn(false);

        assertThrows(InvalidLogException.class, () -> logService.createLog(request, mahasiswaId));
        verify(logRepository, never()).save(any(Log.class));
    }

    @Test
    void testCreateLog_MataKuliahNotFound() {
        when(mataKuliahRepository.findById(mataKuliahId)).thenReturn(Optional.empty());

        assertThrows(InvalidLogException.class, () -> logService.createLog(request, mahasiswaId));
        verify(logRepository, never()).save(any(Log.class));
    }

    @Test
    void testUpdateLog() {
        when(logRepository.findById("log1")).thenReturn(Optional.of(log));
        when(logRepository.save(any(Log.class))).thenReturn(log);

        LogResponse result = logService.updateLog("log1", request, mahasiswaId);

        assertNotNull(result);
        assertEquals(log.getJudul(), result.getJudul());
        verify(logRepository, times(1)).save(any(Log.class));
    }

    @Test
    void testUpdateLog_LogNotFound() {
        when(logRepository.findById("log999")).thenReturn(Optional.empty());

        assertThrows(LogNotFoundException.class, () -> logService.updateLog("log999", request, mahasiswaId));
        verify(logRepository, never()).save(any(Log.class));
    }

    @Test
    void testUpdateLog_UnauthorizedAccess() {
        when(logRepository.findById("log1")).thenReturn(Optional.of(log));

        assertThrows(LogNotFoundException.class, () -> logService.updateLog("log1", request, "unauthorized_mahasiswa"));
        verify(logRepository, never()).save(any(Log.class));
    }

    @Test
    void testUpdateLog_DifferentMataKuliah() {
        when(logRepository.findById("log1")).thenReturn(Optional.of(log));

        LogRequest differentMataKuliahRequest = LogRequest.builder()
                .judul("judul")
                .keterangan("asistensi")
                .kategori(TipeKategori.ASISTENSI)
                .waktuMulai(LocalTime.of(9, 0))
                .waktuSelesai(LocalTime.of(11, 0))
                .tanggalLog(LocalDate.now())
                .pesan("halo")
                .mataKuliahId("different_matakuliah")
                .lowonganId(lowonganId)
                .build();

        assertThrows(InvalidLogException.class, () -> logService.updateLog("log1", differentMataKuliahRequest, mahasiswaId));
        verify(logRepository, never()).save(any(Log.class));
    }

    @Test
    void testUpdateLog_NonDiprosesStatus() {
        Log approvedLog = Log.builder()
                .id("log1")
                .judul("judul")
                .status(StatusLog.DITOLAK)
                .mataKuliah(mataKuliah)
                .mahasiswa(mahasiswa)
                .lowongan(lowongan)
                .build();

        when(logRepository.findById("log1")).thenReturn(Optional.of(approvedLog));

        assertThrows(InvalidLogException.class, () -> logService.updateLog("log1", request, mahasiswaId));
        verify(logRepository, never()).save(any(Log.class));
    }

    @Test
    void testDeleteLog() {
        when(logRepository.findById("log1")).thenReturn(Optional.of(log));

        logService.deleteLog("log1", lowonganId, mahasiswaId);

        verify(logRepository, times(1)).delete(log);
    }

    @Test
    void testDeleteLog_LogNotFound() {
        when(logRepository.findById("log999")).thenReturn(Optional.empty());

        assertThrows(LogNotFoundException.class, () -> logService.deleteLog("log999", lowonganId, mahasiswaId));
        verify(logRepository, never()).delete(any(Log.class));
    }

    @Test
    void testDeleteLog_UnauthorizedAccess() {
        when(logRepository.findById("log1")).thenReturn(Optional.of(log));

        assertThrows(LogNotFoundException.class, () -> logService.deleteLog("log1", lowonganId, "unauthorized_mahasiswa"));
        verify(logRepository, never()).delete(any(Log.class));
    }

    @Test
    void testDeleteLog_DifferentLowongan() {
        when(logRepository.findById("log1")).thenReturn(Optional.of(log));

        assertThrows(InvalidLogException.class, () -> logService.deleteLog("log1", "different_lowongan", mahasiswaId));
        verify(logRepository, never()).delete(any(Log.class));
    }

    @Test
    void testDeleteLog_NonDiprosesStatus() {
        Log approvedLog = Log.builder()
                .id("log1")
                .status(StatusLog.DITOLAK)
                .mataKuliah(mataKuliah)
                .mahasiswa(mahasiswa)
                .lowongan(lowongan)
                .build();

        when(logRepository.findById("log1")).thenReturn(Optional.of(approvedLog));

        assertThrows(InvalidLogException.class, () -> logService.deleteLog("log1", lowonganId, mahasiswaId));
        verify(logRepository, never()).delete(any(Log.class));
    }

    @Test
    void testGetTotalJamPerBulan() {
        Map<String, Double> result = logService.getTotalJamPerBulan(lowonganId, mahasiswaId);

        assertNotNull(result);
        assertEquals(1, result.size());

        // Get current month and year for assertion
        LocalDate now = LocalDate.now();
        String expectedKey = String.format("%02d-%d", now.getMonthValue(), now.getYear());

        assertTrue(result.containsKey(expectedKey));
        assertEquals(2.0, result.get(expectedKey), 0.001);
    }

    @Test
    void testGetTotalJamPerBulan_NotEnrolled() {
        when(asdosRepository.existsByMahasiswaIdAndLowonganId(mahasiswaId, mataKuliahId)).thenReturn(false);

        assertThrows(InvalidLogException.class, () -> logService.getTotalJamPerBulan(mataKuliahId, mahasiswaId));
    }
}