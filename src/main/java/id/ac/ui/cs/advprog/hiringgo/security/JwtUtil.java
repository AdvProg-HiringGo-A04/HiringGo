package id.ac.ui.cs.advprog.hiringgo.security;

public interface JwtUtil {

    String generateToken(String id, String email, String role);

    String extractId(String token);

    String extractEmail(String token);

    String extractRole(String token);

    boolean validateToken(String token);

    String extractId(String token);
}
