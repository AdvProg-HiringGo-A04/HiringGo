package id.ac.ui.cs.advprog.hiringgo.manajemenakun.controller;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.*;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.doNothing;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService service;

    @Test
    void testCreateAccount() throws Exception {
        AccountData data = new AccountData("NIP1","Dr Test","test@example.com","pwd");
        Dosen dummy = new Dosen(data);
        String id = dummy.getId();

        Mockito.when(service.createAccount(eq(Role.DOSEN), any(AccountData.class)))
                .thenReturn(dummy);

        mockMvc.perform(post("/accounts?role=DOSEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{"
                                + "\"nip\":\"NIP1\","
                                + "\"fullName\":\"Dr Test\","
                                + "\"email\":\"test@example.com\","
                                + "\"password\":\"pwd\""
                                + "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.role").value("DOSEN"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void testGetAllAccount() throws Exception {
        Account a1 = new Dosen(
                new AccountData("NIP10", "Dr. Alice", "alice@example.com", "pwd"));
        Account a2 = new Admin(
                new AccountData(null, null, "bob@example.com", "pwd"));

        Mockito.when(service.findAll())
                .thenReturn(Arrays.asList(a1, a2));

        mockMvc.perform(get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testUpdateUser() throws Exception {
        doNothing().when(service).updateRole(anyString(), anyString(), eq(Role.ADMIN));
        mockMvc.perform(put("/accounts/123/role?requesterId=adminId&newRole=ADMIN"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteUser() throws Exception {
        doNothing().when(service).deleteAccount(anyString(), anyString());
        mockMvc.perform(delete("/accounts/123?requesterId=adminId"))
                .andExpect(status().isNoContent());
    }
}
