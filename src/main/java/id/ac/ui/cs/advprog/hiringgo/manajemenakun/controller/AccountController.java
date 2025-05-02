package id.ac.ui.cs.advprog.hiringgo.manajemenakun.controller;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Account;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.AccountData;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Role;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final UserService service;

    public AccountController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestParam Role role,
                                                 @RequestBody AccountData data) {
        Account created = service.createAccount(role, data);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> list = service.findAll();
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<Void> updateRole(@PathVariable String id,
                                           @RequestParam String requesterId,
                                           @RequestParam Role newRole) {
        service.updateRole(id, requesterId, newRole);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String id,
                                              @RequestParam String requesterId) {
        service.deleteAccount(id, requesterId);
        return ResponseEntity.noContent().build();
    }
}