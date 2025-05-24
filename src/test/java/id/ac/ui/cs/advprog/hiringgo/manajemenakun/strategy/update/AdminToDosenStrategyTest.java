package id.ac.ui.cs.advprog.hiringgo.manajemenakun.strategy.update;

import id.ac.ui.cs.advprog.hiringgo.entity.Dosen;
import id.ac.ui.cs.advprog.hiringgo.entity.Role;
import id.ac.ui.cs.advprog.hiringgo.entity.User;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminToDosenStrategyTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private DosenRepository dosenRepository;

    @InjectMocks
    private AdminToDosenStrategy adminToDosenStrategy;

    private User mockUser;
    private RoleUpdateDTO mockRoleUpdateDTO;
    private Dosen mockDosen;

    @BeforeEach
    void setUp() {
        mockUser = mock(User.class);
        mockRoleUpdateDTO = mock(RoleUpdateDTO.class);
        mockDosen = mock(Dosen.class);
    }

    @Test
    void testSupports_AdminToDosen_ReturnsTrue() {
        boolean result = adminToDosenStrategy.supports(Role.ADMIN, Role.DOSEN);

        assertTrue(result);
    }

    @Test
    void testSupports_DosenToAdmin_ReturnsFalse() {
        boolean result = adminToDosenStrategy.supports(Role.DOSEN, Role.ADMIN);

        assertFalse(result);
    }

    @Test
    void testSupports_AdminToMahasiswa_ReturnsFalse() {
        boolean result = adminToDosenStrategy.supports(Role.ADMIN, Role.MAHASISWA);

        assertFalse(result);
    }

    @Test
    void testUpdateRole_Success() {
        String userId = "user-123";
        String namaLengkap = "Dr. John Doe";
        String nip = "197001012000011001";

        when(mockRoleUpdateDTO.getNamaLengkap()).thenReturn(namaLengkap);
        when(mockRoleUpdateDTO.getNip()).thenReturn(nip);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(dosenRepository.findByNIP(nip)).thenReturn(Optional.empty());
        when(userRepository.save(mockUser)).thenReturn(mockUser);
        when(dosenRepository.save(any(Dosen.class))).thenReturn(mockDosen);

        User result = adminToDosenStrategy.updateRole(userId, mockRoleUpdateDTO);

        assertEquals(mockUser, result);
        verify(mockUser).setRole(Role.DOSEN);
        verify(userRepository).save(mockUser);
        verify(adminRepository).deleteById(userId);
        verify(dosenRepository).save(any(Dosen.class));
    }

    @Test
    void testUpdateRole_UserNotFound() {
        String userId = "non-existent-user";
        String namaLengkap = "Dr. John Doe";
        String nip = "197001012000011001";

        when(mockRoleUpdateDTO.getNamaLengkap()).thenReturn(namaLengkap);
        when(mockRoleUpdateDTO.getNip()).thenReturn(nip);
        when(dosenRepository.findByNIP(nip)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> adminToDosenStrategy.updateRole(userId, mockRoleUpdateDTO));

        assertEquals("User not found", exception.getReason());
        verify(userRepository).findById(userId);
        verify(dosenRepository).findByNIP(nip);
        verify(adminRepository, never()).deleteById(anyString());
        verify(dosenRepository, never()).save(any());
    }

    @Test
    void testUpdateRole_NamaLengkapNull() {
        String userId = "user-123";

        when(mockRoleUpdateDTO.getNamaLengkap()).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> adminToDosenStrategy.updateRole(userId, mockRoleUpdateDTO));

        assertEquals("Nama lengkap is required for Dosen role", exception.getReason());
        verifyNoInteractions(userRepository, adminRepository, dosenRepository);
    }

    @Test
    void testUpdateRole_NamaLengkapEmpty() {
        String userId = "user-123";

        when(mockRoleUpdateDTO.getNamaLengkap()).thenReturn("   ");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> adminToDosenStrategy.updateRole(userId, mockRoleUpdateDTO));

        assertEquals("Nama lengkap is required for Dosen role", exception.getReason());
        verifyNoInteractions(userRepository, adminRepository, dosenRepository);
    }

    @Test
    void testUpdateRole_NipNull() {
        String userId = "user-123";
        String namaLengkap = "Dr. John Doe";

        when(mockRoleUpdateDTO.getNamaLengkap()).thenReturn(namaLengkap);
        when(mockRoleUpdateDTO.getNip()).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> adminToDosenStrategy.updateRole(userId, mockRoleUpdateDTO));

        assertEquals("NIP is required for Dosen role", exception.getReason());
        verifyNoInteractions(userRepository, adminRepository, dosenRepository);
    }

    @Test
    void testUpdateRole_NipEmpty() {
        String userId = "user-123";
        String namaLengkap = "Dr. John Doe";

        when(mockRoleUpdateDTO.getNamaLengkap()).thenReturn(namaLengkap);
        when(mockRoleUpdateDTO.getNip()).thenReturn("   ");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> adminToDosenStrategy.updateRole(userId, mockRoleUpdateDTO));

        assertEquals("NIP is required for Dosen role", exception.getReason());
        verifyNoInteractions(userRepository, adminRepository, dosenRepository);
    }

    @Test
    void testUpdateRole_NipAlreadyTaken() {
        String userId = "user-123";
        String namaLengkap = "Dr. John Doe";
        String nip = "197001012000011001";
        Dosen existingDosen = mock(Dosen.class);

        when(mockRoleUpdateDTO.getNamaLengkap()).thenReturn(namaLengkap);
        when(mockRoleUpdateDTO.getNip()).thenReturn(nip);
        when(dosenRepository.findByNIP(nip)).thenReturn(Optional.of(existingDosen));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> adminToDosenStrategy.updateRole(userId, mockRoleUpdateDTO));

        assertEquals("NIP is already taken", exception.getReason());
        verify(dosenRepository).findByNIP(nip);
        verifyNoInteractions(userRepository, adminRepository);
        verify(dosenRepository, never()).save(any());
    }

    @Test
    void testUpdateRole_VerifyDosenCreation() {
        String userId = "user-123";
        String namaLengkap = "Dr. John Doe";
        String nip = "197001012000011001";

        when(mockRoleUpdateDTO.getNamaLengkap()).thenReturn(namaLengkap);
        when(mockRoleUpdateDTO.getNip()).thenReturn(nip);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(dosenRepository.findByNIP(nip)).thenReturn(Optional.empty());
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        adminToDosenStrategy.updateRole(userId, mockRoleUpdateDTO);

        verify(dosenRepository).save(argThat(dosen ->
                userId.equals(dosen.getId()) &&
                        namaLengkap.equals(dosen.getNamaLengkap()) &&
                        nip.equals(dosen.getNIP())
        ));
    }
}