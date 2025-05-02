package id.ac.ui.cs.advprog.hiringgo.manajemenakun.service.updaterole;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.AccountData;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Admin;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Role;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MahasiswaToAdminStrategyTest {
    private final MahasiswaToAdminStrategy strategy = new MahasiswaToAdminStrategy();

    @Test
    void testUpdateMahasiswaToAdminOnly() {
        assertTrue(strategy.supports(Role.MAHASISWA, Role.ADMIN));
        assertFalse(strategy.supports(Role.MAHASISWA, Role.DOSEN));
        assertFalse(strategy.supports(Role.ADMIN, Role.MAHASISWA));
    }

    @Test
    void testUpdateMahasiswaToAdmin() {
        Mahasiswa old = new Mahasiswa(new AccountData("NIM2", "Budi", "budi@example.com", null));
        AccountData data = new AccountData(null, null, null, null);

        Admin admin = (Admin) strategy.changeRole(old, data);
        assertEquals("budi@example.com", admin.getEmail());
        assertEquals(Role.ADMIN, admin.getRole());
        assertNotNull(admin.getId());
    }
}