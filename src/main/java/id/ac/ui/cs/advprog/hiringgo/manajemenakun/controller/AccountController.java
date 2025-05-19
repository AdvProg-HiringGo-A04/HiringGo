package id.ac.ui.cs.advprog.hiringgo.manajemenakun.controller;

import id.ac.ui.cs.advprog.hiringgo.model.WebResponse;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.AccountData;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Users;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Role;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.service.UserService;
import id.ac.ui.cs.advprog.hiringgo.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final UserService service;
    private final JwtUtil jwtUtil;

    public AccountController(UserService service, JwtUtil jwtUtil) {
        this.service = service;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<WebResponse<Users>> createAccount(
            @RequestParam Role role,
            @RequestBody AccountData data,
            @RequestHeader(name = "Authorization", required = false) String token) {

        if (token == null || !token.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        String extractedRole = jwtUtil.extractRole(token.substring(7));
        if (!Role.ADMIN.name().equals(extractedRole)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden");
        }

        Users created = service.createAccount(role, data);
        WebResponse<Users> resp = WebResponse.<Users>builder()
                .data(created)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @GetMapping
    public ResponseEntity<WebResponse<List<Users>>> getAllAccounts(
            @RequestHeader(name = "Authorization", required = false) String token) {
        // Optional: check token if needed
        List<Users> list = service.findAll();
        WebResponse<List<Users>> resp = WebResponse.<List<Users>>builder()
                .data(list)
                .build();
        return ResponseEntity.ok(resp);
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<Void> updateRole(
            @PathVariable String id,
            @RequestParam String requesterId,
            @RequestParam Role newRole,
            @RequestBody AccountData data) {
        service.updateRole(id, requesterId, newRole, data);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(
            @PathVariable String id,
            @RequestParam String requesterId) {
        service.deleteAccount(id, requesterId);
        return ResponseEntity.noContent().build();
    }
}
