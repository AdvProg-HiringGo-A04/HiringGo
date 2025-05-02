package id.ac.ui.cs.advprog.hiringgo.manajemenakun.service;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.repository.UserRepositoryImpl;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Role;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.AccountData;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Users;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Mahasiswa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private UserService service;
    private UserRepositoryImpl repo;


    @BeforeEach
    void setup() {
        repo = new UserRepositoryImpl();
        service = new UserService(repo);
    }

    @Test
    void testCreateAndFindAll() {
        service.createAccount(Role.ADMIN, new AccountData(null, null, "a1@example.com", "pwd1"));
        service.createAccount(Role.DOSEN, new AccountData("NIP1", "Dr. A", "d1@example.com", "pwd2"));
        assertEquals(2, service.findAll().size());
    }

    @Test
    void testCreateMahasiswaViaService_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () ->
                service.createAccount(Role.MAHASISWA, new AccountData("NIM2", "Budi", "budi@example.com", "pwd3"))
        );
    }

    @Test
    void testUpdateRoleDosenToAdmin() {
        Users admin = service.createAccount(Role.ADMIN, new AccountData(null, null, "a1@example.com", "pwd1"));
        Users dosen = service.createAccount(Role.DOSEN, new AccountData("NIP2", "Dr. C", "c@example.com", "pwd"));
        service.updateRole(dosen.getId(), admin.getId(), Role.ADMIN);
        assertEquals(Role.ADMIN, service.findById(dosen.getId()).getRole());
    }

    @Test
    void updateRoleMahasiswaToDosen() {
        Users admin = service.createAccount(Role.ADMIN, new AccountData(null, null, "a1@example.com", "pwd1"));
        Users mhs = new Mahasiswa(new AccountData("NIM3", "Citra", "citra@example.com", "pwd"));
        repo.save(mhs);
        service.updateRole(mhs.getId(), admin.getId(), Role.DOSEN);
        assertEquals(Role.DOSEN, service.findById(mhs.getId()).getRole());
    }

    @Test
    void testAdminUpdateRoleItself() {
        Users admin = service.createAccount(
                Role.ADMIN,
                new AccountData(null, null, "self@example.com", "pwd")
        );
        assertThrows(IllegalArgumentException.class, () ->
                service.updateRole(admin.getId(), admin.getId(), Role.DOSEN)
        );
    }

    @Test
    void testAdminDeleteAccountDosen() {
        Users admin = service.createAccount(Role.ADMIN, new AccountData(null, null, "self@example.com", "pwd"));
        Users other = service.createAccount(Role.DOSEN, new AccountData("NIP3", "Dr. X", "x@example.com", "pwd"));
        service.deleteAccount(other.getId(), admin.getId());
        assertNull(service.findById(other.getId()));
    }

    @Test
    void testAdminDeleteAccountMahasiswa() {
        Users admin = service.createAccount(Role.ADMIN, new AccountData(null, null, "self@example.com", "pwd"));
        Users mhs = new Mahasiswa(new AccountData("NIM3", "Citra", "citra@example.com", "pwd"));
        repo.save(mhs);
        service.deleteAccount(mhs.getId(), admin.getId());
        assertNull(service.findById(mhs.getId()));
    }

    @Test
    void testAdminDeleteAccountAnotherAdmin() {
        Users admin1 = service.createAccount(Role.ADMIN, new AccountData(null, null, "self@example.com", "pwd"));
        Users admin2 = service.createAccount(Role.ADMIN, new AccountData(null, null, "self2@example.com", "pwd"));
        service.deleteAccount(admin2.getId(), admin1.getId());
        assertNull(service.findById(admin2.getId()));
    }

    @Test
    void testAdminDeleteItself() {
        Users admin = service.createAccount(Role.ADMIN, new AccountData(null, null, "root@example.com", "pwd"));
        assertThrows(IllegalArgumentException.class, () ->
                service.deleteAccount(admin.getId(), admin.getId())
        );
    }

}