package id.ac.ui.cs.advprog.hiringgo.manajemenakun.strategy.delete;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.entity.Role;
import id.ac.ui.cs.advprog.hiringgo.repository.MahasiswaRepository;
import id.ac.ui.cs.advprog.hiringgo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DeleteMahasiswaStrategy implements DeleteUserStrategy {

    private final MahasiswaRepository mahasiswaRepository;
    private final UserRepository userRepository;

    @Autowired
    public DeleteMahasiswaStrategy(MahasiswaRepository mahasiswaRepository, UserRepository userRepository) {
        this.mahasiswaRepository = mahasiswaRepository;
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Role role) {
        return Role.MAHASISWA.equals(role);
    }

    @Override
    @Transactional
    public void deleteUser(String id) {
        mahasiswaRepository.deleteById(id);
        userRepository.deleteById(id);
    }
}