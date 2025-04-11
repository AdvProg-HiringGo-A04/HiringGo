package id.ac.ui.cs.advprog.hiringgo.manajemenakun.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DosenTest {

    @Test
    public void dosenShouldHaveCorrectFields() {
        Dosen dosen = new Dosen("1", "123456", "Dr. John Doe", "dosen@example.com", "password");

        assertEquals("1", dosen.getId());
        assertEquals("123456", dosen.getNip());
        assertEquals("dosen@example.com", dosen.getEmail());
        assertEquals("DOSEN", dosen.getRole());
        assertEquals("Dr. John Doe", dosen.getNamaLengkap());
    }
}