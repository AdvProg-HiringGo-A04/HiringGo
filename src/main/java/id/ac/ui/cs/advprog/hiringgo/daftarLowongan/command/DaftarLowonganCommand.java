package id.ac.ui.cs.advprog.hiringgo.daftarLowongan.command;

import java.util.UUID;

public class DaftarLowonganCommand {
    private final int sks;
    private final double ipk;
    private final String lowonganId;
    private final String mahasiswaId;

    public DaftarLowonganCommand(int sks, double ipk, String lowonganId, String mahasiswaId) {
        this.sks = sks;
        this.ipk = ipk;
        this.lowonganId = lowonganId;
        this.mahasiswaId = mahasiswaId;
    }

    public int getSks() { return sks; }
    public double getIpk() { return ipk; }
    public String getLowonganId() { return lowonganId; }
    public String getMahasiswaId() { return mahasiswaId; }
}
