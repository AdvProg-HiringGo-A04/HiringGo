package id.ac.ui.cs.advprog.hiringgo.common.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String judul;
    private String keterangan;

    @Enumerated(EnumType.STRING)
    private LogCategory kategori;

    private LocalTime waktuMulai;
    private LocalTime waktuSelesai;
    private LocalDate tanggalLog;
    private String pesanUntukDosen;

    @Enumerated(EnumType.STRING)
    private LogStatus status;

    @ManyToOne
    private User mahasiswa;

    @ManyToOne
    private Lowongan lowongan;
}