package id.ac.ui.cs.advprog.hiringgo.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DosenStatisticsDTO {
    private long totalMataKuliah;
    private long totalMahasiswaAssistant;
    private long openLowonganCount;
}