package id.ac.ui.cs.advprog.hiringgo.dashboardhonor.model;

import java.math.BigDecimal;
import java.time.YearMonth;

public class DashboardHonorDTO {
    private String vacancyName;
    private YearMonth period;
    private BigDecimal totalHours;
    private BigDecimal ratePerHour;
    private BigDecimal totalHonor;

    public DashboardHonorDTO(String vacancyName, YearMonth period, BigDecimal totalHours, BigDecimal ratePerHour, BigDecimal totalHonor) {
        this.vacancyName  = vacancyName;
        this.period       = period;
        this.totalHours   = totalHours;
        this.ratePerHour  = ratePerHour;
        this.totalHonor   = totalHonor;
    }

    public String getVacancyName() { return vacancyName; }
    public YearMonth getPeriod()   { return period; }
    public BigDecimal getTotalHours()  { return totalHours; }
    public BigDecimal getRatePerHour() { return ratePerHour; }
    public BigDecimal getTotalHonor()  { return totalHonor; }
}
