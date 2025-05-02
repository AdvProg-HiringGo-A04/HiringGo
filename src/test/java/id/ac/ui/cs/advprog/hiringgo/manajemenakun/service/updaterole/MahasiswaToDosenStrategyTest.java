package id.ac.ui.cs.advprog.hiringgo.manajemenakun.service.updaterole;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.AccountData;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Dosen;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Role;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MahasiswaToDosenStrategyTest {
    private final MahasiswaToDosenStrategy strategy = new MahasiswaToDosenStrategy();

    @Test
    void supports_onlyForMahasiswaToDosen() {
        assertTrue(strategy.supports(Role.MAHASISWA, Role.DOSEN));
        assertFalse(strategy.supports(Role.MAHASISWA, Role.ADMIN));
        assertFalse(strategy.supports(Role.DOSEN, Role.MAHASISWA));
    }

    @Test
    void changeRole_createsDosenWithMahasiswaData() {
        Mahasiswa old = new Mahasiswa(new AccountData("NIM3", "Citra", "citra@example.com", "siap"));
        AccountData data = new AccountData("NIP999", "Dr. Citra", "citra@example.com", "siap");

        Dosen dosen = (Dosen) strategy.changeRole(old, data);
        assertEquals("NIP999", dosen.getIdentifier());
        assertEquals("Dr. Citra", dosen.getFullName());
        assertEquals("citra@example.com", dosen.getEmail());
        assertEquals(Role.DOSEN, dosen.getRole());
        assertNotNull(dosen.getId());
    }
}