package id.ac.ui.cs.advprog.hiringgo.daftarLowongan.service;

import id.ac.ui.cs.advprog.hiringgo.daftarLowongan.command.DaftarLowonganCommand;
import id.ac.ui.cs.advprog.hiringgo.daftarLowongan.exception.InvalidDataException;

public class DaftarLowonganService {
    public void execute(DaftarLowonganCommand command) {
        if (command.getIpk() < 0 || command.getIpk() > 4) {
            throw new InvalidDataException("IPK harus antara 0 dan 4");
        }

        if (command.getSks() < 0) {
            throw new InvalidDataException("SKS tidak boleh negatif");
        }
        // cek lowongan, cek udah daftar, simpan ke db
    }
}
