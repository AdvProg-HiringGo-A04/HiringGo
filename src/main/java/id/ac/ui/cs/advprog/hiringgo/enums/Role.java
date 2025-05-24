package id.ac.ui.cs.advprog.hiringgo.enums;

public enum Role {
    ADMIN, DOSEN, MAHASISWA;

    @Override
    public String toString() {
        return this.name();
    }
}