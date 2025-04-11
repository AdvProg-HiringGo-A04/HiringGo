package id.ac.ui.cs.advprog.hiringgo.periksalog.dto;

import id.ac.ui.cs.advprog.hiringgo.common.model.LogCategory;
import id.ac.ui.cs.advprog.hiringgo.common.model.LogStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class LogDTOTest {

    @Test
    void createLogDTO_ShouldCreateWithCorrectFields() {
        // Arrange & Act
        LogDTO logDTO = LogDTO.builder()
                .id(1L)
                .judul("Test Log")
                .keterangan("Test Description")
                .kategori(LogCategory.ASISTENSI)
                .waktuMulai(LocalTime.of(9, 0))
                .waktuSelesai(LocalTime.of(11, 0))
                .tanggalLog(LocalDate.now())
                .pesanUntukDosen("Test message")
                .status(LogStatus.PENDING)
                .mahasiswaName("Student Name")
                .mataKuliahName("Subject Name")
                .mataKuliahCode("CS001")
                .durationInHours(2.0)
                .build();

        // Assert
        assertEquals(1L, logDTO.getId());
        assertEquals("Test Log", logDTO.getJudul());
        assertEquals("Test Description", logDTO.getKeterangan());
        assertEquals(LogCategory.ASISTENSI, logDTO.getKategori());
        assertEquals(LocalTime.of(9, 0), logDTO.getWaktuMulai());
        assertEquals(LocalTime.of(11, 0), logDTO.getWaktuSelesai());
        assertEquals(LocalDate.now(), logDTO.getTanggalLog());
        assertEquals("Test message", logDTO.getPesanUntukDosen());
        assertEquals(LogStatus.PENDING, logDTO.getStatus());
        assertEquals("Student Name", logDTO.getMahasiswaName());
        assertEquals("Subject Name", logDTO.getMataKuliahName());
        assertEquals("CS001", logDTO.getMataKuliahCode());
        assertEquals(2.0, logDTO.getDurationInHours());
    }
}