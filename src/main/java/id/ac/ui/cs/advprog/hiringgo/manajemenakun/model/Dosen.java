package id.ac.ui.cs.advprog.hiringgo.manajemenakun.model;

public class Dosen extends User {
    private String nip;
    private String namaLengkap;

    public Dosen() {
        super();
        setRole("DOSEN");
    }

    public Dosen(String id, String nip, String namaLengkap, String email, String password) {
        super(id, email, password, "DOSEN");
        this.nip = nip;
        this.namaLengkap = namaLengkap;
    }

    public String getNip() { return nip; }
    public void setNip(String nip) { this.nip = nip; }

    @Override
    public String getNamaLengkap() { return namaLengkap; }
    public void setNamaLengkap(String namaLengkap) { this.namaLengkap = namaLengkap; }
}