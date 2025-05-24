package id.ac.ui.cs.advprog.hiringgo.manajemenakun.strategy.update;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.RoleUpdateDTO;
import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.enums.Role;

public interface UpdateRoleStrategy {
    boolean supports(Role from, Role to);
    User updateRole(String userId, RoleUpdateDTO dto);
}