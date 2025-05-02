package id.ac.ui.cs.advprog.hiringgo.dashboardhonor.repository;

import id.ac.ui.cs.advprog.hiringgo.dashboardhonor.model.Log;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class LogRepositoryImpl implements LogRepository {
    private final List<Log> store = new ArrayList<>();
    private long seq = 1;

    @Override
    public Log save(Log log) {
        if (log.getId() == null) log.setId(seq++);
        store.add(log);
        return log;
    }

    @Override
    public List<Log> findByLowonganId(
            String vacancyId, LocalDateTime from, LocalDateTime to) {
        return store.stream()
                .filter(l ->
                        vacancyId.equals(l.getLowonganId())
                                && !l.getStart().isBefore(from)
                                && !l.getStart().isAfter(to))
                .collect(Collectors.toList());
    }
}
