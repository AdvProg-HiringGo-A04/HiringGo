package id.ac.ui.cs.advprog.hiringgo.authentication.controller;

import id.ac.ui.cs.advprog.hiringgo.authentication.model.LoginUserRequest;
import id.ac.ui.cs.advprog.hiringgo.authentication.model.LoginUserResponse;
import id.ac.ui.cs.advprog.hiringgo.authentication.model.RegisterMahasiswaRequest;
import id.ac.ui.cs.advprog.hiringgo.entity.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.enums.Role;
import id.ac.ui.cs.advprog.hiringgo.model.WebResponse;
import id.ac.ui.cs.advprog.hiringgo.repository.MahasiswaRepository;
import id.ac.ui.cs.advprog.hiringgo.repository.UserRepository;
import id.ac.ui.cs.advprog.hiringgo.security.JwtUtil;
import id.ac.ui.cs.advprog.hiringgo.security.annotation.AllowedRoles;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Slf4j
@RestController
public class AuthenticationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MahasiswaRepository mahasiswaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping(
            path = "/auth/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<LoginUserResponse>> login(
            @Valid @RequestBody LoginUserRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole().name());

        LoginUserResponse loginUserResponse = new LoginUserResponse();
        loginUserResponse.setToken(token);

        log.info("Authentication successful for user: {}", user.getEmail());

        WebResponse<LoginUserResponse> response = WebResponse.<LoginUserResponse>builder()
                .data(loginUserResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping(
            path = "/auth/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<String>> register(
            @Valid @RequestBody RegisterMahasiswaRequest request) {

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password and confirm password must match");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already taken");
        }

        if (mahasiswaRepository.findByNPM(request.getNPM()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "NPM is already taken");
        }

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.MAHASISWA);
        userRepository.save(user);

        Mahasiswa mahasiswa = new Mahasiswa();
        mahasiswa.setId(user.getId());
        mahasiswa.setNamaLengkap(request.getNamaLengkap());
        mahasiswa.setNPM(request.getNPM());
        mahasiswaRepository.save(mahasiswa);

        log.info("New user registered with email: {}", user.getEmail());

        WebResponse<String> response = WebResponse.<String>builder()
                .data("Registration successful")
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @AllowedRoles({Role.ADMIN, Role.DOSEN, Role.MAHASISWA})
    @PostMapping(
            path = "/auth/logout",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<String>> logout(
            @RequestHeader(name = "Authorization", required = false) String token) {

        log.info("User logged out: {}", jwtUtil.extractEmail(token.substring(7)));

        WebResponse<String> response = WebResponse.<String>builder()
                .data("OK")
                .build();

        return ResponseEntity.ok(response);
    }
}
