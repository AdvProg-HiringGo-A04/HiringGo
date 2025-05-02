package id.ac.ui.cs.advprog.hiringgo.manajemenakun.service.updaterole;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.AccountData;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Role;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Users;
import org.springframework.stereotype.Component;

@Component
public class DosenToMahasiswaStrategy implements UpdateRoleStrategy {
    @Override public boolean supports(Role from, Role to) {
        return from == Role.DOSEN && to == Role.MAHASISWA;
    }
    @Override public Users changeRole(Users old, AccountData data) {
        if (data.getIdentifier() == null) {
            throw new IllegalArgumentException("Mahasiswa requires NIP");
        }

        Mahasiswa mhs = new Mahasiswa(data);
        mhs.setId(old.getId());
        mhs.setEmail(old.getEmail());
        mhs.setPassword(old.getPassword());
        mhs.setFullName(old.getFullName());
        mhs.setIdentifier(data.getIdentifier());
        return mhs;
    }
}