package id.ac.ui.cs.advprog.hiringgo.manajemenLog.validation.validators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

import id.ac.ui.cs.advprog.hiringgo.manajemenLog.dto.LogRequest;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.validation.LogValidator;

public class TimeValidatorTest {
    private LogValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new TimeValidator();
    }

    @Test
    public void testValidLogRequest_NoErrors() {
        LogRequest request = LogRequest.builder()
                .waktuMulai(LocalTime.of(9, 0))
                .waktuSelesai(LocalTime.of(10, 0))
                .tanggalLog(LocalDate.now())
                .build();

        Map<String, String> errors = validator.validate(request);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testWaktuMulaiAfterWaktuSelesai_RangeWaktuError() {
        LogRequest request = LogRequest.builder()
                .waktuMulai(LocalTime.of(12, 0))
                .waktuSelesai(LocalTime.of(10, 0))
                .tanggalLog(LocalDate.now())
                .build();

        Map<String, String> errors = validator.validate(request);
        assertEquals("Waktu mulai harus sebelum waktu selesai", errors.get("rangeWaktu"));
    }

    @Test
    public void testWaktuMulaiEqualsWaktuSelesai_RangeWaktuError() {
        LogRequest request = LogRequest.builder()
                .waktuMulai(LocalTime.of(12, 0))
                .waktuSelesai(LocalTime.of(12, 0))
                .tanggalLog(LocalDate.now())
                .build();

        Map<String, String> errors = validator.validate(request);
        assertEquals("Waktu mulai harus sebelum waktu selesai", errors.get("rangeWaktu"));
    }

    @Test
    public void testTanggalLogInFuture_TanggalLogError() {
        LogRequest request = LogRequest.builder()
                .waktuMulai(LocalTime.of(9, 0))
                .waktuSelesai(LocalTime.of(10, 0))
                .tanggalLog(LocalDate.now().plusDays(1))
                .build();

        Map<String, String> errors = validator.validate(request);
        assertEquals("Tanggal log tidak boleh di masa depan", errors.get("tanggalLog"));
    }

    @Test
    public void testNullWaktuMulai_WaktuMulaiError() {
        LogRequest request = LogRequest.builder()
                .waktuMulai(null)
                .waktuSelesai(LocalTime.of(10, 0))
                .tanggalLog(LocalDate.now())
                .build();

        Map<String, String> errors = validator.validate(request);
        assertEquals("Waktu mulai tidak boleh kosong", errors.get("waktuMulai"));
    }

    @Test
    public void testNullWaktuSelesai_WaktuSelesaiError() {
        LogRequest request = LogRequest.builder()
                .waktuMulai(LocalTime.of(10, 0))
                .waktuSelesai(null)
                .tanggalLog(LocalDate.now())
                .build();

        Map<String, String> errors = validator.validate(request);
        assertEquals("Waktu selesai tidak boleh kosong", errors.get("waktuSelesai"));
    }

    @Test
    public void testNullTanggalLog_TanggalLogError() {
        LogRequest request = LogRequest.builder()
                .waktuMulai(LocalTime.of(9, 0))
                .waktuSelesai(LocalTime.of(10, 0))
                .tanggalLog(null)
                .build();

        Map<String, String> errors = validator.validate(request);
        assertEquals("Tanggal log tidak boleh kosong", errors.get("tanggalLog"));
    }
}
