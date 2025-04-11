package id.ac.ui.cs.advprog.hiringgo.daftarLowongan.command;

public class DaftarLowonganCommand {
    private final int sks;
    private final double ipk;
    private final Long lowonganId;
    private final Long mahasiswaId;

    public DaftarLowonganCommand(int sks, double ipk, Long lowonganId, Long mahasiswaId) {
        this.sks = sks;
        this.ipk = ipk;
        this.lowonganId = lowonganId;
        this.mahasiswaId = mahasiswaId;
    }

    public int getSks() { return sks; }
    public double getIpk() { return ipk; }
    public Long getLowonganId() { return lowonganId; }
    public Long getMahasiswaId() { return mahasiswaId; }
}
