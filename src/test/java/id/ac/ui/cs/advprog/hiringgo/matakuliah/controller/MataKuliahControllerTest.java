package id.ac.ui.cs.advprog.hiringgo.matakuliah.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.hiringgo.matakuliah.entity.Dosen;
import id.ac.ui.cs.advprog.hiringgo.matakuliah.entity.MataKuliah;
import id.ac.ui.cs.advprog.hiringgo.matakuliah.model.CreateMataKuliahRequest;
import id.ac.ui.cs.advprog.hiringgo.matakuliah.model.MataKuliahResponse;
import id.ac.ui.cs.advprog.hiringgo.matakuliah.model.UpdateMataKuliahRequest;
import id.ac.ui.cs.advprog.hiringgo.matakuliah.model.WebResponse;
import id.ac.ui.cs.advprog.hiringgo.matakuliah.repository.MataKuliahRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    private MataKuliahRepository mataKuliahRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateMataKuliahRequest createMataKuliahRequest;

    @BeforeEach
    void setUp() {
        mataKuliahRepository.deleteAll();

        createMataKuliahRequest = new CreateMataKuliahRequest();
        createMataKuliahRequest.setKodeMataKuliah("CSCM602223");
        createMataKuliahRequest.setNamaMataKuliah("Pemrograman Lanjut");
        createMataKuliahRequest.setDeskripsiMataKuliah("Belajar konsep lanjutan pemrograman.");
        createMataKuliahRequest.setDosenPengampu(List.of(new Dosen()));
    }

    @Test
    void testCreateMataKuliahSuccess() throws Exception {
        mockMvc.perform(
                post("/courses")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMataKuliahRequest))
        ).andExpectAll(
                status().isCreated()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
        });
    }

    @Test
    void testCreateMataKuliahBadRequest() throws Exception {
        CreateMataKuliahRequest createMataKuliahRequest = new CreateMataKuliahRequest();

        mockMvc.perform(
                post("/courses")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMataKuliahRequest))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testCreateMataKuliahDuplicate() throws Exception {
        MataKuliah mataKuliah = new MataKuliah();
        mataKuliah.setKodeMataKuliah("CSCM602223");
        mataKuliah.setNamaMataKuliah("Pemrograman Lanjut");
        mataKuliah.setDeskripsiMataKuliah("Belajar konsep lanjutan pemrograman.");
        mataKuliahRepository.save(mataKuliah);

        mockMvc.perform(
                post("/courses")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMataKuliahRequest))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testCreateMataKuliahWithoutDosen() throws Exception {
        createMataKuliahRequest.setDosenPengampu(null);

        mockMvc.perform(
                post("/courses")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMataKuliahRequest))
        ).andExpectAll(
                status().isCreated()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
        });
    }

    @Test
    void testGetMataKuliahSuccess() throws Exception {
        MataKuliah mataKuliah = new MataKuliah();
        mataKuliah.setKodeMataKuliah("CSCM602223");
        mataKuliah.setNamaMataKuliah("Pemrograman Lanjut");
        mataKuliah.setDeskripsiMataKuliah("Belajar konsep lanjutan pemrograman.");
        mataKuliahRepository.save(mataKuliah);

        mockMvc.perform(
                get("/courses")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<MataKuliahResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(mataKuliah.getKodeMataKuliah(), response.getData().getFirst().getKodeMataKuliah());
            assertEquals(mataKuliah.getNamaMataKuliah(), response.getData().getFirst().getNamaMataKuliah());
            assertEquals(mataKuliah.getDeskripsiMataKuliah(), response.getData().getFirst().getDeskripsiMataKuliah());
            assertEquals(mataKuliah.getDosenPengampu(), response.getData().getFirst().getDosenPengampu());
        });
    }

    @Test
    void testGetMataKuliahEmpty() throws Exception {
        mockMvc.perform(
                get("/courses")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
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
        MataKuliah mataKuliah = new MataKuliah();
        mataKuliah.setKodeMataKuliah("CSCM602223");
        mataKuliah.setNamaMataKuliah("Pemrograman Lanjut");
        mataKuliah.setDeskripsiMataKuliah("Belajar konsep lanjutan pemrograman.");
        mataKuliahRepository.save(mataKuliah);

        mockMvc.perform(
                get("/courses/" + mataKuliah.getKodeMataKuliah())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<MataKuliahResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(mataKuliah.getKodeMataKuliah(), response.getData().getKodeMataKuliah());
            assertEquals(mataKuliah.getNamaMataKuliah(), response.getData().getNamaMataKuliah());
            assertEquals(mataKuliah.getDeskripsiMataKuliah(), response.getData().getDeskripsiMataKuliah());
            assertEquals(mataKuliah.getDosenPengampu(), response.getData().getDosenPengampu());
        });
    }

    @Test
    void testGetMataKuliahNotFound() throws Exception {
        mockMvc.perform(
                get("/courses/CSCM602223")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getErrors());
        });
    }
}
