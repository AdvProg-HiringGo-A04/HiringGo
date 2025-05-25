package id.ac.ui.cs.advprog.hiringgo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MahasiswaDiterimaLowonganId implements Serializable {
    private String mahasiswaId;
    private String lowonganId;
}

