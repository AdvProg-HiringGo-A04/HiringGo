package id.ac.ui.cs.advprog.hiringgo.periksalog.dto;

import id.ac.ui.cs.advprog.hiringgo.manajemenLog.model.enums.StatusLog;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.model.enums.TipeKategori;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogDTO {

    @NotBlank(message = "Log ID cannot be blank")
    private String id;

    @NotBlank(message = "Title cannot be blank")
    private String judul;

    private String keterangan;

    @NotNull(message = "Category cannot be null")
    private TipeKategori kategori;

    @NotNull(message = "Start time cannot be null")
    private LocalTime waktuMulai;

    @NotNull(message = "End time cannot be null")
    private LocalTime waktuSelesai;

    @NotNull(message = "Log date cannot be null")
    private LocalDate tanggalLog;

    private String pesanUntukDosen;

    @NotNull(message = "Status cannot be null")
    private StatusLog status;

    @NotBlank(message = "Student name cannot be blank")
    private String mahasiswaName;

    @NotBlank(message = "Course name cannot be blank")
    private String mataKuliahName;

    @NotBlank(message = "Course code cannot be blank")
    private String mataKuliahCode;

    @PositiveOrZero(message = "Duration must be positive or zero")
    private double durationInHours;
}