package id.ac.ui.cs.advprog.hiringgo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mahasiswa_diterima_lowongan")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(MahasiswaDiterimaLowonganId.class)
public class MahasiswaDiterimaLowongan {
    @Id
    @Column(name = "mahasiswa_id", length = 36)
    private String mahasiswaId;

    @Id
    @Column(name = "lowongan_id", length = 10)
    private String lowonganId;
}
