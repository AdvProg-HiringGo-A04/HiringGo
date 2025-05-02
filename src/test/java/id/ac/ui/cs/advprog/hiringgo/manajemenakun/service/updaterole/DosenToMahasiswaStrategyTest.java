package id.ac.ui.cs.advprog.hiringgo.manajemenakun.service.updaterole;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.AccountData;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Dosen;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Role;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DosenToMahasiswaStrategyTest {
    private final DosenToMahasiswaStrategy strategy = new DosenToMahasiswaStrategy();

    @Test
    void supports_onlyForDosenToMahasiswa() {
        assertTrue(strategy.supports(Role.DOSEN, Role.MAHASISWA));
        assertFalse(strategy.supports(Role.DOSEN, Role.ADMIN));
        assertFalse(strategy.supports(Role.MAHASISWA, Role.DOSEN));
    }

    @Test
    void changeRole_createsMahasiswaWithDosenData() {
        Dosen old = new Dosen(new AccountData("NIP5", "Dr. E", "e@example.com", "null"));
        AccountData data = new AccountData("NIM777", "E", "e@example.com", "null");

        Mahasiswa mhs = (Mahasiswa) strategy.changeRole(old, data);
        assertEquals("NIM777", mhs.getNim());
        assertEquals("E", mhs.getFullName());
        assertEquals("e@example.com", mhs.getEmail());
        assertEquals(Role.MAHASISWA, mhs.getRole());
        assertNotNull(mhs.getId());
    }
}