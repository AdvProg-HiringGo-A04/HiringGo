package id.ac.ui.cs.advprog.hiringgo.daftarLowongan.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DaftarLowonganRequestTest {

    @Test
    void testGettersAndSetters() {
        DaftarLowonganRequest request = new DaftarLowonganRequest();
        request.setSks(24);
        request.setIpk(3.75);

        assertEquals(24, request.getSks());
        assertEquals(3.75, request.getIpk());
    }

    @Test
    void testDefaultConstructor() {
        DaftarLowonganRequest request = new DaftarLowonganRequest();

        assertEquals(0, request.getSks());
        assertEquals(0.0, request.getIpk());
    }

    @Test
    void testSetterAndGetterWithValues() {
        DaftarLowonganRequest request = new DaftarLowonganRequest();
        request.setSks(18);
        request.setIpk(3.6);

        assertEquals(18, request.getSks());
        assertEquals(3.6, request.getIpk());
    }
}
