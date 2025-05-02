package id.ac.ui.cs.advprog.hiringgo.authentication.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterMahasiswaRequest {

    @NotBlank(message = "Nama lengkap is required")
    private String namaLengkap;

    @NotBlank(message = "NPM is required")
    @Size(min = 10, max = 10, message = "NPM must be exactly 10 characters")
    private String NPM;

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;
}