package id.ac.ui.cs.advprog.hiringgo.manajemenakun.model;

import id.ac.ui.cs.advprog.hiringgo.enums.Role;
import lombok.Data;

@Data
public class BaseUserResponse {
    private String id;
    private String email;
    private Role role;
}