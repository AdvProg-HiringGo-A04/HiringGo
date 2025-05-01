package id.ac.ui.cs.advprog.hiringgo.manajemenakun.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DosenTest {

    @Test
    public void dosenShouldHaveCorrectFields() {
        AccountData data = new AccountData("NIP123", "Prof. X", "px@example.com", "pwd");
        Dosen dosen = new Dosen(data);

        assertEquals("NIP123", dosen.getNip());
        assertEquals("Prof. X", dosen.getFullName());
        assertEquals("px@example.com", dosen.getEmail());
        assertEquals(Role.DOSEN, dosen.getRole());
        assertNotNull(dosen.getId());
    }
}