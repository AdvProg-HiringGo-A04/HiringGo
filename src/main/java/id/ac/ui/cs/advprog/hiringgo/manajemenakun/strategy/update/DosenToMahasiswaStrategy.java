package id.ac.ui.cs.advprog.hiringgo.manajemenakun.strategy.update;

import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.entity.Dosen;
import id.ac.ui.cs.advprog.hiringgo.entity.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.entity.Role;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.RoleUpdateDTO;
import id.ac.ui.cs.advprog.hiringgo.repository.DosenRepository;
import id.ac.ui.cs.advprog.hiringgo.repository.MahasiswaRepository;
import id.ac.ui.cs.advprog.hiringgo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Component
public class DosenToMahasiswaStrategy implements UpdateRoleStrategy {
    private final UserRepository userRepository;
    private final DosenRepository dosenRepository;
    private final MahasiswaRepository mahasiswaRepository;

    @Autowired
    public DosenToMahasiswaStrategy(
            UserRepository userRepository,
            DosenRepository dosenRepository,
            MahasiswaRepository mahasiswaRepository) {
        this.userRepository = userRepository;
        this.dosenRepository = dosenRepository;
        this.mahasiswaRepository = mahasiswaRepository;
    }

    @Override
    public boolean supports(Role from, Role to) {
        return from == Role.DOSEN && to == Role.MAHASISWA;
    }

    @Override
    @Transactional
    public User updateRole(String userId, RoleUpdateDTO dto) {
        validateNpm(dto.getNpm());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Dosen dosen = dosenRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dosen not found"));

        String namaLengkap = dosen.getNamaLengkap();

        user.setRole(Role.MAHASISWA);
        userRepository.save(user);

        dosenRepository.deleteById(userId);

        Mahasiswa mahasiswa = new Mahasiswa();
        mahasiswa.setId(userId);
        mahasiswa.setNamaLengkap(namaLengkap);
        mahasiswa.setNPM(dto.getNpm());
        mahasiswaRepository.save(mahasiswa);

        return user;
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