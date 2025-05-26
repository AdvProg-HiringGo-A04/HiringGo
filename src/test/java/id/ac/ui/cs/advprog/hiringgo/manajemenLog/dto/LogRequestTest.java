package id.ac.ui.cs.advprog.hiringgo.manajemenLog.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

import id.ac.ui.cs.advprog.hiringgo.manajemenLog.enums.TipeKategori;

public class LogRequestTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidLogRequest_NoViolations() {
        LogRequest request = LogRequest.builder()
                .judul("Log Seru")
                .keterangan("Ikut rapat")
                .kategori(TipeKategori.OTHER)
                .waktuMulai(LocalTime.of(9, 0))
                .waktuSelesai(LocalTime.of(11, 0))
                .tanggalLog(LocalDate.now())
                .pesan("Semangat ya")
                .mataKuliahId("matakuliah1")
                .lowonganId("lowongan1")
                .build();

        Set<ConstraintViolation<LogRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testEmptyJudul_ValidationError() {
        LogRequest request = LogRequest.builder()
                .judul("")
                .kategori(TipeKategori.OTHER)
                .waktuMulai(LocalTime.of(9, 0))
                .waktuSelesai(LocalTime.of(11, 0))
                .tanggalLog(LocalDate.now())
                .mataKuliahId("IF1234")
                .lowonganId("lowongan1")
                .build();

        Set<ConstraintViolation<LogRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());

        boolean hasJudulViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("judul"));

        assertTrue(hasJudulViolation);
    }

    @Test
    public void testNullFields_ValidationErrors() {
        LogRequest request = new LogRequest();

        Set<ConstraintViolation<LogRequest>> violations = validator.validate(request);

        assertTrue(violations.size() >= 1);

        // Check specific violations
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("judul")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("mataKuliahId")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("lowonganId")));
    }
}
