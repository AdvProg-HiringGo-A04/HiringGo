package id.ac.ui.cs.advprog.hiringgo.manajemenakun.service.updaterole;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.AccountData;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Dosen;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Admin;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Role;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DosenToAdminStrategyTest {
    private final DosenToAdminStrategy strategy = new DosenToAdminStrategy();

    @Test
    void testUpdateDosenToAdminOnly() {
        assertTrue(strategy.supports(Role.DOSEN, Role.ADMIN));
        assertFalse(strategy.supports(Role.DOSEN, Role.MAHASISWA));
        assertFalse(strategy.supports(Role.ADMIN, Role.DOSEN));
    }

    @Test
    void testUpdateDosenToAdmin() {
        Dosen old = new Dosen(new AccountData("NIP1", "Dr. X", "dx@example.com", null));
        AccountData data = new AccountData(null, null, null, null);

        Admin admin = (Admin) strategy.changeRole(old, data);
        assertEquals("dx@example.com", admin.getEmail());
        assertEquals(Role.ADMIN, admin.getRole());
        assertNotNull(admin.getId());
    }
}