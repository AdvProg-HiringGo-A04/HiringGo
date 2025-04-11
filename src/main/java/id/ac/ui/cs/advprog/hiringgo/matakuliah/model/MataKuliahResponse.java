package id.ac.ui.cs.advprog.hiringgo.matakuliah.model;

import id.ac.ui.cs.advprog.hiringgo.matakuliah.entity.Dosen;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MataKuliahResponse {

    private String namaMataKuliah;

    private String kodeMataKuliah;

    private String deskripsiMataKuliah;

    private List<Dosen> dosenPengampu;
}
