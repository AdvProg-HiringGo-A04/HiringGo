package id.ac.ui.cs.advprog.hiringgo.manajemenakun.service.updaterole;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.*;
import org.springframework.stereotype.Component;

@Component
public class MahasiswaToDosenStrategy implements UpdateRoleStrategy {
    @Override public boolean supports(Role from, Role to) {
        return from == Role.MAHASISWA && to == Role.DOSEN;
    }
    @Override
    public Users changeRole(Users old, AccountData data) {
        if (data.getIdentifier() == null) {
            throw new IllegalArgumentException(
                    "Identifier (NIP) must be provided when changing to DOSEN");
        }
        Mahasiswa mhs = (Mahasiswa) old;
        Dosen dosen = new Dosen(
                new AccountData(
                        data.getIdentifier(),
                        data.getFullName(),
                        mhs.getEmail(),
                        mhs.getPassword()
                )
        );
        dosen.setId(mhs.getId());
        return dosen;
    }
}