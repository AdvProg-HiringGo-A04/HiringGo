package id.ac.ui.cs.advprog.hiringgo.manajemenakun.strategy.update;

import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.entity.Admin;
import id.ac.ui.cs.advprog.hiringgo.enums.Role;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.RoleUpdateDTO;
import id.ac.ui.cs.advprog.hiringgo.repository.AdminRepository;
import id.ac.ui.cs.advprog.hiringgo.repository.DosenRepository;
import id.ac.ui.cs.advprog.hiringgo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DosenToAdminStrategyTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private DosenRepository dosenRepository;

    @InjectMocks
    private DosenToAdminStrategy dosenToAdminStrategy;

    private User mockUser;
    private RoleUpdateDTO mockRoleUpdateDTO;
    private Admin mockAdmin;

    @BeforeEach
    void setUp() {
        mockUser = mock(User.class);
        mockRoleUpdateDTO = mock(RoleUpdateDTO.class);
        mockAdmin = mock(Admin.class);
    }

    @Test
    void testSupports_DosenToAdmin_ReturnsTrue() {
        boolean result = dosenToAdminStrategy.supports(Role.DOSEN, Role.ADMIN);

        assertTrue(result);
    }

    @Test
    void testSupports_AdminToDosen_ReturnsFalse() {
        boolean result = dosenToAdminStrategy.supports(Role.ADMIN, Role.DOSEN);

        assertFalse(result);
    }

    @Test
    void testSupports_DosenToMahasiswa_ReturnsFalse() {
        boolean result = dosenToAdminStrategy.supports(Role.DOSEN, Role.MAHASISWA);

        assertFalse(result);
    }

    @Test
    void testSupports_MahasiswaToAdmin_ReturnsFalse() {
        boolean result = dosenToAdminStrategy.supports(Role.MAHASISWA, Role.ADMIN);

        assertFalse(result);
    }

    @Test
    void testUpdateRole_Success() {
        String userId = "user-123";

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(mockUser)).thenReturn(mockUser);
        when(adminRepository.save(any(Admin.class))).thenReturn(mockAdmin);

        User result = dosenToAdminStrategy.updateRole(userId, mockRoleUpdateDTO);

        assertEquals(mockUser, result);
        verify(mockUser).setRole(Role.ADMIN);
        verify(userRepository).save(mockUser);
        verify(dosenRepository).deleteById(userId);
        verify(adminRepository).save(any(Admin.class));
    }

    @Test
    void testUpdateRole_UserNotFound() {
        String userId = "non-existent-user";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> dosenToAdminStrategy.updateRole(userId, mockRoleUpdateDTO));

        assertEquals("User not found", exception.getReason());
        verify(userRepository).findById(userId);
        verifyNoInteractions(adminRepository, dosenRepository);
        verify(mockUser, never()).setRole(any());
    }

    @Test
    void testUpdateRole_VerifyAdminCreation() {
        String userId = "user-123";

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        dosenToAdminStrategy.updateRole(userId, mockRoleUpdateDTO);

        verify(adminRepository).save(argThat(admin ->
                userId.equals(admin.getId())
        ));
    }

    @Test
    void testUpdateRole_VerifyTransactionOrder() {
        String userId = "user-123";

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        dosenToAdminStrategy.updateRole(userId, mockRoleUpdateDTO);

        verify(mockUser).setRole(Role.ADMIN);
        verify(userRepository).save(mockUser);

        verify(dosenRepository).deleteById(userId);

        verify(adminRepository).save(any(Admin.class));
    }

    @Test
    void testUpdateRole_NoValidationRequired() {
        String userId = "user-123";

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        assertDoesNotThrow(() -> dosenToAdminStrategy.updateRole(userId, mockRoleUpdateDTO));

        verify(mockUser).setRole(Role.ADMIN);
        verify(userRepository).save(mockUser);
        verify(dosenRepository).deleteById(userId);
        verify(adminRepository).save(any(Admin.class));
    }

    @Test
    void testUpdateRole_WithNullDTO() {
        String userId = "user-123";

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        assertDoesNotThrow(() -> dosenToAdminStrategy.updateRole(userId, null));

        verify(mockUser).setRole(Role.ADMIN);
        verify(userRepository).save(mockUser);
        verify(dosenRepository).deleteById(userId);
        verify(adminRepository).save(any(Admin.class));
    }
}