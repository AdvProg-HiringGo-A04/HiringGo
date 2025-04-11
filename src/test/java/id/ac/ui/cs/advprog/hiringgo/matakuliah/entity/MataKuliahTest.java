package id.ac.ui.cs.advprog.hiringgo.matakuliah.entity;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
public class MataKuliahTest {

    @Autowired
    private EntityManager entityManager;

    private Dosen dosen1;
    private Dosen dosen2;

    @BeforeEach
    void setUp() {
        dosen1 = new Dosen();
        dosen1.setNip("123456");
        entityManager.persist(dosen1);

        dosen2 = new Dosen();
        dosen2.setNip("654321");
        entityManager.persist(dosen2);
    }

    @Test
    void testPersistMataKuliahWithDosen() {
        MataKuliah mk = new MataKuliah();
        mk.setKodeMataKuliah("CSCM602223");
        mk.setNamaMataKuliah("Pemrograman Lanjut");
        mk.setDeskripsiMataKuliah("Belajar pemrograman lanjut");
        mk.setDosenPengampu(List.of(dosen1, dosen2));

        entityManager.persist(mk);
        entityManager.flush();
        entityManager.clear();

        MataKuliah found = entityManager.find(MataKuliah.class, "CSCM602223");

        assertThat(found).isNotNull();
        assertThat(found.getNamaMataKuliah()).isEqualTo("Pemrograman Lanjut");
        assertThat(found.getDosenPengampu()).hasSize(2);
    }

    @Test
    void testUpdateMataKuliah() {
        MataKuliah mk = new MataKuliah();
        mk.setKodeMataKuliah("CSCM602223");
        mk.setNamaMataKuliah("Pemrograman Lanjut");
        mk.setDeskripsiMataKuliah("Belajar pemrograman lanjut");
        entityManager.persist(mk);

        mk.setNamaMataKuliah("Pemrograman Lanjut - Update");
        entityManager.merge(mk);
        entityManager.flush();
        entityManager.clear();

        MataKuliah updated = entityManager.find(MataKuliah.class, "CSCM602223");
        assertThat(updated.getNamaMataKuliah()).isEqualTo("Pemrograman Lanjut - Update");
    }

    @Test
    void testDeleteMataKuliah() {
        MataKuliah mk = new MataKuliah();
        mk.setKodeMataKuliah("CSGE602040");
        mk.setNamaMataKuliah("Struktur Data & Algoritma");
        mk.setDeskripsiMataKuliah("Stack, Queue, Tree");
        mk.setDosenPengampu(List.of(dosen1));
        entityManager.persist(mk);
        entityManager.flush();

        entityManager.remove(mk);
        entityManager.flush();
        entityManager.clear();

        MataKuliah deleted = entityManager.find(MataKuliah.class, "CSGE602040");
        assertThat(deleted).isNull();
    }

    @Test
    void testMataKuliahWithoutDosen() {
        MataKuliah mk = new MataKuliah();
        mk.setKodeMataKuliah("CSGE602070");
        mk.setNamaMataKuliah("Basis Data");
        mk.setDeskripsiMataKuliah("ERD, SQL");
        mk.setDosenPengampu(Collections.emptyList());

        entityManager.persist(mk);
        entityManager.flush();
        entityManager.clear();

        MataKuliah found = entityManager.find(MataKuliah.class, "CSGE602070");
        assertThat(found.getDosenPengampu()).isEmpty();
    }
}
