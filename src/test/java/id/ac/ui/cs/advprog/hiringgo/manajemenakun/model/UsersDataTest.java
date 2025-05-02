package id.ac.ui.cs.advprog.hiringgo.manajemenakun.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UsersDataTest {
    @Test
    void testAccountData() {
        AccountData data = new AccountData("NIP1", "Arisha", "arisha@example.com", "pwd");
        assertEquals("NIP1", data.getIdentifier());
        assertEquals("Arisha", data.getFullName());
        assertEquals("arisha@example.com", data.getEmail());
        assertEquals("pwd", data.getPassword());
    }
}