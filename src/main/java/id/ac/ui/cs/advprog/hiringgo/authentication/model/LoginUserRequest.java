package id.ac.ui.cs.advprog.hiringgo.authentication.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginUserRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
