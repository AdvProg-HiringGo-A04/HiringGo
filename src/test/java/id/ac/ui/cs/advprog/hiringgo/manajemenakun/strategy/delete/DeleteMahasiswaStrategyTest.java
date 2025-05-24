package id.ac.ui.cs.advprog.hiringgo.manajemenakun.strategy.delete;

import id.ac.ui.cs.advprog.hiringgo.entity.Role;
import id.ac.ui.cs.advprog.hiringgo.repository.MahasiswaRepository;
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
public class DeleteMahasiswaStrategyTest {

    @Mock
    private MahasiswaRepository mahasiswaRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DeleteMahasiswaStrategy deleteMahasiswaStrategy;

    @BeforeEach
    void setUp() {
        reset(mahasiswaRepository, userRepository);
    }

    @Test
    void testSupports_MahasiswaRole_ReturnsTrue() {
        boolean result = deleteMahasiswaStrategy.supports(Role.MAHASISWA);

        assertTrue(result);
    }

    @Test
    void testSupports_AdminRole_ReturnsFalse() {
        boolean result = deleteMahasiswaStrategy.supports(Role.ADMIN);

        assertFalse(result);
    }

    @Test
    void testSupports_DosenRole_ReturnsFalse() {
        boolean result = deleteMahasiswaStrategy.supports(Role.DOSEN);

        assertFalse(result);
    }

    @Test
    void testDeleteUser_Success() {
        String userId = "mahasiswa-123";

        deleteMahasiswaStrategy.deleteUser(userId);

        verify(mahasiswaRepository).deleteById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    void testDeleteUser_VerifyTransactionOrder() {
        String userId = "mahasiswa-123";
        InOrder inOrder = inOrder(mahasiswaRepository, userRepository);

        deleteMahasiswaStrategy.deleteUser(userId);

        inOrder.verify(mahasiswaRepository).deleteById(userId);
        inOrder.verify(userRepository).deleteById(userId);
    }

    @Test
    void testDeleteUser_VerifyRepositoryInteractions() {
        String userId = "mahasiswa-123";

        deleteMahasiswaStrategy.deleteUser(userId);

        verify(mahasiswaRepository, times(1)).deleteById(userId);
        verify(userRepository, times(1)).deleteById(userId);
        verifyNoMoreInteractions(mahasiswaRepository, userRepository);
    }

    @Test
    void testDeleteUser_WithDifferentUserId() {
        String userId = "different-mahasiswa-456";

        deleteMahasiswaStrategy.deleteUser(userId);

        verify(mahasiswaRepository).deleteById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    void testDeleteUser_WithNullUserId() {
        String userId = null;

        deleteMahasiswaStrategy.deleteUser(userId);

        verify(mahasiswaRepository).deleteById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    void testDeleteUser_WithEmptyUserId() {
        String userId = "";

        deleteMahasiswaStrategy.deleteUser(userId);

        verify(mahasiswaRepository).deleteById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    void testDeleteUser_MahasiswaRepositoryException() {
        String userId = "mahasiswa-123";
        RuntimeException exception = new RuntimeException("Mahasiswa deletion failed");

        doThrow(exception).when(mahasiswaRepository).deleteById(userId);

        RuntimeException thrownException = assertThrows(RuntimeException.class,
                () -> deleteMahasiswaStrategy.deleteUser(userId));

        assertEquals("Mahasiswa deletion failed", thrownException.getMessage());
        verify(mahasiswaRepository).deleteById(userId);
        verifyNoInteractions(userRepository);
    }

    @Test
    void testDeleteUser_UserRepositoryException() {
        String userId = "mahasiswa-123";
        RuntimeException exception = new RuntimeException("User deletion failed");

        doThrow(exception).when(userRepository).deleteById(userId);

        RuntimeException thrownException = assertThrows(RuntimeException.class,
                () -> deleteMahasiswaStrategy.deleteUser(userId));

        assertEquals("User deletion failed", thrownException.getMessage());
        verify(mahasiswaRepository).deleteById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    void testSupports_AllRolesCoverage() {
        assertFalse(deleteMahasiswaStrategy.supports(Role.ADMIN));
        assertFalse(deleteMahasiswaStrategy.supports(Role.DOSEN));
        assertTrue(deleteMahasiswaStrategy.supports(Role.MAHASISWA));
    }

    @Test
    void testSupports_WithNullRole() {
        assertFalse(deleteMahasiswaStrategy.supports(null));
    }

    @Test
    void testDeleteUser_NoExceptionThrown() {
        String userId = "mahasiswa-123";

        assertDoesNotThrow(() -> deleteMahasiswaStrategy.deleteUser(userId));

        verify(mahasiswaRepository).deleteById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    void testDeleteUser_TransactionBehavior() {
        String userId = "mahasiswa-123";

        deleteMahasiswaStrategy.deleteUser(userId);

        verify(mahasiswaRepository, times(1)).deleteById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void testDeleteUser_MultipleCallsWithSameId() {
        String userId = "mahasiswa-123";

        deleteMahasiswaStrategy.deleteUser(userId);
        deleteMahasiswaStrategy.deleteUser(userId);

        verify(mahasiswaRepository, times(2)).deleteById(userId);
        verify(userRepository, times(2)).deleteById(userId);
    }

    @Test
    void testDeleteUser_ConsistentBehaviorAcrossStrategies() {
        String userId = "mahasiswa-123";

        deleteMahasiswaStrategy.deleteUser(userId);

        InOrder inOrder = inOrder(mahasiswaRepository, userRepository);
        inOrder.verify(mahasiswaRepository).deleteById(userId);
        inOrder.verify(userRepository).deleteById(userId);
    }

    @Test
    void testDeleteUser_VerifyNoSideEffects() {
        String userId = "mahasiswa-123";

        deleteMahasiswaStrategy.deleteUser(userId);

        verify(mahasiswaRepository, only()).deleteById(userId);
        verify(userRepository, only()).deleteById(userId);
    }
}