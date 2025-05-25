package id.ac.ui.cs.advprog.hiringgo.periksalog.dto;

import id.ac.ui.cs.advprog.hiringgo.manajemenLog.model.enums.StatusLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogStatusUpdateDTO {
    private String logId;
    private StatusLog status;
}