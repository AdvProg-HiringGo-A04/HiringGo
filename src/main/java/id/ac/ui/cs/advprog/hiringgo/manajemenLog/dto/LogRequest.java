package id.ac.ui.cs.advprog.hiringgo.manajemenLog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

import id.ac.ui.cs.advprog.hiringgo.manajemenLog.model.enums.TipeKategori;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogRequest {
    @NotBlank(message = "Judul log tidak boleh kosong")
    private String judul;
    
    private String keterangan;
    
    // @NotNull(message = "Kategori log tidak boleh kosong")
    private TipeKategori kategori;
    
    // @NotNull(message = "Waktu mulai tidak boleh kosong")
    private LocalTime waktuMulai;
    
    // @NotNull(message = "Waktu selesai tidak boleh kosong")
    private LocalTime waktuSelesai;
    
    // @NotNull(message = "Tanggal log tidak boleh kosong")
    private LocalDate tanggalLog;
    
    private String pesan;
    
    @NotNull(message = "ID Mata Kuliah tidak boleh kosong")
    private String mataKuliahId;
}
