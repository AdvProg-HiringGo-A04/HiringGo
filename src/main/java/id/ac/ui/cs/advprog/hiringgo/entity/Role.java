package id.ac.ui.cs.advprog.hiringgo.entity;

public enum Role {
    ADMIN, DOSEN, MAHASISWA;

    @Override
    public String toString() {
        return this.name();
    }
}