package id.ac.ui.cs.advprog.hiringgo.entity;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.entity.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserCreation() {
        User user = new User();
        user.setId("user-id");
        user.setEmail("user@example.com");
        user.setPassword("password");
        user.setRole(Role.valueOf("ADMIN"));

        assertEquals("user-id", user.getId());
        assertEquals("user@example.com", user.getEmail());
        assertEquals("password", user.getPassword());
        assertEquals(Role.ADMIN, user.getRole());
    }

    @Test
    void testLombokGettersAndSetters() {
        User user = new User();

        user.setId("user-id");
        user.setEmail("user@example.com");
        user.setPassword("password");
        user.setRole(Role.valueOf("DOSEN"));

        assertEquals("user-id", user.getId());
        assertEquals("user@example.com", user.getEmail());
        assertEquals("password", user.getPassword());
        assertEquals(Role.DOSEN, user.getRole());
    }

    @Test
    void testEquality() {
        User user1 = new User();
        user1.setId("same-id");
        user1.setEmail("user1@example.com");
        user1.setPassword("password1");
        user1.setRole(Role.valueOf("ADMIN"));

        User user2 = new User();
        user2.setId("same-id");
        user2.setEmail("user2@example.com");
        user2.setPassword("password2");
        user2.setRole(Role.valueOf("DOSEN"));

        User user3 = new User();
        user3.setId("different-id");
        user3.setEmail("user1@example.com");
        user3.setPassword("password1");
        user3.setRole(Role.valueOf("ADMIN"));
        assertNotEquals(user1, user2, "Different User objects should not be equal by default");
        assertNotEquals(user1, user3, "Different User objects should not be equal by default");
        assertEquals(user1, user1, "Object should equal itself");
    }

    @Test
    void testToString() {
        User user = new User();
        user.setId("user-id");
        user.setEmail("user@example.com");
        user.setPassword("password");
        user.setRole(Role.valueOf("ADMIN"));

        String toString = user.toString();

        assertTrue(toString.contains("id"));
        assertTrue(toString.contains("user-id"));
        assertTrue(toString.contains("email"));
        assertTrue(toString.contains("user@example.com"));
        assertTrue(toString.contains("role"));
        assertTrue(toString.contains("ADMIN"));
        assertFalse(toString.contains("password"));
    }
}