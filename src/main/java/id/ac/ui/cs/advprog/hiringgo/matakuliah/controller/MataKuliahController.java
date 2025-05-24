package id.ac.ui.cs.advprog.hiringgo.matakuliah.controller;

import id.ac.ui.cs.advprog.hiringgo.entity.MataKuliah;
import id.ac.ui.cs.advprog.hiringgo.enums.Role;
import id.ac.ui.cs.advprog.hiringgo.matakuliah.mapper.MataKuliahMapper;
import id.ac.ui.cs.advprog.hiringgo.matakuliah.model.CreateMataKuliahRequest;
import id.ac.ui.cs.advprog.hiringgo.matakuliah.model.MataKuliahResponse;
import id.ac.ui.cs.advprog.hiringgo.matakuliah.model.UpdateMataKuliahRequest;
import id.ac.ui.cs.advprog.hiringgo.matakuliah.service.MataKuliahService;
import id.ac.ui.cs.advprog.hiringgo.model.WebResponse;
import id.ac.ui.cs.advprog.hiringgo.security.annotation.AllowedRoles;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

import static id.ac.ui.cs.advprog.hiringgo.matakuliah.mapper.MataKuliahMapper.mataKuliahToMataKuliahResponse;

@RestController
public class MataKuliahController {

    @Autowired
    private MataKuliahService mataKuliahService;

    @AllowedRoles({Role.ADMIN})
    @GetMapping(
            path = "/courses",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<List<MataKuliahResponse>>> getAllCourses() {

        List<MataKuliah> mataKuliah = mataKuliahService.findAll();

        List<MataKuliahResponse> mataKuliahResponses = mataKuliah.stream()
                .map(MataKuliahMapper::mataKuliahToMataKuliahResponse)
                .toList();

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
            @PathVariable("kodeMataKuliah") String kodeMataKuliah) {

        MataKuliah mataKuliah = mataKuliahService.findByKode(kodeMataKuliah);

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
            @Valid @RequestBody CreateMataKuliahRequest request) {

        MataKuliah mataKuliah = mataKuliahService.createMataKuliah(request);

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
            @Valid @RequestBody UpdateMataKuliahRequest request) {

        mataKuliahService.updateMataKuliah(kodeMataKuliah, request);

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
            @PathVariable("kodeMataKuliah") String kodeMataKuliah) {

        mataKuliahService.deleteMataKuliah(kodeMataKuliah);

        WebResponse<String> response = WebResponse.<String>builder()
                .data("OK")
                .build();

        return ResponseEntity.ok(response);
    }
}
