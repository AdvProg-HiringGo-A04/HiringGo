package id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.entity;

import jakarta.persistence.*;
import lombok.*;

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

    private String tahunAjaran; // Contoh "2024/2025"

    @Enumerated(EnumType.STRING)
    private Semester semester; // GANJIL or GENAP

    private int jumlahDibutuhkan;

    private int jumlahPendaftar;

    private int jumlahDiterima;

    public enum Semester {
        GANJIL, GENAP
    }
}