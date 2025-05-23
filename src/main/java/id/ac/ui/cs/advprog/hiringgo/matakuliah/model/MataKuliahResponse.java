package id.ac.ui.cs.advprog.hiringgo.matakuliah.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import id.ac.ui.cs.advprog.hiringgo.entity.Dosen;
import id.ac.ui.cs.advprog.hiringgo.matakuliah.json.DosenDeserializer;
import id.ac.ui.cs.advprog.hiringgo.matakuliah.json.DosenSerializer;
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

    @JsonDeserialize(contentUsing = DosenDeserializer.class)
    @JsonSerialize(contentUsing = DosenSerializer.class)
    private List<Dosen> dosenPengampu;
}
