package id.ac.ui.cs.advprog.hiringgo.manajemenakun.strategy.update;

import id.ac.ui.cs.advprog.hiringgo.entity.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.enums.Role;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.RoleUpdateDTO;
import id.ac.ui.cs.advprog.hiringgo.repository.AdminRepository;
import id.ac.ui.cs.advprog.hiringgo.repository.MahasiswaRepository;
import id.ac.ui.cs.advprog.hiringgo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AdminToMahasiswaStrategy implements UpdateRoleStrategy {
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final MahasiswaRepository mahasiswaRepository;

    @Autowired
    public AdminToMahasiswaStrategy(
            UserRepository userRepository,
            AdminRepository adminRepository,
            MahasiswaRepository mahasiswaRepository) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.mahasiswaRepository = mahasiswaRepository;
    }
    @Override
    public boolean supports(Role from, Role to) {
        return from == Role.ADMIN && to == Role.MAHASISWA;
    }

    @Override
    @Transactional
    public User updateRole(String userId, RoleUpdateDTO dto) {
        validateNamaLengkap(dto.getNamaLengkap());
        validateNpm(dto.getNpm());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        user.setRole(Role.MAHASISWA);
        userRepository.save(user);

        adminRepository.deleteById(userId);

        Mahasiswa mahasiswa = new Mahasiswa();
        mahasiswa.setId(userId);
        mahasiswa.setNamaLengkap(dto.getNamaLengkap());
        mahasiswa.setNPM(dto.getNpm());
        mahasiswaRepository.save(mahasiswa);

        return user;
    }

    private void validateNamaLengkap(String namaLengkap) {
        if (namaLengkap == null || namaLengkap.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nama lengkap is required for Mahasiswa role");
        }
    }

    private void validateNpm(String npm) {
        if (npm == null || npm.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "NPM is required for Mahasiswa role");
        }

        if (mahasiswaRepository.findByNPM(npm).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "NPM is already taken");
        }
    }
}