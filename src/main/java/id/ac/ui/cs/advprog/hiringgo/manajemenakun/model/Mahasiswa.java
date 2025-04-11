package id.ac.ui.cs.advprog.hiringgo.manajemenakun.model;

public class Mahasiswa extends User {
    private String nim;
    private String namaLengkap;

    public Mahasiswa() {
        super();
        setRole("MAHASISWA");
    }

    public Mahasiswa(String id, String nim, String namaLengkap, String email, String password) {
        super(id, email, password, "MAHASISWA");
        this.nim = nim;
        this.namaLengkap = namaLengkap;
    }

    public String getNim() { return nim; }
    public void setNim(String nim) { this.nim = nim; }

    @Override
    public String getNamaLengkap() { return namaLengkap; }
    public void setNamaLengkap(String namaLengkap) { this.namaLengkap = namaLengkap; }
}