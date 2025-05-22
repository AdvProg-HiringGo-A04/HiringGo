package id.ac.ui.cs.advprog.hiringgo.manajemenakun.strategy.update;

import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.entity.Dosen;
import id.ac.ui.cs.advprog.hiringgo.entity.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.entity.Role;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.RoleUpdateDTO;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.repository.DosenRepository;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.strategy.update.DosenToMahasiswaStrategy;
import id.ac.ui.cs.advprog.hiringgo.repository.MahasiswaRepository;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DosenToMahasiswaStrategyTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private DosenRepository dosenRepository;

    @Mock
    private MahasiswaRepository mahasiswaRepository;

    @InjectMocks
    private DosenToMahasiswaStrategy dosenToMahasiswaStrategy;

    private User mockUser;
    private Dosen mockDosen;
    private RoleUpdateDTO mockRoleUpdateDTO;
    private Mahasiswa mockMahasiswa;

    @BeforeEach
    void setUp() {
        mockUser = mock(User.class);
        mockDosen = mock(Dosen.class);
        mockRoleUpdateDTO = mock(RoleUpdateDTO.class);
        mockMahasiswa = mock(Mahasiswa.class);
    }

    @Test
    void testSupports_DosenToMahasiswa_ReturnsTrue() {
        boolean result = dosenToMahasiswaStrategy.supports(Role.DOSEN, Role.MAHASISWA);

        assertTrue(result);
    }

    @Test
    void testSupports_MahasiswaToDosen_ReturnsFalse() {
        boolean result = dosenToMahasiswaStrategy.supports(Role.MAHASISWA, Role.DOSEN);

        assertFalse(result);
    }

    @Test
    void testSupports_DosenToAdmin_ReturnsFalse() {
        boolean result = dosenToMahasiswaStrategy.supports(Role.DOSEN, Role.ADMIN);

        assertFalse(result);
    }

    @Test
    void testSupports_AdminToMahasiswa_ReturnsFalse() {
        boolean result = dosenToMahasiswaStrategy.supports(Role.ADMIN, Role.MAHASISWA);

        assertFalse(result);
    }

    @Test
    void testUpdateRole_Success() {
        String userId = "user-123";
        String npm = "2106123456";
        String namaLengkap = "Dr. John Doe";

        when(mockRoleUpdateDTO.getNpm()).thenReturn(npm);
        when(mahasiswaRepository.findByNPM(npm)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(dosenRepository.findById(userId)).thenReturn(Optional.of(mockDosen));
        when(mockDosen.getNamaLengkap()).thenReturn(namaLengkap);
        when(userRepository.save(mockUser)).thenReturn(mockUser);
        when(mahasiswaRepository.save(any(Mahasiswa.class))).thenReturn(mockMahasiswa);

        User result = dosenToMahasiswaStrategy.updateRole(userId, mockRoleUpdateDTO);

        assertEquals(mockUser, result);
        verify(mockUser).setRole(Role.MAHASISWA);
        verify(userRepository).save(mockUser);
        verify(dosenRepository).deleteById(userId);
        verify(mahasiswaRepository).save(any(Mahasiswa.class));
    }

    @Test
    void testUpdateRole_UserNotFound() {
        String userId = "non-existent-user";
        String npm = "2106123456";

        when(mockRoleUpdateDTO.getNpm()).thenReturn(npm);
        when(mahasiswaRepository.findByNPM(npm)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> dosenToMahasiswaStrategy.updateRole(userId, mockRoleUpdateDTO));

        assertEquals("User not found", exception.getReason());
        verify(userRepository).findById(userId);
        verify(mahasiswaRepository).findByNPM(npm);
        verify(dosenRepository, never()).findById(anyString());
        verify(dosenRepository, never()).deleteById(anyString());
        verify(mahasiswaRepository, never()).save(any());
    }

    @Test
    void testUpdateRole_DosenNotFound() {
        String userId = "user-123";
        String npm = "2106123456";

        when(mockRoleUpdateDTO.getNpm()).thenReturn(npm);
        when(mahasiswaRepository.findByNPM(npm)).thenReturn(Optional.empty()); // Validation will be called first
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(dosenRepository.findById(userId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> dosenToMahasiswaStrategy.updateRole(userId, mockRoleUpdateDTO));

        assertEquals("Dosen not found", exception.getReason());
        verify(userRepository).findById(userId);
        verify(dosenRepository).findById(userId);
        verify(mahasiswaRepository).findByNPM(npm); // This will be called during validation
        verify(dosenRepository, never()).deleteById(anyString());
        verify(mahasiswaRepository, never()).save(any());
    }

    @Test
    void testUpdateRole_NpmNull() {
        String userId = "user-123";

        when(mockRoleUpdateDTO.getNpm()).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> dosenToMahasiswaStrategy.updateRole(userId, mockRoleUpdateDTO));

        assertEquals("NPM is required for Mahasiswa role", exception.getReason());
        verifyNoInteractions(userRepository, dosenRepository, mahasiswaRepository);
    }

    @Test
    void testUpdateRole_NpmEmpty() {
        String userId = "user-123";

        when(mockRoleUpdateDTO.getNpm()).thenReturn("   ");
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> dosenToMahasiswaStrategy.updateRole(userId, mockRoleUpdateDTO));

        assertEquals("NPM is required for Mahasiswa role", exception.getReason());
        verifyNoInteractions(userRepository, dosenRepository, mahasiswaRepository);
    }

    @Test
    void testUpdateRole_NpmAlreadyTaken() {
        String userId = "user-123";
        String npm = "2106123456";
        Mahasiswa existingMahasiswa = mock(Mahasiswa.class);

        when(mockRoleUpdateDTO.getNpm()).thenReturn(npm);
        when(mahasiswaRepository.findByNPM(npm)).thenReturn(Optional.of(existingMahasiswa));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> dosenToMahasiswaStrategy.updateRole(userId, mockRoleUpdateDTO));

        assertEquals("NPM is already taken", exception.getReason());
        verify(mahasiswaRepository).findByNPM(npm);
        verifyNoInteractions(userRepository, dosenRepository);
        verify(mahasiswaRepository, never()).save(any());
    }

    @Test
    void testUpdateRole_VerifyMahasiswaCreation() {
        String userId = "user-123";
        String npm = "2106123456";
        String namaLengkap = "Dr. John Doe";

        when(mockRoleUpdateDTO.getNpm()).thenReturn(npm);
        when(mahasiswaRepository.findByNPM(npm)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(dosenRepository.findById(userId)).thenReturn(Optional.of(mockDosen));
        when(mockDosen.getNamaLengkap()).thenReturn(namaLengkap);
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        dosenToMahasiswaStrategy.updateRole(userId, mockRoleUpdateDTO);

        verify(mahasiswaRepository).save(argThat(mahasiswa ->
                userId.equals(mahasiswa.getId()) &&
                        namaLengkap.equals(mahasiswa.getNamaLengkap()) &&
                        npm.equals(mahasiswa.getNPM())
        ));
    }

    @Test
    void testUpdateRole_VerifyTransactionOrder() {
        String userId = "user-123";
        String npm = "2106123456";
        String namaLengkap = "Dr. John Doe";

        when(mockRoleUpdateDTO.getNpm()).thenReturn(npm);
        when(mahasiswaRepository.findByNPM(npm)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(dosenRepository.findById(userId)).thenReturn(Optional.of(mockDosen));
        when(mockDosen.getNamaLengkap()).thenReturn(namaLengkap);
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        dosenToMahasiswaStrategy.updateRole(userId, mockRoleUpdateDTO);

        verify(mahasiswaRepository).findByNPM(npm);

        verify(userRepository).findById(userId);
        verify(dosenRepository).findById(userId);

        verify(mockDosen).getNamaLengkap();

        verify(mockUser).setRole(Role.MAHASISWA);
        verify(userRepository).save(mockUser);

        verify(dosenRepository).deleteById(userId);

        verify(mahasiswaRepository).save(any(Mahasiswa.class));
    }

    @Test
    void testUpdateRole_InheritsNamaLengkapFromDosen() {
        String userId = "user-123";
        String npm = "2106123456";
        String originalNamaLengkap = "Dr. Original Dosen Name";

        when(mockRoleUpdateDTO.getNpm()).thenReturn(npm);
        when(mahasiswaRepository.findByNPM(npm)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(dosenRepository.findById(userId)).thenReturn(Optional.of(mockDosen));
        when(mockDosen.getNamaLengkap()).thenReturn(originalNamaLengkap);
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        dosenToMahasiswaStrategy.updateRole(userId, mockRoleUpdateDTO);

        verify(mockDosen).getNamaLengkap();
        verify(mahasiswaRepository).save(argThat(mahasiswa ->
                originalNamaLengkap.equals(mahasiswa.getNamaLengkap())
        ));
    }

    @Test
    void testUpdateRole_VerifyRepositoryInteractions() {
        String userId = "user-123";
        String npm = "2106123456";
        String namaLengkap = "Dr. John Doe";

        when(mockRoleUpdateDTO.getNpm()).thenReturn(npm);
        when(mahasiswaRepository.findByNPM(npm)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(dosenRepository.findById(userId)).thenReturn(Optional.of(mockDosen));
        when(mockDosen.getNamaLengkap()).thenReturn(namaLengkap);
        when(userRepository.save(mockUser)).thenReturn(mockUser);
        when(mahasiswaRepository.save(any(Mahasiswa.class))).thenReturn(mockMahasiswa);

        User result = dosenToMahasiswaStrategy.updateRole(userId, mockRoleUpdateDTO);

        assertNotNull(result);
        assertEquals(mockUser, result);

        verify(mahasiswaRepository, times(1)).findByNPM(npm);
        verify(userRepository, times(1)).findById(userId);
        verify(dosenRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(mockUser);
        verify(dosenRepository, times(1)).deleteById(userId);
        verify(mahasiswaRepository, times(1)).save(any(Mahasiswa.class));
    }

    @Test
    void testUpdateRole_VerifyUserRoleChange() {
        String userId = "user-123";
        String npm = "2106123456";
        String namaLengkap = "Dr. John Doe";

        when(mockRoleUpdateDTO.getNpm()).thenReturn(npm);
        when(mahasiswaRepository.findByNPM(npm)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(dosenRepository.findById(userId)).thenReturn(Optional.of(mockDosen));
        when(mockDosen.getNamaLengkap()).thenReturn(namaLengkap);
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        dosenToMahasiswaStrategy.updateRole(userId, mockRoleUpdateDTO);

        verify(mockUser).setRole(Role.MAHASISWA);
        verify(mockUser, never()).setRole(Role.ADMIN);
        verify(mockUser, never()).setRole(Role.DOSEN);
    }

    @Test
    void testUpdateRole_VerifyDosenDeletion() {
        String userId = "user-123";
        String npm = "2106123456";
        String namaLengkap = "Dr. John Doe";

        when(mockRoleUpdateDTO.getNpm()).thenReturn(npm);
        when(mahasiswaRepository.findByNPM(npm)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(dosenRepository.findById(userId)).thenReturn(Optional.of(mockDosen));
        when(mockDosen.getNamaLengkap()).thenReturn(namaLengkap);
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        dosenToMahasiswaStrategy.updateRole(userId, mockRoleUpdateDTO);

        verify(dosenRepository).deleteById(userId);
        verify(dosenRepository, times(1)).deleteById(userId); // Should be called exactly once
    }
}