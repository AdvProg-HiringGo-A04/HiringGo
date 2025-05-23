package id.ac.ui.cs.advprog.hiringgo.manajemenLog.model;

import id.ac.ui.cs.advprog.hiringgo.entity.Log;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.model.enums.StatusLog;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.model.enums.TipeKategori;

public class LogTest {
    @Test
    public void testLogBuilder_CreatesValidLogObject() {
        LocalDate today = LocalDate.now();
        Log log = Log.builder()
                .judul("Mengoreksi kuis 1")
                .keterangan("mengoreksi saja")
                .kategori(TipeKategori.MENGOREKSI)
                .waktuMulai(LocalTime.of(8, 0))
                .waktuSelesai(LocalTime.of(10, 0))
                .tanggalLog(today)
                .pesan("pesan untuk dosen")
                .status(StatusLog.DIPROSES)
                .mataKuliahId("matakuliah1")
                .mahasiswaId("mahasiswa1")
                .createdAt(today)
                .build();

        assertEquals("Mengoreksi kuis 1", log.getJudul());
        assertEquals("mengoreksi saja", log.getKeterangan());
        assertEquals(TipeKategori.MENGOREKSI, log.getKategori());
        assertEquals(LocalTime.of(8, 0), log.getWaktuMulai());
        assertEquals(LocalTime.of(10, 0), log.getWaktuSelesai());
        assertEquals(today, log.getTanggalLog());
        assertEquals("pesan untuk dosen", log.getPesan());
        assertEquals(StatusLog.DIPROSES, log.getStatus());
        assertEquals("matakuliah1", log.getMataKuliahId());
        assertEquals("mahasiswa1", log.getMahasiswaId());
        assertEquals(today, log.getCreatedAt());
        assertNull(log.getUpdatedAt());
    }
}
