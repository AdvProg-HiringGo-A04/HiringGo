package id.ac.ui.cs.advprog.hiringgo.matakuliah.model;

import id.ac.ui.cs.advprog.hiringgo.matakuliah.entity.Dosen;
import jakarta.validation.constraints.NotBlank;
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
public class CreateMataKuliahRequest {

    @NotBlank
    @Size(max = 100)
    private String namaMataKuliah;

    @NotBlank
    @Size(max = 10)
    private String kodeMataKuliah;

    @NotBlank
    private String deskripsiMataKuliah;

    private List<Dosen> dosenPengampu;
}
