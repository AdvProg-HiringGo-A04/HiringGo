package id.ac.ui.cs.advprog.hiringgo.manajemenakun.strategy.delete;

import id.ac.ui.cs.advprog.hiringgo.entity.Role;
import id.ac.ui.cs.advprog.hiringgo.repository.AdminRepository;
import id.ac.ui.cs.advprog.hiringgo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DeleteAdminStrategy implements DeleteUserStrategy {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    @Autowired
    public DeleteAdminStrategy(AdminRepository adminRepository, UserRepository userRepository) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Role role) {
        return Role.ADMIN.equals(role);
    }

    @Async
    @Override
    @Transactional
    public void deleteUser(String id) {
        adminRepository.deleteById(id);
        userRepository.deleteById(id);
    }
}
