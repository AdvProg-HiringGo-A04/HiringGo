package id.ac.ui.cs.advprog.hiringgo.periksalog.service;

import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.enums.Role;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.service.UserService;
import id.ac.ui.cs.advprog.hiringgo.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private static final String TOKEN_REQUIRED_MESSAGE = "Token required";
    private static final String INVALID_TOKEN_MESSAGE = "Invalid token";

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public User authenticateUser(String token) {
        validateToken(token);

        try {
            String jwt = extractJwtFromHeader(token);
            String userId = jwtUtil.extractId(jwt);
            String userRole = jwtUtil.extractRole(jwt);

            validateUserCredentials(userId, userRole);

            User user = userService.getUserById(userId);
            log.debug("Authenticated user: {} with role: {}", userId, userRole);

            return user;
        } catch (Exception e) {
            log.error("Token validation failed", e);
            throw new IllegalArgumentException(INVALID_TOKEN_MESSAGE);
        }
    }

    public boolean isDosenUser(User user) {
        return user != null && Role.DOSEN.equals(user.getRole());
    }

    private void validateToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            log.warn("Unauthorized access attempt - no token provided");
            throw new IllegalArgumentException(TOKEN_REQUIRED_MESSAGE);
        }
    }

    private String extractJwtFromHeader(String token) {
        return token.substring(7);
    }

    private void validateUserCredentials(String userId, String userRole) {
        if (userId == null || userRole == null) {
            log.warn("Invalid token - missing user ID or role");
            throw new IllegalArgumentException(INVALID_TOKEN_MESSAGE);
        }
    }
}