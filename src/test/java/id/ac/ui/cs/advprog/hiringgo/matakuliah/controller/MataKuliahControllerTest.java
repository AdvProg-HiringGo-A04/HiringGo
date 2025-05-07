package id.ac.ui.cs.advprog.hiringgo.matakuliah.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.hiringgo.entity.Dosen;
import id.ac.ui.cs.advprog.hiringgo.entity.MataKuliah;
import id.ac.ui.cs.advprog.hiringgo.matakuliah.model.CreateMataKuliahRequest;
import id.ac.ui.cs.advprog.hiringgo.matakuliah.model.MataKuliahResponse;
import id.ac.ui.cs.advprog.hiringgo.matakuliah.model.UpdateMataKuliahRequest;
import id.ac.ui.cs.advprog.hiringgo.model.WebResponse;
import id.ac.ui.cs.advprog.hiringgo.repository.DosenRepository;
import id.ac.ui.cs.advprog.hiringgo.repository.MataKuliahRepository;
import id.ac.ui.cs.advprog.hiringgo.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MataKuliahControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DosenRepository dosenRepository;

    @Autowired
    private MataKuliahRepository mataKuliahRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    private MataKuliah mataKuliah1;

    private MataKuliah mataKuliah2;

    private String tokenAdmin;

    private String tokenDosen;

    private String tokenMahasiswa;

    @BeforeEach
    void setUp() {
        mataKuliahRepository.deleteAll();
        dosenRepository.deleteAll();

        Dosen dosen = new Dosen();
        dosen.setId(UUID.randomUUID().toString());
        dosen.setNIP("198403262023012008");
        dosen.setNamaLengkap("John Doe");
        dosenRepository.save(dosen);

        mataKuliah1 = new MataKuliah();
        mataKuliah1.setKodeMataKuliah("CSCM602223");
        mataKuliah1.setNamaMataKuliah("Pemrograman Lanjut");
        mataKuliah1.setDeskripsiMataKuliah("Belajar konsep lanjutan pemrograman.");
        mataKuliah1.setDosenPengampu(List.of(dosen));

        mataKuliah2 = new MataKuliah();
        mataKuliah2.setKodeMataKuliah("CSGE601021");
        mataKuliah2.setNamaMataKuliah("Dasar-dasar Pemrograman 2");
        mataKuliah2.setDeskripsiMataKuliah("Belajar dasar pemrograman 2.");

        tokenAdmin = jwtUtil.generateToken("admin", "admin@hiringg@gmail.com", "ADMIN");
        tokenDosen = jwtUtil.generateToken("dosen", "dosen@hiringg@gmail.com", "DOSEN");
        tokenMahasiswa = jwtUtil.generateToken("mahasiswa", "mahasiswa@hiringg@gmail.com", "MAHASISWA");
    }

    @Test
    void testCreateMataKuliahSuccess() throws Exception {
        CreateMataKuliahRequest createMataKuliahRequest = new CreateMataKuliahRequest();
        createMataKuliahRequest.setKodeMataKuliah(mataKuliah1.getKodeMataKuliah());
        createMataKuliahRequest.setNamaMataKuliah(mataKuliah1.getNamaMataKuliah());
        createMataKuliahRequest.setDeskripsiMataKuliah(mataKuliah1.getDeskripsiMataKuliah());
        createMataKuliahRequest.setDosenPengampu(mataKuliah1.getDosenPengampu());

        mockMvc.perform(
                post("/courses")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .content(objectMapper.writeValueAsString(createMataKuliahRequest))
        ).andExpectAll(
                status().isCreated()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getData());
            assertNull(response.getErrors());
        });
    }

    @Test
    void testCreateMataKuliahWhenRequestBodyIsNull() throws Exception {
        CreateMataKuliahRequest createMataKuliahRequest = new CreateMataKuliahRequest();

        mockMvc.perform(
                post("/courses")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .content(objectMapper.writeValueAsString(createMataKuliahRequest))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getData());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testCreateMataKuliahWithEmptyRequestBody() throws Exception {
        mockMvc.perform(
                post("/courses")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getData());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testCreateMataKuliahDuplicate() throws Exception {
        mataKuliahRepository.save(mataKuliah1);

        CreateMataKuliahRequest createMataKuliahRequest = new CreateMataKuliahRequest();
        createMataKuliahRequest.setKodeMataKuliah(mataKuliah1.getKodeMataKuliah());
        createMataKuliahRequest.setNamaMataKuliah(mataKuliah1.getNamaMataKuliah());
        createMataKuliahRequest.setDeskripsiMataKuliah(mataKuliah1.getDeskripsiMataKuliah());
        createMataKuliahRequest.setDosenPengampu(mataKuliah1.getDosenPengampu());

        mockMvc.perform(
                post("/courses")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .content(objectMapper.writeValueAsString(createMataKuliahRequest))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getData());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testCreateMataKuliahWithoutDosen() throws Exception {
        CreateMataKuliahRequest createMataKuliahRequest = new CreateMataKuliahRequest();
        createMataKuliahRequest.setKodeMataKuliah(mataKuliah1.getKodeMataKuliah());
        createMataKuliahRequest.setNamaMataKuliah(mataKuliah1.getNamaMataKuliah());
        createMataKuliahRequest.setDeskripsiMataKuliah(mataKuliah1.getDeskripsiMataKuliah());

        mockMvc.perform(
                post("/courses")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .content(objectMapper.writeValueAsString(createMataKuliahRequest))
        ).andExpectAll(
                status().isCreated()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getData());
            assertNull(response.getErrors());
        });
    }

    @Test
    void testGetMataKuliahSuccess() throws Exception {
        mataKuliahRepository.save(mataKuliah1);

        mockMvc.perform(
                get("/courses")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<MataKuliahResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(mataKuliah1.getKodeMataKuliah(), response.getData().getFirst().getKodeMataKuliah());
            assertEquals(mataKuliah1.getNamaMataKuliah(), response.getData().getFirst().getNamaMataKuliah());
            assertEquals(mataKuliah1.getDeskripsiMataKuliah(), response.getData().getFirst().getDeskripsiMataKuliah());
            assertEquals(mataKuliah1.getDosenPengampu().size(), response.getData().getFirst().getDosenPengampu().size());
        });
    }

    @Test
    void testGetMataKuliahEmpty() throws Exception {
        mockMvc.perform(
                get("/courses")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<MataKuliahResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertTrue(response.getData().isEmpty());
            assertNull(response.getErrors());
        });
    }

    @Test
    void testGetMataKuliahById() throws Exception {
        mataKuliahRepository.save(mataKuliah1);

        mockMvc.perform(
                get("/courses/" + mataKuliah1.getKodeMataKuliah())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<MataKuliahResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(mataKuliah1.getKodeMataKuliah(), response.getData().getKodeMataKuliah());
            assertEquals(mataKuliah1.getNamaMataKuliah(), response.getData().getNamaMataKuliah());
            assertEquals(mataKuliah1.getDeskripsiMataKuliah(), response.getData().getDeskripsiMataKuliah());
            assertEquals(mataKuliah1.getDosenPengampu().size(), response.getData().getDosenPengampu().size());
        });
    }

    @Test
    void testGetMataKuliahNotFound() throws Exception {
        mockMvc.perform(
                get("/courses/CSCMXXXXXX")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin)
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<MataKuliahResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getData());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testUpdateMataKuliahSuccess() throws Exception {
        mataKuliahRepository.save(mataKuliah2);

        UpdateMataKuliahRequest updateMataKuliahRequest = new UpdateMataKuliahRequest();
        updateMataKuliahRequest.setKodeMataKuliah(mataKuliah1.getKodeMataKuliah());
        updateMataKuliahRequest.setNamaMataKuliah(mataKuliah2.getNamaMataKuliah());
        updateMataKuliahRequest.setDeskripsiMataKuliah(mataKuliah2.getDeskripsiMataKuliah());

        mockMvc.perform(
                patch("/courses/" + mataKuliah2.getKodeMataKuliah())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .content(objectMapper.writeValueAsString(updateMataKuliahRequest))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getData());
            assertNull(response.getErrors());
        });

        mockMvc.perform(
                get("/courses/" + updateMataKuliahRequest.getKodeMataKuliah())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<MataKuliahResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(updateMataKuliahRequest.getKodeMataKuliah(), response.getData().getKodeMataKuliah());
            assertEquals(updateMataKuliahRequest.getNamaMataKuliah(), response.getData().getNamaMataKuliah());
            assertEquals(updateMataKuliahRequest.getDeskripsiMataKuliah(), response.getData().getDeskripsiMataKuliah());
            assertEquals(mataKuliah2.getDosenPengampu().size(), response.getData().getDosenPengampu().size());
        });
    }

    @Test
    void testUpdateMataKuliahWhenIdIsTaken() throws Exception {
        mataKuliahRepository.save(mataKuliah1);
        mataKuliahRepository.save(mataKuliah2);

        UpdateMataKuliahRequest updateMataKuliahRequest = new UpdateMataKuliahRequest();
        updateMataKuliahRequest.setKodeMataKuliah("CSGE601021");

        mockMvc.perform(
                patch("/courses/" + mataKuliah1.getKodeMataKuliah())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .content(objectMapper.writeValueAsString(updateMataKuliahRequest))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getData());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testUpdateMataKuliahWhenIdIsNotFound() throws Exception {
        UpdateMataKuliahRequest updateMataKuliahRequest = new UpdateMataKuliahRequest();
        updateMataKuliahRequest.setKodeMataKuliah("CSGE601021");

        mockMvc.perform(
                patch("/courses/CSGEXXXXXX")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .content(objectMapper.writeValueAsString(updateMataKuliahRequest))
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getData());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testUpdateMataKuliahWhenRequestBodyIsNull() throws Exception {
        mataKuliahRepository.save(mataKuliah1);

        UpdateMataKuliahRequest updateMataKuliahRequest = new UpdateMataKuliahRequest();

        mockMvc.perform(
                patch("/courses/" + mataKuliah1.getKodeMataKuliah())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .content(objectMapper.writeValueAsString(updateMataKuliahRequest))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getData());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testUpdateMataKuliahWithEmptyRequestBody() throws Exception {
        mataKuliahRepository.save(mataKuliah1);

        mockMvc.perform(
                patch("/courses/" + mataKuliah1.getKodeMataKuliah())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin)
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getData());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testDeleteMataKuliahSuccess() throws Exception {
        mataKuliahRepository.save(mataKuliah1);

        mockMvc.perform(
                delete("/courses/" + mataKuliah1.getKodeMataKuliah())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getData());
            assertNull(response.getErrors());
        });
    }

    @Test
    void testDeleteMataKuliahWhenIdIsNotFound() throws Exception {
        mockMvc.perform(
                delete("/courses/CSGEXXXXXX")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin)
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getData());
            assertNotNull(response.getErrors());
        });
    }
}
