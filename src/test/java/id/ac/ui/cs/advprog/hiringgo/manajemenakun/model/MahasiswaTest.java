package id.ac.ui.cs.advprog.hiringgo.manajemenakun.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MahasiswaTest {

    @Test
    void testMahasiswa() {
        Mahasiswa mhs = new Mahasiswa(
                new AccountData("NIM456", "Dewi", "dewi@example.com", "pwd"));
        assertEquals("NIM456", mhs.getIdentifier());
        assertEquals("Dewi", mhs.getFullName());
        assertEquals("dewi@example.com", mhs.getEmail());
        assertEquals(Role.MAHASISWA, mhs.getRole());
        assertNotNull(mhs.getId());
    }
}