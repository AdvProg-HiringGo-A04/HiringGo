package id.ac.ui.cs.advprog.hiringgo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "mahasiswa_pendaftar_lowongan")
public class PendaftarLowongan {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "lowongan_id", nullable = false)
    private Lowongan lowongan;

    @ManyToOne
    private Mahasiswa mahasiswa;

    @Column(name = "jumlah_sks")
    private int jumlahSks;

    @Column(name = "ipk")
    private double ipk;

    @Column(name = "diterima")
    private boolean diterima;
}
