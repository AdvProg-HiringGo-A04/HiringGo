package id.ac.ui.cs.advprog.hiringgo.manajemenLog.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

import id.ac.ui.cs.advprog.hiringgo.manajemenLog.model.enums.StatusLog;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.model.enums.TipeKategori;

@Entity
@Table(name = "log")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Log {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    
    @Column(nullable = false)
    private String judul;
    
    @Column(columnDefinition = "TEXT")
    private String keterangan;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipeKategori kategori;
    
    @Column(nullable = false)
    private LocalTime waktuMulai;
    
    @Column(nullable = false)
    private LocalTime waktuSelesai;
    
    @Column(nullable = false)
    private LocalDate tanggalLog;
    
    @Column(name = "pesan")
    private String pesan;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusLog status;
    
    @Column(nullable = false)
    private String mataKuliahId;
    
    @Column(nullable = false)
    private String mahasiswaId;
    
    @Column(nullable = false)
    private LocalDate createdAt;
    
    @Column
    private LocalDate updatedAt;
}
