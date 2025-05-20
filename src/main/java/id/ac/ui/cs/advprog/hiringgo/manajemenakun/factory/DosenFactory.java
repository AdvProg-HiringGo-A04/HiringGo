package id.ac.ui.cs.advprog.hiringgo.manajemenakun.factory;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.DosenDTO;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.entity.Dosen;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.entity.Role;
import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.repository.DosenRepository;
import id.ac.ui.cs.advprog.hiringgo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Component
public class DosenFactory implements AccountFactory<DosenDTO> {

    private final UserRepository userRepository;
    private final DosenRepository dosenRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DosenFactory(
            UserRepository userRepository,
            DosenRepository dosenRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.dosenRepository = dosenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User createAccount(DosenDTO dosenDTO) {
        validateEmailNotTaken(dosenDTO.getEmail());

        validateNIPNotTaken(dosenDTO.getNip());

        User user = createUserEntity(dosenDTO.getEmail(), dosenDTO.getPassword(), Role.DOSEN);

        Dosen dosen = new Dosen();
        dosen.setId(user.getId());
        dosen.setNamaLengkap(dosenDTO.getNamaLengkap());
        dosen.setNIP(dosenDTO.getNip());

        dosenRepository.save(dosen);
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

    private void validateNIPNotTaken(String nip) {
        if (dosenRepository.findByNIP(nip).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "NIP is already taken");
        }
    }
}