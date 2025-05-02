package id.ac.ui.cs.advprog.hiringgo.dashboardhonor.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LowonganTest {
    @Test
    void testSetterAndGetter() {
        Lowongan vac = new Lowongan();
        vac.setId("csui-123");
        vac.setMahasiswaId("mhs-456");
        assertEquals("csui-123", vac.getId());
        assertEquals("mhs-456", vac.getMahasiswaId());
    }
}
