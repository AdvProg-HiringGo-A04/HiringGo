package id.ac.ui.cs.advprog.hiringgo.dashboardhonor.service;

import id.ac.ui.cs.advprog.hiringgo.dashboardhonor.model.Log;
import id.ac.ui.cs.advprog.hiringgo.dashboardhonor.model.Lowongan;
import id.ac.ui.cs.advprog.hiringgo.dashboardhonor.repository.LowonganRepositoryImpl;
import id.ac.ui.cs.advprog.hiringgo.dashboardhonor.repository.LogRepositoryImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HonorDashboardServiceTest {

    @Mock
    LogRepositoryImpl logRepo;
    @Mock
    LowonganRepositoryImpl lowonganRepo;
    @InjectMocks
    HonorDashboardService service;

    @Test
    void testCalculateHonorForMahasiswa() {
        YearMonth period = YearMonth.of(2025, 5);
        LocalDateTime from = period.atDay(1).atStartOfDay();
        LocalDateTime to   = period.atEndOfMonth().atTime(23,59,59);

        Lowongan vacA = new Lowongan();
        vacA.setId("A"); vacA.setMahasiswaId("stu-1");

        Lowongan vacB = new Lowongan();
        vacB.setId("B"); vacB.setMahasiswaId("stu-1");
        when(lowonganRepo.findByMahasiswaId("stu-1")).thenReturn(List.of(vacA, vacB));

        Log logA = new Log();
        logA.setLowonganId("A"); logA.setStart(from.plusHours(9)); logA.setEnd(from.plusHours(10));

        Log logB = new Log();
        logB.setLowonganId("B"); logB.setStart(from.plusHours(14)); logB.setEnd(from.plusHours(16));

        when(logRepo.findByLowonganId("A", from, to)).thenReturn(List.of(logA));
        when(logRepo.findByLowonganId("B", from, to)).thenReturn(List.of(logB));

        Map<String, List<Log>> logs = service.getLogsForMahasiswaByPeriod("stu-1", period);
        assertEquals(1, logs.get("A").size());
        assertEquals(1, logs.get("B").size());
    }
}