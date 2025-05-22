package id.ac.ui.cs.advprog.hiringgo.manajemenakun.strategy.update;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.RoleUpdateDTO;
import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.entity.Role;

public interface UpdateRoleStrategy {
    boolean supports(Role from, Role to);
    User updateRole(String userId, RoleUpdateDTO dto);
}