package id.ac.ui.cs.advprog.hiringgo.matakuliah.mapper;

import id.ac.ui.cs.advprog.hiringgo.entity.MataKuliah;
import id.ac.ui.cs.advprog.hiringgo.matakuliah.model.MataKuliahResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static id.ac.ui.cs.advprog.hiringgo.matakuliah.mapper.MataKuliahMapper.mataKuliahToMataKuliahResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MataKuliahMapperTest {

    @Test
    void testMataKuliahToMataKuliahResponse() {
        MataKuliah mataKuliah = new MataKuliah();
        mataKuliah.setKodeMataKuliah("CSCM602223");
        mataKuliah.setNamaMataKuliah("Pemrograman Lanjut");
        mataKuliah.setDeskripsiMataKuliah("Belajar konsep lanjutan pemrograman.");
        mataKuliah.setDosenPengampu(List.of());

        MataKuliahResponse response = mataKuliahToMataKuliahResponse(mataKuliah);

        assertNotNull(response);
        assertEquals(mataKuliah.getKodeMataKuliah(), response.getKodeMataKuliah());
        assertEquals(mataKuliah.getNamaMataKuliah(), response.getNamaMataKuliah());
        assertEquals(mataKuliah.getDeskripsiMataKuliah(), response.getDeskripsiMataKuliah());
        assertEquals(mataKuliah.getDosenPengampu().size(), response.getDosenPengampu().size());
    }
}
