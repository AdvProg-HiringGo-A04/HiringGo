package id.ac.ui.cs.advprog.hiringgo.periksalog.dto;

import id.ac.ui.cs.advprog.hiringgo.manajemenLog.model.enums.StatusLog;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LogStatusUpdateDTOTest {

    @Test
    void createLogStatusUpdateDTO_ShouldCreateWithCorrectFields() {
        // Arrange & Act
        LogStatusUpdateDTO updateDTO = LogStatusUpdateDTO.builder()
                .logId("log-123")
                .status(StatusLog.DITERIMA)
                .build();

        // Assert
        assertEquals("log-123", updateDTO.getLogId());
        assertEquals(StatusLog.DITERIMA, updateDTO.getStatus());
    }
}