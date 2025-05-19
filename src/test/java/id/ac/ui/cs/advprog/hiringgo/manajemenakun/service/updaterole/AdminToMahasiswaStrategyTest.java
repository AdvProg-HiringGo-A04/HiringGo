package id.ac.ui.cs.advprog.hiringgo.manajemenakun.service.updaterole;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.AccountData;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Admin;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Role;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AdminToMahasiswaStrategyTest {
    private final AdminToMahasiswaStrategy strategy = new AdminToMahasiswaStrategy();

    @Test
    void testUpdateAdminToMahasiswaOnly() {
        assertTrue(strategy.supports(Role.ADMIN, Role.MAHASISWA));
        assertFalse(strategy.supports(Role.ADMIN, Role.DOSEN));
        assertFalse(strategy.supports(Role.MAHASISWA, Role.ADMIN));
    }

    @Test
    void testUpdateRoleAdminToMahasiswa() {
        Admin old = new Admin(new AccountData(null, null, "admin@example.com", null));
        AccountData data = new AccountData("NIM456", "Citra", "admin@example.com", "pwd");

        Mahasiswa mhs = (Mahasiswa) strategy.changeRole(old, data);
        assertEquals("NIM456", mhs.getIdentifier());
        assertEquals("Citra", mhs.getFullName());
        assertEquals("admin@example.com", mhs.getEmail());
        assertEquals(Role.MAHASISWA, mhs.getRole());
        assertNotNull(mhs.getId());
    }
}