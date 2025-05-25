package id.ac.ui.cs.advprog.hiringgo.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MahasiswaStatisticsDTO {
    private long openLowonganCount;
    private long acceptedLowonganCount;
    private long rejectedLowonganCount;
    private long pendingLowonganCount;
    private double totalLogHours;
    private double totalInsentif;
    private List<LowonganDTO> acceptedLowonganList;
}