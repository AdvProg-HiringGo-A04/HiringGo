package id.ac.ui.cs.advprog.hiringgo.manajemenakun.service;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.repository.UserRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private AccountService service;

    @BeforeEach
    void setup() {
        service = new AccountService(new UserRepositoryImpl());
    }

    @Test
    void testCreateAndFindAll() {
        service.createAccount(Role.ADMIN, new AccountData(null, null, "a1@example.com", "pwd1"));
        service.createAccount(Role.DOSEN, new AccountData("NIP1", "Dr. A", "d1@example.com", "pwd2"));
        assertEquals(2, service.findAll().size());
    }

    @Test
    void testUpdateRole() {
        Account acc = service.createAccount(Role.DOSEN, new AccountData("NIP2", "Dr. C", "c@example.com", "pwd"));
        service.updateRole(acc.getId(), Role.ADMIN);
        assertEquals(Role.ADMIN, service.findById(acc.getId()).getRole());
    }

    @Test
    void deleteAccount_notSelf_shouldRemoveAccount() {
        Account admin = service.createAccount(Role.ADMIN, new AccountData(null, null, "self@example.com", "pwd"));
        Account other = service.createAccount(Role.DOSEN, new AccountData("NIP3", "Dr. X", "x@example.com", "pwd"));
        service.deleteAccount(other.getId(), admin.getId());
        assertNull(service.findById(other.getId()));
    }

    @Test
    void deleteAccount_selfDeletion_shouldThrowException() {
        Account admin = service.createAccount(Role.ADMIN, new AccountData(null, null, "root@example.com", "pwd"));
        assertThrows(IllegalArgumentException.class, () ->
                service.deleteAccount(admin.getId(), admin.getId())
        );
    }
}