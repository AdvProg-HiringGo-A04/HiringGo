package id.ac.ui.cs.advprog.hiringgo.manajemenakun.service;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.AccountData;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.*;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.repository.UserRepository;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.service.updaterole.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository repo;

    private List<UpdateRoleStrategy> strategies;

    @InjectMocks
    private UserService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        strategies = Arrays.asList(
                new AdminToDosenStrategy(),
                new AdminToMahasiswaStrategy(),
                new DosenToAdminStrategy(),
                new DosenToMahasiswaStrategy(),
                new MahasiswaToAdminStrategy(),
                new MahasiswaToDosenStrategy()
        );
        service = new UserService(repo, strategies);
    }

    @Test
    void testCreateAndFindAll() {
        Users u1 = new Admin(new AccountData(null, null, "a1@example.com", "pwd1"));
        Users u2 = new Dosen(new AccountData("NIP1", "Dr. A", "d1@example.com", "pwd2"));

        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(repo.findAll()).thenReturn(Arrays.asList(u1, u2));

        service.createAccount(Role.ADMIN, new AccountData(null, null, "a1@example.com", "pwd1"));
        service.createAccount(Role.DOSEN, new AccountData("NIP1", "Dr. A", "d1@example.com", "pwd2"));

        List<Users> all = service.findAll();
        assertEquals(2, all.size());
        verify(repo).findAll();
    }

    @Test
    void testCreateMahasiswaViaService_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () ->
                service.createAccount(Role.MAHASISWA,
                        new AccountData("NIM2", "Budi", "budi@example.com", "pwd3"))
        );
        verify(repo, never()).save(any());
    }

    @Test
    void testUpdateRoleDosenToAdmin() {
        Users admin = new Admin(new AccountData(null, null, "a1@example.com", "pwd1"));
        Users dosen = new Dosen(new AccountData("NIP2", "Dr. C", "c@example.com", "pwd"));

        when(repo.findById(admin.getId())).thenReturn(Optional.of(admin));
        when(repo.findById(dosen.getId())).thenReturn(Optional.of(dosen));
        ArgumentCaptor<Users> captor = ArgumentCaptor.forClass(Users.class);
        when(repo.save(captor.capture())).thenAnswer(inv -> inv.getArgument(0));

        service.updateRole(dosen.getId(), admin.getId(), Role.ADMIN, new AccountData(null, null, null, null));

        Users saved = captor.getValue();
        assertTrue(saved instanceof Admin, "Should produce an Admin instance");
        assertEquals(Role.ADMIN, saved.getRole());
    }

    @Test
    void testUpdateRoleMahasiswaToDosen() {
        Users admin = new Admin(new AccountData(null,null,"a1@example.com","pwd1"));
        Users mhs   = new Mahasiswa(new AccountData("NIM3","Citra","citra@example.com","pwd"));

        when(repo.findById(admin.getId())).thenReturn(Optional.of(admin));
        when(repo.findById(mhs.getId())).thenReturn(Optional.of(mhs));
        ArgumentCaptor<Users> captor = ArgumentCaptor.forClass(Users.class);
        when(repo.save(captor.capture())).thenAnswer(inv -> inv.getArgument(0));

        AccountData data = new AccountData("NIP999","Dr. Citra",null,null);
        service.updateRole(mhs.getId(), admin.getId(), Role.DOSEN, data);

        Users saved = captor.getValue();
        assertTrue(saved instanceof Dosen, "Should produce a Dosen instance");
        Dosen d = (Dosen) saved;
        assertEquals(Role.DOSEN, d.getRole());
        assertEquals("NIP999", d.getIdentifier());
    }


    @Test
    void testAdminUpdateRoleItself() {
        Users admin = new Admin(new AccountData(null,null,"self@example.com","pwd"));
        when(repo.findById(admin.getId())).thenReturn(Optional.of(admin));

        assertThrows(IllegalArgumentException.class, () ->
                service.updateRole(admin.getId(), admin.getId(), Role.DOSEN,
                        new AccountData(null,null,null,null))
        );
        verify(repo, never()).save(any());
    }

    @Test
    void testDeleteAndFindById() {
        Users dosen = new Dosen(new AccountData("NIP3","Dr. X","x@example.com","pwd"));
        when(repo.findById(dosen.getId())).thenReturn(Optional.of(dosen));
        doNothing().when(repo).deleteById(dosen.getId());

        service.deleteAccount(dosen.getId(), "adminId");

        verify(repo).deleteById(dosen.getId());
    }

    @Test
    void testDeleteItself() {
        Users admin = new Admin(new AccountData(null,null,"root@example.com","pwd"));
        when(repo.findById(admin.getId())).thenReturn(Optional.of(admin));

        assertThrows(IllegalArgumentException.class, () ->
                service.deleteAccount(admin.getId(), admin.getId())
        );
        verify(repo, never()).deleteById(any());
    }
}
