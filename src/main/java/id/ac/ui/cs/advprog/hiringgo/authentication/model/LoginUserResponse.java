package id.ac.ui.cs.advprog.hiringgo.authentication.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginUserResponse {

    private String token;
}
