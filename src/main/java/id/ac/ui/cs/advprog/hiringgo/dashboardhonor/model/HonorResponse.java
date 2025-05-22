package id.ac.ui.cs.advprog.hiringgo.dashboardhonor.model;

import id.ac.ui.cs.advprog.hiringgo.entity.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.entity.MataKuliah;

import java.util.Date;

public class HonorResponse {

    private Date tanggal;

    private Mahasiswa mahasiswa;

    private MataKuliah mataKuliah;

    private Double totalJam;

    private Double honorPerJam;

    private Double totalPembayaran;

    private String status;
}
