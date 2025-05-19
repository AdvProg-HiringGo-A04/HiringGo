package id.ac.ui.cs.advprog.hiringgo.manajemenakun.service.updaterole;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.AccountData;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Admin;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Dosen;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Role;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.service.updaterole.AdminToDosenStrategy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdminToDosenStrategyTest {
    private final AdminToDosenStrategy strategy = new AdminToDosenStrategy();

    @Test
    void testUpdateAdminToDosenOnly() {
        assertTrue(strategy.supports(Role.ADMIN, Role.DOSEN));
        assertFalse(strategy.supports(Role.ADMIN, Role.MAHASISWA));
        assertFalse(strategy.supports(Role.DOSEN, Role.ADMIN));
    }

    @Test
    void changeRole_createsDosenAccountWithData() {
        Admin old = new Admin(new AccountData(null, null, "admin@example.com", null));
        AccountData data = new AccountData("NIP123", "Dr. Budi", "admin@example.com", "pwd");

        Dosen dosen = (Dosen) strategy.changeRole(old, data);
        assertEquals("NIP123", dosen.getIdentifier());
        assertEquals("Dr. Budi", dosen.getFullName());
        assertEquals("admin@example.com", dosen.getEmail());
        assertEquals(Role.DOSEN, dosen.getRole());
        assertNotNull(dosen.getId());
    }
}
