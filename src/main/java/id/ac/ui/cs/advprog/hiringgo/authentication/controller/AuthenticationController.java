package id.ac.ui.cs.advprog.hiringgo.authentication.controller;

import id.ac.ui.cs.advprog.hiringgo.authentication.model.LoginUserRequest;
import id.ac.ui.cs.advprog.hiringgo.authentication.model.LoginUserResponse;
import id.ac.ui.cs.advprog.hiringgo.authentication.model.WebResponse;
import id.ac.ui.cs.advprog.hiringgo.authentication.repository.UserRepository;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    Validator validator;

    @PostMapping(
            path = "/auth/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<LoginUserResponse>> login(@RequestBody LoginUserRequest request) {
        // TODO: implement logic

        LoginUserResponse loginUserResponse = new LoginUserResponse();
        loginUserResponse.setToken("test"); // Ganti dengan JWT beneran

        WebResponse<LoginUserResponse> response = WebResponse.<LoginUserResponse>builder()
                .data(loginUserResponse)
                .build();

        return ResponseEntity.ok(response);
    }
}
