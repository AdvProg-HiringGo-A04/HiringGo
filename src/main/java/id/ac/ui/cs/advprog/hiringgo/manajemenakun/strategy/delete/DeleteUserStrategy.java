package id.ac.ui.cs.advprog.hiringgo.manajemenakun.strategy.delete;

import id.ac.ui.cs.advprog.hiringgo.enums.Role;

public interface DeleteUserStrategy {
    boolean supports(Role role);
    void deleteUser(String id);
}