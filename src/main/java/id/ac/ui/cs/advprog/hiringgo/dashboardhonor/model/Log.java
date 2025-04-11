package id.ac.ui.cs.advprog.hiringgo.dashboardhonor.model;

import java.time.LocalDateTime;

public class Log {
    private String id;
    private String jobId;
    private String mahasiswaId;
    private double hours;
    private LocalDateTime timestamp;
    private boolean approved;

    public Log(String id, String jobId, String mahasiswaId, double hours,
               LocalDateTime timestamp, boolean approved) {
        this.id = id;
        this.jobId = jobId;
        this.mahasiswaId = mahasiswaId;
        this.hours = hours;
        this.timestamp = timestamp;
        this.approved = approved;
    }

    public String getId() { return id; }
    public String getJobId() { return jobId; }
    public String getMahasiswaId() { return mahasiswaId; }
    public double getHours() { return hours; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public boolean isApproved() { return approved; }
}