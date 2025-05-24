package id.ac.ui.cs.advprog.hiringgo.manajemenakun.strategy.update;

import id.ac.ui.cs.advprog.hiringgo.entity.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.entity.Dosen;
import id.ac.ui.cs.advprog.hiringgo.entity.Role;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.RoleUpdateDTO;
import id.ac.ui.cs.advprog.hiringgo.repository.DosenRepository;
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
public class MahasiswaToDosenStrategyTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private MahasiswaRepository mahasiswaRepository;

    @Mock
    private DosenRepository dosenRepository;

    @InjectMocks
    private MahasiswaToDosenStrategy mahasiswaToDosenStrategy;

    private User mockUser;
    private Mahasiswa mockMahasiswa;
    private RoleUpdateDTO mockRoleUpdateDTO;
    private Dosen mockDosen;

    @BeforeEach
    void setUp() {
        mockUser = mock(User.class);
        mockMahasiswa = mock(Mahasiswa.class);
        mockRoleUpdateDTO = mock(RoleUpdateDTO.class);
        mockDosen = mock(Dosen.class);
    }

    @Test
    void testSupports_MahasiswaToDosen_ReturnsTrue() {
        boolean result = mahasiswaToDosenStrategy.supports(Role.MAHASISWA, Role.DOSEN);

        assertTrue(result);
    }

    @Test
    void testSupports_DosenToMahasiswa_ReturnsFalse() {
        boolean result = mahasiswaToDosenStrategy.supports(Role.DOSEN, Role.MAHASISWA);

        assertFalse(result);
    }

    @Test
    void testSupports_MahasiswaToAdmin_ReturnsFalse() {
        boolean result = mahasiswaToDosenStrategy.supports(Role.MAHASISWA, Role.ADMIN);

        assertFalse(result);
    }

    @Test
    void testSupports_AdminToDosen_ReturnsFalse() {
        boolean result = mahasiswaToDosenStrategy.supports(Role.ADMIN, Role.DOSEN);

        assertFalse(result);
    }

    @Test
    void testUpdateRole_Success() {
        String userId = "user-123";
        String nip = "197001012000011001";
        String namaLengkap = "John Doe";

        when(mockRoleUpdateDTO.getNip()).thenReturn(nip);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(mahasiswaRepository.findById(userId)).thenReturn(Optional.of(mockMahasiswa));
        when(mockMahasiswa.getNamaLengkap()).thenReturn(namaLengkap);
        when(dosenRepository.findByNIP(nip)).thenReturn(Optional.empty());
        when(userRepository.save(mockUser)).thenReturn(mockUser);
        when(dosenRepository.save(any(Dosen.class))).thenReturn(mockDosen);

        User result = mahasiswaToDosenStrategy.updateRole(userId, mockRoleUpdateDTO);

        assertEquals(mockUser, result);
        verify(mockUser).setRole(Role.DOSEN);
        verify(userRepository).save(mockUser);
        verify(mahasiswaRepository).deleteById(userId);
        verify(dosenRepository).save(any(Dosen.class));
    }

    @Test
    void testUpdateRole_UserNotFound() {
        String userId = "non-existent-user";
        String nip = "197001012000011001";

        when(mockRoleUpdateDTO.getNip()).thenReturn(nip);
        when(dosenRepository.findByNIP(nip)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> mahasiswaToDosenStrategy.updateRole(userId, mockRoleUpdateDTO));

        assertEquals("User not found", exception.getReason());
        verify(userRepository).findById(userId);
        verify(dosenRepository).findByNIP(nip);
        verify(mahasiswaRepository, never()).findById(anyString());
        verify(mahasiswaRepository, never()).deleteById(anyString());
        verify(dosenRepository, never()).save(any());
    }

    @Test
    void testUpdateRole_MahasiswaNotFound() {
        String userId = "user-123";
        String nip = "197001012000011001";

        when(mockRoleUpdateDTO.getNip()).thenReturn(nip);
        when(dosenRepository.findByNIP(nip)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(mahasiswaRepository.findById(userId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> mahasiswaToDosenStrategy.updateRole(userId, mockRoleUpdateDTO));

        assertEquals("Mahasiswa not found", exception.getReason());
        verify(userRepository).findById(userId);
        verify(mahasiswaRepository).findById(userId);
        verify(dosenRepository).findByNIP(nip);
        verify(mahasiswaRepository, never()).deleteById(anyString());
        verify(dosenRepository, never()).save(any());
    }

    @Test
    void testUpdateRole_NipNull() {
        String userId = "user-123";

        when(mockRoleUpdateDTO.getNip()).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> mahasiswaToDosenStrategy.updateRole(userId, mockRoleUpdateDTO));

        assertEquals("NIP is required for Dosen role", exception.getReason());
        verifyNoInteractions(userRepository, mahasiswaRepository, dosenRepository);
    }

    @Test
    void testUpdateRole_NipEmpty() {
        String userId = "user-123";

        when(mockRoleUpdateDTO.getNip()).thenReturn("   ");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> mahasiswaToDosenStrategy.updateRole(userId, mockRoleUpdateDTO));

        assertEquals("NIP is required for Dosen role", exception.getReason());
        verifyNoInteractions(userRepository, mahasiswaRepository, dosenRepository);
    }

    @Test
    void testUpdateRole_NipAlreadyTaken() {
        String userId = "user-123";
        String nip = "197001012000011001";
        Dosen existingDosen = mock(Dosen.class);

        when(mockRoleUpdateDTO.getNip()).thenReturn(nip);
        when(dosenRepository.findByNIP(nip)).thenReturn(Optional.of(existingDosen));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> mahasiswaToDosenStrategy.updateRole(userId, mockRoleUpdateDTO));

        assertEquals("NIP is already taken", exception.getReason());
        verify(dosenRepository).findByNIP(nip);
        verifyNoInteractions(userRepository, mahasiswaRepository);
        verify(dosenRepository, never()).save(any());
    }

    @Test
    void testUpdateRole_VerifyDosenCreation() {
        String userId = "user-123";
        String nip = "197001012000011001";
        String namaLengkap = "John Doe";

        when(mockRoleUpdateDTO.getNip()).thenReturn(nip);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(mahasiswaRepository.findById(userId)).thenReturn(Optional.of(mockMahasiswa));
        when(mockMahasiswa.getNamaLengkap()).thenReturn(namaLengkap);
        when(dosenRepository.findByNIP(nip)).thenReturn(Optional.empty());
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        mahasiswaToDosenStrategy.updateRole(userId, mockRoleUpdateDTO);

        verify(dosenRepository).save(argThat(dosen ->
                userId.equals(dosen.getId()) &&
                        namaLengkap.equals(dosen.getNamaLengkap()) &&
                        nip.equals(dosen.getNIP())
        ));
    }

    @Test
    void testUpdateRole_VerifyTransactionOrder() {
        String userId = "user-123";
        String nip = "197001012000011001";
        String namaLengkap = "John Doe";

        when(mockRoleUpdateDTO.getNip()).thenReturn(nip);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(mahasiswaRepository.findById(userId)).thenReturn(Optional.of(mockMahasiswa));
        when(mockMahasiswa.getNamaLengkap()).thenReturn(namaLengkap);
        when(dosenRepository.findByNIP(nip)).thenReturn(Optional.empty());
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        mahasiswaToDosenStrategy.updateRole(userId, mockRoleUpdateDTO);

        verify(dosenRepository).findByNIP(nip);

        verify(userRepository).findById(userId);
        verify(mahasiswaRepository).findById(userId);

        verify(mockMahasiswa).getNamaLengkap();

        verify(mockUser).setRole(Role.DOSEN);
        verify(userRepository).save(mockUser);

        verify(mahasiswaRepository).deleteById(userId);

        verify(dosenRepository).save(any(Dosen.class));
    }

    @Test
    void testUpdateRole_InheritsNamaLengkapFromMahasiswa() {
        String userId = "user-123";
        String nip = "197001012000011001";
        String originalNamaLengkap = "Original Mahasiswa Name";

        when(mockRoleUpdateDTO.getNip()).thenReturn(nip);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(mahasiswaRepository.findById(userId)).thenReturn(Optional.of(mockMahasiswa));
        when(mockMahasiswa.getNamaLengkap()).thenReturn(originalNamaLengkap);
        when(dosenRepository.findByNIP(nip)).thenReturn(Optional.empty());
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        mahasiswaToDosenStrategy.updateRole(userId, mockRoleUpdateDTO);

        verify(mockMahasiswa).getNamaLengkap();
        verify(dosenRepository).save(argThat(dosen ->
                originalNamaLengkap.equals(dosen.getNamaLengkap())
        ));
    }

    @Test
    void testUpdateRole_VerifyRepositoryInteractions() {
        String userId = "user-123";
        String nip = "197001012000011001";
        String namaLengkap = "John Doe";

        when(mockRoleUpdateDTO.getNip()).thenReturn(nip);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(mahasiswaRepository.findById(userId)).thenReturn(Optional.of(mockMahasiswa));
        when(mockMahasiswa.getNamaLengkap()).thenReturn(namaLengkap);
        when(dosenRepository.findByNIP(nip)).thenReturn(Optional.empty());
        when(userRepository.save(mockUser)).thenReturn(mockUser);
        when(dosenRepository.save(any(Dosen.class))).thenReturn(mockDosen);

        User result = mahasiswaToDosenStrategy.updateRole(userId, mockRoleUpdateDTO);

        assertNotNull(result);
        assertEquals(mockUser, result);

        verify(dosenRepository, times(1)).findByNIP(nip);
        verify(userRepository, times(1)).findById(userId);
        verify(mahasiswaRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(mockUser);
        verify(mahasiswaRepository, times(1)).deleteById(userId);
        verify(dosenRepository, times(1)).save(any(Dosen.class));
    }
}