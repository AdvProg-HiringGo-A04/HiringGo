//package id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import id.ac.ui.cs.advprog.hiringgo.entity.Dosen;
//import id.ac.ui.cs.advprog.hiringgo.entity.MataKuliah;
//import id.ac.ui.cs.advprog.hiringgo.entity.User;
//import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.dto.LowonganForm;
//import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.entity.Lowongan;
//import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.entity.PendaftarLowongan;
//import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.repository.LowonganRepository;
//import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.service.LowonganService;
//import id.ac.ui.cs.advprog.hiringgo.repository.DosenRepository;
//import id.ac.ui.cs.advprog.hiringgo.repository.MataKuliahRepository;
//import id.ac.ui.cs.advprog.hiringgo.repository.UserRepository;
//import id.ac.ui.cs.advprog.hiringgo.security.JwtUtil;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//import java.util.UUID;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class LowonganControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private DosenRepository dosenRepository;
//
//    @Autowired
//    private MataKuliahRepository mataKuliahRepository;
//
//    @Autowired
//    private LowonganRepository lowonganRepository;
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private String tokenDosen;
//
//    private Lowongan lowongan;
//
//    @BeforeEach
//    void setUp() {
//        lowonganRepository.deleteAll();
//        mataKuliahRepository.deleteAll();
//        dosenRepository.deleteAll();
//        userRepository.deleteAll();
//
//        // Create and save User and Dosen
//        String dosenId = UUID.randomUUID().toString();
//        User dosenUser = new User();
//        dosenUser.setId(dosenId);
//        dosenUser.setEmail("dosen@hiringgo.com");
//        dosenUser.setPassword("securepassword");
//        dosenUser.setRole("DOSEN");
//        userRepository.save(dosenUser);
//
//        Dosen dosen = new Dosen();
//        dosen.setId(dosenId);
//        dosen.setNIP("198403262023012008");
//        dosen.setNamaLengkap("John Doe");
//        dosenRepository.save(dosen);
//
//        MataKuliah mk = new MataKuliah();
//        mk.setKodeMataKuliah("PL");
//        mk.setNamaMataKuliah("Pemrograman Lanjut");
//        mk.setDeskripsiMataKuliah("Deskripsi PL");
//        mk.setDosenPengampu(List.of(dosen));
//        mataKuliahRepository.save(mk);
//
//        lowongan = Lowongan.builder()
//                .id(UUID.randomUUID().toString())
//                .mataKuliah(mk)
//                .tahunAjaran("2024")
//                .semester("Ganjil")
//                .jumlahDibutuhkan(2)
//                .build();
//        lowonganRepository.save(lowongan);
//
//        tokenDosen = jwtUtil.generateToken("dosen", dosenUser.getEmail(), "DOSEN");
//    }
//
//    @AfterEach
//    void tearDown() {
//        lowonganRepository.deleteAll();
//        mataKuliahRepository.deleteAll();
//        dosenRepository.deleteAll();
//        userRepository.deleteAll();
//    }
//
//    @Test
//    void testGetAllLowongan_returnsList() throws Exception {
//        mockMvc.perform(get("/lowongan")
//                        .header("Authorization", "Bearer " + tokenDosen)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data[0].mataKuliah.kodeMataKuliah").value("PL"))
//                .andExpect(jsonPath("$.data[0].semester").value("Ganjil"))
//                .andExpect(jsonPath("$.data[0].jumlahDibutuhkan").value(2))
//                .andExpect(jsonPath("$.errors").doesNotExist());
//    }
//
////     @Test
////     void testCreateLowongan_success() throws Exception {
////         LowonganForm form = LowonganForm.builder()
////                 .mataKuliah("PL")
////                 .tahunAjaran("2024/2025")
////                 .semester("Ganjil")
////                 .jumlahAsistenDibutuhkan(2)
////                 .build();
//
////         Lowongan dummyLowongan = Lowongan.builder()
////                 .id(UUID.randomUUID())
////                 .mataKuliah(form.getMataKuliah())
////                 .tahunAjaran(form.getTahunAjaran())
////                 .semester(form.getSemester())
////                 .jumlahDibutuhkan(form.getJumlahAsistenDibutuhkan())
////                 .build();
//
////         when(lowonganService.createLowongan(any(LowonganForm.class))).thenReturn(dummyLowongan);
//
////         mockMvc.perform(post("/api/lowongan/create")
////                 .contentType("application/json")
////                 .content("{\"mataKuliah\": \"PL\", \"tahunAjaran\": \"2024/2025\", \"semester\": \"Ganjil\", \"jumlahAsistenDibutuhkan\": 2}"))
////                 .andExpect(status().isCreated())
////                 .andExpect(jsonPath("$.data.id").value(dummyLowongan.getId().toString()))
////                 .andExpect(jsonPath("$.errors").isEmpty());
////     }
//
////     @Test
////     void testUpdateLowongan_success() throws Exception {
////         UUID id = UUID.randomUUID();
////         Lowongan updated = Lowongan.builder()
////                 .id(id)
////                 .mataKuliah("Algo")
////                 .tahunAjaran("2024")
////                 .semester("Genap")
////                 .jumlahDibutuhkan(3)
////                 .build();
//
////         when(lowonganService.updateLowongan(eq(id), any(LowonganForm.class)))
////                 .thenReturn(updated);
//
////         mockMvc.perform(put("/api/lowongan/" + id)
////                 .contentType("application/json")
////                 .content("""
////                     {
////                         "mataKuliah": "Algo",
////                         "tahunAjaran": "2024",
////                         "semester": "Genap",
////                         "jumlahAsistenDibutuhkan": 3
////                     }
////                 """))
////                 .andExpect(status().isOk())
////                 .andExpect(jsonPath("$.data.mataKuliah").value("Algo"))
////                 .andExpect(jsonPath("$.errors").isEmpty());
////     }
//
////     @Test
////     void testGetPendaftar_returnsList() throws Exception {
////         UUID lowonganId = UUID.randomUUID();
////         List<PendaftarLowongan> dummyPendaftar = List.of(
////             PendaftarLowongan.builder()
////                 .id(UUID.randomUUID())
////                 .diterima(false)
////                 .ipk(3.75)
////                 .jumlahSks(90)
////                 .lowongan(Lowongan.builder().id(lowonganId).build())
////                 .build()
////         );
//
////         when(lowonganService.getPendaftarByLowongan(lowonganId)).thenReturn(dummyPendaftar);
//
////         mockMvc.perform(get("/api/lowongan/" + lowonganId + "/pendaftar"))
////             .andExpect(status().isOk())
////             .andExpect(jsonPath("$.data[0].diterima").value(false))
////             .andExpect(jsonPath("$.data[0].ipk").value(3.75))
////             .andExpect(jsonPath("$.errors").isEmpty());
////     }
//
////     @Test
////     void testSetStatusPendaftar_returnsOk() throws Exception {
////         UUID lowonganId = UUID.randomUUID();
////         String npm = "12345678";
//
////         mockMvc.perform(post("/api/lowongan/" + lowonganId + "/pendaftar/" + npm)
////                         .param("diterima", "true"))
////                 .andExpect(status().isOk())
////                 .andExpect(jsonPath("$.data").doesNotExist())  // since your response is empty (Void)
////                 .andExpect(jsonPath("$.errors").isEmpty());
//
////         verify(lowonganService).setStatusPendaftar(lowonganId, npm, true);
////     }
//
//}
