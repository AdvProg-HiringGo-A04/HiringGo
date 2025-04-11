package id.ac.ui.cs.advprog.hiringgo.manajemenakun.service;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.*;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserManagementServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserManagementService userManagementService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        userManagementService = new UserManagementServiceImpl(userRepository);
    }

    @Test
    public void getAllUserAccountsShouldReturnUserDisplayDTOs() {
        Admin admin = new Admin("1", "admin@example.com", "password");
        Dosen dosen = new Dosen("2", "12345", "Nama Dosen", "dosen@example.com", "password");
        Mahasiswa mahasiswa = new Mahasiswa("3", "M12345", "Nama Mahasiswa", "mahasiswa@example.com", "password");

        when(userRepository.getAllUsers()).thenReturn(Arrays.asList(admin, dosen, mahasiswa));

        List<UserAccountDTO> userAccounts = userManagementService.getAllUserAccounts();

        assertEquals(3, userAccounts.size());

        assertEquals("-", userAccounts.get(0).getNamaLengkap());
        assertEquals("admin@example.com", userAccounts.get(0).getEmail());
        assertEquals("Admin", userAccounts.get(0).getRoleDisplay());

        assertEquals("Nama Dosen", userAccounts.get(1).getNamaLengkap());
        assertEquals("dosen@example.com", userAccounts.get(1).getEmail());
        assertEquals("Dosen", userAccounts.get(1).getRoleDisplay());

        assertEquals("Nama Mahasiswa", userAccounts.get(2).getNamaLengkap());
        assertEquals("mahasiswa@example.com", userAccounts.get(2).getEmail());
        assertEquals("Mahasiswa", userAccounts.get(2).getRoleDisplay());

        verify(userRepository).getAllUsers();
    }
}