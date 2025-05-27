package id.ac.ui.cs.advprog.hiringgo.periksalog.service;

import id.ac.ui.cs.advprog.hiringgo.manajemenLog.enums.StatusLog;
import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogStatusUpdateDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidationServiceTest {

    private ValidationService validationService;

    @Mock
    private PeriksaLogService periksaLogService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validationService = new ValidationService();
    }

    @Test
    void testValidateDosenIdWithValidId() {
        assertDoesNotThrow(() -> validationService.validateDosenId("dosen-123"));
    }

    @Test
    void testValidateDosenIdWithNullId() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> validationService.validateDosenId(null));
        assertEquals("Dosen ID cannot be null or empty", exception.getMessage());
    }

    @Test
    void testValidateDosenIdWithEmptyId() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> validationService.validateDosenId(""));
        assertEquals("Dosen ID cannot be null or empty", exception.getMessage());
    }

    @Test
    void testValidateDosenIdWithWhitespaceId() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> validationService.validateDosenId("   "));
        assertEquals("Dosen ID cannot be null or empty", exception.getMessage());
    }

    @Test
    void testValidateLogIdWithValidId() {
        assertDoesNotThrow(() -> validationService.validateLogId("log-123"));
    }

    @Test
    void testValidateLogIdWithNullId() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> validationService.validateLogId(null));
        assertEquals("Log ID cannot be null or empty", exception.getMessage());
    }

    @Test
    void testValidateLogIdWithEmptyId() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> validationService.validateLogId(""));
        assertEquals("Log ID cannot be null or empty", exception.getMessage());
    }

    @Test
    void testValidateLogIdWithWhitespaceId() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> validationService.validateLogId("   "));
        assertEquals("Log ID cannot be null or empty", exception.getMessage());
    }

    @Test
    void testValidateLogStatusUpdateDTOWithValidDTO() {
        LogStatusUpdateDTO dto = LogStatusUpdateDTO.builder()
                .logId("log-123")
                .status(StatusLog.DITERIMA)
                .build();

        assertDoesNotThrow(() -> validationService.validateLogStatusUpdateDTO(dto));
    }

    @Test
    void testValidateLogStatusUpdateDTOWithNullDTO() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> validationService.validateLogStatusUpdateDTO(null));
        assertEquals("LogStatusUpdateDTO cannot be null", exception.getMessage());
    }

    @Test
    void testValidateLogStatusUpdateDTOWithNullLogId() {
        LogStatusUpdateDTO dto = LogStatusUpdateDTO.builder()
                .logId(null)
                .status(StatusLog.DITERIMA)
                .build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> validationService.validateLogStatusUpdateDTO(dto));
        assertEquals("Log ID cannot be null or empty", exception.getMessage());
    }

    @Test
    void testValidateLogStatusUpdateDTOWithEmptyLogId() {
        LogStatusUpdateDTO dto = LogStatusUpdateDTO.builder()
                .logId("")
                .status(StatusLog.DITERIMA)
                .build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> validationService.validateLogStatusUpdateDTO(dto));
        assertEquals("Log ID cannot be null or empty", exception.getMessage());
    }

    @Test
    void testValidateLogStatusUpdateDTOWithNullStatus() {
        LogStatusUpdateDTO dto = LogStatusUpdateDTO.builder()
                .logId("log-123")
                .status(null)
                .build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> validationService.validateLogStatusUpdateDTO(dto));
        assertEquals("Status cannot be null", exception.getMessage());
    }

    @Test
    void testValidateInputsWithValidInputs() {
        LogStatusUpdateDTO dto = LogStatusUpdateDTO.builder()
                .logId("log-123")
                .status(StatusLog.DITERIMA)
                .build();

        assertDoesNotThrow(() -> validationService.validateInputs("dosen-123", dto));
    }

    @Test
    void testValidateInputsWithInvalidDosenId() {
        LogStatusUpdateDTO dto = LogStatusUpdateDTO.builder()
                .logId("log-123")
                .status(StatusLog.DITERIMA)
                .build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> validationService.validateInputs(null, dto));
        assertEquals("Dosen ID cannot be null or empty", exception.getMessage());
    }

    @Test
    void testValidateInputsWithInvalidDTO() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> validationService.validateInputs("dosen-123", null));
        assertEquals("LogStatusUpdateDTO cannot be null", exception.getMessage());
    }

    @Test
    void testValidateDosenOwnershipWhenOwnershipExists() {
        String logId = "log-123";
        String dosenId = "dosen-123";

        when(periksaLogService.isLogOwnedByDosen(logId, dosenId)).thenReturn(true);

        assertDoesNotThrow(() -> validationService.validateDosenOwnership(logId, dosenId, periksaLogService));

        verify(periksaLogService).isLogOwnedByDosen(logId, dosenId);
    }

    @Test
    void testValidateDosenOwnershipWhenOwnershipDoesNotExist() {
        String logId = "log-123";
        String dosenId = "dosen-123";

        when(periksaLogService.isLogOwnedByDosen(logId, dosenId)).thenReturn(false);

        SecurityException exception = assertThrows(SecurityException.class,
                () -> validationService.validateDosenOwnership(logId, dosenId, periksaLogService));
        assertEquals("You don't have permission to update this log", exception.getMessage());

        verify(periksaLogService).isLogOwnedByDosen(logId, dosenId);
    }
}