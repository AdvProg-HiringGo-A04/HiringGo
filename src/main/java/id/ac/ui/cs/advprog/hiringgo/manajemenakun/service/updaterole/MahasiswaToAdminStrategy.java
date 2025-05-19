package id.ac.ui.cs.advprog.hiringgo.manajemenakun.service.updaterole;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.*;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.AccountData;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Role;
import org.springframework.stereotype.Component;

@Component
public class MahasiswaToAdminStrategy implements UpdateRoleStrategy {
    @Override public boolean supports(Role from, Role to) {
        return from == Role.MAHASISWA && to == Role.ADMIN;
    }
    @Override public Users changeRole(Users old, AccountData data) {
        Admin admin = new Admin(new AccountData(null, null, old.getEmail(), old.getPassword()));
        admin.setId(old.getId());
        return admin;
    }
}