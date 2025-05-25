package id.ac.ui.cs.advprog.hiringgo.manajemenakun.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DosenDTO {
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Password is required")
    private String password;
    @NotBlank(message = "Nama lengkap is required")
    private String namaLengkap;
    @NotBlank(message = "NIP is required")
    private String nip;
}