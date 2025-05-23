package id.ac.ui.cs.advprog.hiringgo.matakuliah.mapper;

import id.ac.ui.cs.advprog.hiringgo.entity.MataKuliah;
import id.ac.ui.cs.advprog.hiringgo.matakuliah.model.CreateMataKuliahRequest;
import id.ac.ui.cs.advprog.hiringgo.matakuliah.model.MataKuliahResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static id.ac.ui.cs.advprog.hiringgo.matakuliah.mapper.MataKuliahMapper.createMataKuliahRequestToMataKuliah;
import static id.ac.ui.cs.advprog.hiringgo.matakuliah.mapper.MataKuliahMapper.mataKuliahToMataKuliahResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MataKuliahMapperTest {

    @Test
    void testMataKuliahToMataKuliahResponse() {
        MataKuliah mataKuliah = MataKuliah.builder()
                .kodeMataKuliah("CSCM602223")
                .namaMataKuliah("Pemrograman Lanjut")
                .deskripsiMataKuliah("Belajar konsep lanjutan pemrograman.")
                .dosenPengampu(List.of())
                .build();

        MataKuliahResponse response = mataKuliahToMataKuliahResponse(mataKuliah);

        assertNotNull(response);
        assertEquals(mataKuliah.getKodeMataKuliah(), response.getKodeMataKuliah());
        assertEquals(mataKuliah.getNamaMataKuliah(), response.getNamaMataKuliah());
        assertEquals(mataKuliah.getDeskripsiMataKuliah(), response.getDeskripsiMataKuliah());
        assertEquals(mataKuliah.getDosenPengampu().size(), response.getDosenPengampu().size());
    }

    @Test
    void testCreateMataKuliahRequestToMataKuliah() {
        CreateMataKuliahRequest request = CreateMataKuliahRequest.builder()
                .kodeMataKuliah("CSCM602223")
                .namaMataKuliah("Pemrograman Lanjut")
                .deskripsiMataKuliah("Belajar konsep lanjutan pemrograman.")
                .dosenPengampu(List.of())
                .build();

        MataKuliah mataKuliah = createMataKuliahRequestToMataKuliah(request);

        assertNotNull(mataKuliah);
        assertEquals(request.getKodeMataKuliah(), mataKuliah.getKodeMataKuliah());
        assertEquals(request.getNamaMataKuliah(), mataKuliah.getNamaMataKuliah());
        assertEquals(request.getDeskripsiMataKuliah(), mataKuliah.getDeskripsiMataKuliah());
        assertEquals(request.getDosenPengampu().size(), mataKuliah.getDosenPengampu().size());
    }
}
