package id.ac.ui.cs.advprog.hiringgo.manajemenakun.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MahasiswaTest {

    @Test
    public void mahasiswaShouldHaveCorrectFields() {
        Mahasiswa mahasiswa = new Mahasiswa("1", "M12345", "Jane Doe", "mahasiswa@example.com", "password");

        assertEquals("1", mahasiswa.getId());
        assertEquals("M12345", mahasiswa.getNim());
        assertEquals("mahasiswa@example.com", mahasiswa.getEmail());
        assertEquals("MAHASISWA", mahasiswa.getRole());
        assertEquals("Jane Doe", mahasiswa.getNamaLengkap());
    }
}