package id.ac.ui.cs.advprog.hiringgo.periksalog.dto;

import id.ac.ui.cs.advprog.hiringgo.common.model.LogCategory;
import id.ac.ui.cs.advprog.hiringgo.common.model.LogStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogDTO {
    private Long id;
    private String judul;
    private String keterangan;
    private LogCategory kategori;
    private LocalTime waktuMulai;
    private LocalTime waktuSelesai;
    private LocalDate tanggalLog;
    private String pesanUntukDosen;
    private LogStatus status;
    private String mahasiswaName;
    private String mataKuliahName;
    private String mataKuliahCode;
    private double durationInHours;
}