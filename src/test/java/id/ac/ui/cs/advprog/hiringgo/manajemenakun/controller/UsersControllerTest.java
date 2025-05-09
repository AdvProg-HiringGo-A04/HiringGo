package id.ac.ui.cs.advprog.hiringgo.manajemenakun.controller;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.AccountData;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Admin;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Dosen;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Users;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Role;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.service.UserService;
import id.ac.ui.cs.advprog.hiringgo.security.JwtUtilImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc(addFilters = false)
class UsersControllerTest {

    @MockBean
    private JwtUtilImpl jwtUtil;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService service;

    @Test
    void testCreateAccount() throws Exception {
        // stub JWT to allow ADMIN role
        Mockito.when(jwtUtil.extractRole("token")).thenReturn("ADMIN");
        AccountData data = new AccountData("NIP1", "Dr Test", "test@example.com", "pwd");
        Dosen dummy = new Dosen(data);
        String id = dummy.getId();

        Mockito.when(service.createAccount(eq(Role.DOSEN), any(AccountData.class)))
                .thenReturn(dummy);

        mockMvc.perform(post("/accounts?role=DOSEN")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{"
                                + "\"identifier\":\"NIP1\","
                                + "\"fullName\":\"Dr Test\","
                                + "\"email\":\"test@example.com\","
                                + "\"password\":\"pwd\""
                                + "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(id))
                .andExpect(jsonPath("$.data.role").value("DOSEN"))
                .andExpect(jsonPath("$.data.email").value("test@example.com"));
    }

    @Test
    void testGetAllAccount() throws Exception {
        Users a1 = new Dosen(new AccountData("NIP10", "Dr. Alice", "alice@example.com", "pwd"));
        Users a2 = new Admin(new AccountData(null, null, "bob@example.com", "pwd"));

        Mockito.when(service.findAll())
                .thenReturn(Arrays.asList(a1, a2));

        mockMvc.perform(get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    void testUpdateUser() throws Exception {
        doNothing().when(service)
                .updateRole(
                        anyString(),
                        anyString(),
                        eq(Role.ADMIN),
                        any(AccountData.class)
                );

        mockMvc.perform(put("/accounts/123/role?requesterId=adminId&newRole=ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{"
                                + "\"identifier\":null,"
                                + "\"fullName\":null,"
                                + "\"email\":null,"
                                + "\"password\":null"
                                + "}"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteUser() throws Exception {
        doNothing().when(service).deleteAccount(anyString(), anyString());
        mockMvc.perform(delete("/accounts/123?requesterId=adminId"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testCreateAccountForbiddenForDosen() throws Exception {
        Mockito.when(jwtUtil.extractRole("token")).thenReturn("DOSEN");
        mockMvc.perform(post("/accounts?role=ADMIN")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{"
                                + "\"identifier\":null,"
                                + "\"fullName\":null,"
                                + "\"email\":\"dosen@example.com\","
                                + "\"password\":\"pwd\""
                                + "}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testCreateAccountForbiddenForMahasiswa() throws Exception {
        Mockito.when(jwtUtil.extractRole("token")).thenReturn("MAHASISWA");
        mockMvc.perform(post("/accounts?role=ADMIN")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{"
                                + "\"identifier\":null,"
                                + "\"fullName\":null,"
                                + "\"email\":\"mhs@example.com\","
                                + "\"password\":\"pwd\""
                                + "}"))
                .andExpect(status().isForbidden());
    }
}
