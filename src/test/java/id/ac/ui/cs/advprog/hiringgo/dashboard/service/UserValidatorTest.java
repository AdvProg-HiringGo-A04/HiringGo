package id.ac.ui.cs.advprog.hiringgo.dashboard.service;

import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {

    private UserValidator userValidator;

    @BeforeEach
    void setUp() {
        userValidator = new UserValidator();
    }

    @Test
    void validateUser_WithValidUser_ShouldNotThrowException() {
        // Given
        User user = new User();
        user.setRole(Role.ADMIN);

        // When & Then
        assertDoesNotThrow(() -> userValidator.validateUser(user));
    }

    @Test
    void validateUser_WithNullUser_ShouldThrowException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userValidator.validateUser(null)
        );
        assertEquals("User cannot be null", exception.getMessage());
    }

    @Test
    void validateUser_WithNullRole_ShouldThrowException() {
        // Given
        User user = new User();
        user.setRole(null);

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userValidator.validateUser(user)
        );
        assertEquals("User role cannot be null", exception.getMessage());
    }
}