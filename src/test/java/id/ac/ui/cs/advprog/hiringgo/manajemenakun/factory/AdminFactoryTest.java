package id.ac.ui.cs.advprog.hiringgo.manajemenakun.factory;

import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.entity.Admin;
import id.ac.ui.cs.advprog.hiringgo.entity.Role;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.AdminDTO;
import id.ac.ui.cs.advprog.hiringgo.repository.AdminRepository;
import id.ac.ui.cs.advprog.hiringgo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminFactoryTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminFactory adminFactory;

    private AdminDTO adminDTO;
    private User user;
    private Admin admin;

    @BeforeEach
    void setUp() {
        adminDTO = new AdminDTO("admin@example.com", "password123");

        user = new User();
        user.setId("test-uuid");
        user.setEmail("admin@example.com");
        user.setPassword("encoded-password");
        user.setRole(Role.ADMIN);

        admin = new Admin();
        admin.setId("test-uuid");
    }

    @Test
    void createAccount_Success() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = adminFactory.createAccount(adminDTO);

        assertNotNull(result);
        assertEquals("admin@example.com", result.getEmail());
        assertEquals("encoded-password", result.getPassword());
        assertEquals(Role.ADMIN, result.getRole());

        verify(userRepository).findByEmail("admin@example.com");
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));

        ArgumentCaptor<Admin> adminCaptor = ArgumentCaptor.forClass(Admin.class);
        verify(adminRepository).save(adminCaptor.capture());
        assertEquals(user.getId(), adminCaptor.getValue().getId());
    }

    @Test
    void createAccount_EmailAlreadyTaken() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            adminFactory.createAccount(adminDTO);
        });

        assertEquals("Email is already taken", exception.getReason());
        assertEquals(400, exception.getStatusCode().value());

        verify(userRepository).findByEmail("admin@example.com");
        verify(userRepository, never()).save(any(User.class));
        verify(adminRepository, never()).save(any(Admin.class));
    }

    @Test
    void createUserEntity_SetsRandomUUID() {
        when(passwordEncoder.encode(anyString())).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = null;
        try {
            java.lang.reflect.Method method = AdminFactory.class.getDeclaredMethod(
                    "createUserEntity", String.class, String.class, Role.class);
            method.setAccessible(true);
            result = (User) method.invoke(adminFactory, "admin@example.com", "password123", Role.ADMIN);
        } catch (Exception e) {
            fail("Failed to invoke private method: " + e.getMessage());
        }

        assertNotNull(result);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertNotNull(userCaptor.getValue().getId());
    }

    @Test
    void validateEmailNotTaken_ThrowsException_WhenEmailExists() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        try {
            java.lang.reflect.Method method = AdminFactory.class.getDeclaredMethod(
                    "validateEmailNotTaken", String.class);
            method.setAccessible(true);

            InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
                method.invoke(adminFactory, "admin@example.com");
            });

            assertTrue(exception.getCause() instanceof ResponseStatusException);
            ResponseStatusException responseException = (ResponseStatusException) exception.getCause();
            assertEquals("Email is already taken", responseException.getReason());
            assertEquals(HttpStatus.BAD_REQUEST, responseException.getStatusCode());
        } catch (Exception e) {
            fail("Failed to invoke private method: " + e.getMessage());
        }

        verify(userRepository).findByEmail("admin@example.com");
    }
}