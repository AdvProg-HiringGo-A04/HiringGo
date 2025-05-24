package id.ac.ui.cs.advprog.hiringgo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
            inverseJoinColumns = @JoinColumn(name = "id")
    )
    private List<Dosen> dosenPengampu;
}
