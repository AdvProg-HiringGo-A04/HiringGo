package id.ac.ui.cs.advprog.hiringgo.manajemenakun.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void userShouldHaveCorrectFields() {
        User user = new User("1", "user@example.com", "password", "ROLE_USER");

        assertEquals("1", user.getId());
        assertEquals("user@example.com", user.getEmail());
        assertEquals("ROLE_USER", user.getRole());
        assertEquals("-", user.getNamaLengkap(), "Default user should return '-' for Nama Lengkap");
    }
}