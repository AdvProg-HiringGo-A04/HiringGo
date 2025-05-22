package id.ac.ui.cs.advprog.hiringgo.manajemenakun.strategy.delete;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.entity.Role;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.repository.DosenRepository;
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
public class DeleteDosenStrategyTest {

    @Mock
    private DosenRepository dosenRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DeleteDosenStrategy deleteDosenStrategy;

    @BeforeEach
    void setUp() {
        reset(dosenRepository, userRepository);
    }

    @Test
    void testSupports_DosenRole_ReturnsTrue() {
        boolean result = deleteDosenStrategy.supports(Role.DOSEN);

        assertTrue(result);
    }

    @Test
    void testSupports_AdminRole_ReturnsFalse() {
        boolean result = deleteDosenStrategy.supports(Role.ADMIN);

        assertFalse(result);
    }

    @Test
    void testSupports_MahasiswaRole_ReturnsFalse() {
        boolean result = deleteDosenStrategy.supports(Role.MAHASISWA);

        assertFalse(result);
    }

    @Test
    void testDeleteUser_Success() {
        String userId = "dosen-123";

        deleteDosenStrategy.deleteUser(userId);

        verify(dosenRepository).deleteById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    void testDeleteUser_VerifyTransactionOrder() {
        String userId = "dosen-123";
        InOrder inOrder = inOrder(dosenRepository, userRepository);

        deleteDosenStrategy.deleteUser(userId);

        inOrder.verify(dosenRepository).deleteById(userId);
        inOrder.verify(userRepository).deleteById(userId);
    }

    @Test
    void testDeleteUser_VerifyRepositoryInteractions() {
        String userId = "dosen-123";

        deleteDosenStrategy.deleteUser(userId);

        verify(dosenRepository, times(1)).deleteById(userId);
        verify(userRepository, times(1)).deleteById(userId);
        verifyNoMoreInteractions(dosenRepository, userRepository);
    }

    @Test
    void testDeleteUser_WithDifferentUserId() {
        String userId = "different-dosen-456";

        deleteDosenStrategy.deleteUser(userId);

        verify(dosenRepository).deleteById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    void testDeleteUser_WithNullUserId() {
        String userId = null;

        deleteDosenStrategy.deleteUser(userId);

        verify(dosenRepository).deleteById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    void testDeleteUser_WithEmptyUserId() {
        String userId = "";

        deleteDosenStrategy.deleteUser(userId);

        verify(dosenRepository).deleteById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    void testDeleteUser_DosenRepositoryException() {
        String userId = "dosen-123";
        RuntimeException exception = new RuntimeException("Dosen deletion failed");

        doThrow(exception).when(dosenRepository).deleteById(userId);

        RuntimeException thrownException = assertThrows(RuntimeException.class,
                () -> deleteDosenStrategy.deleteUser(userId));

        assertEquals("Dosen deletion failed", thrownException.getMessage());
        verify(dosenRepository).deleteById(userId);
        verifyNoInteractions(userRepository);
    }

    @Test
    void testDeleteUser_UserRepositoryException() {
        String userId = "dosen-123";
        RuntimeException exception = new RuntimeException("User deletion failed");

        doThrow(exception).when(userRepository).deleteById(userId);

        RuntimeException thrownException = assertThrows(RuntimeException.class,
                () -> deleteDosenStrategy.deleteUser(userId));

        assertEquals("User deletion failed", thrownException.getMessage());
        verify(dosenRepository).deleteById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    void testSupports_AllRolesCoverage() {
        assertFalse(deleteDosenStrategy.supports(Role.ADMIN));
        assertTrue(deleteDosenStrategy.supports(Role.DOSEN));
        assertFalse(deleteDosenStrategy.supports(Role.MAHASISWA));
    }

    @Test
    void testSupports_WithNullRole() {
        assertFalse(deleteDosenStrategy.supports(null));
    }

    @Test
    void testDeleteUser_NoExceptionThrown() {
        String userId = "dosen-123";

        assertDoesNotThrow(() -> deleteDosenStrategy.deleteUser(userId));

        verify(dosenRepository).deleteById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    void testDeleteUser_TransactionBehavior() {
        String userId = "dosen-123";

        deleteDosenStrategy.deleteUser(userId);

        verify(dosenRepository, times(1)).deleteById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void testDeleteUser_MultipleCallsWithSameId() {
        String userId = "dosen-123";

        deleteDosenStrategy.deleteUser(userId);
        deleteDosenStrategy.deleteUser(userId);

        verify(dosenRepository, times(2)).deleteById(userId);
        verify(userRepository, times(2)).deleteById(userId);
    }
}