package id.ac.ui.cs.advprog.hiringgo.manajemenLog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.quality.Strictness;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;

import id.ac.ui.cs.advprog.hiringgo.manajemenLog.dto.LogRequest;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.dto.LogResponse;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.exception.InvalidLogException;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.exception.LogNotFoundException;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.model.Log;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.model.enums.StatusLog;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.model.enums.TipeKategori;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.repository.LogRepository;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.validation.LogValidatorFactory;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.validation.validators.TimeValidator;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class LogServiceTest {
    @Mock
    private LogRepository logRepository;

    @Mock
    private LogValidatorFactory validatorFactory;

    @InjectMocks
    private LogServiceImpl logService;

    private LogRequest request;
    private Log log;
    private final String mahasiswaId = "mahasiswa1";
    private final String mataKuliahId = "matakuliah1";

    @BeforeEach
    void setUp() {
        request = LogRequest.builder()
                .judul("judul")
                .keterangan("asistensi")
                .kategori(TipeKategori.ASISTENSI)
                .waktuMulai(LocalTime.of(9, 0))
                .waktuSelesai(LocalTime.of(11, 0))
                .tanggalLog(LocalDate.now())
                .pesan("halo")
                .mataKuliahId(mataKuliahId)
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
                .mataKuliahId(mataKuliahId)
                .mahasiswaId(mahasiswaId)
                .createdAt(LocalDate.now())
                .build();
        
        TimeValidator timeValidator = new TimeValidator();
        when(validatorFactory.createValidators()).thenReturn(Collections.singletonList(timeValidator));
        
        when(logRepository.existsByMataKuliahIdAndMahasiswaId(anyString(), anyString())).thenReturn(true);
        
        when(logRepository.findByMataKuliahIdAndMahasiswaIdOrderByTanggalLogDescWaktuMulaiDesc(mataKuliahId, mahasiswaId))
            .thenReturn(Collections.singletonList(log));
    }

    @Test
    void testGetAllLogs() {
        List<LogResponse> results = logService.getAllLogs(mataKuliahId, mahasiswaId);
        
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(log.getJudul(), results.get(0).getJudul());
        verify(logRepository).findByMataKuliahIdAndMahasiswaIdOrderByTanggalLogDescWaktuMulaiDesc(mataKuliahId, mahasiswaId);
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
    void testGetLogById_DifferentMahasiswa() {
        String differentMahasiswaId = "mahasiswa2";
        when(logRepository.findById("log1")).thenReturn(Optional.of(log));
        
        assertThrows(LogNotFoundException.class, () -> 
            logService.getLogById("log1", differentMahasiswaId));
        verify(logRepository, never()).delete(any(Log.class));
    }

    @Test
    void testCreateLog() {
        when(logRepository.save(any(Log.class))).thenReturn(log);
        LogResponse result = logService.createLog(request, mahasiswaId);
        
        assertNotNull(result);
        assertEquals(log.getJudul(), result.getJudul());
        assertEquals(StatusLog.DIPROSES, result.getStatus());
        verify(logRepository, times(1)).save(any(Log.class));
    }

    @Test
    void testCreateLog_InvalidLog() {
        LogRequest invalidRequest = LogRequest.builder()
                .judul("judul")
                .kategori(TipeKategori.ASISTENSI)
                .waktuMulai(LocalTime.of(11, 0))
                .waktuSelesai(LocalTime.of(9, 0))
                .tanggalLog(LocalDate.now())
                .mataKuliahId(mataKuliahId)
                .build();
        
        assertThrows(InvalidLogException.class, () -> logService.createLog(invalidRequest, mahasiswaId));
        verify(logRepository, never()).save(any(Log.class));
    }
    
    @Test
    void testCreateLog_NotEnrolled() {
        when(logRepository.existsByMataKuliahIdAndMahasiswaId(mataKuliahId, mahasiswaId)).thenReturn(false);
        
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
    void testUpdateLog_DifferentMahasiswa() {
        String differentMahasiswaId = "mahasiswa2";
        when(logRepository.findById("log1")).thenReturn(Optional.of(log));
        
        assertThrows(LogNotFoundException.class, () -> logService.updateLog("log1", request, differentMahasiswaId));
        verify(logRepository, never()).save(any(Log.class));
    }

    @Test
    void testUpdateLog_DifferentMataKuliah() {
        LogRequest differentRequest = LogRequest.builder()
                .judul("judul")
                .keterangan("asistensi")
                .kategori(TipeKategori.ASISTENSI)
                .waktuMulai(LocalTime.of(9, 0))
                .waktuSelesai(LocalTime.of(11, 0))
                .tanggalLog(LocalDate.now())
                .mataKuliahId("matakuliah999")
                .build();
        when(logRepository.findById("log1")).thenReturn(Optional.of(log));
        
        assertThrows(InvalidLogException.class, () -> 
            logService.updateLog("log1", differentRequest, mahasiswaId));
            
        verify(logRepository, never()).delete(any(Log.class));
    }
    
    @Test
    void testUpdateLog_AlreadyProcessed() {
        Log processedLog = Log.builder()
                .id("log1")
                .judul("judul")
                .kategori(TipeKategori.ASISTENSI)
                .status(StatusLog.DITERIMA)  // Sudah diterima
                .mataKuliahId(mataKuliahId)
                .mahasiswaId(mahasiswaId)
                .createdAt(LocalDate.now())
                .build();
                
        when(logRepository.findById("log1")).thenReturn(Optional.of(processedLog));
        
        assertThrows(InvalidLogException.class, () -> logService.updateLog("log1", request, mahasiswaId));
        verify(logRepository, never()).save(any(Log.class));
    }

    @Test
    void testDeleteLog() {
        when(logRepository.findById("log1")).thenReturn(Optional.of(log));
        logService.deleteLog("log1", mataKuliahId, mahasiswaId);

        verify(logRepository, times(1)).delete(log);
    }

    @Test
    void testDeleteLog_LogNotFound() {
        when(logRepository.findById("log999")).thenReturn(Optional.empty());
        
        assertThrows(LogNotFoundException.class, () -> logService.deleteLog("log999", mataKuliahId, mahasiswaId));
        verify(logRepository, never()).delete(any(Log.class));
    }
    
    @Test
    void testDeleteLog_DifferentMahasiswa() {
        String differentMahasiswaId = "mahasiswa2";
        when(logRepository.findById("log1")).thenReturn(Optional.of(log));
        
        assertThrows(LogNotFoundException.class, () -> 
            logService.deleteLog("log1", mataKuliahId, differentMahasiswaId));
        verify(logRepository, never()).delete(any(Log.class));
    }

    @Test
    void testDeleteLog_DifferentMataKuliah() {
        String differentMataKuliahId = "matakuliah999";
        when(logRepository.findById("log1")).thenReturn(Optional.of(log));
        
        assertThrows(InvalidLogException.class, () -> 
            logService.deleteLog("log1", differentMataKuliahId, mahasiswaId));
            
        verify(logRepository, never()).delete(any(Log.class));
    }
    
    @Test
    void testDeleteLog_AlreadyProcessed() {
        Log processedLog = Log.builder()
                .id("log1")
                .judul("judul")
                .kategori(TipeKategori.ASISTENSI)
                .status(StatusLog.DITERIMA)  // Sudah diterima
                .mataKuliahId(mataKuliahId)
                .mahasiswaId(mahasiswaId)
                .createdAt(LocalDate.now())
                .build();
                
        when(logRepository.findById("log1")).thenReturn(Optional.of(processedLog));
        
        assertThrows(InvalidLogException.class, () -> 
            logService.deleteLog("log1", mataKuliahId, mahasiswaId));
        verify(logRepository, never()).delete(any(Log.class));
    }
}