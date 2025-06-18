package id.ac.ui.cs.advprog.hiringgo.manajemenakun.strategy.update;

import id.ac.ui.cs.advprog.hiringgo.entity.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.entity.Dosen;
import id.ac.ui.cs.advprog.hiringgo.enums.Role;
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
public class MahasiswaToDosenStrategy implements UpdateRoleStrategy {
    private final UserRepository userRepository;
    private final MahasiswaRepository mahasiswaRepository;
    private final DosenRepository dosenRepository;

    @Autowired
    public MahasiswaToDosenStrategy(
            UserRepository userRepository,
            MahasiswaRepository mahasiswaRepository,
            DosenRepository dosenRepository) {
        this.userRepository = userRepository;
        this.mahasiswaRepository = mahasiswaRepository;
        this.dosenRepository = dosenRepository;
    }

    @Override
    public boolean supports(Role from, Role to) {
        return from == Role.MAHASISWA && to == Role.DOSEN;
    }

    @Override
    @Transactional
    public User updateRole(String userId, RoleUpdateDTO dto) {
        validateNip(dto.getNip());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Mahasiswa mahasiswa = mahasiswaRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mahasiswa not found"));

        String namaLengkap = mahasiswa.getNamaLengkap();

        user.setRole(Role.DOSEN);
        userRepository.save(user);

        mahasiswaRepository.deleteById(userId);

        Dosen dosen = new Dosen();
        dosen.setId(userId);
        dosen.setNamaLengkap(namaLengkap);
        dosen.setNIP(dto.getNip());
        dosenRepository.save(dosen);

        return user;
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