package id.ac.ui.cs.advprog.hiringgo.manajemenakun.controller;

import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.*;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.entity.Admin;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.entity.Dosen;
import id.ac.ui.cs.advprog.hiringgo.entity.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/admin")
    public ResponseEntity<User> createAdmin(@RequestBody AdminDTO adminDTO) {
        User createdUser = userService.createAdmin(adminDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping("/dosen")
    public ResponseEntity<User> createDosen(@RequestBody DosenDTO dosenDTO) {
        User createdUser = userService.createDosen(dosenDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<AdminUserResponse>> getAllAdmins() {
        List<Admin> admins = userService.getAllAdmins();
        List<AdminUserResponse> responses = admins.stream()
                .map(admin -> {
                    User user = userService.getUserById(admin.getId());
                    AdminUserResponse response = new AdminUserResponse();
                    response.setId(admin.getId());
                    response.setEmail(user.getEmail());
                    response.setRole(user.getRole());
                    return response;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/dosen")
    public ResponseEntity<List<DosenUserResponse>> getAllDosens() {
        List<Dosen> dosens = userService.getAllDosens();
        List<DosenUserResponse> responses = dosens.stream()
                .map(dosen -> {
                    User user = userService.getUserById(dosen.getId());
                    DosenUserResponse response = new DosenUserResponse();
                    response.setId(dosen.getId());
                    response.setEmail(user.getEmail());
                    response.setRole(user.getRole());
                    response.setNamaLengkap(dosen.getNamaLengkap());
                    response.setNip(dosen.getNIP());
                    return response;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/mahasiswa")
    public ResponseEntity<List<MahasiswaUserResponse>> getAllMahasiswas() {
        List<Mahasiswa> mahasiswas = userService.getAllMahasiswas();
        List<MahasiswaUserResponse> responses = mahasiswas.stream()
                .map(mahasiswa -> {
                    User user = userService.getUserById(mahasiswa.getId());
                    MahasiswaUserResponse response = new MahasiswaUserResponse();
                    response.setId(mahasiswa.getId());
                    response.setEmail(user.getEmail());
                    response.setRole(user.getRole());
                    response.setNamaLengkap(mahasiswa.getNamaLengkap());
                    response.setNim(mahasiswa.getNPM());
                    return response;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<AdminUserResponse> getAdminById(@PathVariable String id) {
        Admin admin = userService.getAdminById(id);
        User user = userService.getUserById(id);

        AdminUserResponse response = new AdminUserResponse();
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/dosen/{id}")
    public ResponseEntity<DosenUserResponse> getDosenById(@PathVariable String id) {
        Dosen dosen = userService.getDosenById(id);
        User user = userService.getUserById(id);

        DosenUserResponse response = new DosenUserResponse();
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setNamaLengkap(dosen.getNamaLengkap());
        response.setNip(dosen.getNIP());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/mahasiswa/{id}")
    public ResponseEntity<MahasiswaUserResponse> getMahasiswaById(@PathVariable String id) {
        Mahasiswa mahasiswa = userService.getMahasiswaById(id);
        User user = userService.getUserById(id);

        MahasiswaUserResponse response = new MahasiswaUserResponse();
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setNamaLengkap(mahasiswa.getNamaLengkap());
        response.setNim(mahasiswa.getNPM());

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/admin/{id}")
    public ResponseEntity<User> updateAdminRole(
            @PathVariable String id,
            @RequestBody RoleUpdateDTO roleUpdateDTO) {
        User updatedUser = userService.updateUserRole(id, roleUpdateDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/dosen/{id}")
    public ResponseEntity<User> updateDosenRole(
            @PathVariable String id,
            @RequestBody RoleUpdateDTO roleUpdateDTO) {
        User updatedUser = userService.updateUserRole(id, roleUpdateDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/mahasiswa/{id}")
    public ResponseEntity<User> updateMahasiswaRole(
            @PathVariable String id,
            @RequestBody RoleUpdateDTO roleUpdateDTO) {
        User updatedUser = userService.updateUserRole(id, roleUpdateDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable String id) {
        userService.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/dosen/{id}")
    public ResponseEntity<Void> deleteDosen(@PathVariable String id) {
        userService.deleteDosen(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/mahasiswa/{id}")
    public ResponseEntity<Void> deleteMahasiswa(@PathVariable String id) {
        userService.deleteMahasiswa(id);
        return ResponseEntity.noContent().build();
    }
}