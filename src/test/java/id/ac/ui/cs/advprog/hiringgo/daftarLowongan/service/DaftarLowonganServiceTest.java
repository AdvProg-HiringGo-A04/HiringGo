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

    @Test
    void testFailedIfSKSNegative() {
        DaftarLowonganCommand command = new DaftarLowonganCommand(-5, 3.0, 1L, 1001L);

        DaftarLowonganService service = new DaftarLowonganService();

        Exception exception = assertThrows(InvalidDataException.class, () -> {
            service.execute(command);
        });

        assertEquals("SKS tidak boleh negatif", exception.getMessage());
    }

    @Test
    void testSuccessForValidData() {
        DaftarLowonganCommand command = new DaftarLowonganCommand(18, 3.25, 1L, 1001L);

        DaftarLowonganService service = new DaftarLowonganService();

        // sementara anggap ga throw exception artinya sukses
        assertDoesNotThrow(() -> service.execute(command));
    }

    @Test
    void testFailedIfIPKOver4() {
        DaftarLowonganCommand command = new DaftarLowonganCommand(
                20, 4.01, 1L, 1001L
        );

        DaftarLowonganService service = new DaftarLowonganService();

        Exception exception = assertThrows(InvalidDataException.class, () -> {
            service.execute(command);
        });

        assertEquals("IPK harus antara 0 dan 4", exception.getMessage());
    }
}
