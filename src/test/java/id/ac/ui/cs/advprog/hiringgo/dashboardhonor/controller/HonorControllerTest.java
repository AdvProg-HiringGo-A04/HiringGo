package id.ac.ui.cs.advprog.hiringgo.dashboardhonor.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.hiringgo.dashboardhonor.model.HonorResponse;
import id.ac.ui.cs.advprog.hiringgo.entity.Log;
import id.ac.ui.cs.advprog.hiringgo.entity.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.entity.MataKuliah;
import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.model.WebResponse;
import id.ac.ui.cs.advprog.hiringgo.repository.LogRepository;
import id.ac.ui.cs.advprog.hiringgo.repository.MahasiswaRepository;
import id.ac.ui.cs.advprog.hiringgo.repository.MataKuliahRepository;
import id.ac.ui.cs.advprog.hiringgo.repository.UserRepository;
import id.ac.ui.cs.advprog.hiringgo.security.JwtUtilImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HonorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MahasiswaRepository mahasiswaRepository;

    @Autowired
    private MataKuliahRepository mataKuliahRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtilImpl jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String userId;

    private String userId2;

    private String tokenMahasiswa;

    private Mahasiswa mahasiswa;

    private MataKuliah mataKuliah;

    private final Double honorPerJam = 27500D;

    private Double totalJam;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID().toString();
        userId2 = UUID.randomUUID().toString();

        User user = new User();
        user.setId(userId);
        user.setEmail("mahasiswa@hiringgo.com");
        user.setPassword(passwordEncoder.encode("securepassword"));
        user.setRole("MAHASISWA");
        userRepository.save(user);

        User user2 = new User();
        user2.setId(userId2);
        user2.setEmail("mahasiswa2@hiringgo.com");
        user2.setPassword(passwordEncoder.encode("securepassword"));
        user2.setRole("MAHASISWA");
        userRepository.save(user2);

        mahasiswa = new Mahasiswa();
        mahasiswa.setId(userId);
        mahasiswa.setNPM("2306275752");
        mahasiswa.setNamaLengkap("John Doe");
        mahasiswaRepository.save(mahasiswa);

        mataKuliah = new MataKuliah();
        mataKuliah.setKodeMataKuliah("CSCM602223");
        mataKuliah.setNamaMataKuliah("Pemrograman Lanjut");
        mataKuliah.setDeskripsiMataKuliah("Belajar konsep lanjutan pemrograman.");
        mataKuliah.setDosenPengampu(List.of());
        mataKuliahRepository.save(mataKuliah);

        Log log = new Log();
        log.setId(UUID.randomUUID().toString());
        log.setJudul("Log Test");
        log.setKeterangan("Keterangan log test");
        log.setKategori("Kuliah");
        log.setWaktuMulai(LocalTime.parse("08:00:00"));
        log.setWaktuSelesai(LocalTime.parse("10:00:00"));
        log.setTanggalLog(LocalDate.of(2025, 5, 22));
        log.setPesan("Ini hanya log dummy");
        log.setStatus("Selesai");
        log.setMahasiswa(mahasiswa);
        log.setMataKuliah(mataKuliah);
        log.setCreatedAt(LocalDate.of(2025, 5, 22));
        log.setUpdatedAt(LocalDate.of(2025, 5, 22));
        logRepository.save(log);

        Duration durasi = Duration.between(log.getWaktuMulai(), log.getWaktuSelesai());
        totalJam = durasi.toMinutes() / 60D;

        tokenMahasiswa = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());
    }

    @AfterEach
    void tearDown() {
        logRepository.deleteAll();
        mataKuliahRepository.deleteAll();
        mahasiswaRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testFindHonorSuccess() throws Exception {
        mockMvc.perform(
                get("/mahasiswa/" + userId + "/honors?year=2025&month=5")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenMahasiswa)
        ).andExpectAll(
            status().isOk()
        ).andDo(result -> {
            WebResponse<List<HonorResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            HonorResponse log = response.getData().getFirst();

            assertEquals(LocalDate.of(2025, 5, 1), log.getTanggalAwal());
            assertEquals(LocalDate.of(2025, 5, 31), log.getTanggalAkhir());
            assertEquals(mahasiswa.getId(), log.getMahasiswa().getId());
            assertEquals(mataKuliah.getKodeMataKuliah(), log.getMataKuliah().getKodeMataKuliah());
            assertEquals(totalJam, log.getTotalJam());
            assertEquals(honorPerJam, log.getHonorPerJam());
            assertEquals(totalJam * honorPerJam, log.getTotalPembayaran());
            assertEquals("Selesai", log.getStatus());
            assertNull(response.getErrors());
        });
    }

    @Test
    void testFindHonorWithoutParam() throws Exception {
        mockMvc.perform(
                get("/mahasiswa/" + userId + "/honors")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenMahasiswa)
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<List<HonorResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getData());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testFindHonorWhenParamMonthIsInvalid() throws Exception {
        mockMvc.perform(
                get("/mahasiswa/" + userId + "/honors?year=2025&month=13")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenMahasiswa)
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<List<HonorResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getData());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testFindHonorWhenParamYearIsInvalid() throws Exception {
        mockMvc.perform(
                get("/mahasiswa/" + userId + "/honors?year=202X&month=12")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenMahasiswa)
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<List<HonorResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getData());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testFindHonorWithoutParamYear() throws Exception {
        mockMvc.perform(
                get("/mahasiswa/" + userId + "/honors?month=5")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenMahasiswa)
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<List<HonorResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getData());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testFindHonorWithoutParamMonth() throws Exception {
        mockMvc.perform(
                get("/mahasiswa/" + userId + "/honors?year=2025")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenMahasiswa)
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<List<HonorResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getData());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testFindHonorWhenLogIsEmpty() throws Exception {
        mockMvc.perform(
                get("/mahasiswa/" + userId + "/honors?year=2025&month=1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenMahasiswa)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<HonorResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertTrue(response.getData().isEmpty());
            assertNull(response.getErrors());
        });
    }

    @Test
    void testFindHonorWhenMahasiswaTriesToAccessAnotherUsersData() throws Exception {
        mockMvc.perform(
                get("/mahasiswa/" + userId2 + "/honors?year=2025&month=5")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenMahasiswa)
        ).andExpectAll(
                status().isForbidden()
        ).andDo(result -> {
            WebResponse<List<HonorResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getData());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testFindHonorWhenUserIsNotAuthenticate() throws Exception {
        mockMvc.perform(
                get("/mahasiswa/" + userId + "/honors?year=2025&month=1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<List<HonorResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getData());
            assertNotNull(response.getErrors());
        });
    }
}
