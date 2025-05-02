package id.ac.ui.cs.advprog.hiringgo.authentication.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.hiringgo.authentication.model.LoginUserRequest;
import id.ac.ui.cs.advprog.hiringgo.authentication.model.LoginUserResponse;
import id.ac.ui.cs.advprog.hiringgo.authentication.model.RegisterMahasiswaRequest;
import id.ac.ui.cs.advprog.hiringgo.entity.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.model.WebResponse;
import id.ac.ui.cs.advprog.hiringgo.repository.MahasiswaRepository;
import id.ac.ui.cs.advprog.hiringgo.repository.UserRepository;
import id.ac.ui.cs.advprog.hiringgo.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MahasiswaRepository mahasiswaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        User admin = new User();
        admin.setId("admin");
        admin.setEmail("admin@hiringgo.com");
        admin.setPassword(passwordEncoder.encode("securepassword"));
        admin.setRole("ADMIN");
        userRepository.save(admin);

        User dosen = new User();
        dosen.setId("dosen");
        dosen.setEmail("dosen@hiringgo.com");
        dosen.setPassword(passwordEncoder.encode("securepassword"));
        dosen.setRole("DOSEN");
        userRepository.save(dosen);

        User mahasiswa = new User();
        mahasiswa.setId("mahasiswa");
        mahasiswa.setEmail("mahasiswa@hiringgo.com");
        mahasiswa.setPassword(passwordEncoder.encode("securepassword"));
        mahasiswa.setRole("MAHASISWA");
        userRepository.save(mahasiswa);

        Mahasiswa mahasiswa2 = new Mahasiswa();
        mahasiswa2.setId(mahasiswa.getId());
        mahasiswa2.setNamaLengkap("Jane Doe");
        mahasiswa2.setNPM("1234567890");
        mahasiswaRepository.save(mahasiswa2);
    }

    @Test
    void testLoginEmailNotFound() throws Exception {
        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setEmail("notfound@hiringgo.com");
        loginUserRequest.setPassword("securepassword");

        mockMvc.perform(
                post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUserRequest))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<LoginUserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getData());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testLoginWrongPassword() throws Exception {
        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setEmail("admin@hiringgo.com");
        loginUserRequest.setPassword("wrongpassword");

        mockMvc.perform(
                post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUserRequest))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<LoginUserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getData());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testLoginRoleIsAdmin() throws Exception {
        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setEmail("admin@hiringgo.com");
        loginUserRequest.setPassword("securepassword");

        mockMvc.perform(
                post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUserRequest))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<LoginUserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(loginUserRequest.getEmail(), jwtUtil.extractEmail(response.getData().getToken()));
            assertEquals("ADMIN", jwtUtil.extractRole(response.getData().getToken()));
        });
    }

    @Test
    void testLoginRoleIsDosen() throws Exception {
        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setEmail("dosen@hiringgo.com");
        loginUserRequest.setPassword("securepassword");

        mockMvc.perform(
                post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUserRequest))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<LoginUserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(loginUserRequest.getEmail(), jwtUtil.extractEmail(response.getData().getToken()));
            assertEquals("DOSEN", jwtUtil.extractRole(response.getData().getToken()));
        });
    }

    @Test
    void testLoginRoleIsMahasiswa() throws Exception {
        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setEmail("mahasiswa@hiringgo.com");
        loginUserRequest.setPassword("securepassword");

        mockMvc.perform(
                post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUserRequest))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<LoginUserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(loginUserRequest.getEmail(), jwtUtil.extractEmail(response.getData().getToken()));
            assertEquals("MAHASISWA", jwtUtil.extractRole(response.getData().getToken()));
        });
    }

    @Test
    void testLoginMethodNotAllowed() throws Exception {
        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setEmail("mahasiswa@hiringgo.com");
        loginUserRequest.setPassword("securepassword");

        mockMvc.perform(
                patch("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUserRequest))
        ).andExpectAll(
                status().isMethodNotAllowed()
        ).andDo(result -> {
            WebResponse<LoginUserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getData());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testLoginWhenRequestIsNull() throws Exception {
        LoginUserRequest loginUserRequest = new LoginUserRequest();

        mockMvc.perform(
                post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUserRequest))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<LoginUserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getData());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testLoginWithEmptyRequestBody() throws Exception {
        mockMvc.perform(
                post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<LoginUserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getData());
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testRegisterMahasiswaSuccess() throws Exception {
        RegisterMahasiswaRequest registerMahasiswaRequest = new RegisterMahasiswaRequest();
        registerMahasiswaRequest.setNamaLengkap("John Doe");
        registerMahasiswaRequest.setNPM("2306123456");
        registerMahasiswaRequest.setEmail("johndoe@hiringgo.com");
        registerMahasiswaRequest.setPassword("securepassword");
        registerMahasiswaRequest.setConfirmPassword("securepassword");

        mockMvc.perform(
                post("/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerMahasiswaRequest))
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
    void testRegisterMahasiswaWithNonMatchingPasswords() throws Exception {
        RegisterMahasiswaRequest registerMahasiswaRequest = new RegisterMahasiswaRequest();
        registerMahasiswaRequest.setNamaLengkap("John Doe");
        registerMahasiswaRequest.setNPM("2306123456");
        registerMahasiswaRequest.setEmail("johndoe@hiringgo.com");
        registerMahasiswaRequest.setPassword("securepassword");
        registerMahasiswaRequest.setConfirmPassword("nonmatchingpassword");

        mockMvc.perform(
                post("/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerMahasiswaRequest))
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
    void testRegisterMahasiswaWithTakenEmail() throws Exception {
        RegisterMahasiswaRequest registerMahasiswaRequest = new RegisterMahasiswaRequest();
        registerMahasiswaRequest.setNamaLengkap("John Doe");
        registerMahasiswaRequest.setNPM("2306123456");
        registerMahasiswaRequest.setEmail("mahasiswa@hiringgo.com");
        registerMahasiswaRequest.setPassword("securepassword");
        registerMahasiswaRequest.setConfirmPassword("securepassword");

        mockMvc.perform(
                post("/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerMahasiswaRequest))
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
    void testRegisterMahasiswaWithTakenNPM() throws Exception {
        RegisterMahasiswaRequest registerMahasiswaRequest = new RegisterMahasiswaRequest();
        registerMahasiswaRequest.setNamaLengkap("John Doe");
        registerMahasiswaRequest.setNPM("1234567890");
        registerMahasiswaRequest.setEmail("johndoe@hiringgo.com");
        registerMahasiswaRequest.setPassword("securepassword");
        registerMahasiswaRequest.setConfirmPassword("securepassword");

        mockMvc.perform(
                post("/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerMahasiswaRequest))
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
    void testRegisterMahasiswaWithTooLongNPM() throws Exception {
        RegisterMahasiswaRequest registerMahasiswaRequest = new RegisterMahasiswaRequest();
        registerMahasiswaRequest.setNamaLengkap("John Doe");
        registerMahasiswaRequest.setNPM("01234567890");
        registerMahasiswaRequest.setEmail("johndoe@hiringgo.com");
        registerMahasiswaRequest.setPassword("securepassword");
        registerMahasiswaRequest.setConfirmPassword("securepassword");

        mockMvc.perform(
                post("/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerMahasiswaRequest))
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
    void testRegisterMahasiswaWhenRequestIsNull() throws Exception {
        RegisterMahasiswaRequest registerMahasiswaRequest = new RegisterMahasiswaRequest();

        mockMvc.perform(
                post("/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerMahasiswaRequest))
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
    void testRegisterMahasiswaWithEmptyRequestBody() throws Exception {
        mockMvc.perform(
                post("/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
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
}
