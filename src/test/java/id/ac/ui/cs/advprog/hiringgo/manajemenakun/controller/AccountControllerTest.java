package id.ac.ui.cs.advprog.hiringgo.manajemenakun.controller;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Account;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.AccountData;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Role;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.doNothing;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService service;

    @Test
    void testCreateAccount() throws Exception {
        String id = UUID.randomUUID().toString();
        AccountData data = new AccountData("NIP1","Dr Test","test@example.com","pwd");
        Account dummy = Mockito.mock(Account.class);
        Mockito.when(dummy.getId()).thenReturn(id);
        Mockito.when(service.createAccount(eq(Role.DOSEN), any(AccountData.class))).thenReturn(dummy);

        mockMvc.perform(post("/accounts?role=DOSEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nip\":\"NIP1\",\"fullName\":\"Dr Test\",\"email\":\"test@example.com\",\"password\":\"pwd\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void testGetAllAccount() throws Exception {
        Account a1 = Mockito.mock(Account.class);
        Account a2 = Mockito.mock(Account.class);
        Mockito.when(service.findAll()).thenReturn(Arrays.asList(a1,a2));

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
