package id.ac.ui.cs.advprog.hiringgo.manajemenakun.strategy.delete;

import id.ac.ui.cs.advprog.hiringgo.entity.Role;
import id.ac.ui.cs.advprog.hiringgo.repository.DosenRepository;
import id.ac.ui.cs.advprog.hiringgo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DeleteDosenStrategy implements DeleteUserStrategy {

    private final DosenRepository dosenRepository;
    private final UserRepository userRepository;

    @Autowired
    public DeleteDosenStrategy(DosenRepository dosenRepository, UserRepository userRepository) {
        this.dosenRepository = dosenRepository;
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Role role) {
        return Role.DOSEN.equals(role);
    }

    @Async
    @Override
    @Transactional
    public void deleteUser(String id) {
        dosenRepository.deleteById(id);
        userRepository.deleteById(id);
    }
}
