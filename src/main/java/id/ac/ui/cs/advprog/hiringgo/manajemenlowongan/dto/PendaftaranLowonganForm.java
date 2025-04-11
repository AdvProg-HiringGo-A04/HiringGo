package id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PendaftaranLowonganForm {

    @NotNull
    private UUID lowonganId;

    @Min(0)
    @Max(24)
    private int jumlahSks;

    @Min(0)
    @Max(4)
    private double ipk;
}
