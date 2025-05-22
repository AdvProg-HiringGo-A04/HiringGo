package id.ac.ui.cs.advprog.hiringgo.dashboardhonor.model;

import id.ac.ui.cs.advprog.hiringgo.entity.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.entity.MataKuliah;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HonorResponse {

    private LocalDate tanggalAwal;

    private LocalDate tanggalAkhir;

    private Mahasiswa mahasiswa;

    private MataKuliah mataKuliah;

    private Double totalJam;

    private Double honorPerJam;

    private Double totalPembayaran;

    private String status;
}
