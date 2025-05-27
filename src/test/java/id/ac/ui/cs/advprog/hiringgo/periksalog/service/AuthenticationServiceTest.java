package id.ac.ui.cs.advprog.hiringgo.periksalog.service;

import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.enums.Role;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.service.UserService;
import id.ac.ui.cs.advprog.hiringgo.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock private JwtUtil jwtUtil;
    @Mock private UserService userService;
    @InjectMocks private AuthenticationService authenticationService;

    private User dosenUser;
    private User mahasiswaUser;
    private String validToken;
    private String jwt;

    @BeforeEach
    void setUp() {
        dosenUser = new User();
        dosenUser.setId("dosen1");
        dosenUser.setRole(Role.DOSEN);

        mahasiswaUser = new User();
        mahasiswaUser.setId("mahasiswa1");
        mahasiswaUser.setRole(Role.MAHASISWA);

        validToken = "Bearer validjwttoken";
        jwt = "validjwttoken";
    }

    @Test
    void authenticateUser_ValidToken_ReturnsUser() {
        when(jwtUtil.extractId(jwt)).thenReturn("dosen1");
        when(jwtUtil.extractRole(jwt)).thenReturn("DOSEN");
        when(userService.getUserById("dosen1")).thenReturn(dosenUser);

        User result = authenticationService.authenticateUser(validToken);

        assertEquals(dosenUser, result);
        verify(jwtUtil).extractId(jwt);
        verify(jwtUtil).extractRole(jwt);
        verify(userService).getUserById("dosen1");
    }

    @Test
    void authenticateUser_NullToken_ThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> authenticationService.authenticateUser(null));

        assertEquals("Token required", exception.getMessage());
    }

    @Test
    void authenticateUser_InvalidTokenFormat_ThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> authenticationService.authenticateUser("InvalidToken"));

        assertEquals("Token required", exception.getMessage());
    }

    @Test
    void authenticateUser_TokenNotStartingWithBearer_ThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> authenticationService.authenticateUser("Token validjwttoken"));

        assertEquals("Token required", exception.getMessage());
    }

    @Test
    void authenticateUser_NullUserId_ThrowsException() {
        when(jwtUtil.extractId(jwt)).thenReturn(null);
        when(jwtUtil.extractRole(jwt)).thenReturn("DOSEN");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> authenticationService.authenticateUser(validToken));

        assertEquals("Invalid token", exception.getMessage());
    }

    @Test
    void authenticateUser_NullUserRole_ThrowsException() {
        when(jwtUtil.extractId(jwt)).thenReturn("dosen1");
        when(jwtUtil.extractRole(jwt)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> authenticationService.authenticateUser(validToken));

        assertEquals("Invalid token", exception.getMessage());
    }

    @Test
    void authenticateUser_JwtExtractionException_ThrowsException() {
        when(jwtUtil.extractId(jwt)).thenThrow(new RuntimeException("JWT parsing error"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> authenticationService.authenticateUser(validToken));

        assertEquals("Invalid token", exception.getMessage());
    }

    @Test
    void authenticateUser_UserServiceException_ThrowsException() {
        when(jwtUtil.extractId(jwt)).thenReturn("dosen1");
        when(jwtUtil.extractRole(jwt)).thenReturn("DOSEN");
        when(userService.getUserById("dosen1")).thenThrow(new RuntimeException("User not found"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> authenticationService.authenticateUser(validToken));

        assertEquals("Invalid token", exception.getMessage());
    }

    @Test
    void isDosenUser_DosenUser_ReturnsTrue() {
        boolean result = authenticationService.isDosenUser(dosenUser);
        assertTrue(result);
    }

    @Test
    void isDosenUser_MahasiswaUser_ReturnsFalse() {
        boolean result = authenticationService.isDosenUser(mahasiswaUser);
        assertFalse(result);
    }

    @Test
    void isDosenUser_NullUser_ReturnsFalse() {
        boolean result = authenticationService.isDosenUser(null);
        assertFalse(result);
    }

    @Test
    void isDosenUser_UserWithNullRole_ReturnsFalse() {
        User userWithNullRole = new User();
        userWithNullRole.setId("user1");
        userWithNullRole.setRole(null);

        boolean result = authenticationService.isDosenUser(userWithNullRole);
        assertFalse(result);
    }
}
