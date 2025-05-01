package id.ac.ui.cs.advprog.hiringgo.manajemenakun.repository;

import  id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Account;
import  id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.AccountData;
import  id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Role;
import  id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.AccountFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryImplTest {
        private UserRepositoryImpl repo;
        private Account dosen;
        private Account admin;

        @BeforeEach
        void setup() {
            repo = new UserRepositoryImpl();
            dosen = AccountFactory.createAccount(Role.DOSEN,
                    new AccountData("NIP10", "Dr. Test", "dt@example.com", "pwd"));
            admin = AccountFactory.createAccount(Role.ADMIN,
                    new AccountData(null, null, "admin@test.com", "pwd"));
        }

        @Test
        void saveAndFindById() {
            repo.save(dosen);
            Account found = repo.findById(dosen.getId());
            assertNotNull(found);
            assertEquals(dosen.getId(), found.getId());
        }

        @Test
        void testFindAllUser() {
            repo.save(dosen);
            repo.save(admin);
            List<Account> list = repo.findAll();
            assertEquals(2, list.size());
            assertTrue(list.stream().anyMatch(a -> a.getId().equals(dosen.getId())));
            assertTrue(list.stream().anyMatch(a -> a.getId().equals(admin.getId())));
        }

        @Test
        void testDeleteUser() {
            repo.save(dosen);
            repo.delete(dosen.getId());
            assertNull(repo.findById(dosen.getId()));
            assertTrue(repo.findAll().isEmpty());
        }

        @Test
        void testDeleteNonExistingId() {
            repo.delete("non-existing");
            assertTrue(repo.findAll().isEmpty());
        }
    }
