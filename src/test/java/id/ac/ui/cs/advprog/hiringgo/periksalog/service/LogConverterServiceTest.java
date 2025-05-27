package id.ac.ui.cs.advprog.hiringgo.periksalog.service;

import id.ac.ui.cs.advprog.hiringgo.entity.Log;
import id.ac.ui.cs.advprog.hiringgo.entity.Lowongan;
import id.ac.ui.cs.advprog.hiringgo.entity.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.entity.MataKuliah;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.enums.StatusLog;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.enums.TipeKategori;
import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class LogConverterServiceTest {

    private LogConverterService logConverterService;

    @BeforeEach
    void setUp() {
        logConverterService = new LogConverterService();
    }

    @Test
    void testConvertToDTOWithValidLog() {
        // Arrange
        Mahasiswa mahasiswa = new Mahasiswa();
        mahasiswa.setNamaLengkap("John Doe");

        MataKuliah mataKuliah = new MataKuliah();
        mataKuliah.setNamaMataKuliah("Advanced Programming");
        mataKuliah.setKodeMataKuliah("CSGE601021");

        Lowongan lowongan = new Lowongan();
        lowongan.setMataKuliah(mataKuliah);

        Log log = new Log();
        log.setId("log-123");
        log.setJudul("Test Log");
        log.setKeterangan("Test Description");
        log.setKategori(TipeKategori.ASISTENSI);
        log.setWaktuMulai(LocalTime.of(9, 0));
        log.setWaktuSelesai(LocalTime.of(11, 0));
        log.setTanggalLog(LocalDate.now());
        log.setPesan("Test Message");
        log.setStatus(StatusLog.DIPROSES);
        log.setMahasiswa(mahasiswa);
        log.setLowongan(lowongan);

        // Act
        LogDTO result = logConverterService.convertToDTO(log);

        // Assert
        assertNotNull(result);
        assertEquals("log-123", result.getId());
        assertEquals("Test Log", result.getJudul());
        assertEquals("Test Description", result.getKeterangan());
        assertEquals(TipeKategori.ASISTENSI, result.getKategori());
        assertEquals(LocalTime.of(9, 0), result.getWaktuMulai());
        assertEquals(LocalTime.of(11, 0), result.getWaktuSelesai());
        assertEquals("Test Message", result.getPesanUntukDosen());
        assertEquals(StatusLog.DIPROSES, result.getStatus());
        assertEquals("John Doe", result.getMahasiswaName());
        assertEquals("Advanced Programming", result.getMataKuliahName());
        assertEquals("CSGE601021", result.getMataKuliahCode());
        assertEquals(2.0, result.getDurationInHours());
    }

    @Test
    void testConvertToDTOWithNullLog() {
        LogDTO result = logConverterService.convertToDTO(null);
        assertNull(result);
    }

    @Test
    void testConvertToDTOWithNullMahasiswa() {
        Log log = new Log();
        log.setId("log-123");
        log.setJudul("Test Log");
        log.setKategori(TipeKategori.ASISTENSI);
        log.setStatus(StatusLog.DIPROSES);
        log.setWaktuMulai(LocalTime.of(9, 0));
        log.setWaktuSelesai(LocalTime.of(11, 0));
        log.setMahasiswa(null);

        LogDTO result = logConverterService.convertToDTO(log);

        assertNotNull(result);
        assertEquals("Unknown Student", result.getMahasiswaName());
    }

    @Test
    void testConvertToDTOWithNullLowongan() {
        Mahasiswa mahasiswa = new Mahasiswa();
        mahasiswa.setNamaLengkap("John Doe");

        Log log = new Log();
        log.setId("log-123");
        log.setJudul("Test Log");
        log.setKategori(TipeKategori.ASISTENSI);
        log.setStatus(StatusLog.DIPROSES);
        log.setWaktuMulai(LocalTime.of(9, 0));
        log.setWaktuSelesai(LocalTime.of(11, 0));
        log.setMahasiswa(mahasiswa);
        log.setLowongan(null);

        LogDTO result = logConverterService.convertToDTO(log);

        assertNotNull(result);
        assertEquals("Unknown Course", result.getMataKuliahName());
        assertEquals("Unknown Code", result.getMataKuliahCode());
    }

    @Test
    void testConvertToDTOWithNullTimes() {
        Mahasiswa mahasiswa = new Mahasiswa();
        mahasiswa.setNamaLengkap("John Doe");

        Log log = new Log();
        log.setId("log-123");
        log.setJudul("Test Log");
        log.setKategori(TipeKategori.ASISTENSI);
        log.setStatus(StatusLog.DIPROSES);
        log.setWaktuMulai(null);
        log.setWaktuSelesai(null);
        log.setMahasiswa(mahasiswa);

        LogDTO result = logConverterService.convertToDTO(log);

        assertNotNull(result);
        assertEquals(0.0, result.getDurationInHours());
    }

    @Test
    void testConvertToDTOWithInvalidKategori() {
        Log log = new Log();
        log.setId("log-123");
        log.setJudul("Test Log");
        log.setKategori(null);
        log.setStatus(StatusLog.DIPROSES);
        log.setWaktuMulai(LocalTime.of(9, 0));
        log.setWaktuSelesai(LocalTime.of(11, 0));

        LogDTO result = logConverterService.convertToDTO(log);

        assertNotNull(result);
        assertEquals(TipeKategori.values()[0], result.getKategori());
    }

    @Test
    void testConvertToDTOWithInvalidStatus() {
        Log log = new Log();
        log.setId("log-123");
        log.setJudul("Test Log");
        log.setKategori(TipeKategori.ASISTENSI);
        log.setStatus(null);
        log.setWaktuMulai(LocalTime.of(9, 0));
        log.setWaktuSelesai(LocalTime.of(11, 0));

        LogDTO result = logConverterService.convertToDTO(log);

        assertNotNull(result);
        assertEquals(StatusLog.values()[0], result.getStatus());
    }

    @Test
    void testConvertToDTOWithNullKategori() {
        Log log = new Log();
        log.setId("log-123");

        LogDTO dto = logConverterService.convertToDTO(log);

        assertNotNull(dto);
        assertEquals(TipeKategori.values()[0], dto.getKategori());
    }


    @Test
    void testDurationCalculationWithVariousTimes() {
        Log log = new Log();
        log.setId("log-123");
        log.setJudul("Test Log");
        log.setKategori(TipeKategori.ASISTENSI);
        log.setStatus(StatusLog.DIPROSES);

        // Test 30 minutes = 0.5 hours
        log.setWaktuMulai(LocalTime.of(10, 0));
        log.setWaktuSelesai(LocalTime.of(10, 30));

        LogDTO result = logConverterService.convertToDTO(log);
        assertEquals(0.5, result.getDurationInHours());

        // Test 1.5 hours = 1.5 hours
        log.setWaktuMulai(LocalTime.of(9, 0));
        log.setWaktuSelesai(LocalTime.of(10, 30));

        result = logConverterService.convertToDTO(log);
        assertEquals(1.5, result.getDurationInHours());
    }

    @Test
    void testParseKategoriFromEmptyString() {
        Log log = new Log();
        log.setId("log-123");
        log.setJudul("Test Log");
        log.setStatus(StatusLog.DIPROSES);
        log.setWaktuMulai(LocalTime.of(9, 0));
        log.setWaktuSelesai(LocalTime.of(11, 0));

        // Set kategori to empty enum value to test parsing
        // This will test the parseKategoriFromString method with invalid input
        // We can't directly set an invalid enum, so we'll create a scenario where
        // the string representation might be invalid
        log.setKategori(null); // This will trigger the null check in parseKategoriFromString

        // This should handle null kategori gracefully and return default value
        LogDTO result = logConverterService.convertToDTO(log);
        assertNotNull(result);
        assertEquals(TipeKategori.values()[0], result.getKategori()); // Should return first enum value
    }

    @Test
    void testParseStatusFromEmptyString() {
        Log log = new Log();
        log.setId("log-123");
        log.setJudul("Test Log");
        log.setKategori(TipeKategori.ASISTENSI);
        log.setWaktuMulai(LocalTime.of(9, 0));
        log.setWaktuSelesai(LocalTime.of(11, 0));

        // Set status to null to test parsing
        log.setStatus(null);

        // This should handle null status gracefully and return default value
        LogDTO result = logConverterService.convertToDTO(log);
        assertNotNull(result);
        assertEquals(StatusLog.values()[0], result.getStatus()); // Should return first enum value
    }
}
