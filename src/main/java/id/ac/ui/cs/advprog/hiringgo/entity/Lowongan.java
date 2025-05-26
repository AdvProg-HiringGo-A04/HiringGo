package id.ac.ui.cs.advprog.hiringgo.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "lowongan")
public class Lowongan {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "kode_mata_kuliah", referencedColumnName = "kode_mata_kuliah")
    private MataKuliah mataKuliah;

    @Column(name = "tahun_ajaran")
    private String tahunAjaran;

    @Column(name = "semester")
    private String semester;

    @Column(name = "jumlah_dibutuhkan")
    private int jumlahDibutuhkan;

    @Builder.Default
    @OneToMany(mappedBy = "lowongan", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PendaftarLowongan> pendaftar = new ArrayList<>();
}
