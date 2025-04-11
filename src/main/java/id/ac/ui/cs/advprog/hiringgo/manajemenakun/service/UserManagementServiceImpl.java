package id.ac.ui.cs.advprog.hiringgo.manajemenakun.service;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.User;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserManagementServiceImpl implements UserManagementService {
    private final UserRepository userRepository;
    private final Map<String, String> roleDisplayMap;

    public UserManagementServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;

        Map<String, String> map = new HashMap<>();
        map.put("ADMIN", "Admin");
        map.put("DOSEN", "Dosen");
        map.put("MAHASISWA", "Mahasiswa");
        this.roleDisplayMap = map;
    }

    @Override
    public List<UserAccountDTO> getAllUserAccounts() {
        List<User> users = userRepository.getAllUsers();

        return users.stream()
                .map(this::mapToAccountDTO)
                .collect(Collectors.toList());
    }

    private UserAccountDTO mapToAccountDTO(User user) {
        String roleDisplay = roleDisplayMap.getOrDefault(user.getRole(), user.getRole());

        return new UserAccountDTO(
                user.getId(),
                user.getNamaLengkap(),
                user.getEmail(),
                roleDisplay
        );
    }
}