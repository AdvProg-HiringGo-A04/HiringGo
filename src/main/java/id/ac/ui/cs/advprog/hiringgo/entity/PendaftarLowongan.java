package id.ac.ui.cs.advprog.hiringgo.entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
    @JoinColumn(name = "lowongan_id", nullable = false)
    private id.ac.ui.cs.advprog.hiringgo.entity.Lowongan lowongan;

    @ManyToOne
    private Mahasiswa mahasiswa;

    private int jumlahSks;

    private double ipk;

    private boolean diterima; // default false
}