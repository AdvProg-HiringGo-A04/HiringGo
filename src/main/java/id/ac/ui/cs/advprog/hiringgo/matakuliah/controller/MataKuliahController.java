package id.ac.ui.cs.advprog.hiringgo.matakuliah.controller;

import id.ac.ui.cs.advprog.hiringgo.entity.MataKuliah;
import id.ac.ui.cs.advprog.hiringgo.matakuliah.model.CreateMataKuliahRequest;
import id.ac.ui.cs.advprog.hiringgo.matakuliah.model.UpdateMataKuliahRequest;
import id.ac.ui.cs.advprog.hiringgo.model.WebResponse;
import id.ac.ui.cs.advprog.hiringgo.repository.MataKuliahRepository;
import id.ac.ui.cs.advprog.hiringgo.security.JwtUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
public class MataKuliahController {

    @Autowired
    private MataKuliahRepository mataKuliahRepository;

    @Autowired
    private Validator validator;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping(
            path = "/courses",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<List<MataKuliah>>> getAllCourses(
            @RequestHeader(name = "Authorization", required = false) String token) {

        if (token == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        token = token.substring(7);

        if (!jwtUtil.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        String role = jwtUtil.extractRole(token);

        if (!role.equals("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden");
        }

        List<MataKuliah> mataKuliah = mataKuliahRepository.findAll();

        WebResponse<List<MataKuliah>> response = WebResponse.<List<MataKuliah>>builder()
                .data(mataKuliah)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping(
            path = "/courses/{kodeMataKuliah}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<MataKuliah>> getCoursesByCode(
            @RequestHeader(name = "Authorization", required = false) String token,
            @PathVariable("kodeMataKuliah") String kodeMataKuliah) {

        if (token == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        token = token.substring(7);

        if (!jwtUtil.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        String role = jwtUtil.extractRole(token);

        if (!role.equals("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden");
        }

        Optional<MataKuliah> mataKuliah = mataKuliahRepository.findById(kodeMataKuliah);

        if (mataKuliah.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found");
        }

        WebResponse<MataKuliah> response = WebResponse.<MataKuliah>builder()
                .data(mataKuliah.get())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping(
            path = "/courses",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<String>> createCourses(
            @RequestHeader(name = "Authorization", required = false) String token,
            @RequestBody CreateMataKuliahRequest request) {

        if (token == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        token = token.substring(7);

        if (!jwtUtil.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        String role = jwtUtil.extractRole(token);

        if (!role.equals("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden");
        }

        Set<ConstraintViolation<CreateMataKuliahRequest>> constraintViolations = validator.validate(request);

        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }

        if (mataKuliahRepository.existsById(request.getKodeMataKuliah())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duplicate");
        }

        MataKuliah mataKuliah = new MataKuliah();
        mataKuliah.setNamaMataKuliah(request.getNamaMataKuliah());
        mataKuliah.setKodeMataKuliah(request.getKodeMataKuliah());
        mataKuliah.setDeskripsiMataKuliah(request.getDeskripsiMataKuliah());
        mataKuliah.setDosenPengampu(request.getDosenPengampu());
        mataKuliahRepository.save(mataKuliah);

        WebResponse<String> response = WebResponse.<String>builder()
                .data("Created")
                .build();

        return ResponseEntity.created(URI.create("/courses/" + mataKuliah.getKodeMataKuliah())).body(response);
    }

    @PatchMapping(
            path = "/courses/{kodeMataKuliah}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<String>> updateCourses(
            @RequestHeader(name = "Authorization", required = false) String token,
            @PathVariable("kodeMataKuliah") String kodeMataKuliah,
            @RequestBody UpdateMataKuliahRequest request) {

        if (token == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        token = token.substring(7);

        if (!jwtUtil.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        String role = jwtUtil.extractRole(token);

        if (!role.equals("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden");
        }

        Set<ConstraintViolation<UpdateMataKuliahRequest>> constraintViolations = validator.validate(request);

        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }

        Optional<MataKuliah> optionalMataKuliah = mataKuliahRepository.findByKodeMataKuliah(kodeMataKuliah);

        if (optionalMataKuliah.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found");
        }

        MataKuliah mataKuliah = optionalMataKuliah.get();

        if (request.getNamaMataKuliah() != null) {
            mataKuliah.setNamaMataKuliah(request.getNamaMataKuliah());
        }
        if (request.getDeskripsiMataKuliah() != null) {
            mataKuliah.setDeskripsiMataKuliah(request.getDeskripsiMataKuliah());
        }
        if (request.getDosenPengampu() != null) {
            mataKuliah.setDosenPengampu(request.getDosenPengampu());
        }

        mataKuliahRepository.save(mataKuliah);

        WebResponse<String> response = WebResponse.<String>builder()
                .data("OK")
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(
            path = "/courses/{kodeMataKuliah}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<String>> deleteCourses(
            @RequestHeader(name = "Authorization", required = false) String token,
            @PathVariable("kodeMataKuliah") String kodeMataKuliah) {

        if (token == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        token = token.substring(7);

        if (!jwtUtil.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        String role = jwtUtil.extractRole(token);

        if (!role.equals("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden");
        }

        if (!mataKuliahRepository.existsById(kodeMataKuliah)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found");
        }

        mataKuliahRepository.deleteById(kodeMataKuliah);

        WebResponse<String> response = WebResponse.<String>builder()
                .data("OK")
                .build();

        return ResponseEntity.ok(response);
    }
}
