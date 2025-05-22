package id.ac.ui.cs.advprog.hiringgo.manajemenakun.controller;

import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.*;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.entity.Admin;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.entity.Dosen;
import id.ac.ui.cs.advprog.hiringgo.entity.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.entity.Role;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private User mockUser;
    private Admin mockAdmin;
    private Dosen mockDosen;
    private Mahasiswa mockMahasiswa;
    private AdminDTO mockAdminDTO;
    private DosenDTO mockDosenDTO;
    private RoleUpdateDTO mockRoleUpdateDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();

        // Setup mock objects
        mockUser = new User();
        mockUser.setId("user-123");
        mockUser.setEmail("test@example.com");
        mockUser.setRole(Role.ADMIN);

        mockAdmin = new Admin();
        mockAdmin.setId("admin-123");

        mockDosen = new Dosen();
        mockDosen.setId("dosen-123");
        mockDosen.setNamaLengkap("Dr. John Doe");
        mockDosen.setNIP("197001012000011001");

        mockMahasiswa = new Mahasiswa();
        mockMahasiswa.setId("mahasiswa-123");
        mockMahasiswa.setNamaLengkap("Jane Doe");
        mockMahasiswa.setNPM("2106123456");

        mockAdminDTO = new AdminDTO();
        mockAdminDTO.setEmail("admin@example.com");
        mockAdminDTO.setPassword("password123");

        mockDosenDTO = new DosenDTO();
        mockDosenDTO.setEmail("dosen@example.com");
        mockDosenDTO.setPassword("password123");
        mockDosenDTO.setNamaLengkap("Dr. John Doe");
        mockDosenDTO.setNip("197001012000011001");

        mockRoleUpdateDTO = new RoleUpdateDTO();
        mockRoleUpdateDTO.setRole(Role.DOSEN);
    }

    @Test
    void testCreateAdmin_Success() throws Exception {
        when(userService.createAdmin(any(AdminDTO.class))).thenReturn(mockUser);

        mockMvc.perform(post("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockAdminDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("user-123"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.role").value("ADMIN"));

        verify(userService).createAdmin(any(AdminDTO.class));
    }

    @Test
    void testCreateDosen_Success() throws Exception {
        when(userService.createDosen(any(DosenDTO.class))).thenReturn(mockUser);

        mockMvc.perform(post("/dosen")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockDosenDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("user-123"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.role").value("ADMIN"));

        verify(userService).createDosen(any(DosenDTO.class));
    }

    @Test
    void testCreateAdmin_InvalidRequestBody() throws Exception {
        mockMvc.perform(post("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("invalid json"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
    }

    @Test
    void testGetAllAdmins_Success() throws Exception {
        List<Admin> adminList = Arrays.asList(mockAdmin);
        when(userService.getAllAdmins()).thenReturn(adminList);
        when(userService.getUserById("admin-123")).thenReturn(mockUser);

        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].email").value("test@example.com"))
                .andExpect(jsonPath("$[0].role").value("ADMIN"));

        verify(userService).getAllAdmins();
        verify(userService).getUserById("admin-123");
    }

    @Test
    void testGetAllDosens_Success() throws Exception {
        List<Dosen> dosenList = Arrays.asList(mockDosen);
        when(userService.getAllDosens()).thenReturn(dosenList);
        when(userService.getUserById("dosen-123")).thenReturn(mockUser);

        mockMvc.perform(get("/dosen"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].email").value("test@example.com"))
                .andExpect(jsonPath("$[0].role").value("ADMIN"))
                .andExpect(jsonPath("$[0].namaLengkap").value("Dr. John Doe"))
                .andExpect(jsonPath("$[0].nip").value("197001012000011001"));

        verify(userService).getAllDosens();
        verify(userService).getUserById("dosen-123");
    }

    @Test
    void testGetAllMahasiswas_Success() throws Exception {
        List<Mahasiswa> mahasiswaList = Arrays.asList(mockMahasiswa);
        when(userService.getAllMahasiswas()).thenReturn(mahasiswaList);
        when(userService.getUserById("mahasiswa-123")).thenReturn(mockUser);

        mockMvc.perform(get("/mahasiswa"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].email").value("test@example.com"))
                .andExpect(jsonPath("$[0].role").value("ADMIN"))
                .andExpect(jsonPath("$[0].namaLengkap").value("Jane Doe"))
                .andExpect(jsonPath("$[0].nim").value("2106123456"));

        verify(userService).getAllMahasiswas();
        verify(userService).getUserById("mahasiswa-123");
    }

    @Test
    void testGetAllAdmins_EmptyList() throws Exception {
        when(userService.getAllAdmins()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(userService).getAllAdmins();
        verify(userService, never()).getUserById(anyString());
    }

    @Test
    void testGetAdminById_Success() throws Exception {
        String adminId = "admin-123";
        when(userService.getAdminById(adminId)).thenReturn(mockAdmin);
        when(userService.getUserById(adminId)).thenReturn(mockUser);

        mockMvc.perform(get("/admin/{id}", adminId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.role").value("ADMIN"));

        verify(userService).getAdminById(adminId);
        verify(userService).getUserById(adminId);
    }

    @Test
    void testGetDosenById_Success() throws Exception {
        String dosenId = "dosen-123";
        when(userService.getDosenById(dosenId)).thenReturn(mockDosen);
        when(userService.getUserById(dosenId)).thenReturn(mockUser);

        mockMvc.perform(get("/dosen/{id}", dosenId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.role").value("ADMIN"))
                .andExpect(jsonPath("$.namaLengkap").value("Dr. John Doe"))
                .andExpect(jsonPath("$.nip").value("197001012000011001"));

        verify(userService).getDosenById(dosenId);
        verify(userService).getUserById(dosenId);
    }

    @Test
    void testGetMahasiswaById_Success() throws Exception {
        String mahasiswaId = "mahasiswa-123";
        when(userService.getMahasiswaById(mahasiswaId)).thenReturn(mockMahasiswa);
        when(userService.getUserById(mahasiswaId)).thenReturn(mockUser);

        mockMvc.perform(get("/mahasiswa/{id}", mahasiswaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.role").value("ADMIN"))
                .andExpect(jsonPath("$.namaLengkap").value("Jane Doe"))
                .andExpect(jsonPath("$.nim").value("2106123456"));

        verify(userService).getMahasiswaById(mahasiswaId);
        verify(userService).getUserById(mahasiswaId);
    }

    @Test
    void testUpdateAdminRole_Success() throws Exception {
        String adminId = "admin-123";
        when(userService.updateUserRole(eq(adminId), any(RoleUpdateDTO.class))).thenReturn(mockUser);

        mockMvc.perform(patch("/admin/{id}", adminId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRoleUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("user-123"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.role").value("ADMIN"));

        verify(userService).updateUserRole(eq(adminId), any(RoleUpdateDTO.class));
    }

    @Test
    void testUpdateDosenRole_Success() throws Exception {
        String dosenId = "dosen-123";
        when(userService.updateUserRole(eq(dosenId), any(RoleUpdateDTO.class))).thenReturn(mockUser);

        mockMvc.perform(patch("/dosen/{id}", dosenId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRoleUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("user-123"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.role").value("ADMIN"));

        verify(userService).updateUserRole(eq(dosenId), any(RoleUpdateDTO.class));
    }

    @Test
    void testUpdateMahasiswaRole_Success() throws Exception {
        String mahasiswaId = "mahasiswa-123";
        when(userService.updateUserRole(eq(mahasiswaId), any(RoleUpdateDTO.class))).thenReturn(mockUser);

        mockMvc.perform(patch("/mahasiswa/{id}", mahasiswaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockRoleUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("user-123"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.role").value("ADMIN"));

        verify(userService).updateUserRole(eq(mahasiswaId), any(RoleUpdateDTO.class));
    }

    @Test
    void testUpdateRole_InvalidRequestBody() throws Exception {
        mockMvc.perform(patch("/admin/{id}", "admin-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("invalid json"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
    }

    @Test
    void testDeleteAdmin_Success() throws Exception {
        String adminId = "admin-123";
        doNothing().when(userService).deleteAdmin(adminId);

        mockMvc.perform(delete("/admin/{id}", adminId))
                .andExpect(status().isNoContent());

        verify(userService).deleteAdmin(adminId);
    }

    @Test
    void testDeleteDosen_Success() throws Exception {
        String dosenId = "dosen-123";
        doNothing().when(userService).deleteDosen(dosenId);

        mockMvc.perform(delete("/dosen/{id}", dosenId))
                .andExpect(status().isNoContent());

        verify(userService).deleteDosen(dosenId);
    }

    @Test
    void testDeleteMahasiswa_Success() throws Exception {
        String mahasiswaId = "mahasiswa-123";
        doNothing().when(userService).deleteMahasiswa(mahasiswaId);

        mockMvc.perform(delete("/mahasiswa/{id}", mahasiswaId))
                .andExpect(status().isNoContent());

        verify(userService).deleteMahasiswa(mahasiswaId);
    }

    @Test
    void testCreateAdmin_WithValidInput() throws Exception {
        AdminDTO validAdminDTO = new AdminDTO();
        validAdminDTO.setEmail("valid@example.com");
        validAdminDTO.setPassword("validPassword123");

        when(userService.createAdmin(any(AdminDTO.class))).thenReturn(mockUser);

        mockMvc.perform(post("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validAdminDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("user-123"))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(userService).createAdmin(any(AdminDTO.class));
    }

    @Test
    void testUpdateRole_WithValidInput() throws Exception {
        String adminId = "admin-123";
        RoleUpdateDTO validRoleUpdate = new RoleUpdateDTO();
        validRoleUpdate.setRole(Role.DOSEN);
        validRoleUpdate.setNamaLengkap("Dr. John Smith");
        validRoleUpdate.setNip("197001012000011001");

        when(userService.updateUserRole(eq(adminId), any(RoleUpdateDTO.class))).thenReturn(mockUser);

        mockMvc.perform(patch("/admin/{id}", adminId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRoleUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("user-123"));

        verify(userService).updateUserRole(eq(adminId), any(RoleUpdateDTO.class));
    }

    @Test
    void testGetById_WithValidPathVariable() throws Exception {
        String adminId = "valid-admin-123";
        when(userService.getAdminById(adminId)).thenReturn(mockAdmin);
        when(userService.getUserById(adminId)).thenReturn(mockUser);

        mockMvc.perform(get("/admin/{id}", adminId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.role").value("ADMIN"));

        verify(userService).getAdminById(adminId);
        verify(userService).getUserById(adminId);
    }

    @Test
    void testCompleteWorkflow_CreateAndRetrieveAdmin() throws Exception {
        when(userService.createAdmin(any(AdminDTO.class))).thenReturn(mockUser);
        when(userService.getAdminById("user-123")).thenReturn(mockAdmin);
        when(userService.getUserById("user-123")).thenReturn(mockUser);

        mockMvc.perform(post("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockAdminDTO)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/admin/{id}", "user-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(userService).createAdmin(any(AdminDTO.class));
        verify(userService).getAdminById("user-123");
        verify(userService).getUserById("user-123");
    }

    @Test
    void testMultipleUsersInList() throws Exception {
        Admin admin1 = new Admin();
        admin1.setId("admin-1");
        Admin admin2 = new Admin();
        admin2.setId("admin-2");

        User user1 = new User();
        user1.setId("admin-1");
        user1.setEmail("admin1@example.com");
        user1.setRole(Role.ADMIN);

        User user2 = new User();
        user2.setId("admin-2");
        user2.setEmail("admin2@example.com");
        user2.setRole(Role.ADMIN);

        when(userService.getAllAdmins()).thenReturn(Arrays.asList(admin1, admin2));
        when(userService.getUserById("admin-1")).thenReturn(user1);
        when(userService.getUserById("admin-2")).thenReturn(user2);

        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].email").value("admin1@example.com"))
                .andExpect(jsonPath("$[1].email").value("admin2@example.com"));

        verify(userService).getAllAdmins();
        verify(userService).getUserById("admin-1");
        verify(userService).getUserById("admin-2");
    }
}