package id.ac.ui.cs.advprog.hiringgo.dashboardhonor.controller;

import id.ac.ui.cs.advprog.hiringgo.dashboardhonor.model.Log;
import id.ac.ui.cs.advprog.hiringgo.dashboardhonor.service.HonorDashboardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HonorDashboardController.class)
class HonorDashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HonorDashboardService service;

    @Test
    @WithMockUser(username = "anyUser")
    void getHonor_returnsMap() throws Exception {
        YearMonth periode = YearMonth.of(2025, 5);
        Map<String, BigDecimal> mockResult = Map.of("vac-1", new BigDecimal("27500"));
        given(service.calculate(periode)).willReturn(mockResult);

        mockMvc.perform(get("/api/honor")
                        .param("year", "2025")
                        .param("month", "5")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$['vac-1']").value(27500));
    }

    @Test
    @WithMockUser(username = "studentX")
    void getLogs_returnsLogs() throws Exception {
        YearMonth periode = YearMonth.of(2025, 5);
        LocalDateTime start = LocalDateTime.of(2025, 5, 1, 9, 0);
        LocalDateTime end   = LocalDateTime.of(2025, 5, 1, 11, 0);

        Log log = new Log();
        log.setId(1L);
        log.setLowonganId("vac-1");
        log.setStart(start);
        log.setEnd(end);

        Map<String, List<Log>> mockLogs = Map.of("vac-1", List.of(log));
        given(service.getLogsForMahasiswaByPeriod("studentX", periode))
                .willReturn(mockLogs);

        mockMvc.perform(get("/api/honor/logs")
                        .param("year", "2025")
                        .param("month", "5")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$['vac-1']").isArray())
                .andExpect(jsonPath("$['vac-1'][0].id").value(1))
                .andExpect(jsonPath("$['vac-1'][0].vacancyId").value("vac-1"))
                .andExpect(jsonPath("$['vac-1'][0].start").exists())
                .andExpect(jsonPath("$['vac-1'][0].end").exists());
    }
}
