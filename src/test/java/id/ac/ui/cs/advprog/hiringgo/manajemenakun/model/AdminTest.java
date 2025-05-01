package id.ac.ui.cs.advprog.hiringgo.manajemenakun.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AdminTest {

    @Test
    public void adminShouldHaveCorrectFields() {
        AccountData data = new AccountData(null, null, "admin@example.com", "adminpwd");
        Admin admin = new Admin(data);

        assertEquals("admin@example.com", admin.getEmail());
        assertEquals(Role.ADMIN, admin.getRole());
        assertNotNull(admin.getId());
        assertEquals("", admin.getFullName());
    }
}