package id.ac.ui.cs.advprog.hiringgo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

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
    private String waktuMulai;

    @Column(name = "waktu_selesai")
    private String waktuSelesai;

    @Column(name = "tanggal_log")
    private Date tanggalLog;

    @Column(name = "pesan")
    private String pesan;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "mata_kuliah_id", referencedColumnName = "kode_mata_kuliah")
    private MataKuliah kodeMataKuliah;

    @ManyToOne
    @JoinColumn(name = "mahasiswa_id", referencedColumnName = "id")
    private Mahasiswa mahasiswa;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;
}
