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

class DosenDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsValid_thenNoViolations() {
        DosenDTO dosenDTO = new DosenDTO("dosen@example.com", "password123", "Dr. Budi Santoso", "198501152010121002");

        Set<ConstraintViolation<DosenDTO>> violations = validator.validate(dosenDTO);

        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    void whenEmailInvalid_thenViolationOccurs(String email) {
        DosenDTO dosenDTO = new DosenDTO(email, "password123", "Dr. Budi Santoso", "198501152010121002");

        Set<ConstraintViolation<DosenDTO>> violations = validator.validate(dosenDTO);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    void whenPasswordInvalid_thenViolationOccurs(String password) {
        DosenDTO dosenDTO = new DosenDTO("dosen@example.com", password, "Dr. Budi Santoso", "198501152010121002");

        Set<ConstraintViolation<DosenDTO>> violations = validator.validate(dosenDTO);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    void whenNamaLengkapInvalid_thenViolationOccurs(String namaLengkap) {
        DosenDTO dosenDTO = new DosenDTO("dosen@example.com", "password123", namaLengkap, "198501152010121002");

        Set<ConstraintViolation<DosenDTO>> violations = validator.validate(dosenDTO);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("namaLengkap")));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    void whenNipInvalid_thenViolationOccurs(String nip) {
        DosenDTO dosenDTO = new DosenDTO("dosen@example.com", "password123", "Dr. Budi Santoso", nip);

        Set<ConstraintViolation<DosenDTO>> violations = validator.validate(dosenDTO);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("nip")));
    }

    @Test
    void testGettersAndSetters() {
        DosenDTO dosenDTO = new DosenDTO();

        dosenDTO.setEmail("dosen@example.com");
        dosenDTO.setPassword("password123");
        dosenDTO.setNamaLengkap("Dr. Budi Santoso");
        dosenDTO.setNip("198501152010121002");

        assertEquals("dosen@example.com", dosenDTO.getEmail());
        assertEquals("password123", dosenDTO.getPassword());
        assertEquals("Dr. Budi Santoso", dosenDTO.getNamaLengkap());
        assertEquals("198501152010121002", dosenDTO.getNip());
    }

    @Test
    void testEqualsAndHashCode() {
        DosenDTO dosenDTO1 = new DosenDTO("dosen@example.com", "password123", "Dr. Budi Santoso", "198501152010121002");
        DosenDTO dosenDTO2 = new DosenDTO("dosen@example.com", "password123", "Dr. Budi Santoso", "198501152010121002");
        DosenDTO dosenDTO3 = new DosenDTO("other@example.com", "password123", "Dr. Ahmad Wijaya", "197005062005011003");

        assertEquals(dosenDTO1, dosenDTO2);
        assertNotEquals(dosenDTO1, dosenDTO3);
        assertEquals(dosenDTO1.hashCode(), dosenDTO2.hashCode());
    }

    @Test
    void testAllArgsConstructor() {
        DosenDTO dosenDTO = new DosenDTO("dosen@example.com", "password123", "Dr. Budi Santoso", "198501152010121002");

        assertEquals("dosen@example.com", dosenDTO.getEmail());
        assertEquals("password123", dosenDTO.getPassword());
        assertEquals("Dr. Budi Santoso", dosenDTO.getNamaLengkap());
        assertEquals("198501152010121002", dosenDTO.getNip());
    }

    @Test
    void testNoArgsConstructor() {
        DosenDTO dosenDTO = new DosenDTO();

        assertNull(dosenDTO.getEmail());
        assertNull(dosenDTO.getPassword());
        assertNull(dosenDTO.getNamaLengkap());
        assertNull(dosenDTO.getNip());
    }

    @Test
    void testToString() {
        DosenDTO dosenDTO = new DosenDTO("dosen@example.com", "password123", "Dr. Budi Santoso", "198501152010121002");

        String toString = dosenDTO.toString();

        assertTrue(toString.contains("dosen@example.com"));
        assertTrue(toString.contains("password123"));
        assertTrue(toString.contains("Dr. Budi Santoso"));
        assertTrue(toString.contains("198501152010121002"));
    }
}