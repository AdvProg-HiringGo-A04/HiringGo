package id.ac.ui.cs.advprog.hiringgo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@Table(name = "log")
@Entity
public class Log {

    @Id
    private String id;

    @Column(name = "judul")
    private String judul;

    @Column(name = "keterangan")
    private String keterangan;

    @Column(name = "kategori")
    private String kategori;

    @Column(name = "waktu_mulai")
    private LocalTime waktuMulai;

    @Column(name = "waktu_selesai")
    private LocalTime waktuSelesai;

    @Column(name = "tanggal_log")
    private LocalDate tanggalLog;

    @Column(name = "pesan")
    private String pesan;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "mahasiswa_id", referencedColumnName = "id")
    private Mahasiswa mahasiswa;
    
    @ManyToOne
    @JoinColumn(name = "lowongan_id", referencedColumnName = "id")
    private Lowongan lowongan;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;
}
