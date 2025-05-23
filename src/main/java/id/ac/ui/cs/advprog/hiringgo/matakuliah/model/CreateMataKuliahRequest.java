package id.ac.ui.cs.advprog.hiringgo.matakuliah.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import id.ac.ui.cs.advprog.hiringgo.entity.Dosen;
import id.ac.ui.cs.advprog.hiringgo.matakuliah.json.DosenDeserializer;
import id.ac.ui.cs.advprog.hiringgo.matakuliah.json.DosenSerializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @JsonDeserialize(contentUsing = DosenDeserializer.class)
    @JsonSerialize(contentUsing = DosenSerializer.class)
    private List<Dosen> dosenPengampu;
}
