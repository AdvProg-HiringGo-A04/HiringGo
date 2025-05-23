package id.ac.ui.cs.advprog.hiringgo.manajemenakun.strategy.update;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.entity.Dosen;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.entity.Role;
import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.RoleUpdateDTO;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.repository.AdminRepository;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.repository.DosenRepository;
import id.ac.ui.cs.advprog.hiringgo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AdminToDosenStrategy implements UpdateRoleStrategy {
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final DosenRepository dosenRepository;

    @Autowired
    public AdminToDosenStrategy(
            UserRepository userRepository,
            AdminRepository adminRepository,
            DosenRepository dosenRepository) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.dosenRepository = dosenRepository;
    }

    @Override
    public boolean supports(Role from, Role to) {
        return from == Role.ADMIN && to == Role.DOSEN;
    }

    @Override
    @Transactional
    public User updateRole(String userId, RoleUpdateDTO dto) {
        validateNamaLengkap(dto.getNamaLengkap());
        validateNip(dto.getNip());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        user.setRole(Role.DOSEN);
        userRepository.save(user);

        adminRepository.deleteById(userId);

        Dosen dosen = new Dosen();
        dosen.setId(userId);
        dosen.setNamaLengkap(dto.getNamaLengkap());
        dosen.setNIP(dto.getNip());
        dosenRepository.save(dosen);

        return user;
    }

    private void validateNamaLengkap(String namaLengkap) {
        if (namaLengkap == null || namaLengkap.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nama lengkap is required for Dosen role");
        }
    }

    private void validateNip(String nip) {
        if (nip == null || nip.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "NIP is required for Dosen role");
        }

        if (dosenRepository.findByNIP(nip).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "NIP is already taken");
        }
    }
}