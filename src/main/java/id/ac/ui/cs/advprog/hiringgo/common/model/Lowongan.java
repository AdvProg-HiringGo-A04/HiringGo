package id.ac.ui.cs.advprog.hiringgo.common.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lowongan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private MataKuliah mataKuliah;

    private int tahunAjaran;
    private String semester; // GENAP or GANJIL
    private int jumlahDibutuhkan;

    @ManyToMany
    @JoinTable(
            name = "lowongan_pendaftar",
            joinColumns = @JoinColumn(name = "lowongan_id"),
            inverseJoinColumns = @JoinColumn(name = "mahasiswa_id")
    )
    private Set<User> pendaftar = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "lowongan_diterima",
            joinColumns = @JoinColumn(name = "lowongan_id"),
            inverseJoinColumns = @JoinColumn(name = "mahasiswa_id")
    )
    private Set<User> diterima = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "lowongan_ditolak",
            joinColumns = @JoinColumn(name = "lowongan_id"),
            inverseJoinColumns = @JoinColumn(name = "mahasiswa_id")
    )
    private Set<User> ditolak = new HashSet<>();
}