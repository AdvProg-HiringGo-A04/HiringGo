package id.ac.ui.cs.advprog.hiringgo.manajemenakun.strategy.update;

import id.ac.ui.cs.advprog.hiringgo.entity.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.enums.Role;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.RoleUpdateDTO;
import id.ac.ui.cs.advprog.hiringgo.repository.AdminRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminToMahasiswaStrategyTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private MahasiswaRepository mahasiswaRepository;

    @InjectMocks
    private AdminToMahasiswaStrategy adminToMahasiswaStrategy;

    private User mockUser;
    private RoleUpdateDTO mockRoleUpdateDTO;
    private Mahasiswa mockMahasiswa;

    @BeforeEach
    void setUp() {
        mockUser = mock(User.class);
        mockRoleUpdateDTO = mock(RoleUpdateDTO.class);
        mockMahasiswa = mock(Mahasiswa.class);
    }

    @Test
    void testSupports_AdminToMahasiswa_ReturnsTrue() {
        boolean result = adminToMahasiswaStrategy.supports(Role.ADMIN, Role.MAHASISWA);

        assertTrue(result);
    }

    @Test
    void testSupports_MahasiswaToAdmin_ReturnsFalse() {
        boolean result = adminToMahasiswaStrategy.supports(Role.MAHASISWA, Role.ADMIN);

        assertFalse(result);
    }

    @Test
    void testSupports_AdminToDosen_ReturnsFalse() {
        boolean result = adminToMahasiswaStrategy.supports(Role.ADMIN, Role.DOSEN);

        assertFalse(result);
    }

    @Test
    void testUpdateRole_Success() {
        String userId = "user-123";
        String namaLengkap = "John Doe";
        String npm = "2106123456";

        when(mockRoleUpdateDTO.getNamaLengkap()).thenReturn(namaLengkap);
        when(mockRoleUpdateDTO.getNpm()).thenReturn(npm);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(mahasiswaRepository.findByNPM(npm)).thenReturn(Optional.empty());
        when(userRepository.save(mockUser)).thenReturn(mockUser);
        when(mahasiswaRepository.save(any(Mahasiswa.class))).thenReturn(mockMahasiswa);

        User result = adminToMahasiswaStrategy.updateRole(userId, mockRoleUpdateDTO);

        assertEquals(mockUser, result);
        verify(mockUser).setRole(Role.MAHASISWA);
        verify(userRepository).save(mockUser);
        verify(adminRepository).deleteById(userId);
        verify(mahasiswaRepository).save(any(Mahasiswa.class));
    }

    @Test
    void testUpdateRole_UserNotFound() {
        String userId = "non-existent-user";
        String namaLengkap = "John Doe";
        String npm = "2106123456";

        when(mockRoleUpdateDTO.getNamaLengkap()).thenReturn(namaLengkap);
        when(mockRoleUpdateDTO.getNpm()).thenReturn(npm);
        when(mahasiswaRepository.findByNPM(npm)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> adminToMahasiswaStrategy.updateRole(userId, mockRoleUpdateDTO));

        assertEquals("User not found", exception.getReason());
        verify(userRepository).findById(userId);
        verify(mahasiswaRepository).findByNPM(npm);
        verify(adminRepository, never()).deleteById(anyString());
        verify(mahasiswaRepository, never()).save(any());
    }

    @Test
    void testUpdateRole_NamaLengkapNull() {
        String userId = "user-123";

        when(mockRoleUpdateDTO.getNamaLengkap()).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> adminToMahasiswaStrategy.updateRole(userId, mockRoleUpdateDTO));

        assertEquals("Nama lengkap is required for Mahasiswa role", exception.getReason());
        verifyNoInteractions(userRepository, adminRepository, mahasiswaRepository);
    }

    @Test
    void testUpdateRole_NamaLengkapEmpty() {
        String userId = "user-123";

        when(mockRoleUpdateDTO.getNamaLengkap()).thenReturn("   ");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> adminToMahasiswaStrategy.updateRole(userId, mockRoleUpdateDTO));

        assertEquals("Nama lengkap is required for Mahasiswa role", exception.getReason());
        verifyNoInteractions(userRepository, adminRepository, mahasiswaRepository);
    }

    @Test
    void testUpdateRole_NpmNull() {
        String userId = "user-123";
        String namaLengkap = "John Doe";

        when(mockRoleUpdateDTO.getNamaLengkap()).thenReturn(namaLengkap);
        when(mockRoleUpdateDTO.getNpm()).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> adminToMahasiswaStrategy.updateRole(userId, mockRoleUpdateDTO));

        assertEquals("NPM is required for Mahasiswa role", exception.getReason());
        verifyNoInteractions(userRepository, adminRepository, mahasiswaRepository);
    }

    @Test
    void testUpdateRole_NpmEmpty() {
        String userId = "user-123";
        String namaLengkap = "John Doe";

        when(mockRoleUpdateDTO.getNamaLengkap()).thenReturn(namaLengkap);
        when(mockRoleUpdateDTO.getNpm()).thenReturn("   ");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> adminToMahasiswaStrategy.updateRole(userId, mockRoleUpdateDTO));

        assertEquals("NPM is required for Mahasiswa role", exception.getReason());
        verifyNoInteractions(userRepository, adminRepository, mahasiswaRepository);
    }

    @Test
    void testUpdateRole_NpmAlreadyTaken() {
        String userId = "user-123";
        String namaLengkap = "John Doe";
        String npm = "2106123456";
        Mahasiswa existingMahasiswa = mock(Mahasiswa.class);

        when(mockRoleUpdateDTO.getNamaLengkap()).thenReturn(namaLengkap);
        when(mockRoleUpdateDTO.getNpm()).thenReturn(npm);
        when(mahasiswaRepository.findByNPM(npm)).thenReturn(Optional.of(existingMahasiswa));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> adminToMahasiswaStrategy.updateRole(userId, mockRoleUpdateDTO));

        assertEquals("NPM is already taken", exception.getReason());
        verify(mahasiswaRepository).findByNPM(npm);
        verifyNoInteractions(userRepository, adminRepository);
        verify(mahasiswaRepository, never()).save(any());
    }

    @Test
    void testUpdateRole_VerifyMahasiswaCreation() {
        String userId = "user-123";
        String namaLengkap = "John Doe";
        String npm = "2106123456";

        when(mockRoleUpdateDTO.getNamaLengkap()).thenReturn(namaLengkap);
        when(mockRoleUpdateDTO.getNpm()).thenReturn(npm);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(mahasiswaRepository.findByNPM(npm)).thenReturn(Optional.empty());
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        adminToMahasiswaStrategy.updateRole(userId, mockRoleUpdateDTO);

        verify(mahasiswaRepository).save(argThat(mahasiswa ->
                userId.equals(mahasiswa.getId()) &&
                        namaLengkap.equals(mahasiswa.getNamaLengkap()) &&
                        npm.equals(mahasiswa.getNPM())
        ));
    }
}