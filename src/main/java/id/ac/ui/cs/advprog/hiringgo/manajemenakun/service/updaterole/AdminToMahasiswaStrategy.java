package id.ac.ui.cs.advprog.hiringgo.manajemenakun.service.updaterole;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.AccountData;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Role;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Users;
import org.springframework.stereotype.Component;

@Component
public class AdminToMahasiswaStrategy implements UpdateRoleStrategy {
    @Override
    public boolean supports(Role from, Role to) {
        return from == Role.ADMIN && to == Role.MAHASISWA;
    }
    @Override
    public Users changeRole(Users old, AccountData data) {
        return new Mahasiswa(data);
    }
}
