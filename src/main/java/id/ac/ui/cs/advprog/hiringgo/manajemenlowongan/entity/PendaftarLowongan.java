package id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.entity;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Mahasiswa;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.ManyToOne;
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
public class PendaftarLowongan {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private Lowongan lowongan;

    @ManyToOne
    private Mahasiswa mahasiswa; // You’ll need a Mahasiswa entity

    private int jumlahSks;

    @Column(precision = 2, scale = 2)
    private double ipk;

    private boolean diterima; // default false
}
