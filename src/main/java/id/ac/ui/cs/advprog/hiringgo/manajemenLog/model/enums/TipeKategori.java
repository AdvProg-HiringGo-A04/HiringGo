package id.ac.ui.cs.advprog.hiringgo.manajemenLog.model.enums;

public enum TipeKategori {
    ASISTENSI("Asistensi"),
    MENGOREKSI("Mengoreksi"),
    MENGAWAS("Mengawas"),
    OTHER("Lain-lain");

    private final String displayName;

    TipeKategori(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}