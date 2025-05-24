package id.ac.ui.cs.advprog.hiringgo.manajemenakun.strategy.update;

import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.entity.Admin;
import id.ac.ui.cs.advprog.hiringgo.enums.Role;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.RoleUpdateDTO;
import id.ac.ui.cs.advprog.hiringgo.repository.AdminRepository;
import id.ac.ui.cs.advprog.hiringgo.repository.DosenRepository;
import id.ac.ui.cs.advprog.hiringgo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class DosenToAdminStrategy implements UpdateRoleStrategy {
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final DosenRepository dosenRepository;

    @Autowired
    public DosenToAdminStrategy(
            UserRepository userRepository,
            AdminRepository adminRepository,
            DosenRepository dosenRepository) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.dosenRepository = dosenRepository;
    }
    @Override
    public boolean supports(Role from, Role to) {
        return from == Role.DOSEN && to == Role.ADMIN;
    }

    @Override
    @Transactional
    public User updateRole(String userId, RoleUpdateDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        user.setRole(Role.ADMIN);
        userRepository.save(user);

        dosenRepository.deleteById(userId);

        Admin admin = new Admin();
        admin.setId(userId);
        adminRepository.save(admin);

        return user;
    }
}