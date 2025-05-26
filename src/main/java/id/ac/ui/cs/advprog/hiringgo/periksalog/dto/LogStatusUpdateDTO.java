package id.ac.ui.cs.advprog.hiringgo.periksalog.dto;

import id.ac.ui.cs.advprog.hiringgo.manajemenLog.enums.StatusLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogStatusUpdateDTO {

    @NotBlank(message = "Log ID cannot be blank")
    private String logId;

    @NotNull(message = "Status cannot be null")
    private StatusLog status;
}