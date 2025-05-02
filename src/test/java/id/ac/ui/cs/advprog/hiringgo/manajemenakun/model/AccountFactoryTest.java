package id.ac.ui.cs.advprog.hiringgo.manajemenakun.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AccountFactoryTest {
    @Test
    void testAdminCreateDosenAccount() {
        Account dosen = AccountFactory.createAccount(Role.DOSEN, new AccountData("12345", "Dr. Budi", "budi@example.com", "secret"));
        assertTrue(dosen instanceof Dosen);
        assertEquals("12345", ((Dosen) dosen).getIdentifier());
        assertEquals("Dr. Budi", dosen.getFullName());
        assertEquals("budi@example.com", dosen.getEmail());
    }

    @Test
    void testAdminCreateAdminAccount() {
        Account admin = AccountFactory.createAccount(Role.ADMIN, new AccountData(null, null, "admin@example.com", "adminpwd"));
        assertTrue(admin instanceof Admin);
        assertEquals("admin@example.com", admin.getEmail());
    }

    @Test
    void testAdminCreateMahasiswa() {
        assertThrows(IllegalArgumentException.class, () ->
                AccountFactory.createAccount(Role.MAHASISWA, new AccountData(null, "Zula", "zula@example.com", "pwd"))
        );
    }

    @Test
    void createUnknownRole_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () ->
                AccountFactory.createAccount(
                        null,
                        new AccountData(null, "Cleo", "cleo@example.com", "pwd")
                )
        );
    }
}