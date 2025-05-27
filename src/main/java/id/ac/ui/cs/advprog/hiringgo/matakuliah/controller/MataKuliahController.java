package id.ac.ui.cs.advprog.hiringgo.matakuliah.controller;

import id.ac.ui.cs.advprog.hiringgo.entity.MataKuliah;
import id.ac.ui.cs.advprog.hiringgo.enums.Role;
import id.ac.ui.cs.advprog.hiringgo.matakuliah.mapper.MataKuliahMapper;
import id.ac.ui.cs.advprog.hiringgo.matakuliah.model.CreateMataKuliahRequest;
import id.ac.ui.cs.advprog.hiringgo.matakuliah.model.MataKuliahResponse;
import id.ac.ui.cs.advprog.hiringgo.matakuliah.model.UpdateMataKuliahRequest;
import id.ac.ui.cs.advprog.hiringgo.matakuliah.service.MataKuliahService;
import id.ac.ui.cs.advprog.hiringgo.model.WebResponse;
import id.ac.ui.cs.advprog.hiringgo.security.JwtUtil;
import id.ac.ui.cs.advprog.hiringgo.security.annotation.AllowedRoles;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.net.URI;
import java.util.List;

import static id.ac.ui.cs.advprog.hiringgo.matakuliah.mapper.MataKuliahMapper.mataKuliahToMataKuliahResponse;

@Slf4j
@RestController
public class MataKuliahController {

    @Autowired
    private MataKuliahService mataKuliahService;

    @Autowired
    private JwtUtil jwtUtil;

    @AllowedRoles({Role.ADMIN, Role.DOSEN})
    @GetMapping(
            path = "/courses",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<List<MataKuliahResponse>>> getAllCourses(
            @RequestHeader(name = "Authorization", required = false) String token) {

        List<MataKuliah> mataKuliah = mataKuliahService.findAll();

        List<MataKuliahResponse> mataKuliahResponses = mataKuliah.stream()
                .map(MataKuliahMapper::mataKuliahToMataKuliahResponse)
                .toList();

        token = token.substring(7);
        log.info("User with email '{}' and role '{}' Read all courses",
                jwtUtil.extractEmail(token), jwtUtil.extractRole(token));

        WebResponse<List<MataKuliahResponse>> response = WebResponse.<List<MataKuliahResponse>>builder()
                .data(mataKuliahResponses)
                .build();

        return ResponseEntity.ok(response);
    }

    @AllowedRoles({Role.ADMIN})
    @GetMapping(
            path = "/courses/{kodeMataKuliah}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<MataKuliahResponse>> getCoursesByCode(
            @PathVariable("kodeMataKuliah") String kodeMataKuliah,
            @RequestHeader(name = "Authorization", required = false) String token) {

        MataKuliah mataKuliah = mataKuliahService.findByKode(kodeMataKuliah);

        token = token.substring(7);
        log.info("User with email '{}' and role '{}' Read course '{}'",
                jwtUtil.extractEmail(token), jwtUtil.extractRole(token), kodeMataKuliah);

        MataKuliahResponse mataKuliahResponse = mataKuliahToMataKuliahResponse(mataKuliah);

        WebResponse<MataKuliahResponse> response = WebResponse.<MataKuliahResponse>builder()
                .data(mataKuliahResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @AllowedRoles({Role.ADMIN})
    @PostMapping(
            path = "/courses",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<String>> createCourses(
            @Valid @RequestBody CreateMataKuliahRequest request,
            @RequestHeader(name = "Authorization", required = false) String token) {

        MataKuliah mataKuliah = mataKuliahService.createMataKuliah(request);

        token = token.substring(7);
        log.info("User with email '{}' and role '{}' created course '{}'",
                jwtUtil.extractEmail(token), jwtUtil.extractRole(token), mataKuliah.getKodeMataKuliah());

        WebResponse<String> response = WebResponse.<String>builder()
                .data("Created")
                .build();

        return ResponseEntity.created(URI.create("/courses/" + mataKuliah.getKodeMataKuliah())).body(response);
    }

    @AllowedRoles({Role.ADMIN})
    @PatchMapping(
            path = "/courses/{kodeMataKuliah}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<String>> updateCourses(
            @PathVariable("kodeMataKuliah") String kodeMataKuliah,
            @Valid @RequestBody UpdateMataKuliahRequest request,
            @RequestHeader(name = "Authorization", required = false) String token) {

        mataKuliahService.updateMataKuliah(kodeMataKuliah, request);

        token = token.substring(7);
        log.info("User with email '{}' and role '{}' updated course '{}'",
                jwtUtil.extractEmail(token), jwtUtil.extractRole(token), kodeMataKuliah);

        WebResponse<String> response = WebResponse.<String>builder()
                .data("OK")
                .build();

        return ResponseEntity.ok(response);
    }

    @AllowedRoles({Role.ADMIN})
    @DeleteMapping(
            path = "/courses/{kodeMataKuliah}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<String>> deleteCourses(
            @PathVariable("kodeMataKuliah") String kodeMataKuliah,
            @RequestHeader(name = "Authorization", required = false) String token) {

        mataKuliahService.deleteMataKuliah(kodeMataKuliah);

        token = token.substring(7);
        log.info("User with email '{}' and role '{}' deleted course '{}'",
                jwtUtil.extractEmail(token), jwtUtil.extractRole(token), kodeMataKuliah);

        WebResponse<String> response = WebResponse.<String>builder()
                .data("OK")
                .build();

        return ResponseEntity.ok(response);
    }
}
