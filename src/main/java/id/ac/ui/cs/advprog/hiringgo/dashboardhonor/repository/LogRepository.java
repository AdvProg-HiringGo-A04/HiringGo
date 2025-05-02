package id.ac.ui.cs.advprog.hiringgo.dashboardhonor.repository;

import id.ac.ui.cs.advprog.hiringgo.dashboardhonor.model.Log;

import java.time.LocalDateTime;
import java.util.List;

public interface LogRepository {
    Log save(Log log);
    List<Log> findByLowonganId(
            String lowonganId, LocalDateTime from, LocalDateTime to);
}
