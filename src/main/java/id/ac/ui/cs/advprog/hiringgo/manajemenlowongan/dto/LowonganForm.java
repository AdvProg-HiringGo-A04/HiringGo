package id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LowonganForm {

    @NotBlank
    private String mataKuliah;

    @NotBlank
    private String tahunAjaran;

    @NotNull
    private String semester;

    @NotNull
    private Integer jumlahAsistenDibutuhkan;
}
