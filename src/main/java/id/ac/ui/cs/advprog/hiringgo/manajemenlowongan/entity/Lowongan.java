package id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import id.ac.ui.cs.advprog.hiringgo.entity.MataKuliah;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lowongan {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kode_mata_kuliah", nullable = false) // FK to MataKuliah
    private MataKuliah mataKuliah;

    private String tahunAjaran; // Contoh "2024/2025"

    private String semester; // GANJIL or GENAP

    private int jumlahDibutuhkan;

    @Builder.Default
    @OneToMany(mappedBy = "lowongan", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PendaftarLowongan> pendaftar = new ArrayList<>();

    public int getJumlahPendaftar() {
        return pendaftar.size();
    }

    public int getJumlahDiterima() {
        return (int) pendaftar.stream().filter(PendaftarLowongan::isDiterima).count();
    }

}
