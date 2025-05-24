package id.ac.ui.cs.advprog.hiringgo.manajemenakun.factory;

import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.entity.Dosen;
import id.ac.ui.cs.advprog.hiringgo.entity.Role;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.DosenDTO;
import id.ac.ui.cs.advprog.hiringgo.repository.DosenRepository;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DosenFactoryTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private DosenRepository dosenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DosenFactory dosenFactory;

    private DosenDTO dosenDTO;
    private User user;
    private Dosen dosen;

    @BeforeEach
    void setUp() {
        dosenDTO = new DosenDTO("dosen@example.com", "password123", "Dr. Budi Santoso", "198501152010121002");

        user = new User();
        user.setId("test-uuid");
        user.setEmail("dosen@example.com");
        user.setPassword("encoded-password");
        user.setRole(Role.DOSEN);

        dosen = new Dosen();
        dosen.setId("test-uuid");
        dosen.setNamaLengkap("Dr. Budi Santoso");
        dosen.setNIP("198501152010121002");
    }

    @Test
    void createAccount_Success() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(dosenRepository.findByNIP(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = dosenFactory.createAccount(dosenDTO);

        assertNotNull(result);
        assertEquals("dosen@example.com", result.getEmail());
        assertEquals("encoded-password", result.getPassword());
        assertEquals(Role.DOSEN, result.getRole());

        verify(userRepository).findByEmail("dosen@example.com");
        verify(dosenRepository).findByNIP("198501152010121002");
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));

        ArgumentCaptor<Dosen> dosenCaptor = ArgumentCaptor.forClass(Dosen.class);
        verify(dosenRepository).save(dosenCaptor.capture());
        assertEquals(user.getId(), dosenCaptor.getValue().getId());
        assertEquals("Dr. Budi Santoso", dosenCaptor.getValue().getNamaLengkap());
        assertEquals("198501152010121002", dosenCaptor.getValue().getNIP());
    }

    @Test
    void createAccount_EmailAlreadyTaken() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            dosenFactory.createAccount(dosenDTO);
        });

        assertEquals("Email is already taken", exception.getReason());
        assertEquals(400, exception.getStatusCode().value());

        verify(userRepository).findByEmail("dosen@example.com");
        verify(dosenRepository, never()).findByNIP(anyString());
        verify(userRepository, never()).save(any(User.class));
        verify(dosenRepository, never()).save(any(Dosen.class));
    }

    @Test
    void createAccount_NIPAlreadyTaken() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(dosenRepository.findByNIP(anyString())).thenReturn(Optional.of(dosen));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            dosenFactory.createAccount(dosenDTO);
        });

        assertEquals("NIP is already taken", exception.getReason());
        assertEquals(400, exception.getStatusCode().value());

        verify(userRepository).findByEmail("dosen@example.com");
        verify(dosenRepository).findByNIP("198501152010121002");
        verify(userRepository, never()).save(any(User.class));
        verify(dosenRepository, never()).save(any(Dosen.class));
    }

    @Test
    void createUserEntity_SetsRandomUUID() {
        when(passwordEncoder.encode(anyString())).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = null;
        try {
            java.lang.reflect.Method method = DosenFactory.class.getDeclaredMethod(
                    "createUserEntity", String.class, String.class, Role.class);
            method.setAccessible(true);
            result = (User) method.invoke(dosenFactory, "dosen@example.com", "password123", Role.DOSEN);
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
            java.lang.reflect.Method method = DosenFactory.class.getDeclaredMethod(
                    "validateEmailNotTaken", String.class);
            method.setAccessible(true);

            java.lang.reflect.InvocationTargetException exception = assertThrows(
                    java.lang.reflect.InvocationTargetException.class, () -> {
                        method.invoke(dosenFactory, "dosen@example.com");
                    });

            Throwable cause = exception.getCause();
            assertTrue(cause instanceof ResponseStatusException);

            ResponseStatusException responseException = (ResponseStatusException) cause;
            assertEquals("Email is already taken", responseException.getReason());
            assertEquals(HttpStatus.BAD_REQUEST, responseException.getStatusCode());
        } catch (Exception e) {
            fail("Failed to invoke private method: " + e.getMessage());
        }

        verify(userRepository).findByEmail("dosen@example.com");
    }

    @Test
    void validateNIPNotTaken_ThrowsException_WhenNIPExists() {
        when(dosenRepository.findByNIP(anyString())).thenReturn(Optional.of(dosen));

        try {
            java.lang.reflect.Method method = DosenFactory.class.getDeclaredMethod(
                    "validateNIPNotTaken", String.class);
            method.setAccessible(true);

            java.lang.reflect.InvocationTargetException exception = assertThrows(
                    java.lang.reflect.InvocationTargetException.class, () -> {
                        method.invoke(dosenFactory, "198501152010121002");
                    });

            assertTrue(exception.getCause() instanceof ResponseStatusException);
            ResponseStatusException responseException = (ResponseStatusException) exception.getCause();
            assertEquals("NIP is already taken", responseException.getReason());
            assertEquals(400, responseException.getStatusCode().value());

        } catch (Exception e) {
            fail("Failed to invoke private method: " + e.getMessage());
        }

        verify(dosenRepository).findByNIP("198501152010121002");
    }
}