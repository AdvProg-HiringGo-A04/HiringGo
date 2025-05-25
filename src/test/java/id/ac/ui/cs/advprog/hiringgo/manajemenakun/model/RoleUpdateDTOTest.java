package id.ac.ui.cs.advprog.hiringgo.manajemenakun.model;

import id.ac.ui.cs.advprog.hiringgo.enums.Role;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RoleUpdateDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllRequiredFieldsValid_thenNoViolations() {
        RoleUpdateDTO dto = new RoleUpdateDTO(Role.ADMIN, null, null, null);

        Set<ConstraintViolation<RoleUpdateDTO>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @NullSource
    void whenRoleIsNull_thenViolationOccurs(Role role) {
        RoleUpdateDTO dto = new RoleUpdateDTO(role, "Nama Lengkap", "12345", "NPM12345");

        Set<ConstraintViolation<RoleUpdateDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("role")));
    }

    @ParameterizedTest
    @EnumSource(Role.class)
    void whenRoleIsValid_thenNoViolations(Role role) {
        RoleUpdateDTO dto = new RoleUpdateDTO(role, "Nama Lengkap", "12345", "NPM12345");

        Set<ConstraintViolation<RoleUpdateDTO>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void testGettersAndSetters() {
        RoleUpdateDTO dto = new RoleUpdateDTO();

        dto.setRole(Role.DOSEN);
        dto.setNamaLengkap("Dr. Budi Santoso");
        dto.setNip("198501152010121002");
        dto.setNpm(null);

        assertEquals(Role.DOSEN, dto.getRole());
        assertEquals("Dr. Budi Santoso", dto.getNamaLengkap());
        assertEquals("198501152010121002", dto.getNip());
        assertNull(dto.getNpm());
    }

    @Test
    void testSetRoleToMahasiswa() {
        RoleUpdateDTO dto = new RoleUpdateDTO();

        dto.setRole(Role.MAHASISWA);
        dto.setNamaLengkap("Budi Santoso");
        dto.setNpm("1906123456");
        dto.setNip(null);

        assertEquals(Role.MAHASISWA, dto.getRole());
        assertEquals("Budi Santoso", dto.getNamaLengkap());
        assertEquals("1906123456", dto.getNpm());
        assertNull(dto.getNip());
    }

    @Test
    void testSetRoleToAdmin() {
        RoleUpdateDTO dto = new RoleUpdateDTO();

        dto.setRole(Role.ADMIN);
        dto.setNamaLengkap(null);
        dto.setNpm(null);
        dto.setNip(null);

        assertEquals(Role.ADMIN, dto.getRole());
        assertNull(dto.getNamaLengkap());
        assertNull(dto.getNpm());
        assertNull(dto.getNip());
    }

    @Test
    void testEqualsAndHashCode() {
        RoleUpdateDTO dto1 = new RoleUpdateDTO(Role.DOSEN, "Dr. Budi Santoso", "198501152010121002", null);
        RoleUpdateDTO dto2 = new RoleUpdateDTO(Role.DOSEN, "Dr. Budi Santoso", "198501152010121002", null);
        RoleUpdateDTO dto3 = new RoleUpdateDTO(Role.MAHASISWA, "Andi Wijaya", null, "1906123456");

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testAllArgsConstructor() {
        RoleUpdateDTO dto = new RoleUpdateDTO(Role.DOSEN, "Dr. Budi Santoso", "198501152010121002", null);

        assertEquals(Role.DOSEN, dto.getRole());
        assertEquals("Dr. Budi Santoso", dto.getNamaLengkap());
        assertEquals("198501152010121002", dto.getNip());
        assertNull(dto.getNpm());
    }

    @Test
    void testNoArgsConstructor() {
        RoleUpdateDTO dto = new RoleUpdateDTO();

        assertNull(dto.getRole());
        assertNull(dto.getNamaLengkap());
        assertNull(dto.getNip());
        assertNull(dto.getNpm());
    }

    @Test
    void testToString() {
        RoleUpdateDTO dto = new RoleUpdateDTO(Role.DOSEN, "Dr. Budi Santoso", "198501152010121002", null);

        String toString = dto.toString();

        assertTrue(toString.contains("DOSEN"));
        assertTrue(toString.contains("Dr. Budi Santoso"));
        assertTrue(toString.contains("198501152010121002"));
    }

    @Test
    void testValidRoleUpdateScenarios() {
        RoleUpdateDTO adminDTO = new RoleUpdateDTO(Role.ADMIN, null, null, null);
        assertTrue(validator.validate(adminDTO).isEmpty());

        RoleUpdateDTO dosenDTO = new RoleUpdateDTO(Role.DOSEN, "Dr. Budi Santoso", "198501152010121002", null);
        assertTrue(validator.validate(dosenDTO).isEmpty());

        RoleUpdateDTO mahasiswaDTO = new RoleUpdateDTO(Role.MAHASISWA, "Andi Wijaya", null, "1906123456");
        assertTrue(validator.validate(mahasiswaDTO).isEmpty());
    }
}