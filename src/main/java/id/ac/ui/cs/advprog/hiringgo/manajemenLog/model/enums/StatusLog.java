package id.ac.ui.cs.advprog.hiringgo.manajemenLog.model.enums;

public enum StatusLog {
    DIPROSES("Diproses"),
    DITERIMA("Diterima"),
    DITOLAK("Ditolak");
    
    private final String displayName;
    
    StatusLog(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
