package id.ac.ui.cs.advprog.hiringgo.manajemenakun.repository;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryImplTest {
        private UserRepositoryImpl repo;
        private Users dosen;
        private Users admin;
        private Users mhs;

    @BeforeEach
        void setup() {
            repo = new UserRepositoryImpl();
            dosen = new Dosen(new AccountData("NIP10", "Dr. Test", "dt@example.com", "pwd"));
            admin = new Admin(new AccountData(null, null, "admin@test.com", "pwd"));
            mhs = new Mahasiswa(new AccountData("NIM10", "Test Mhs", "mhs@test.com", "pwd"));
        }

        @Test
        void testSaveAndFindByIdDosen() {
            repo.save(dosen);
            Users found = repo.findById(dosen.getId());
            assertNotNull(found);
            assertEquals(dosen.getId(), found.getId());
        }

        @Test
        void testSaveAndFindByIdAdmin() {
            repo.save(admin);
            Users found = repo.findById(admin.getId());
            assertNotNull(found);
            assertEquals(admin.getId(), found.getId());
        }

        @Test
        void testSaveAndFindByIdMahasiswa() {
            repo.save(mhs);
            Users found = repo.findById(mhs.getId());
            assertNotNull(found);
            assertEquals(mhs.getId(), found.getId());
        }

        @Test
        void testFindAllUser() {
            repo.save(dosen);
            repo.save(admin);
            repo.save(mhs);
            List<Users> list = repo.findAll();
            assertEquals(3, list.size());
            assertTrue(list.stream().anyMatch(a -> a.getId().equals(dosen.getId())));
            assertTrue(list.stream().anyMatch(a -> a.getId().equals(admin.getId())));
            assertTrue(list.stream().anyMatch(a -> a.getId().equals(mhs.getId())));
        }

        @Test
        void testDeleteDosen() {
            repo.save(dosen);
            repo.delete(dosen.getId());
            assertNull(repo.findById(dosen.getId()));
            assertTrue(repo.findAll().isEmpty());
        }

        @Test
        void testDeleteAdmin() {
            repo.save(admin);
            repo.delete(admin.getId());
            assertNull(repo.findById(admin.getId()));
            assertTrue(repo.findAll().isEmpty());
        }

        @Test
        void testDeleteMahasiswa() {
            repo.save(mhs);
            repo.delete(mhs.getId());
            assertNull(repo.findById(mhs.getId()));
            assertTrue(repo.findAll().isEmpty());
        }

        @Test
        void testDeleteNonExistingId() {
            repo.delete("non-existing");
            assertTrue(repo.findAll().isEmpty());
        }
    }
