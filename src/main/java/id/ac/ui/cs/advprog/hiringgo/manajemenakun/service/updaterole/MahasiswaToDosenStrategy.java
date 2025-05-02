package id.ac.ui.cs.advprog.hiringgo.manajemenakun.service.updaterole;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.*;
import org.springframework.stereotype.Component;

@Component
public class MahasiswaToDosenStrategy implements UpdateRoleStrategy {
    @Override public boolean supports(Role from, Role to) {
        return from == Role.MAHASISWA && to == Role.DOSEN;
    }
    @Override public Users changeRole(Users old, AccountData data) {
        if (data.getIdentifier() == null) {
            throw new IllegalArgumentException("Dosen requires NIM");
        }

        Dosen dosen = new Dosen(data);
        dosen.setId(old.getId());
        dosen.setEmail(old.getEmail());
        dosen.setPassword(old.getPassword());
        dosen.setFullName(old.getFullName());
        dosen.setIdentifier(data.getIdentifier());
        return dosen;
    }
}