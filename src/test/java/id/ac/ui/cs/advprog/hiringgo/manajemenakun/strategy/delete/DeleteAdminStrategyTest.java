package id.ac.ui.cs.advprog.hiringgo.manajemenakun.strategy.delete;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.entity.Role;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.repository.AdminRepository;
import id.ac.ui.cs.advprog.hiringgo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteAdminStrategyTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DeleteAdminStrategy deleteAdminStrategy;

    @BeforeEach
    void setUp() {
        reset(adminRepository, userRepository);
    }

    @Test
    void testSupports_AdminRole_ReturnsTrue() {
        boolean result = deleteAdminStrategy.supports(Role.ADMIN);

        assertTrue(result);
    }

    @Test
    void testSupports_DosenRole_ReturnsFalse() {
        boolean result = deleteAdminStrategy.supports(Role.DOSEN);

        assertFalse(result);
    }

    @Test
    void testSupports_MahasiswaRole_ReturnsFalse() {
        boolean result = deleteAdminStrategy.supports(Role.MAHASISWA);

        assertFalse(result);
    }

    @Test
    void testDeleteUser_Success() {
        String userId = "admin-123";

        deleteAdminStrategy.deleteUser(userId);

        verify(adminRepository).deleteById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    void testDeleteUser_VerifyTransactionOrder() {
        String userId = "admin-123";
        InOrder inOrder = inOrder(adminRepository, userRepository);

        deleteAdminStrategy.deleteUser(userId);

        inOrder.verify(adminRepository).deleteById(userId);
        inOrder.verify(userRepository).deleteById(userId);
    }

    @Test
    void testDeleteUser_VerifyRepositoryInteractions() {
        String userId = "admin-123";

        deleteAdminStrategy.deleteUser(userId);

        verify(adminRepository, times(1)).deleteById(userId);
        verify(userRepository, times(1)).deleteById(userId);
        verifyNoMoreInteractions(adminRepository, userRepository);
    }

    @Test
    void testDeleteUser_WithDifferentUserId() {
        String userId = "different-admin-456";

        deleteAdminStrategy.deleteUser(userId);

        verify(adminRepository).deleteById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    void testDeleteUser_WithNullUserId() {
        String userId = null;

        deleteAdminStrategy.deleteUser(userId);

        verify(adminRepository).deleteById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    void testDeleteUser_WithEmptyUserId() {
        String userId = "";

        deleteAdminStrategy.deleteUser(userId);

        verify(adminRepository).deleteById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    void testDeleteUser_AdminRepositoryException() {
        String userId = "admin-123";
        RuntimeException exception = new RuntimeException("Admin deletion failed");

        doThrow(exception).when(adminRepository).deleteById(userId);

        RuntimeException thrownException = assertThrows(RuntimeException.class,
                () -> deleteAdminStrategy.deleteUser(userId));

        assertEquals("Admin deletion failed", thrownException.getMessage());
        verify(adminRepository).deleteById(userId);
        verifyNoInteractions(userRepository);
    }

    @Test
    void testDeleteUser_UserRepositoryException() {
        String userId = "admin-123";
        RuntimeException exception = new RuntimeException("User deletion failed");

        doThrow(exception).when(userRepository).deleteById(userId);

        RuntimeException thrownException = assertThrows(RuntimeException.class,
                () -> deleteAdminStrategy.deleteUser(userId));

        assertEquals("User deletion failed", thrownException.getMessage());
        verify(adminRepository).deleteById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    void testSupports_AllRolesCoverage() {
        assertTrue(deleteAdminStrategy.supports(Role.ADMIN));
        assertFalse(deleteAdminStrategy.supports(Role.DOSEN));
        assertFalse(deleteAdminStrategy.supports(Role.MAHASISWA));
    }

    @Test
    void testSupports_WithNullRole() {
        assertFalse(deleteAdminStrategy.supports(null));
    }

    @Test
    void testDeleteUser_NoExceptionThrown() {
        String userId = "admin-123";

        assertDoesNotThrow(() -> deleteAdminStrategy.deleteUser(userId));

        verify(adminRepository).deleteById(userId);
        verify(userRepository).deleteById(userId);
    }
}