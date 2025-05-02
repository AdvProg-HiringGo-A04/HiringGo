package id.ac.ui.cs.advprog.hiringgo.dashboardhonor.repository;

import id.ac.ui.cs.advprog.hiringgo.dashboardhonor.model.Log;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class LogRepositoryTest {

    @Autowired
    private LogRepository logRepository;

    @Test
    void testFindByVacancyIdAndStartBetween() {
        String lowonganId = "csui-1";
        LocalDateTime now = LocalDateTime.now();

        Log logInRange = new Log();
        logInRange.setLowonganid(lowonganId);
        logInRange.setStart(now.minusHours(5));
        logInRange.setEnd(now.minusHours(3));
        logRepository.save(logInRange);

        Log logOutOfRange = new Log();
        logOutOfRange.setLowonganid(lowonganId);
        logOutOfRange.setStart(now.minusMonths(1));
        logOutOfRange.setEnd(now.minusMonths(1).plusHours(1));
        logRepository.save(logOutOfRange);

        LocalDateTime from = now.minusDays(1);
        LocalDateTime to   = now.plusDays(1);

        List<Log> results =
                logRepository.findByLowonganId(lowonganId, from, to);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getId()).isEqualTo(logInRange.getId());
    }
}
