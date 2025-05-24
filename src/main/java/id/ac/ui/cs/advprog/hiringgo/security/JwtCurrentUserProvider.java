package id.ac.ui.cs.advprog.hiringgo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtCurrentUserProvider implements CurrentUserProvider {

    private final JwtUtil jwtUtil;

    @Autowired
    public JwtCurrentUserProvider(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public String getCurrentUserId() {
        String token = getTokenFromRequest();
        return jwtUtil.extractId(token);
    }

    @Override
    public String getCurrentUserEmail() {
        String token = getTokenFromRequest();
        return jwtUtil.extractEmail(token);
    }

    @Override
    public String getCurrentUserRole() {
        String token = getTokenFromRequest();
        return jwtUtil.extractRole(token);
    }

    private String getTokenFromRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or missing token");
        }

        String token = bearerToken.substring(7);

        if (!jwtUtil.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }

        return token;
    }
}