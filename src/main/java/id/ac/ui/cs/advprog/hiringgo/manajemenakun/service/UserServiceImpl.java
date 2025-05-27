package id.ac.ui.cs.advprog.hiringgo.manajemenakun.service;

import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.AdminDTO;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.DosenDTO;
import id.ac.ui.cs.advprog.hiringgo.entity.Admin;
import id.ac.ui.cs.advprog.hiringgo.entity.Dosen;
import id.ac.ui.cs.advprog.hiringgo.entity.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.enums.Role;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.factory.AccountFactory;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.RoleUpdateDTO;
import id.ac.ui.cs.advprog.hiringgo.repository.AdminRepository;
import id.ac.ui.cs.advprog.hiringgo.repository.DosenRepository;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.strategy.delete.DeleteUserStrategy;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.util.UserMapper;
import id.ac.ui.cs.advprog.hiringgo.repository.MahasiswaRepository;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.strategy.update.UpdateRoleStrategy;
import id.ac.ui.cs.advprog.hiringgo.repository.UserRepository;
import id.ac.ui.cs.advprog.hiringgo.security.CurrentUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final CurrentUserProvider currentUserProvider;
    private final AccountFactory<AdminDTO> adminFactory;
    private final AccountFactory<DosenDTO> dosenFactory;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final DosenRepository dosenRepository;
    private final MahasiswaRepository mahasiswaRepository;
    private final List<UpdateRoleStrategy> updateRoleStrategies;
    private final List<DeleteUserStrategy> deleteUserStrategies;

    @Autowired
    public UserServiceImpl(
            AccountFactory<AdminDTO> adminFactory,
            AccountFactory<DosenDTO> dosenFactory,
            UserRepository userRepository,
            AdminRepository adminRepository,
            DosenRepository dosenRepository,
            MahasiswaRepository mahasiswaRepository,
            List<UpdateRoleStrategy> updateRoleStrategies,
            List<DeleteUserStrategy> deleteUserStrategies,
            CurrentUserProvider currentUserProvider,
            UserMapper userMapper) {
        this.adminFactory = adminFactory;
        this.dosenFactory = dosenFactory;
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.dosenRepository = dosenRepository;
        this.mahasiswaRepository = mahasiswaRepository;
        this.updateRoleStrategies = updateRoleStrategies;
        this.deleteUserStrategies = deleteUserStrategies;
        this.currentUserProvider = currentUserProvider;
        this.userMapper = userMapper;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Object getUserResponseById(String id) {
        User user = getUserById(id);
        return userMapper.mapUserToResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<Object> getAllUsersResponse() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::mapUserToResponse)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public User createAdmin(AdminDTO adminDTO) {
        return adminFactory.createAccount(adminDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public User createDosen(DosenDTO dosenDTO) {
        return dosenFactory.createAccount(dosenDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<Dosen> getAllDosens() {
        return dosenRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<Mahasiswa> getAllMahasiswas() {
        return mahasiswaRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Admin getAdminById(String id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin not found"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Dosen getDosenById(String id) {
        return dosenRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dosen not found"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Mahasiswa getMahasiswaById(String id) {
        return mahasiswaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mahasiswa not found"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public User updateUserRole(String id, RoleUpdateDTO roleUpdateDTO) {
        String currentUserId = currentUserProvider.getCurrentUserId();

        if (id.equals(currentUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Admin cannot update their own role");
        }

        User user = getUserById(id);
        Role oldRole = user.getRole();
        Role newRole = roleUpdateDTO.getRole();

        if (newRole == oldRole) {
            return user;
        }

        for (UpdateRoleStrategy strategy : updateRoleStrategies) {
            if (strategy.supports(oldRole, newRole)) {
                return strategy.updateRole(id, roleUpdateDTO);
            }
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Cannot change role from " + oldRole + " to " + newRole);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteAdmin(String id) {
        deleteUserByRole(id, Role.ADMIN);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteDosen(String id) {
        deleteUserByRole(id, Role.DOSEN);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteMahasiswa(String id) {
        deleteUserByRole(id, Role.MAHASISWA);
    }

    private void deleteUserByRole(String id, Role expectedRole) {
        String currentUserId = currentUserProvider.getCurrentUserId();

        if (id.equals(currentUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Admin cannot delete their own account");
        }

        User user = getUserById(id);

        if (!expectedRole.equals(user.getRole())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "User role mismatch. Expected: " + expectedRole + ", Actual: " + user.getRole());
        }

        for (DeleteUserStrategy strategy : deleteUserStrategies) {
            if (strategy.supports(user.getRole())) {
                strategy.deleteUser(id);
                return;
            }
        }

        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "No delete strategy found for role: " + user.getRole());
    }

}
