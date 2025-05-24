package id.ac.ui.cs.advprog.hiringgo.dashboardhonor.controller;

import id.ac.ui.cs.advprog.hiringgo.dashboardhonor.service.HonorService;
import id.ac.ui.cs.advprog.hiringgo.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HonorController.class)
@AutoConfigureMockMvc(addFilters = false)
class HonorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HonorService honorService;

    @MockBean
    private JwtUtil jwtUtil;

    private final String MAHASISWA_ROLE = "MAHASISWA";
    private final String VALID_JWT = "validJwtToken";
    private final String BEARER = "Bearer " + VALID_JWT;
    private final String USER_ID = "12345";

    @Test
    void whenGetHonorsSuccess_thenReturns200() throws Exception {
        int year = 2025, month = 5;

        when(jwtUtil.extractRole(VALID_JWT)).thenReturn(MAHASISWA_ROLE);
        when(jwtUtil.extractId(VALID_JWT)).thenReturn(USER_ID);
        when(honorService.getHonorsByMahasiswaAndPeriod(USER_ID, year, month))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/mahasiswa/{id}/honors", USER_ID)
                        .param("year", String.valueOf(year))
                        .param("month", String.valueOf(month))
                        .header("Authorization", BEARER)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isEmpty());

        verify(honorService).getHonorsByMahasiswaAndPeriod(USER_ID, year, month);
    }

    @Test
    void whenMonthInvalid_thenReturns400() throws Exception {
        mockMvc.perform(get("/mahasiswa/{id}/honors", USER_ID)
                        .param("year", "2025")
                        .param("month", "13")
                        .header("Authorization", BEARER))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(jwtUtil, honorService);
    }

    @Test
    void whenNoToken_thenReturns401() throws Exception {
        mockMvc.perform(get("/mahasiswa/{id}/honors", USER_ID)
                        .param("year", "2025")
                        .param("month", "5"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(jwtUtil, honorService);
    }

    @Test
    void whenRoleMismatch_thenReturns401() throws Exception {
        when(jwtUtil.extractRole(VALID_JWT)).thenReturn("ADMIN");
        when(jwtUtil.extractId(VALID_JWT)).thenReturn("ignoredId");

        mockMvc.perform(get("/mahasiswa/{id}/honors", USER_ID)
                        .param("year", "2025")
                        .param("month", "5")
                        .header("Authorization", BEARER))
                .andExpect(status().isUnauthorized());

        verify(jwtUtil).extractRole(VALID_JWT);
        verify(jwtUtil).extractId(VALID_JWT);
        verifyNoInteractions(honorService);
    }

    @Test
    void whenIdMismatch_thenReturns403() throws Exception {
        when(jwtUtil.extractRole(VALID_JWT)).thenReturn(MAHASISWA_ROLE);
        when(jwtUtil.extractId(VALID_JWT)).thenReturn("differentId");

        mockMvc.perform(get("/mahasiswa/{id}/honors", USER_ID)
                        .param("year", "2025")
                        .param("month", "5")
                        .header("Authorization", BEARER))
                .andExpect(status().isForbidden());

        verify(jwtUtil).extractRole(VALID_JWT);
        verify(jwtUtil).extractId(VALID_JWT);
        verifyNoInteractions(honorService);
    }

    @Test
    void whenServiceThrows_thenReturns500() throws Exception {
        when(jwtUtil.extractRole(VALID_JWT)).thenReturn(MAHASISWA_ROLE);
        when(jwtUtil.extractId(VALID_JWT)).thenReturn(USER_ID);
        doThrow(new RuntimeException("DB down")).when(honorService)
                .getHonorsByMahasiswaAndPeriod(USER_ID, 2025, 5);

        mockMvc.perform(get("/mahasiswa/{id}/honors", USER_ID)
                        .param("year", "2025")
                        .param("month", "5")
                        .header("Authorization", BEARER))
                .andExpect(status().isInternalServerError());

        verify(honorService).getHonorsByMahasiswaAndPeriod(USER_ID, 2025, 5);
    }
}
