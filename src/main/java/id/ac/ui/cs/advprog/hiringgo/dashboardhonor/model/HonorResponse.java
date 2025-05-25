package id.ac.ui.cs.advprog.hiringgo.dashboardhonor.model;

import id.ac.ui.cs.advprog.hiringgo.entity.Mahasiswa;
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

    private LocalDate tanggalLog;
    private Mahasiswa mahasiswa;
    private String mataKuliahNama;
    private Double totalJam;
    private Double honorPerJam;
    private Double totalPembayaran;
    private String status;

    public String getFormattedHonor() {
        return String.format("Rp %,.0f", totalPembayaran);
    }

    public String getFormattedJam() {
        if (totalJam == totalJam.intValue()) {
            return String.format("%d jam", totalJam.intValue());
        }
        return String.format("%.1f jam", totalJam);
    }
}
