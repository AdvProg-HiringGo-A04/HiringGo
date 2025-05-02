package id.ac.ui.cs.advprog.hiringgo.dashboardhonor.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

public class Log {
    private Long id;
    private String lowonganid;
    private LocalDateTime start;
    private LocalDateTime end;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLowonganid() {
        return lowonganid;
    }

    public void setLowonganid(String lowonganid) {
        this.lowonganid = lowonganid;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public BigDecimal getHours() {
        long minutes = Duration.between(start, end).toMinutes();
        return new BigDecimal(minutes)
                .divide(new BigDecimal("60"), 2, RoundingMode.HALF_UP);
    }
}