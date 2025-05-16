package id.ac.ui.cs.advprog.hiringgo.dashboardhonor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.hiringgo.dashboardhonor.model.Log;
import id.ac.ui.cs.advprog.hiringgo.dashboardhonor.service.HonorDashboardService;
import id.ac.ui.cs.advprog.hiringgo.security.JwtUtilImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = HonorDashboardController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class
)

class HonorDashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HonorDashboardService service;

    @MockBean
    private JwtUtilImpl jwtUtil;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void getLogsForMahasiswa_shouldReturnGroupedLogs() throws Exception {
        String mahasiswaId = "12345";
        YearMonth periode = YearMonth.of(2025, 5);
        LocalDateTime start = LocalDateTime.of(2025, 5, 1, 9, 0);
        LocalDateTime end   = LocalDateTime.of(2025, 5, 1, 11, 30);

        Log sampleLog = new Log();
        sampleLog.setId(1L);
        sampleLog.setLowonganId("lowonganA");
        sampleLog.setStart(start);
        sampleLog.setEnd(end);

        Map<String, List<Log>> mockResponse =
                Map.of("lowonganA", List.of(sampleLog));

        given(service.getLogsForMahasiswaByPeriod(mahasiswaId, periode))
                .willReturn(mockResponse);

        given(jwtUtil.extractRole("dummyToken")).willReturn("STUDENT");

        String expectedStart = start.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String expectedEnd   = end  .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        mockMvc.perform(get("/api/students/{studentId}/honor/logs", mahasiswaId)
                        .param("year",  "2025")
                        .param("month", "5")
                        .header("Authorization", "Bearer dummyToken")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data['lowonganA']").isArray())
                .andExpect(jsonPath("$.data['lowonganA'][0].id").value(1))
                .andExpect(jsonPath("$.data['lowonganA'][0].lowonganId").value("lowonganA"))
                .andExpect(jsonPath("$.data['lowonganA'][0].start").value(expectedStart))
                .andExpect(jsonPath("$.data['lowonganA'][0].end").value(expectedEnd));
    }
}
