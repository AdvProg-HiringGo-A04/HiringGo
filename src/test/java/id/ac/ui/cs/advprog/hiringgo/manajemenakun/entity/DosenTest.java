package id.ac.ui.cs.advprog.hiringgo.manajemenakun.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DosenTest {

    private Dosen dosen;
    private final String ID = "d12345";
    private final String NAMA_LENGKAP = "Dr. Budi Santoso";
    private final String NIP = "198501152010121002";

    @BeforeEach
    void setUp() {
        dosen = new Dosen();
        dosen.setId(ID);
        dosen.setNamaLengkap(NAMA_LENGKAP);
        dosen.setNIP(NIP);
    }

    @Test
    void testGetId() {
        assertEquals(ID, dosen.getId());
    }

    @Test
    void testSetId() {
        String newId = "d67890";
        dosen.setId(newId);
        assertEquals(newId, dosen.getId());
    }

    @Test
    void testGetNamaLengkap() {
        assertEquals(NAMA_LENGKAP, dosen.getNamaLengkap());
    }

    @Test
    void testSetNamaLengkap() {
        String newNamaLengkap = "Prof. Ahmad Wijaya";
        dosen.setNamaLengkap(newNamaLengkap);
        assertEquals(newNamaLengkap, dosen.getNamaLengkap());
    }

    @Test
    void testGetNIP() {
        assertEquals(NIP, dosen.getNIP());
    }

    @Test
    void testSetNIP() {
        String newNIP = "197005062005011003";
        dosen.setNIP(newNIP);
        assertEquals(newNIP, dosen.getNIP());
    }

    @Test
    void testToString() {
        String expectedToString = "Dosen(id=" + ID + ", namaLengkap=" + NAMA_LENGKAP + ", NIP=" + NIP + ")";
        assertEquals(expectedToString, dosen.toString());
    }


    @Test
    void testConstructor() {
        Dosen emptyDosen = new Dosen();
        assertNull(emptyDosen.getId());
        assertNull(emptyDosen.getNamaLengkap());
        assertNull(emptyDosen.getNIP());
    }
}