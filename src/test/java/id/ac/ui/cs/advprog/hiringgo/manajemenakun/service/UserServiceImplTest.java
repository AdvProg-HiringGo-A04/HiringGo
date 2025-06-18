package id.ac.ui.cs.advprog.hiringgo.manajemenakun.service;

import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.AdminDTO;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.DosenDTO;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.RoleUpdateDTO;
import id.ac.ui.cs.advprog.hiringgo.entity.Admin;
import id.ac.ui.cs.advprog.hiringgo.entity.Dosen;
import id.ac.ui.cs.advprog.hiringgo.entity.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.enums.Role;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.factory.AccountFactory;
import id.ac.ui.cs.advprog.hiringgo.repository.AdminRepository;
import id.ac.ui.cs.advprog.hiringgo.repository.DosenRepository;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.strategy.delete.DeleteUserStrategy;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.strategy.update.UpdateRoleStrategy;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.util.UserMapper;
import id.ac.ui.cs.advprog.hiringgo.repository.MahasiswaRepository;
import id.ac.ui.cs.advprog.hiringgo.repository.UserRepository;
import id.ac.ui.cs.advprog.hiringgo.security.CurrentUserProvider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private DosenRepository dosenRepository;

    @Mock
    private MahasiswaRepository mahasiswaRepository;

    @Mock
    private AccountFactory<AdminDTO> adminFactory;

    @Mock
    private AccountFactory<DosenDTO> dosenFactory;

    @Mock
    private List<UpdateRoleStrategy> updateRoleStrategies;

    @Mock
    private List<DeleteUserStrategy> deleteUserStrategies;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UpdateRoleStrategy updateRoleStrategy;

    @Mock
    private DeleteUserStrategy deleteUserStrategy;

    // Remove @InjectMocks annotation and create manually
    private UserServiceImpl userService;

    private User mockUser;
    private Admin mockAdmin;
    private Dosen mockDosen;
    private Mahasiswa mockMahasiswa;
    private AdminDTO mockAdminDTO;
    private DosenDTO mockDosenDTO;
    private RoleUpdateDTO mockRoleUpdateDTO;

    @BeforeEach
    void setUp() {
        // Setup common test objects
        mockUser = mock(User.class);
        mockAdmin = mock(Admin.class);
        mockDosen = mock(Dosen.class);
        mockMahasiswa = mock(Mahasiswa.class);
        mockAdminDTO = mock(AdminDTO.class);
        mockDosenDTO = mock(DosenDTO.class);
        mockRoleUpdateDTO = mock(RoleUpdateDTO.class);

        // Create UserServiceImpl manually with all mocks
        userService = new UserServiceImpl(
                adminFactory, dosenFactory, userRepository, adminRepository,
                dosenRepository, mahasiswaRepository,
                Arrays.asList(), Arrays.asList(), // Empty strategy lists for basic tests
                currentUserProvider, userMapper
        );
    }

    // ========== CREATE OPERATIONS TESTS ==========

    @Test
    void testCreateAdmin() {
        // Arrange
        when(adminFactory.createAccount(any(AdminDTO.class))).thenReturn(mockUser);

        // Act
        User result = userService.createAdmin(mockAdminDTO);

        // Assert
        assertNotNull(result, "createAdmin should return a User object");
        assertEquals(mockUser, result);
        verify(adminFactory).createAccount(mockAdminDTO);
    }

    @Test
    void testCreateDosen() {
        // Arrange
        when(dosenFactory.createAccount(any(DosenDTO.class))).thenReturn(mockUser);

        // Act
        User result = userService.createDosen(mockDosenDTO);

        // Assert
        assertNotNull(result, "createDosen should return a User object");
        assertEquals(mockUser, result);
        verify(dosenFactory).createAccount(mockDosenDTO);
    }

    @Test
    void testMockSetup() {
        // Simple test to verify mock setup
        when(adminFactory.createAccount(any(AdminDTO.class))).thenReturn(mockUser);
        User result = adminFactory.createAccount(mockAdminDTO);
        assertNotNull(result);
        assertEquals(mockUser, result);
    }

    // ========== READ OPERATIONS TESTS ==========

    @Test
    void testGetUserResponseById() {
        // Arrange
        String userId = "user-123";
        Object expectedResponse = new Object();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(userMapper.mapUserToResponse(mockUser)).thenReturn(expectedResponse);

        Object result = userService.getUserResponseById(userId);

        assertEquals(expectedResponse, result);
        verify(userRepository).findById(userId);
        verify(userMapper).mapUserToResponse(mockUser);
    }

    @Test
    void testGetAllUsersResponse() {
        List<User> userList = Arrays.asList(mockUser);
        Object mockResponse = new Object();

        when(userRepository.findAll()).thenReturn(userList);
        when(userMapper.mapUserToResponse(any(User.class))).thenReturn(mockResponse);

        List<Object> result = userService.getAllUsersResponse();

        assertEquals(1, result.size());
        assertEquals(mockResponse, result.get(0));
        verify(userRepository).findAll();
        verify(userMapper).mapUserToResponse(mockUser);
    }

    @Test
    void testGetAllUsers() {
        List<User> expectedUsers = Arrays.asList(mockUser);
        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<User> result = userService.getAllUsers();

        assertEquals(expectedUsers, result);
        verify(userRepository).findAll();
    }

    @Test
    void testGetAllAdmins() {
        List<Admin> expectedAdmins = Arrays.asList(mockAdmin);
        when(adminRepository.findAll()).thenReturn(expectedAdmins);

        List<Admin> result = userService.getAllAdmins();

        assertEquals(expectedAdmins, result);
        verify(adminRepository).findAll();
    }

    @Test
    void testGetAllDosens() {
        // Arrange
        List<Dosen> expectedDosens = Arrays.asList(mockDosen);
        when(dosenRepository.findAll()).thenReturn(expectedDosens);

        // Act
        List<Dosen> result = userService.getAllDosens();

        // Assert
        assertEquals(expectedDosens, result);
        verify(dosenRepository).findAll();
    }

    @Test
    void testGetAllMahasiswas() {
        // Arrange
        List<Mahasiswa> expectedMahasiswas = Arrays.asList(mockMahasiswa);
        when(mahasiswaRepository.findAll()).thenReturn(expectedMahasiswas);

        // Act
        List<Mahasiswa> result = userService.getAllMahasiswas();

        // Assert
        assertEquals(expectedMahasiswas, result);
        verify(mahasiswaRepository).findAll();
    }

    @Test
    void testGetUserById_UserExists() {
        // Arrange
        String userId = "user-123";
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // Act
        User result = userService.getUserById(userId);

        // Assert
        assertEquals(mockUser, result);
        verify(userRepository).findById(userId);
    }

    @Test
    void testGetUserById_UserNotFound() {
        // Arrange
        String userId = "non-existent-user";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.getUserById(userId));

        assertEquals("User not found", exception.getReason());
        verify(userRepository).findById(userId);
    }

    @Test
    void testGetAdminById_AdminExists() {
        // Arrange
        String adminId = "admin-123";
        when(adminRepository.findById(adminId)).thenReturn(Optional.of(mockAdmin));

        // Act
        Admin result = userService.getAdminById(adminId);

        // Assert
        assertEquals(mockAdmin, result);
        verify(adminRepository).findById(adminId);
    }

    @Test
    void testGetAdminById_AdminNotFound() {
        // Arrange
        String adminId = "non-existent-admin";
        when(adminRepository.findById(adminId)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.getAdminById(adminId));

        assertEquals("Admin not found", exception.getReason());
        verify(adminRepository).findById(adminId);
    }

    @Test
    void testGetDosenById_DosenExists() {
        // Arrange
        String dosenId = "dosen-123";
        when(dosenRepository.findById(dosenId)).thenReturn(Optional.of(mockDosen));

        // Act
        Dosen result = userService.getDosenById(dosenId);

        // Assert
        assertEquals(mockDosen, result);
        verify(dosenRepository).findById(dosenId);
    }

    @Test
    void testGetDosenById_DosenNotFound() {
        // Arrange
        String dosenId = "non-existent-dosen";
        when(dosenRepository.findById(dosenId)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.getDosenById(dosenId));

        assertEquals("Dosen not found", exception.getReason());
        verify(dosenRepository).findById(dosenId);
    }

    @Test
    void testGetMahasiswaById_MahasiswaExists() {
        // Arrange
        String mahasiswaId = "mahasiswa-123";
        when(mahasiswaRepository.findById(mahasiswaId)).thenReturn(Optional.of(mockMahasiswa));

        // Act
        Mahasiswa result = userService.getMahasiswaById(mahasiswaId);

        // Assert
        assertEquals(mockMahasiswa, result);
        verify(mahasiswaRepository).findById(mahasiswaId);
    }

    @Test
    void testGetMahasiswaById_MahasiswaNotFound() {
        // Arrange
        String mahasiswaId = "non-existent-mahasiswa";
        when(mahasiswaRepository.findById(mahasiswaId)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.getMahasiswaById(mahasiswaId));

        assertEquals("Mahasiswa not found", exception.getReason());
        verify(mahasiswaRepository).findById(mahasiswaId);
    }

    // ========== UPDATE OPERATIONS TESTS ==========

    @Test
    void testUpdateUserRole_CannotUpdateOwnRole() {
        // Arrange
        String userId = "admin-123";
        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.updateUserRole(userId, mockRoleUpdateDTO));

        assertEquals("Admin cannot update their own role", exception.getReason());
        verify(currentUserProvider).getCurrentUserId();
        verifyNoInteractions(userRepository);
    }

    @Test
    void testUpdateUserRole_SameRole() {
        // Arrange
        String userId = "user-123";
        String currentUserId = "admin-123";
        Role role = Role.DOSEN;

        when(currentUserProvider.getCurrentUserId()).thenReturn(currentUserId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(mockUser.getRole()).thenReturn(role);
        when(mockRoleUpdateDTO.getRole()).thenReturn(role);

        // Act
        User result = userService.updateUserRole(userId, mockRoleUpdateDTO);

        // Assert
        assertEquals(mockUser, result);
        verify(currentUserProvider).getCurrentUserId();
        verify(userRepository).findById(userId);
        verify(mockUser).getRole();
        verify(mockRoleUpdateDTO).getRole();
    }

    @Test
    void testUpdateUserRole_StrategyFound() {
        // Arrange
        String userId = "user-123";
        String currentUserId = "admin-123";
        Role oldRole = Role.DOSEN;
        Role newRole = Role.ADMIN;

        userService = new UserServiceImpl(
                adminFactory, dosenFactory, userRepository, adminRepository,
                dosenRepository, mahasiswaRepository, Arrays.asList(updateRoleStrategy),
                Arrays.asList(deleteUserStrategy), currentUserProvider, userMapper
        );

        when(currentUserProvider.getCurrentUserId()).thenReturn(currentUserId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(mockUser.getRole()).thenReturn(oldRole);
        when(mockRoleUpdateDTO.getRole()).thenReturn(newRole);
        when(updateRoleStrategy.supports(oldRole, newRole)).thenReturn(true);
        when(updateRoleStrategy.updateRole(userId, mockRoleUpdateDTO)).thenReturn(mockUser);

        // Act
        User result = userService.updateUserRole(userId, mockRoleUpdateDTO);

        // Assert
        assertEquals(mockUser, result);
        verify(currentUserProvider).getCurrentUserId();
        verify(userRepository).findById(userId);
        verify(mockUser).getRole();
        verify(mockRoleUpdateDTO).getRole();
        verify(updateRoleStrategy).supports(oldRole, newRole);
        verify(updateRoleStrategy).updateRole(userId, mockRoleUpdateDTO);
    }

    @Test
    void testUpdateUserRole_NoStrategyFound() {
        // Arrange
        String userId = "user-123";
        String currentUserId = "admin-123";
        Role oldRole = Role.DOSEN;
        Role newRole = Role.ADMIN;

        userService = new UserServiceImpl(
                adminFactory, dosenFactory, userRepository, adminRepository,
                dosenRepository, mahasiswaRepository, Arrays.asList(updateRoleStrategy),
                Arrays.asList(deleteUserStrategy), currentUserProvider, userMapper
        );

        when(currentUserProvider.getCurrentUserId()).thenReturn(currentUserId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(mockUser.getRole()).thenReturn(oldRole);
        when(mockRoleUpdateDTO.getRole()).thenReturn(newRole);
        when(updateRoleStrategy.supports(oldRole, newRole)).thenReturn(false);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.updateUserRole(userId, mockRoleUpdateDTO));

        assertEquals("Cannot change role from " + oldRole + " to " + newRole, exception.getReason());
        verify(currentUserProvider).getCurrentUserId();
        verify(userRepository).findById(userId);
        verify(mockUser).getRole();
        verify(mockRoleUpdateDTO).getRole();
        verify(updateRoleStrategy).supports(oldRole, newRole);
        verify(updateRoleStrategy, never()).updateRole(anyString(), any());
    }

    @Test
    void testUpdateUserRole_UserNotFound() {
        // Arrange
        String userId = "non-existent-user";
        String currentUserId = "admin-123";

        when(currentUserProvider.getCurrentUserId()).thenReturn(currentUserId);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.updateUserRole(userId, mockRoleUpdateDTO));

        assertEquals("User not found", exception.getReason());
        verify(currentUserProvider).getCurrentUserId();
        verify(userRepository).findById(userId);
    }

    // ========== DELETE OPERATIONS TESTS ==========

    @Test
    void testDeleteAdmin_Success() {
        // Arrange
        String userId = "admin-123";
        String currentUserId = "other-admin-456";

        userService = new UserServiceImpl(
                adminFactory, dosenFactory, userRepository, adminRepository,
                dosenRepository, mahasiswaRepository, Arrays.asList(updateRoleStrategy),
                Arrays.asList(deleteUserStrategy), currentUserProvider, userMapper
        );

        when(currentUserProvider.getCurrentUserId()).thenReturn(currentUserId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(mockUser.getRole()).thenReturn(Role.ADMIN);
        when(deleteUserStrategy.supports(Role.ADMIN)).thenReturn(true);

        // Act
        userService.deleteAdmin(userId);

        // Assert
        verify(currentUserProvider).getCurrentUserId();
        verify(userRepository).findById(userId);
        verify(mockUser, atLeastOnce()).getRole(); // Allow multiple calls
        verify(deleteUserStrategy).supports(Role.ADMIN);
        verify(deleteUserStrategy).deleteUser(userId);
    }

    @Test
    void testDeleteAdmin_CannotDeleteOwnAccount() {
        // Arrange
        String userId = "admin-123";
        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.deleteAdmin(userId));

        assertEquals("Admin cannot delete their own account", exception.getReason());
        verify(currentUserProvider).getCurrentUserId();
        verifyNoInteractions(userRepository);
    }

    @Test
    void testDeleteAdmin_RoleMismatch() {
        // Arrange
        String userId = "dosen-123";
        String currentUserId = "admin-456";

        when(currentUserProvider.getCurrentUserId()).thenReturn(currentUserId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(mockUser.getRole()).thenReturn(Role.DOSEN); // User is actually DOSEN, not ADMIN

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.deleteAdmin(userId));

        assertEquals("User role mismatch. Expected: ADMIN, Actual: DOSEN", exception.getReason());
        verify(currentUserProvider).getCurrentUserId();
        verify(userRepository).findById(userId);
        verify(mockUser, atLeastOnce()).getRole(); // Allow multiple calls
    }

    @Test
    void testDeleteDosen_Success() {
        // Arrange
        String userId = "dosen-123";
        String currentUserId = "admin-456";

        userService = new UserServiceImpl(
                adminFactory, dosenFactory, userRepository, adminRepository,
                dosenRepository, mahasiswaRepository, Arrays.asList(updateRoleStrategy),
                Arrays.asList(deleteUserStrategy), currentUserProvider, userMapper
        );

        when(currentUserProvider.getCurrentUserId()).thenReturn(currentUserId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(mockUser.getRole()).thenReturn(Role.DOSEN);
        when(deleteUserStrategy.supports(Role.DOSEN)).thenReturn(true);

        // Act
        userService.deleteDosen(userId);

        // Assert
        verify(currentUserProvider).getCurrentUserId();
        verify(userRepository).findById(userId);
        verify(mockUser, atLeastOnce()).getRole(); // Allow multiple calls
        verify(deleteUserStrategy).supports(Role.DOSEN);
        verify(deleteUserStrategy).deleteUser(userId);
    }

    @Test
    void testDeleteMahasiswa_Success() {
        // Arrange
        String userId = "mahasiswa-123";
        String currentUserId = "admin-456";

        userService = new UserServiceImpl(
                adminFactory, dosenFactory, userRepository, adminRepository,
                dosenRepository, mahasiswaRepository, Arrays.asList(updateRoleStrategy),
                Arrays.asList(deleteUserStrategy), currentUserProvider, userMapper
        );

        when(currentUserProvider.getCurrentUserId()).thenReturn(currentUserId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(mockUser.getRole()).thenReturn(Role.MAHASISWA);
        when(deleteUserStrategy.supports(Role.MAHASISWA)).thenReturn(true);

        // Act
        userService.deleteMahasiswa(userId);

        // Assert
        verify(currentUserProvider).getCurrentUserId();
        verify(userRepository).findById(userId);
        verify(mockUser, atLeastOnce()).getRole(); // Allow multiple calls
        verify(deleteUserStrategy).supports(Role.MAHASISWA);
        verify(deleteUserStrategy).deleteUser(userId);
    }

    @Test
    void testDeleteUser_NoStrategyFound() {
        // Arrange
        String userId = "user-123";
        String currentUserId = "admin-456";

        userService = new UserServiceImpl(
                adminFactory, dosenFactory, userRepository, adminRepository,
                dosenRepository, mahasiswaRepository, Arrays.asList(updateRoleStrategy),
                Arrays.asList(deleteUserStrategy), currentUserProvider, userMapper
        );

        when(currentUserProvider.getCurrentUserId()).thenReturn(currentUserId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(mockUser.getRole()).thenReturn(Role.ADMIN);
        when(deleteUserStrategy.supports(Role.ADMIN)).thenReturn(false);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.deleteAdmin(userId));

        assertEquals("No delete strategy found for role: ADMIN", exception.getReason());
        verify(currentUserProvider).getCurrentUserId();
        verify(userRepository).findById(userId);
        verify(mockUser, atLeastOnce()).getRole(); // Allow multiple calls
        verify(deleteUserStrategy).supports(Role.ADMIN);
        verify(deleteUserStrategy, never()).deleteUser(anyString());
    }

    @Test
    void testDeleteUser_UserNotFound() {
        // Arrange
        String userId = "non-existent-user";
        String currentUserId = "admin-123";

        when(currentUserProvider.getCurrentUserId()).thenReturn(currentUserId);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.deleteAdmin(userId));

        assertEquals("User not found", exception.getReason());
        verify(currentUserProvider).getCurrentUserId();
        verify(userRepository).findById(userId);
    }

    @Test
    void testDeleteUser_MultipleStrategies() {
        String userId = "admin-123";
        String currentUserId = "other-admin-456";

        DeleteUserStrategy strategy1 = mock(DeleteUserStrategy.class);
        DeleteUserStrategy strategy2 = mock(DeleteUserStrategy.class);

        userService = new UserServiceImpl(
                adminFactory, dosenFactory, userRepository, adminRepository,
                dosenRepository, mahasiswaRepository, Arrays.asList(updateRoleStrategy),
                Arrays.asList(strategy1, strategy2), currentUserProvider, userMapper
        );

        when(currentUserProvider.getCurrentUserId()).thenReturn(currentUserId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(mockUser.getRole()).thenReturn(Role.ADMIN);
        when(strategy1.supports(Role.ADMIN)).thenReturn(false);
        when(strategy2.supports(Role.ADMIN)).thenReturn(true);

        userService.deleteAdmin(userId);

        verify(strategy1).supports(Role.ADMIN);
        verify(strategy2).supports(Role.ADMIN);
        verify(strategy2).deleteUser(userId);
        verify(strategy1, never()).deleteUser(anyString());
    }
}