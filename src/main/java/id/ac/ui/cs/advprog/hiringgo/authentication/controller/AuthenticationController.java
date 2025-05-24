package id.ac.ui.cs.advprog.hiringgo.authentication.controller;

import id.ac.ui.cs.advprog.hiringgo.authentication.model.LoginUserRequest;
import id.ac.ui.cs.advprog.hiringgo.authentication.model.LoginUserResponse;
import id.ac.ui.cs.advprog.hiringgo.authentication.model.RegisterMahasiswaRequest;
import id.ac.ui.cs.advprog.hiringgo.entity.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.entity.Role;
import id.ac.ui.cs.advprog.hiringgo.model.WebResponse;
import id.ac.ui.cs.advprog.hiringgo.repository.MahasiswaRepository;
import id.ac.ui.cs.advprog.hiringgo.repository.UserRepository;
import id.ac.ui.cs.advprog.hiringgo.security.JwtUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
public class AuthenticationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MahasiswaRepository mahasiswaRepository;

    @Autowired
    private Validator validator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping(
            path = "/auth/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<LoginUserResponse>> login(@RequestBody(required = false) LoginUserRequest request) {

        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body is missing or invalid");
        }

        Set<ConstraintViolation<LoginUserRequest>> constraintViolations = validator.validate(request);

        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }

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
    public ResponseEntity<WebResponse<String>> register(@RequestBody(required = false) RegisterMahasiswaRequest request) {

        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body is missing or invalid");
        }

        Set<ConstraintViolation<RegisterMahasiswaRequest>> constraintViolations = validator.validate(request);

        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }

        // Check if passwords match
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password and confirm password must match");
        }

        // Check if email is already taken
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already taken");
        }

        // Check if NPM is already taken
        if (mahasiswaRepository.findByNPM(request.getNPM()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "NPM is already taken");
        }

        // Create user
        User user = new User();
        user.setId(java.util.UUID.randomUUID().toString());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.valueOf("MAHASISWA")); // Only mahasiswa role is allowed to register
        userRepository.save(user);

        // Create mahasiswa profile
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

    @PostMapping(
            path = "/auth/logout",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<String>> logout(@RequestHeader(name = "Authorization", required = false) String token) {

        if (token == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        token = token.substring(7);

        if (!jwtUtil.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        log.info("User logged out: {}", jwtUtil.extractEmail(token));

        WebResponse<String> response = WebResponse.<String>builder()
                .data("OK")
                .build();

        return ResponseEntity.ok(response);
    }
}
