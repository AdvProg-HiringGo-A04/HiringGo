package id.ac.ui.cs.advprog.hiringgo.manajemenakun.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AdminDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsValid_thenNoViolations() {
        AdminDTO adminDTO = new AdminDTO("admin@example.com", "password123");

        Set<ConstraintViolation<AdminDTO>> violations = validator.validate(adminDTO);

        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    void whenEmailInvalid_thenViolationOccurs(String email) {
        AdminDTO adminDTO = new AdminDTO(email, "password123");

        Set<ConstraintViolation<AdminDTO>> violations = validator.validate(adminDTO);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    void whenPasswordInvalid_thenViolationOccurs(String password) {
        AdminDTO adminDTO = new AdminDTO("admin@example.com", password);

        Set<ConstraintViolation<AdminDTO>> violations = validator.validate(adminDTO);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        AdminDTO adminDTO = new AdminDTO();

        adminDTO.setEmail("admin@example.com");
        adminDTO.setPassword("password123");

        assertEquals("admin@example.com", adminDTO.getEmail());
        assertEquals("password123", adminDTO.getPassword());
    }

    @Test
    void testEqualsAndHashCode() {
        AdminDTO adminDTO1 = new AdminDTO("admin@example.com", "password123");
        AdminDTO adminDTO2 = new AdminDTO("admin@example.com", "password123");
        AdminDTO adminDTO3 = new AdminDTO("other@example.com", "password123");

        assertEquals(adminDTO1, adminDTO2);
        assertNotEquals(adminDTO1, adminDTO3);
        assertEquals(adminDTO1.hashCode(), adminDTO2.hashCode());
    }

    @Test
    void testAllArgsConstructor() {
        AdminDTO adminDTO = new AdminDTO("admin@example.com", "password123");

        assertEquals("admin@example.com", adminDTO.getEmail());
        assertEquals("password123", adminDTO.getPassword());
    }

    @Test
    void testNoArgsConstructor() {
        AdminDTO adminDTO = new AdminDTO();

        assertNull(adminDTO.getEmail());
        assertNull(adminDTO.getPassword());
    }

    @Test
    void testToString() {
        AdminDTO adminDTO = new AdminDTO("admin@example.com", "password123");

        String toString = adminDTO.toString();

        assertTrue(toString.contains("admin@example.com"));
        assertTrue(toString.contains("password123"));
    }
}