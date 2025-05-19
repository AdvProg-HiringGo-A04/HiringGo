package id.ac.ui.cs.advprog.hiringgo.manajemenakun.service;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.repository.UserRepository;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.AccountFactory;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Role;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Users;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.AccountData;

import java.util.List;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.service.updaterole.UpdateRoleStrategy;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repo;
    private final List<UpdateRoleStrategy> strategies;

    public UserService(UserRepository repo, List<UpdateRoleStrategy> strategies) {
        this.repo = repo;
        this.strategies = strategies;
    }

    public Users createAccount(Role role, AccountData data) {
        Users acc = AccountFactory.createAccount(role, data);
        return repo.save(acc);
    }

    public List<Users> findAll() {
        return repo.findAll();
    }

    public Users findById(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }

    public void updateRole(String id, String requesterId, Role newRole, AccountData data) {
        if (id.equals(requesterId)) {
            throw new IllegalArgumentException("Cannot update own role");
        }
        Users old = findById(id);
        UpdateRoleStrategy strat = strategies.stream()
                .filter(s -> s.supports(old.getRole(), newRole))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Transition " + old.getRole() + "->" + newRole + " not supported"));
        Users updated = strat.changeRole(old, data);
        repo.save(updated);
    }

    public void deleteAccount(String targetId, String requesterId) {
        if (targetId.equals(requesterId)) {
            throw new IllegalArgumentException("Cannot delete self");
        }
        repo.deleteById(targetId);
    }
}
