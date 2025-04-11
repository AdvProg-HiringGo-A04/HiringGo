package id.ac.ui.cs.advprog.hiringgo.matakuliah.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "mata_kuliah")
public class MataKuliah {

    @Column(name = "nama_mata_kuliah")
    private String namaMataKuliah;

    @Id
    @Column(name = "kode_mata_kuliah")
    private String kodeMataKuliah;

    @Column(name = "deskripsi_mata_kuliah")
    private String deskripsiMataKuliah;

    @ManyToMany
    @JoinTable(
            name = "mengampu_mata_kuliah",
            joinColumns = @JoinColumn(name = "kode_mata_kuliah"),
            inverseJoinColumns = @JoinColumn(name = "nip")
    )
    private List<Dosen> dosenPengampu;

}
