package id.ac.ui.cs.advprog.hiringgo.manajemenakun.model;

import id.ac.ui.cs.advprog.hiringgo.enums.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MahasiswaUserResponseTest {

    @Test
    void testGettersAndSetters() {
        MahasiswaUserResponse response = new MahasiswaUserResponse();

        response.setEmail("mahasiswa@example.com");
        response.setRole(Role.MAHASISWA);
        response.setNamaLengkap("Andi Wijaya");
        response.setNim("1906123456");

        assertEquals("mahasiswa@example.com", response.getEmail());
        assertEquals(Role.MAHASISWA, response.getRole());
        assertEquals("Andi Wijaya", response.getNamaLengkap());
        assertEquals("1906123456", response.getNim());
    }

    @Test
    void testEqualsAndHashCode() {
        MahasiswaUserResponse response1 = new MahasiswaUserResponse();
        response1.setEmail("mahasiswa@example.com");
        response1.setRole(Role.MAHASISWA);
        response1.setNamaLengkap("Andi Wijaya");
        response1.setNim("1906123456");

        MahasiswaUserResponse response2 = new MahasiswaUserResponse();
        response2.setEmail("mahasiswa@example.com");
        response2.setRole(Role.MAHASISWA);
        response2.setNamaLengkap("Andi Wijaya");
        response2.setNim("1906123456");

        MahasiswaUserResponse response3 = new MahasiswaUserResponse();
        response3.setEmail("mahasiswa@example.com");
        response3.setRole(Role.MAHASISWA);
        response3.setNamaLengkap("Budi Prakoso");
        response3.setNim("2006123789");

        assertEquals(response1, response2);
        assertNotEquals(response1, response3);
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void testInheritance() {
        MahasiswaUserResponse response = new MahasiswaUserResponse();

        assertTrue(response instanceof BaseUserResponse);
    }

    @Test
    void testToString() {
        MahasiswaUserResponse response = new MahasiswaUserResponse();
        response.setEmail("mahasiswa@example.com");
        response.setRole(Role.MAHASISWA);
        response.setNamaLengkap("Andi Wijaya");
        response.setNim("1906123456");

        String toString = response.toString();

        assertTrue(toString.contains("mahasiswa@example.com"));
        assertTrue(toString.contains("MAHASISWA"));
        assertTrue(toString.contains("Andi Wijaya"));
        assertTrue(toString.contains("1906123456"));
    }

    @Test
    void testAdditionalFields() {
        MahasiswaUserResponse response = new MahasiswaUserResponse();

        response.setNamaLengkap("Andi Wijaya");
        response.setNim("1906123456");

        assertEquals("Andi Wijaya", response.getNamaLengkap());
        assertEquals("1906123456", response.getNim());
    }
}
