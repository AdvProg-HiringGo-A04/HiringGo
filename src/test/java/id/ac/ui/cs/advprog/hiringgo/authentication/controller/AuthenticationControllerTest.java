package id.ac.ui.cs.advprog.hiringgo.authentication.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.hiringgo.authentication.entity.User;
import id.ac.ui.cs.advprog.hiringgo.authentication.model.LoginUserRequest;
import id.ac.ui.cs.advprog.hiringgo.authentication.model.LoginUserResponse;
import id.ac.ui.cs.advprog.hiringgo.authentication.model.WebResponse;
import id.ac.ui.cs.advprog.hiringgo.authentication.repository.UserRepository;
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
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    PasswordEncoder passwordEncoder;

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
    void testLoginEmptyUsernameOrPassword() throws Exception {
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
}
