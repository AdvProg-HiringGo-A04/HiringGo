package id.ac.ui.cs.advprog.hiringgo.manajemenakun.service.updaterole;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.*;
import org.springframework.stereotype.Component;

@Component
public class AdminToMahasiswaStrategy implements UpdateRoleStrategy {
    @Override
    public boolean supports(Role from, Role to) {
        return from == Role.ADMIN && to == Role.MAHASISWA;
    }
    @Override
    public Users changeRole(Users old, AccountData data) {
        if (data.getIdentifier() == null || data.getFullName() == null) {
            throw new IllegalArgumentException("Mahasiswa requires NIM and full name");
        }

        Mahasiswa mhs = new Mahasiswa(data);
        mhs.setId(old.getId());
        mhs.setEmail(old.getEmail());
        mhs.setPassword(old.getPassword());
        mhs.setIdentifier(data.getIdentifier());
        return mhs;    }
}
