package id.ac.ui.cs.advprog.hiringgo.dashboardhonor.repository;

import id.ac.ui.cs.advprog.hiringgo.dashboardhonor.model.Log;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LogRepositoryImplTest {
    private LogRepositoryImpl repo = new LogRepositoryImpl();

    @Test
    void findByVacancyId() {
        String lowonganId = "csui-1";
        LocalDateTime now = LocalDateTime.now();

        Log in = new Log();
        in.setLowonganId(lowonganId);
        in.setStart(now.minusHours(1));
        in.setEnd(now);
        repo.save(in);

        Log out = new Log();
        out.setLowonganId(lowonganId);
        out.setStart(now.minusMonths(1));
        out.setEnd(now.minusMonths(1).plusHours(1));
        repo.save(out);

        List<Log> found = repo
                .findByLowonganId(lowonganId, now.minusDays(1), now.plusDays(1));

        assertEquals(1, found.size());
        assertEquals(in.getId(), found.get(0).getId());
    }
}
