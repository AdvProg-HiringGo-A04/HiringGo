package id.ac.ui.cs.advprog.hiringgo.manajemenakun.model;

import id.ac.ui.cs.advprog.hiringgo.enums.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BaseUserResponseTest {

    @Test
    void testGettersAndSetters() {
        BaseUserResponse response = new BaseUserResponse();

        response.setEmail("user@example.com");
        response.setRole(Role.ADMIN);

        assertEquals("user@example.com", response.getEmail());
        assertEquals(Role.ADMIN, response.getRole());
    }

    @Test
    void testEqualsAndHashCode() {
        BaseUserResponse response1 = new BaseUserResponse();
        response1.setEmail("user@example.com");
        response1.setRole(Role.ADMIN);

        BaseUserResponse response2 = new BaseUserResponse();
        response2.setEmail("user@example.com");
        response2.setRole(Role.ADMIN);

        BaseUserResponse response3 = new BaseUserResponse();
        response3.setEmail("other@example.com");
        response3.setRole(Role.ADMIN);

        assertEquals(response1, response2);
        assertNotEquals(response1, response3);
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void testToString() {
        BaseUserResponse response = new BaseUserResponse();
        response.setEmail("user@example.com");
        response.setRole(Role.ADMIN);

        String toString = response.toString();

        assertTrue(toString.contains("user@example.com"));
        assertTrue(toString.contains("ADMIN"));
    }
}
