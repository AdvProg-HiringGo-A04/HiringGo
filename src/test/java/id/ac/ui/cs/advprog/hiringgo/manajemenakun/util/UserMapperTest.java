package id.ac.ui.cs.advprog.hiringgo.manajemenakun.util;

import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.entity.Dosen;
import id.ac.ui.cs.advprog.hiringgo.enums.Role;
import id.ac.ui.cs.advprog.hiringgo.entity.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.AdminUserResponse;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.DosenUserResponse;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.MahasiswaUserResponse;
import id.ac.ui.cs.advprog.hiringgo.repository.DosenRepository;
import id.ac.ui.cs.advprog.hiringgo.repository.MahasiswaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {

    @Mock
    private DosenRepository dosenRepository;

    @Mock
    private MahasiswaRepository mahasiswaRepository;

    @InjectMocks
    private UserMapper userMapper;

    private User mockAdminUser;
    private User mockDosenUser;
    private User mockMahasiswaUser;
    private Dosen mockDosen;
    private Mahasiswa mockMahasiswa;

    @BeforeEach
    void setUp() {
        mockAdminUser = new User();
        mockAdminUser.setId("admin-123");
        mockAdminUser.setEmail("admin@example.com");
        mockAdminUser.setRole(Role.ADMIN);

        mockDosenUser = new User();
        mockDosenUser.setId("dosen-123");
        mockDosenUser.setEmail("dosen@example.com");
        mockDosenUser.setRole(Role.DOSEN);

        mockMahasiswaUser = new User();
        mockMahasiswaUser.setId("mahasiswa-123");
        mockMahasiswaUser.setEmail("mahasiswa@example.com");
        mockMahasiswaUser.setRole(Role.MAHASISWA);

        mockDosen = new Dosen();
        mockDosen.setId("dosen-123");
        mockDosen.setNamaLengkap("Dr. John Doe");
        mockDosen.setNIP("197001012000011001");

        mockMahasiswa = new Mahasiswa();
        mockMahasiswa.setId("mahasiswa-123");
        mockMahasiswa.setNamaLengkap("Jane Smith");
        mockMahasiswa.setNPM("2106123456");
    }

    @Test
    void testMapUserToResponse_NullUser_ReturnsNull() {
        Object result = userMapper.mapUserToResponse(null);

        assertNull(result);
        verifyNoInteractions(dosenRepository, mahasiswaRepository);
    }

    @Test
    void testMapUserToResponse_AdminUser_ReturnsAdminUserResponse() {
        Object result = userMapper.mapUserToResponse(mockAdminUser);

        assertNotNull(result);
        assertTrue(result instanceof AdminUserResponse);

        AdminUserResponse adminResponse = (AdminUserResponse) result;
        assertEquals("admin@example.com", adminResponse.getEmail());
        assertEquals(Role.ADMIN, adminResponse.getRole());

        verifyNoInteractions(dosenRepository, mahasiswaRepository);
    }

    @Test
    void testMapUserToResponse_DosenUser_WithDosenData_ReturnsDosenUserResponse() {
        when(dosenRepository.findById("dosen-123")).thenReturn(Optional.of(mockDosen));

        Object result = userMapper.mapUserToResponse(mockDosenUser);

        assertNotNull(result);
        assertTrue(result instanceof DosenUserResponse);

        DosenUserResponse dosenResponse = (DosenUserResponse) result;
        assertEquals("dosen@example.com", dosenResponse.getEmail());
        assertEquals(Role.DOSEN, dosenResponse.getRole());
        assertEquals("Dr. John Doe", dosenResponse.getNamaLengkap());
        assertEquals("197001012000011001", dosenResponse.getNip());

        verify(dosenRepository).findById("dosen-123");
        verifyNoInteractions(mahasiswaRepository);
    }

    @Test
    void testMapUserToResponse_DosenUser_WithoutDosenData_ReturnsDosenUserResponseWithNullFields() {
        when(dosenRepository.findById("dosen-123")).thenReturn(Optional.empty());

        Object result = userMapper.mapUserToResponse(mockDosenUser);

        assertNotNull(result);
        assertTrue(result instanceof DosenUserResponse);

        DosenUserResponse dosenResponse = (DosenUserResponse) result;
        assertEquals("dosen@example.com", dosenResponse.getEmail());
        assertEquals(Role.DOSEN, dosenResponse.getRole());
        assertNull(dosenResponse.getNamaLengkap());
        assertNull(dosenResponse.getNip());

        verify(dosenRepository).findById("dosen-123");
        verifyNoInteractions(mahasiswaRepository);
    }

    @Test
    void testMapUserToResponse_MahasiswaUser_WithMahasiswaData_ReturnsMahasiswaUserResponse() {
        when(mahasiswaRepository.findById("mahasiswa-123")).thenReturn(Optional.of(mockMahasiswa));

        Object result = userMapper.mapUserToResponse(mockMahasiswaUser);

        assertNotNull(result);
        assertTrue(result instanceof MahasiswaUserResponse);

        MahasiswaUserResponse mahasiswaResponse = (MahasiswaUserResponse) result;
        assertEquals("mahasiswa@example.com", mahasiswaResponse.getEmail());
        assertEquals(Role.MAHASISWA, mahasiswaResponse.getRole());
        assertEquals("Jane Smith", mahasiswaResponse.getNamaLengkap());
        assertEquals("2106123456", mahasiswaResponse.getNim());

        verify(mahasiswaRepository).findById("mahasiswa-123");
        verifyNoInteractions(dosenRepository);
    }

    @Test
    void testMapUserToResponse_MahasiswaUser_WithoutMahasiswaData_ReturnsMahasiswaUserResponseWithNullFields() {
        when(mahasiswaRepository.findById("mahasiswa-123")).thenReturn(Optional.empty());

        Object result = userMapper.mapUserToResponse(mockMahasiswaUser);

        assertNotNull(result);
        assertTrue(result instanceof MahasiswaUserResponse);

        MahasiswaUserResponse mahasiswaResponse = (MahasiswaUserResponse) result;
        assertEquals("mahasiswa@example.com", mahasiswaResponse.getEmail());
        assertEquals(Role.MAHASISWA, mahasiswaResponse.getRole());
        assertNull(mahasiswaResponse.getNamaLengkap());
        assertNull(mahasiswaResponse.getNim());

        verify(mahasiswaRepository).findById("mahasiswa-123");
        verifyNoInteractions(dosenRepository);
    }

    @Test
    void testMapUserToResponse_NullRole_ReturnsBaseUserResponse() {
        User userWithNullRole = new User();
        userWithNullRole.setId("unknown-123");
        userWithNullRole.setEmail("unknown@example.com");
        userWithNullRole.setRole(null);
        assertThrows(NullPointerException.class, () -> userMapper.mapUserToResponse(userWithNullRole));

        verifyNoInteractions(dosenRepository, mahasiswaRepository);
    }

    @Test
    void testMapUserToResponse_UnknownRole_ReturnsBaseUserResponse() {
        User adminUser = new User();
        adminUser.setId("admin-123");
        adminUser.setEmail("admin@example.com");
        adminUser.setRole(Role.ADMIN);

        Object result = userMapper.mapUserToResponse(adminUser);

        assertNotNull(result);
        assertTrue(result instanceof AdminUserResponse);

        AdminUserResponse adminResponse = (AdminUserResponse) result;
        assertEquals("admin@example.com", adminResponse.getEmail());
        assertEquals(Role.ADMIN, adminResponse.getRole());

        verifyNoInteractions(dosenRepository, mahasiswaRepository);
    }

    @Test
    void testMapUserToResponse_NullRole_ThrowsNullPointerException() {
        User userWithNullRole = new User();
        userWithNullRole.setId("unknown-123");
        userWithNullRole.setEmail("unknown@example.com");
        userWithNullRole.setRole(null);
        assertThrows(NullPointerException.class, () -> userMapper.mapUserToResponse(userWithNullRole));

        verifyNoInteractions(dosenRepository, mahasiswaRepository);
    }

    @Test
    void testMapUserToResponse_AllRoleTypes_CorrectMapping() {
        when(dosenRepository.findById("dosen-123")).thenReturn(Optional.of(mockDosen));
        when(mahasiswaRepository.findById("mahasiswa-123")).thenReturn(Optional.of(mockMahasiswa));

        Object adminResult = userMapper.mapUserToResponse(mockAdminUser);
        Object dosenResult = userMapper.mapUserToResponse(mockDosenUser);
        Object mahasiswaResult = userMapper.mapUserToResponse(mockMahasiswaUser);

        assertTrue(adminResult instanceof AdminUserResponse);
        assertTrue(dosenResult instanceof DosenUserResponse);
        assertTrue(mahasiswaResult instanceof MahasiswaUserResponse);

        assertFalse(adminResult instanceof DosenUserResponse);
        assertFalse(dosenResult instanceof MahasiswaUserResponse);
        assertFalse(mahasiswaResult instanceof AdminUserResponse);

        verify(dosenRepository).findById("dosen-123");
        verify(mahasiswaRepository).findById("mahasiswa-123");
    }

    @Test
    void testMapUserToResponse_DosenUser_WithNullDosenFields() {
        Dosen dosenWithNullFields = new Dosen();
        dosenWithNullFields.setId("dosen-123");
        dosenWithNullFields.setNamaLengkap(null);
        dosenWithNullFields.setNIP(null);

        when(dosenRepository.findById("dosen-123")).thenReturn(Optional.of(dosenWithNullFields));

        Object result = userMapper.mapUserToResponse(mockDosenUser);

        assertNotNull(result);
        assertTrue(result instanceof DosenUserResponse);

        DosenUserResponse dosenResponse = (DosenUserResponse) result;
        assertEquals("dosen@example.com", dosenResponse.getEmail());
        assertEquals(Role.DOSEN, dosenResponse.getRole());
        assertNull(dosenResponse.getNamaLengkap());
        assertNull(dosenResponse.getNip());

        verify(dosenRepository).findById("dosen-123");
    }

    @Test
    void testMapUserToResponse_MahasiswaUser_WithNullMahasiswaFields() {
        Mahasiswa mahasiswaWithNullFields = new Mahasiswa();
        mahasiswaWithNullFields.setId("mahasiswa-123");
        mahasiswaWithNullFields.setNamaLengkap(null);
        mahasiswaWithNullFields.setNPM(null);

        when(mahasiswaRepository.findById("mahasiswa-123")).thenReturn(Optional.of(mahasiswaWithNullFields));

        Object result = userMapper.mapUserToResponse(mockMahasiswaUser);

        assertNotNull(result);
        assertTrue(result instanceof MahasiswaUserResponse);

        MahasiswaUserResponse mahasiswaResponse = (MahasiswaUserResponse) result;
        assertEquals("mahasiswa@example.com", mahasiswaResponse.getEmail());
        assertEquals(Role.MAHASISWA, mahasiswaResponse.getRole());
        assertNull(mahasiswaResponse.getNamaLengkap());
        assertNull(mahasiswaResponse.getNim());

        verify(mahasiswaRepository).findById("mahasiswa-123");
    }

    @Test
    void testMapUserToResponse_UserWithNullEmail() {
        User userWithNullEmail = new User();
        userWithNullEmail.setId("admin-123");
        userWithNullEmail.setEmail(null);
        userWithNullEmail.setRole(Role.ADMIN);

        Object result = userMapper.mapUserToResponse(userWithNullEmail);

        assertNotNull(result);
        assertTrue(result instanceof AdminUserResponse);

        AdminUserResponse adminResponse = (AdminUserResponse) result;
        assertNull(adminResponse.getEmail());
        assertEquals(Role.ADMIN, adminResponse.getRole());
    }

    @Test
    void testMapUserToResponse_UserWithEmptyStringFields() {
        User userWithEmptyEmail = new User();
        userWithEmptyEmail.setId("admin-123");
        userWithEmptyEmail.setEmail("");
        userWithEmptyEmail.setRole(Role.ADMIN);

        Object result = userMapper.mapUserToResponse(userWithEmptyEmail);

        assertNotNull(result);
        assertTrue(result instanceof AdminUserResponse);

        AdminUserResponse adminResponse = (AdminUserResponse) result;
        assertEquals("", adminResponse.getEmail());
        assertEquals(Role.ADMIN, adminResponse.getRole());
    }


    @Test
    void testMapUserToResponse_DosenUser_RepositoryException() {
        when(dosenRepository.findById("dosen-123")).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> userMapper.mapUserToResponse(mockDosenUser));

        verify(dosenRepository).findById("dosen-123");
        verifyNoInteractions(mahasiswaRepository);
    }

    @Test
    void testMapUserToResponse_MahasiswaUser_RepositoryException() {
        when(mahasiswaRepository.findById("mahasiswa-123")).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> userMapper.mapUserToResponse(mockMahasiswaUser));

        verify(mahasiswaRepository).findById("mahasiswa-123");
        verifyNoInteractions(dosenRepository);
    }

    @Test
    void testMapUserToResponse_MultipleMappings() {
        when(dosenRepository.findById("dosen-123")).thenReturn(Optional.of(mockDosen));
        when(mahasiswaRepository.findById("mahasiswa-123")).thenReturn(Optional.of(mockMahasiswa));

        Object adminResult = userMapper.mapUserToResponse(mockAdminUser);
        Object dosenResult = userMapper.mapUserToResponse(mockDosenUser);
        Object mahasiswaResult = userMapper.mapUserToResponse(mockMahasiswaUser);

        assertTrue(adminResult instanceof AdminUserResponse);
        assertTrue(dosenResult instanceof DosenUserResponse);
        assertTrue(mahasiswaResult instanceof MahasiswaUserResponse);

        verify(dosenRepository).findById("dosen-123");
        verify(mahasiswaRepository).findById("mahasiswa-123");

        AdminUserResponse adminResponse = (AdminUserResponse) adminResult;
        DosenUserResponse dosenResponse = (DosenUserResponse) dosenResult;
        MahasiswaUserResponse mahasiswaResponse = (MahasiswaUserResponse) mahasiswaResult;

        assertEquals("admin@example.com", adminResponse.getEmail());
        assertEquals("dosen@example.com", dosenResponse.getEmail());
        assertEquals("mahasiswa@example.com", mahasiswaResponse.getEmail());

        assertEquals("Dr. John Doe", dosenResponse.getNamaLengkap());
        assertEquals("Jane Smith", mahasiswaResponse.getNamaLengkap());
    }

    @Test
    void testMapUserToResponse_DoesNotCacheRepositoryResults() {
        when(dosenRepository.findById("dosen-123")).thenReturn(Optional.of(mockDosen));

        userMapper.mapUserToResponse(mockDosenUser);
        userMapper.mapUserToResponse(mockDosenUser);

        verify(dosenRepository, times(2)).findById("dosen-123");
    }

    @Test
    void testMapUserToResponse_EachRoleTypeUsesCorrectRepository() {
        when(dosenRepository.findById("dosen-123")).thenReturn(Optional.of(mockDosen));
        when(mahasiswaRepository.findById("mahasiswa-123")).thenReturn(Optional.of(mockMahasiswa));

        userMapper.mapUserToResponse(mockAdminUser);     // Should not call any repository
        userMapper.mapUserToResponse(mockDosenUser);     // Should call dosenRepository only
        userMapper.mapUserToResponse(mockMahasiswaUser); // Should call mahasiswaRepository only

        verify(dosenRepository, times(1)).findById("dosen-123");
        verify(mahasiswaRepository, times(1)).findById("mahasiswa-123");

        verify(dosenRepository, never()).findById("admin-123");
        verify(mahasiswaRepository, never()).findById("admin-123");
    }
}