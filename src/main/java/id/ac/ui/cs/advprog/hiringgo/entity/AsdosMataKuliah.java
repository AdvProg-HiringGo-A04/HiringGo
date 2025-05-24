package id.ac.ui.cs.advprog.hiringgo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "asdos_mata_kuliah")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(AsdosMataKuliahId.class)
public class AsdosMataKuliah {
    @Id
    @Column(name = "mahasiswa_id", length = 36)
    private String mahasiswaId;

    @Id
    @Column(name = "mata_kuliah_id", length = 10)
    private String mataKuliahId;
}
