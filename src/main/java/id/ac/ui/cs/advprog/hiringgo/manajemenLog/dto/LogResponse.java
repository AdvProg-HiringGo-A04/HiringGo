package id.ac.ui.cs.advprog.hiringgo.manajemenLog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

import id.ac.ui.cs.advprog.hiringgo.manajemenLog.model.enums.StatusLog;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.model.enums.TipeKategori;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogResponse {
    private String id;
    private String judul;
    private String keterangan;
    private TipeKategori kategori;
    private String kategoriDisplayName;
    private LocalTime waktuMulai;
    private LocalTime waktuSelesai;
    private LocalDate tanggalLog;
    private String pesan;
    private StatusLog status;
    private String statusDisplayName;
    private String mataKuliahId;
    private String mahasiswaId;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
