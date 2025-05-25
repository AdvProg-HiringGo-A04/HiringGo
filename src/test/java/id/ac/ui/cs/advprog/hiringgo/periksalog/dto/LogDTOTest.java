
package id.ac.ui.cs.advprog.hiringgo.periksalog.dto;

import id.ac.ui.cs.advprog.hiringgo.manajemenLog.model.enums.StatusLog;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.model.enums.TipeKategori;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class LogDTOTest {

    @Test
    void createLogDTO_ShouldCreateWithCorrectFields() {
        // Arrange & Act
        LogDTO logDTO = LogDTO.builder()
                .id("log-123")
                .judul("Test Log")
                .keterangan("Test Description")
                .kategori(TipeKategori.ASISTENSI)
                .waktuMulai(LocalTime.of(9, 0))
                .waktuSelesai(LocalTime.of(11, 0))
                .tanggalLog(LocalDate.now())
                .pesanUntukDosen("Test message")
                .status(StatusLog.DIPROSES)
                .mahasiswaName("Student Name")
                .mataKuliahName("Subject Name")
                .mataKuliahCode("CS001")
                .durationInHours(2.0)
                .build();

        // Assert
        assertEquals("log-123", logDTO.getId());
        assertEquals("Test Log", logDTO.getJudul());
        assertEquals("Test Description", logDTO.getKeterangan());
        assertEquals(TipeKategori.ASISTENSI, logDTO.getKategori());
        assertEquals(LocalTime.of(9, 0), logDTO.getWaktuMulai());
        assertEquals(LocalTime.of(11, 0), logDTO.getWaktuSelesai());
        assertEquals(LocalDate.now(), logDTO.getTanggalLog());
        assertEquals("Test message", logDTO.getPesanUntukDosen());
        assertEquals(StatusLog.DIPROSES, logDTO.getStatus());
        assertEquals("Student Name", logDTO.getMahasiswaName());
        assertEquals("Subject Name", logDTO.getMataKuliahName());
        assertEquals("CS001", logDTO.getMataKuliahCode());
        assertEquals(2.0, logDTO.getDurationInHours());
    }
}