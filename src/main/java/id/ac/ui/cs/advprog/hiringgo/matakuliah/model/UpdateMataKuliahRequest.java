package id.ac.ui.cs.advprog.hiringgo.matakuliah.model;

import id.ac.ui.cs.advprog.hiringgo.entity.Dosen;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateMataKuliahRequest {

    @Size(max = 100)
    private String namaMataKuliah;

    @Size(max = 10)
    private String kodeMataKuliah;

    private String deskripsiMataKuliah;

    private List<Dosen> dosenPengampu;
}
