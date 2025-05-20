package id.ac.ui.cs.advprog.hiringgo.manajemenakun.model;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.entity.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdminUserResponseTest {

    @Test
    void testInheritedGettersAndSetters() {
        AdminUserResponse response = new AdminUserResponse();

        response.setEmail("admin@example.com");
        response.setRole(Role.ADMIN);

        assertEquals("admin@example.com", response.getEmail());
        assertEquals(Role.ADMIN, response.getRole());
    }

    @Test
    void testEqualsAndHashCode() {
        AdminUserResponse response1 = new AdminUserResponse();
        response1.setEmail("admin@example.com");
        response1.setRole(Role.ADMIN);

        AdminUserResponse response2 = new AdminUserResponse();
        response2.setEmail("admin@example.com");
        response2.setRole(Role.ADMIN);

        AdminUserResponse response3 = new AdminUserResponse();
        response3.setEmail("other@example.com");
        response3.setRole(Role.ADMIN);

        assertEquals(response1, response2);
        assertNotEquals(response1, response3);
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void testInheritance() {
        AdminUserResponse response = new AdminUserResponse();

        assertTrue(response instanceof BaseUserResponse);
    }

    @Test
    void testToString() {
        AdminUserResponse response = new AdminUserResponse();
        response.setEmail("admin@example.com");
        response.setRole(Role.ADMIN);

        String toString = response.toString().toLowerCase();

        assertTrue(toString.contains("admin@example.com"));
        assertTrue(toString.contains("admin")); // role dalam huruf kecil
    }
}
