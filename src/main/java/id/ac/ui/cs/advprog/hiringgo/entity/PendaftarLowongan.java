package id.ac.ui.cs.advprog.hiringgo.entity;

import java.util.UUID;

import jakarta.persistence.*;
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
    @JoinColumn(name = "mahasiswa_id", nullable = false)
    private Mahasiswa mahasiswa;

    private int jumlahSks;

    private double ipk;

    private boolean diterima; // default false
}