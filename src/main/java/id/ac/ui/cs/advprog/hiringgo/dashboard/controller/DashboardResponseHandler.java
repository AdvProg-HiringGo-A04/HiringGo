package id.ac.ui.cs.advprog.hiringgo.dashboard.controller;

import id.ac.ui.cs.advprog.hiringgo.dashboard.service.DashboardService;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.service.UserService;
import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.security.JwtUtil;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class DashboardResponseHandler {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final int BEARER_PREFIX_LENGTH = 7;

    public static ResponseEntity<?> handleDashboardRequest(
            String token,
            JwtUtil jwtUtil,
            UserService userService,
            DashboardService dashboardService,
            Logger log) {

        try {
            TokenValidationResult validationResult = validateToken(token, log);
            if (!validationResult.isValid()) {
                return validationResult.getErrorResponse();
            }

            UserExtractionResult userResult = extractUser(
                    validationResult.getJwt(), jwtUtil, userService, log
            );
            if (!userResult.isValid()) {
                return userResult.getErrorResponse();
            }

            return generateSuccessResponse(userResult.getUser(), dashboardService, log);

        } catch (IllegalArgumentException e) {
            log.error("Invalid argument when getting dashboard: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body("Invalid request: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error when getting dashboard: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve dashboard");
        }
    }

    private static TokenValidationResult validateToken(String token, Logger log) {
        if (token == null || !token.startsWith(BEARER_PREFIX)) {
            log.warn("Unauthorized access attempt to dashboard - no token provided");
            return TokenValidationResult.invalid(
                    ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token required")
            );
        }

        String jwt = token.substring(BEARER_PREFIX_LENGTH);
        return TokenValidationResult.valid(jwt);
    }

    private static UserExtractionResult extractUser(
            String jwt, JwtUtil jwtUtil, UserService userService, Logger log) {

        String userId = jwtUtil.extractId(jwt);
        String userRole = jwtUtil.extractRole(jwt);

        if (userId == null || userRole == null) {
            log.warn("Invalid token - missing user ID or role");
            return UserExtractionResult.invalid(
                    ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token")
            );
        }

        User user = userService.getUserById(userId);
        log.debug("Dashboard request from user: {} with role: {}", userId, userRole);

        return UserExtractionResult.valid(user);
    }

    private static ResponseEntity<?> generateSuccessResponse(
            User user, DashboardService dashboardService, Logger log) {

        Object statistics = dashboardService.getStatisticsForUser(user);
        log.debug("Dashboard statistics retrieved for user role: {}", user.getRole());

        return ResponseEntity.ok(statistics);
    }

    private static class TokenValidationResult {
        private final boolean valid;
        private final String jwt;
        private final ResponseEntity<?> errorResponse;

        private TokenValidationResult(boolean valid, String jwt, ResponseEntity<?> errorResponse) {
            this.valid = valid;
            this.jwt = jwt;
            this.errorResponse = errorResponse;
        }

        public static TokenValidationResult valid(String jwt) {
            return new TokenValidationResult(true, jwt, null);
        }

        public static TokenValidationResult invalid(ResponseEntity<?> errorResponse) {
            return new TokenValidationResult(false, null, errorResponse);
        }

        public boolean isValid() { return valid; }
        public String getJwt() { return jwt; }
        public ResponseEntity<?> getErrorResponse() { return errorResponse; }
    }

    private static class UserExtractionResult {
        private final boolean valid;
        private final User user;
        private final ResponseEntity<?> errorResponse;

        private UserExtractionResult(boolean valid, User user, ResponseEntity<?> errorResponse) {
            this.valid = valid;
            this.user = user;
            this.errorResponse = errorResponse;
        }

        public static UserExtractionResult valid(User user) {
            return new UserExtractionResult(true, user, null);
        }

        public static UserExtractionResult invalid(ResponseEntity<?> errorResponse) {
            return new UserExtractionResult(false, null, errorResponse);
        }

        public boolean isValid() { return valid; }
        public User getUser() { return user; }
        public ResponseEntity<?> getErrorResponse() { return errorResponse; }
    }
}
