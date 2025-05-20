package id.ac.ui.cs.advprog.hiringgo.manajemenakun.model;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.entity.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DosenUserResponseTest {

    @Test
    void testGettersAndSetters() {
        DosenUserResponse response = new DosenUserResponse();

        response.setEmail("dosen@example.com");
        response.setRole(Role.DOSEN);
        response.setNamaLengkap("Dr. Budi Santoso");
        response.setNip("198501152010121002");

        assertEquals("dosen@example.com", response.getEmail());
        assertEquals(Role.DOSEN, response.getRole());
        assertEquals("Dr. Budi Santoso", response.getNamaLengkap());
        assertEquals("198501152010121002", response.getNip());
    }

    @Test
    void testEqualsAndHashCode() {
        DosenUserResponse response1 = new DosenUserResponse();
        response1.setEmail("dosen@example.com");
        response1.setRole(Role.DOSEN);
        response1.setNamaLengkap("Dr. Budi Santoso");
        response1.setNip("198501152010121002");

        DosenUserResponse response2 = new DosenUserResponse();
        response2.setEmail("dosen@example.com");
        response2.setRole(Role.DOSEN);
        response2.setNamaLengkap("Dr. Budi Santoso");
        response2.setNip("198501152010121002");

        DosenUserResponse response3 = new DosenUserResponse();
        response3.setEmail("dosen@example.com");
        response3.setRole(Role.DOSEN);
        response3.setNamaLengkap("Dr. Ahmad Wijaya");
        response3.setNip("197005062005011003");

        assertEquals(response1, response2);
        assertNotEquals(response1, response3);
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void testInheritance() {
        DosenUserResponse response = new DosenUserResponse();

        assertTrue(response instanceof BaseUserResponse);
    }

    @Test
    void testToString() {
        DosenUserResponse response = new DosenUserResponse();
        response.setEmail("dosen@example.com");
        response.setRole(Role.DOSEN);
        response.setNamaLengkap("Dr. Budi Santoso");
        response.setNip("198501152010121002");

        String toString = response.toString();

        assertTrue(toString.contains("dosen@example.com"));
        assertTrue(toString.contains("DOSEN"));
        assertTrue(toString.contains("Dr. Budi Santoso"));
        assertTrue(toString.contains("198501152010121002"));
    }

    @Test
    void testAdditionalFields() {
        DosenUserResponse response = new DosenUserResponse();

        response.setNamaLengkap("Dr. Budi Santoso");
        response.setNip("198501152010121002");

        assertEquals("Dr. Budi Santoso", response.getNamaLengkap());
        assertEquals("198501152010121002", response.getNip());
    }
}
