package id.ac.ui.cs.advprog.hiringgo.manajemenakun.service;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.repository.UserRepository;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.AccountFactory;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Role;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Account;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.AccountData;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Dosen;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Admin;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Mahasiswa;

import java.util.List;

public class UserService {
    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public Account createAccount(Role role, AccountData data) {
        Account acc = AccountFactory.createAccount(role, data);
        repo.save(acc);
        return acc;
    }

    public List<Account> findAll() { return repo.findAll(); }

    public Account findById(String id) { return repo.findById(id); }

    public void updateRole(String id, Role newRole) {
        Account existing = repo.findById(id);
        if (existing == null) {
            throw new IllegalArgumentException("Account not found");
        }
        if (existing instanceof Dosen) {
            ((Dosen) existing).setRole(newRole);
        }
        else if (existing instanceof Admin) {
            ((Admin) existing).setRole(newRole);
        }
        else if (existing instanceof Mahasiswa) {
            ((Mahasiswa) existing).setRole(newRole);
        }
        else {
            throw new IllegalArgumentException("Cannot update role for this account type");
        }
        repo.save(existing);
    }

    public void deleteAccount(String targetId, String requesterId) {
        if (targetId.equals(requesterId)) throw new IllegalArgumentException("Cannot delete self");
        repo.delete(targetId);
    }
}
