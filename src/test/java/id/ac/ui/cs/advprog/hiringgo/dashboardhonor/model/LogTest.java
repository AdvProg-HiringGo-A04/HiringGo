package id.ac.ui.cs.advprog.hiringgo.honor.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

public class LogTest {

    @Test
    public void shouldCreateLogWithCorrectValues() {
        String id = "1";
        String jobId = "job-123";
        String mahasiswaId = "mhs-456";
        double hours = 2.5;
        LocalDateTime timestamp = LocalDateTime.now();
        boolean approved = true;

        Log log = new Log(id, jobId, mahasiswaId, hours, timestamp, approved);

        assertEquals(id, log.getId());
        assertEquals(jobId, log.getJobId());
        assertEquals(mahasiswaId, log.getMahasiswaId());
        assertEquals(hours, log.getHours());
        assertEquals(timestamp, log.getTimestamp());
        assertEquals(approved, log.isApproved());
    }
}