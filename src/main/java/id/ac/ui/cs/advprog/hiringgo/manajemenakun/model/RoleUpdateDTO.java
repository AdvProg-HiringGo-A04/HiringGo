package id.ac.ui.cs.advprog.hiringgo.manajemenakun.model;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleUpdateDTO {
    @NotNull(message = "Role is required")
    private Role role;
    private String namaLengkap;
    private String nip;
    private String npm;
}