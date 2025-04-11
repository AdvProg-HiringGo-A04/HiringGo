package id.ac.ui.cs.advprog.hiringgo.daftarLowongan.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import id.ac.ui.cs.advprog.hiringgo.daftarLowongan.command.DaftarLowonganCommand;
import id.ac.ui.cs.advprog.hiringgo.daftarLowongan.exception.InvalidDataException;

public class DaftarLowonganServiceTest {
    @Test
    void testFailedIfIPKInvalid() {
        DaftarLowonganCommand command = new DaftarLowonganCommand(20, 4.5, 1L, 1001L);

        DaftarLowonganService service = new DaftarLowonganService();

        Exception exception = assertThrows(InvalidDataException.class, () -> {
            service.execute(command);
        });

        assertEquals("IPK harus antara 0 dan 4", exception.getMessage());
    }
}
