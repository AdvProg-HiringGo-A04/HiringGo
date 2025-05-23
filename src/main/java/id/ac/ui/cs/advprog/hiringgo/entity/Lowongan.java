package id.ac.ui.cs.advprog.hiringgo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lowongan {

    @Id
    @GeneratedValue
    private UUID id;

    private String mataKuliah;

    private String tahunAjaran;

    private String semester;

    private int jumlahDibutuhkan;

    private int jumlahPendaftar;

    private int jumlahDiterima;

    @Builder.Default
    @OneToMany(mappedBy = "lowongan", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PendaftarLowongan> pendaftar = new ArrayList<>();

}