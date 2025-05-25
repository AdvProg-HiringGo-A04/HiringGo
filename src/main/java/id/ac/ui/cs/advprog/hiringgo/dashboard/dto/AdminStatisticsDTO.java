package id.ac.ui.cs.advprog.hiringgo.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminStatisticsDTO {
    private long totalDosen;
    private long totalMahasiswa;
    private long totalMataKuliah;
    private long totalLowongan;
}