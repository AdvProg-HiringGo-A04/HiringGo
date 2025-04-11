package id.ac.ui.cs.advprog.hiringgo.manajemenakun.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AdminTest {

    @Test
    public void adminShouldHaveCorrectFields() {
        Admin admin = new Admin("1", "admin@example.com", "password");

        assertEquals("1", admin.getId());
        assertEquals("admin@example.com", admin.getEmail());
        assertEquals("ADMIN", admin.getRole());
        assertEquals("-", admin.getNamaLengkap(), "Admin should return '-' for nama lengkap");
    }
}