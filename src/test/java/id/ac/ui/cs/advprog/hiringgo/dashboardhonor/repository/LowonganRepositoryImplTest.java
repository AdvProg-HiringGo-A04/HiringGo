package id.ac.ui.cs.advprog.hiringgo.dashboardhonor.repository;

import id.ac.ui.cs.advprog.hiringgo.dashboardhonor.model.Lowongan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LowonganRepositoryImplTest {

    private LowonganRepositoryImpl repo;

    @BeforeEach
    void setUp() {
        repo = new LowonganRepositoryImpl();
    }

    @Test
    void testStoreMahasiswaToLowongan() {
        Lowongan vac = new Lowongan();
        vac.setMahasiswaId("mhs-123");
        assertNull(vac.getId());

        Lowongan saved = repo.save(vac);
        assertNotNull(saved.getId(), "ID harus di-assign oleh repository");
        assertEquals("mhs-123", saved.getMahasiswaId());

        List<Lowongan> all = repo.findByMahasiswaId("mhs-123");
        assertEquals(1, all.size());
        assertEquals(saved.getId(), all.get(0).getId());
    }

    @Test
    void findByStudentId_shouldReturnOnlyMatchingEntities() {
        Lowongan vac1 = new Lowongan();
        vac1.setMahasiswaId("mhs-1");
        repo.save(vac1);

        Lowongan vac2 = new Lowongan();
        vac2.setMahasiswaId("mhs-2");
        repo.save(vac2);

        List<Lowongan> result = repo.findByMahasiswaId("mhs-1");
        assertEquals(1, result.size());
        assertEquals("mhs-1", result.get(0).getMahasiswaId());

        result = repo.findByMahasiswaId("mhs-2");
        assertEquals(1, result.size());
        assertEquals("mhs-2", result.get(0).getMahasiswaId());

        result = repo.findByMahasiswaId("unknown");
        assertTrue(result.isEmpty());
    }
}
