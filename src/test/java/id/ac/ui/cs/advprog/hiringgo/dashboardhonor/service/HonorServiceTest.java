package id.ac.ui.cs.advprog.hiringgo.dashboardhonor.service;

import id.ac.ui.cs.advprog.hiringgo.dashboardhonor.model.HonorResponse;
import id.ac.ui.cs.advprog.hiringgo.entity.Log;
import id.ac.ui.cs.advprog.hiringgo.entity.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.entity.MataKuliah;
import id.ac.ui.cs.advprog.hiringgo.entity.Lowongan;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.enums.StatusLog;
import id.ac.ui.cs.advprog.hiringgo.repository.LogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HonorServiceTest {

    @Mock
    private LogRepository logRepository;

    @InjectMocks
    private HonorService honorService;

    private Log testLog1;
    private Log testLog2;
    private Mahasiswa testMahasiswa;
    private MataKuliah testMataKuliah;
    private Lowongan testLowongan;

    @BeforeEach
    void setUp() {
        testMahasiswa = new Mahasiswa();
        testMahasiswa.setId("mhs001");
        testMahasiswa.setNamaLengkap("John Doe");
        testMahasiswa.setNPM("2106123456");

        testMataKuliah = new MataKuliah();
        testMataKuliah.setKodeMataKuliah("CS1234");
        testMataKuliah.setNamaMataKuliah("Struktur Data");

        testLowongan = new Lowongan();
        testLowongan.setId(UUID.randomUUID().toString());
        testLowongan.setMataKuliah(testMataKuliah);
        testLowongan.setTahunAjaran("2024/2025");
        testLowongan.setSemester("GANJIL");

        testLog1 = new Log();
        testLog1.setId("log001");
        testLog1.setTanggalLog(LocalDate.of(2024, 5, 1));
        testLog1.setWaktuMulai(LocalTime.of(8, 0));
        testLog1.setWaktuSelesai(LocalTime.of(12, 0));
        testLog1.setStatus(StatusLog.DITERIMA);
        testLog1.setMahasiswa(testMahasiswa);
        testLog1.setLowongan(testLowongan);

        testLog2 = new Log();
        testLog2.setId("log002");
        testLog2.setTanggalLog(LocalDate.of(2024, 5, 2));
        testLog2.setWaktuMulai(LocalTime.of(14, 0));
        testLog2.setWaktuSelesai(LocalTime.of(17, 0));
        testLog2.setStatus(StatusLog.DITERIMA);
        testLog2.setMahasiswa(testMahasiswa);
        testLog2.setLowongan(testLowongan);
    }

    @Test
    void testGetHonorsByMahasiswaAndPeriod_WithValidLogs_ShouldReturnHonorResponses() {
        String mahasiswaId = "mhs001";
        int year = 2024;
        int month = 5;

        List<Log> mockLogs = Arrays.asList(testLog1, testLog2);
        when(logRepository.findByTanggalLogBetweenAndMahasiswaIdOrderByLowonganMataKuliahNamaMataKuliahAsc(any(LocalDate.class), any(LocalDate.class), eq(mahasiswaId)))
                .thenReturn(mockLogs);

        List<HonorResponse> result = honorService.getHonorsByMahasiswaAndPeriod(mahasiswaId, year, month);

        assertNotNull(result);
        assertEquals(2, result.size());

        HonorResponse honor1 = result.get(0);
        assertEquals(LocalDate.of(2024, 5, 1), honor1.getTanggalLog());
        assertEquals("mhs001", honor1.getMahasiswa().getId());
        assertEquals("Struktur Data", honor1.getMataKuliahNama());
        assertEquals(4.0, honor1.getTotalJam());
        assertEquals(27500.0, honor1.getHonorPerJam());
        assertEquals(110000.0, honor1.getTotalPembayaran());
        assertEquals("DIPROSES", honor1.getStatus());

        HonorResponse honor2 = result.get(1);
        assertEquals(LocalDate.of(2024, 5, 2), honor2.getTanggalLog());
        assertEquals(3.0, honor2.getTotalJam());
        assertEquals(82500.0, honor2.getTotalPembayaran());

        verify(logRepository, times(1)).findByTanggalLogBetweenAndMahasiswaIdOrderByLowonganMataKuliahNamaMataKuliahAsc(
                eq(LocalDate.of(2024, 5, 1)),
                eq(LocalDate.of(2024, 5, 31)),
                eq(mahasiswaId)
        );
    }

    @Test
    void testGetHonorsByMahasiswaAndPeriod_WithNoLogs_ShouldReturnEmptyList() {
        String mahasiswaId = "mhs001";
        int year = 2024;
        int month = 5;

        when(logRepository.findByTanggalLogBetweenAndMahasiswaIdOrderByLowonganMataKuliahNamaMataKuliahAsc(any(LocalDate.class), any(LocalDate.class), eq(mahasiswaId)))
                .thenReturn(Collections.emptyList());

        List<HonorResponse> result = honorService.getHonorsByMahasiswaAndPeriod(mahasiswaId, year, month);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetHonorsByMahasiswaAndPeriod_WithNullLogs_ShouldReturnEmptyList() {
        String mahasiswaId = "mhs001";
        int year = 2024;
        int month = 5;

        when(logRepository.findByTanggalLogBetweenAndMahasiswaIdOrderByLowonganMataKuliahNamaMataKuliahAsc(any(LocalDate.class), any(LocalDate.class), eq(mahasiswaId)))
                .thenReturn(null);

        List<HonorResponse> result = honorService.getHonorsByMahasiswaAndPeriod(mahasiswaId, year, month);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetHonorsByMahasiswaAndPeriod_WithRepositoryException_ShouldReturnEmptyList() {
        String mahasiswaId = "mhs001";
        int year = 2024;
        int month = 5;

        when(logRepository.findByTanggalLogBetweenAndMahasiswaIdOrderByLowonganMataKuliahNamaMataKuliahAsc(any(LocalDate.class), any(LocalDate.class), eq(mahasiswaId)))
                .thenThrow(new RuntimeException("Database error"));

        List<HonorResponse> result = honorService.getHonorsByMahasiswaAndPeriod(mahasiswaId, year, month);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetHonorsByMahasiswaAndPeriod_WithLogHavingNullTimes_ShouldReturnZeroHours() {
        String mahasiswaId = "mhs001";
        int year = 2024;
        int month = 5;

        Log logWithNullTimes = new Log();
        logWithNullTimes.setId("log003");
        logWithNullTimes.setTanggalLog(LocalDate.of(2024, 5, 3));
        logWithNullTimes.setWaktuMulai(null);
        logWithNullTimes.setWaktuSelesai(null);
        logWithNullTimes.setStatus(StatusLog.DITERIMA);
        logWithNullTimes.setMahasiswa(testMahasiswa);
        logWithNullTimes.setLowongan(testLowongan);

        when(logRepository.findByTanggalLogBetweenAndMahasiswaIdOrderByLowonganMataKuliahNamaMataKuliahAsc(any(LocalDate.class), any(LocalDate.class), eq(mahasiswaId)))
                .thenReturn(Arrays.asList(logWithNullTimes));

        List<HonorResponse> result = honorService.getHonorsByMahasiswaAndPeriod(mahasiswaId, year, month);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(0.0, result.get(0).getTotalJam());
        assertEquals(0.0, result.get(0).getTotalPembayaran());
    }

    @Test
    void testGetHonorsByMahasiswaAndPeriod_WithLogHavingNullLowongan_ShouldUseMataKuliahName() {
        String mahasiswaId = "mhs001";
        int year = 2024;
        int month = 5;

        Log logWithNullLowongan = new Log();
        logWithNullLowongan.setId("log004");
        logWithNullLowongan.setTanggalLog(LocalDate.of(2024, 5, 4));
        logWithNullLowongan.setWaktuMulai(LocalTime.of(9, 0));
        logWithNullLowongan.setWaktuSelesai(LocalTime.of(11, 0));
        logWithNullLowongan.setStatus(StatusLog.DITERIMA);
        logWithNullLowongan.setMahasiswa(testMahasiswa);
        logWithNullLowongan.setLowongan(null); // Null lowongan

        when(logRepository.findByTanggalLogBetweenAndMahasiswaIdOrderByLowonganMataKuliahNamaMataKuliahAsc(any(LocalDate.class), any(LocalDate.class), eq(mahasiswaId)))
                .thenReturn(Arrays.asList(logWithNullLowongan));

        List<HonorResponse> result = honorService.getHonorsByMahasiswaAndPeriod(mahasiswaId, year, month);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Unknown", result.get(0).getMataKuliahNama());
        assertEquals(2.0, result.get(0).getTotalJam());
    }

    @Test
    void testGetHonorsByMahasiswaAndPeriod_WithLogHavingNullMataKuliah_ShouldReturnUnknown() {
        String mahasiswaId = "mhs001";
        int year = 2024;
        int month = 5;

        Log logWithNullMataKuliah = new Log();
        logWithNullMataKuliah.setId("log005");
        logWithNullMataKuliah.setTanggalLog(LocalDate.of(2024, 5, 5));
        logWithNullMataKuliah.setWaktuMulai(LocalTime.of(10, 0));
        logWithNullMataKuliah.setWaktuSelesai(LocalTime.of(12, 0));
        logWithNullMataKuliah.setStatus(StatusLog.DITERIMA);
        logWithNullMataKuliah.setMahasiswa(testMahasiswa);
        logWithNullMataKuliah.setLowongan(null);

        when(logRepository.findByTanggalLogBetweenAndMahasiswaIdOrderByLowonganMataKuliahNamaMataKuliahAsc(any(LocalDate.class), any(LocalDate.class), eq(mahasiswaId)))
                .thenReturn(Arrays.asList(logWithNullMataKuliah));

        List<HonorResponse> result = honorService.getHonorsByMahasiswaAndPeriod(mahasiswaId, year, month);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Unknown", result.get(0).getMataKuliahNama());
    }

    @Test
    void testGetHonorsByMahasiswaAndPeriod_WithDifferentMonths_ShouldCallRepositoryWithCorrectDates() {
        String mahasiswaId = "mhs001";
        int year = 2024;
        int month = 2;

        when(logRepository.findByTanggalLogBetweenAndMahasiswaIdOrderByLowonganMataKuliahNamaMataKuliahAsc(any(LocalDate.class), any(LocalDate.class), eq(mahasiswaId)))
                .thenReturn(Collections.emptyList());

        honorService.getHonorsByMahasiswaAndPeriod(mahasiswaId, year, month);

        verify(logRepository, times(1)).findByTanggalLogBetweenAndMahasiswaIdOrderByLowonganMataKuliahNamaMataKuliahAsc(
                eq(LocalDate.of(2024, 2, 1)),
                eq(LocalDate.of(2024, 2, 29)),
                eq(mahasiswaId)
        );
    }

    @Test
    void testGetHonorsByMahasiswaAndPeriod_WithFilteredNullLogs_ShouldSkipNullEntries() {
        String mahasiswaId = "mhs001";
        int year = 2024;
        int month = 5;

        List<Log> logsWithNull = Arrays.asList(testLog1, null, testLog2, null);
        when(logRepository.findByTanggalLogBetweenAndMahasiswaIdOrderByLowonganMataKuliahNamaMataKuliahAsc(any(LocalDate.class), any(LocalDate.class), eq(mahasiswaId)))
                .thenReturn(logsWithNull);

        List<HonorResponse> result = honorService.getHonorsByMahasiswaAndPeriod(mahasiswaId, year, month);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("log001", testLog1.getId());
        assertEquals("log002", testLog2.getId());
    }

    @Test
    void testCalculateHoursFromLog_WithValidTimes_ShouldCalculateCorrectly() {
        String mahasiswaId = "mhs001";
        int year = 2024;
        int month = 5;

        Log logWithHalfHour = new Log();
        logWithHalfHour.setId("log006");
        logWithHalfHour.setTanggalLog(LocalDate.of(2024, 5, 6));
        logWithHalfHour.setWaktuMulai(LocalTime.of(9, 0));
        logWithHalfHour.setWaktuSelesai(LocalTime.of(11, 30));
        logWithHalfHour.setStatus(StatusLog.DITERIMA);
        logWithHalfHour.setMahasiswa(testMahasiswa);
        logWithHalfHour.setLowongan(testLowongan);

        when(logRepository.findByTanggalLogBetweenAndMahasiswaIdOrderByLowonganMataKuliahNamaMataKuliahAsc(any(LocalDate.class), any(LocalDate.class), eq(mahasiswaId)))
                .thenReturn(Arrays.asList(logWithHalfHour));

        List<HonorResponse> result = honorService.getHonorsByMahasiswaAndPeriod(mahasiswaId, year, month);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(2.5, result.get(0).getTotalJam());
        assertEquals(68750.0, result.get(0).getTotalPembayaran());
    }
}