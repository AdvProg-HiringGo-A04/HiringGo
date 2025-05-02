package id.ac.ui.cs.advprog.hiringgo.manajemenakun.service.updaterole;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.AccountData;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Dosen;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Role;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Users;
import org.springframework.stereotype.Component;

@Component
public class AdminToDosenStrategy implements UpdateRoleStrategy {
    @Override
    public boolean supports(Role from, Role to) {
        return from==Role.ADMIN && to==Role.DOSEN;
    }
    @Override
    public Users changeRole(Users old, AccountData data) {
        if (data.getIdentifier() == null || data.getFullName() == null) {
            throw new IllegalArgumentException("Dosen requires NIP and full name");
        }

        Dosen dosen = new Dosen(data);
        dosen.setId(old.getId());
        dosen.setEmail(old.getEmail());
        dosen.setPassword(old.getPassword());
        dosen.setFullName(data.getFullName());
        dosen.setIdentifier(data.getIdentifier());
        return dosen;
    }
}