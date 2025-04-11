package id.ac.ui.cs.advprog.hiringgo.periksalog.dto;

import id.ac.ui.cs.advprog.hiringgo.common.model.LogStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LogStatusUpdateDTOTest {

    @Test
    void createLogStatusUpdateDTO_ShouldCreateWithCorrectFields() {
        // Arrange & Act
        LogStatusUpdateDTO updateDTO = LogStatusUpdateDTO.builder()
                .logId(1L)
                .status(LogStatus.APPROVED)
                .build();

        // Assert
        assertEquals(1L, updateDTO.getLogId());
        assertEquals(LogStatus.APPROVED, updateDTO.getStatus());
    }
}