package id.ac.ui.cs.advprog.hiringgo.entity;

import id.ac.ui.cs.advprog.hiringgo.manajemenLog.enums.StatusLog;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.enums.TipeKategori;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "log")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Log {

    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "judul", nullable = false)
    private String judul;

    @Column(name = "keterangan", columnDefinition = "TEXT")
    private String keterangan;

    @Enumerated(EnumType.STRING)
    @Column(name = "kategori", nullable = false)
    private TipeKategori kategori;

    @Column(name = "waktu_mulai", nullable = false)
    private LocalTime waktuMulai;

    @Column(name = "waktu_selesai", nullable = false)
    private LocalTime waktuSelesai;

    @Column(name = "tanggal_log", nullable = false)
    private LocalDate tanggalLog;

    @Column(name = "pesan")
    private String pesan;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusLog status;

    @ManyToOne
    @JoinColumn(name = "mahasiswa_id", referencedColumnName = "id")
    private Mahasiswa mahasiswa;

    @ManyToOne
    @JoinColumn(name = "lowongan_id", referencedColumnName = "id")
    private Lowongan lowongan;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;
}
