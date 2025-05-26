package id.ac.ui.cs.advprog.hiringgo.manajemenLog.model;

import id.ac.ui.cs.advprog.hiringgo.entity.Log;
import id.ac.ui.cs.advprog.hiringgo.entity.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.entity.MataKuliah;
import id.ac.ui.cs.advprog.hiringgo.entity.Lowongan;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.enums.StatusLog;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.enums.TipeKategori;

public class LogTest {

    @Test
    public void testLogBuilder_CreatesValidLogObject() {
        LocalDate today = LocalDate.now();

        MataKuliah mataKuliah = new MataKuliah();
        mataKuliah.setKodeMataKuliah("matakuliah1");

        Mahasiswa mahasiswa = new Mahasiswa();
        mahasiswa.setId("mahasiswa1");

        Lowongan lowongan = new Lowongan();
        lowongan.setId("lowongan1");
        lowongan.setMataKuliah(mataKuliah);

        Log log = Log.builder()
                .id("aecaa64a-16ce-4cff-9318-f92445fe8bb4")
                .judul("Mengoreksi kuis 1")
                .keterangan("mengoreksi saja")
                .kategori(TipeKategori.MENGOREKSI)
                .waktuMulai(LocalTime.of(8, 0))
                .waktuSelesai(LocalTime.of(10, 0))
                .tanggalLog(today)
                .pesan("pesan untuk dosen")
                .status(StatusLog.DIPROSES)
                .mahasiswa(mahasiswa)
                .lowongan(lowongan)
                .createdAt(today)
                .updatedAt(null)
                .build();

        assertEquals("Mengoreksi kuis 1", log.getJudul());
        assertEquals("mengoreksi saja", log.getKeterangan());
        assertEquals(TipeKategori.MENGOREKSI, log.getKategori());
        assertEquals(LocalTime.of(8, 0), log.getWaktuMulai());
        assertEquals(LocalTime.of(10, 0), log.getWaktuSelesai());
        assertEquals(today, log.getTanggalLog());
        assertEquals("pesan untuk dosen", log.getPesan());
        assertEquals(StatusLog.DIPROSES, log.getStatus());
        assertEquals("matakuliah1", log.getLowongan().getMataKuliah().getKodeMataKuliah());  // Test MataKuliah
        assertEquals("mahasiswa1", log.getMahasiswa().getId());  // Test Mahasiswa
        assertEquals("lowongan1", log.getLowongan().getId());  // Test Lowongan
        assertEquals(today, log.getCreatedAt());
        assertNull(log.getUpdatedAt());
    }
}