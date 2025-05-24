package id.ac.ui.cs.advprog.hiringgo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsdosMataKuliahId implements Serializable {
    private String mahasiswaId;
    private String mataKuliahId;
}

