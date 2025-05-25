package id.ac.ui.cs.advprog.hiringgo.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MahasiswaTest {

    private Mahasiswa mahasiswa;
    private final String TEST_ID = "mhs-123";
    private final String TEST_NAMA = "John Doe";
    private final String TEST_NPM = "2106123456";

    @BeforeEach
    void setUp() {
        mahasiswa = new Mahasiswa();
    }

    @Test
    void testGetterAndSetterForId() {
        mahasiswa.setId(TEST_ID);
        assertEquals(TEST_ID, mahasiswa.getId(), "getId should return the set ID");
    }

    @Test
    void testGetterAndSetterForNamaLengkap() {
        mahasiswa.setNamaLengkap(TEST_NAMA);
        assertEquals(TEST_NAMA, mahasiswa.getNamaLengkap(), "getNamaLengkap should return the set name");
    }

    @Test
    void testGetterAndSetterForNPM() {
        mahasiswa.setNPM(TEST_NPM);
        assertEquals(TEST_NPM, mahasiswa.getNPM(), "getNPM should return the set NPM");
    }

    @Test
    void testAllFieldsSetAtOnce() {
        mahasiswa.setId(TEST_ID);
        mahasiswa.setNamaLengkap(TEST_NAMA);
        mahasiswa.setNPM(TEST_NPM);

        assertEquals(TEST_ID, mahasiswa.getId(), "getId should return the set ID");
        assertEquals(TEST_NAMA, mahasiswa.getNamaLengkap(), "getNamaLengkap should return the set name");
        assertEquals(TEST_NPM, mahasiswa.getNPM(), "getNPM should return the set NPM");
    }

    @Test
    void testDefaultConstructor() {
        Mahasiswa newMahasiswa = new Mahasiswa();

        assertNull(newMahasiswa.getId(), "Default ID should be null");
        assertNull(newMahasiswa.getNamaLengkap(), "Default namaLengkap should be null");
        assertNull(newMahasiswa.getNPM(), "Default NPM should be null");
    }

    @Test
    void testToStringContainsImportantFields() {
        mahasiswa.setId(TEST_ID);
        mahasiswa.setNamaLengkap(TEST_NAMA);
        mahasiswa.setNPM(TEST_NPM);

        String toString = mahasiswa.toString();

        assertTrue(toString.contains(TEST_ID), "toString should contain the ID");
        assertTrue(toString.contains(TEST_NAMA), "toString should contain the name");
        assertTrue(toString.contains(TEST_NPM), "toString should contain the NPM");
    }

    @Test
    void testEqualsAndHashCode() {
        Mahasiswa mahasiswa1 = new Mahasiswa();
        mahasiswa1.setId("same-id");
        mahasiswa1.setNamaLengkap("John Doe");
        mahasiswa1.setNPM("1234567890");

        Mahasiswa mahasiswa2 = new Mahasiswa();
        mahasiswa2.setId("same-id");
        mahasiswa2.setNamaLengkap("Jane Smith"); // Different name
        mahasiswa2.setNPM("0987654321"); // Different NPM

        Mahasiswa mahasiswa3 = new Mahasiswa();
        mahasiswa3.setId("different-id");
        mahasiswa3.setNamaLengkap("John Doe");
        mahasiswa3.setNPM("1234567890");

        assertEquals(mahasiswa1, mahasiswa1, "An object should equal itself");
    }

    @Test
    void testNullFields() {
        assertDoesNotThrow(() -> {
            mahasiswa.setId(null);
            mahasiswa.setNamaLengkap(null);
            mahasiswa.setNPM(null);
        });

        assertNull(mahasiswa.getId());
        assertNull(mahasiswa.getNamaLengkap());
        assertNull(mahasiswa.getNPM());
    }
}