package id.ac.ui.cs.advprog.hiringgo.manajemenakun.factory;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.AdminDTO;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.entity.Admin;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.entity.Role;
import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.repository.AdminRepository;
import id.ac.ui.cs.advprog.hiringgo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AdminFactory implements AccountFactory<AdminDTO> {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminFactory(
            UserRepository userRepository,
            AdminRepository adminRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User createAccount(AdminDTO adminDTO) {
        validateEmailNotTaken(adminDTO.getEmail());

        User user = createUserEntity(adminDTO.getEmail(), adminDTO.getPassword(), Role.ADMIN);

        Admin admin = new Admin();
        admin.setId(user.getId());
        adminRepository.save(admin);
        return user;
    }

    private User createUserEntity(String email, String password, Role role) {
        User user = new User();
        user.setId(java.util.UUID.randomUUID().toString());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        return userRepository.save(user);
    }
    
    private void validateEmailNotTaken(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already taken");
        }
    }
}