package id.ac.ui.cs.advprog.hiringgo.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
