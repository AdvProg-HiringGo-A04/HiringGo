package id.ac.ui.cs.advprog.hiringgo.security;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtCurrentUserProviderTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private ServletRequestAttributes servletRequestAttributes;

    @InjectMocks
    private JwtCurrentUserProvider jwtCurrentUserProvider;

    private MockedStatic<RequestContextHolder> requestContextHolderMock;

    @BeforeEach
    void setUp() {
        requestContextHolderMock = mockStatic(RequestContextHolder.class);
    }

    @AfterEach
    void tearDown() {
        requestContextHolderMock.close();
    }

    private void setupMockRequest() {
        requestContextHolderMock.when(RequestContextHolder::getRequestAttributes)
                .thenReturn(servletRequestAttributes);
        when(servletRequestAttributes.getRequest()).thenReturn(httpServletRequest);
    }

    @Test
    void testGetCurrentUserId_Success() {
        String token = "valid.jwt.token";
        String bearerToken = "Bearer " + token;
        String expectedUserId = "user-123";

        setupMockRequest();
        when(httpServletRequest.getHeader("Authorization")).thenReturn(bearerToken);
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.extractId(token)).thenReturn(expectedUserId);

        String result = jwtCurrentUserProvider.getCurrentUserId();

        assertEquals(expectedUserId, result);
        verify(httpServletRequest).getHeader("Authorization");
        verify(jwtUtil).validateToken(token);
        verify(jwtUtil).extractId(token);
    }

    @Test
    void testGetCurrentUserId_MissingAuthorizationHeader() {
        setupMockRequest();
        when(httpServletRequest.getHeader("Authorization")).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> jwtCurrentUserProvider.getCurrentUserId());

        assertEquals("Invalid or missing token", exception.getReason());
        verify(httpServletRequest).getHeader("Authorization");
        verifyNoInteractions(jwtUtil);
    }

    @Test
    void testGetCurrentUserId_InvalidTokenFormat() {
        String invalidToken = "InvalidTokenFormat";
        setupMockRequest();
        when(httpServletRequest.getHeader("Authorization")).thenReturn(invalidToken);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> jwtCurrentUserProvider.getCurrentUserId());

        assertEquals("Invalid or missing token", exception.getReason());
        verify(httpServletRequest).getHeader("Authorization");
        verifyNoInteractions(jwtUtil);
    }

    @Test
    void testGetCurrentUserId_InvalidToken() {
        String token = "invalid.jwt.token";
        String bearerToken = "Bearer " + token;

        setupMockRequest();
        when(httpServletRequest.getHeader("Authorization")).thenReturn(bearerToken);
        when(jwtUtil.validateToken(token)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> jwtCurrentUserProvider.getCurrentUserId());

        assertEquals("Invalid token", exception.getReason());
        verify(httpServletRequest).getHeader("Authorization");
        verify(jwtUtil).validateToken(token);
        verify(jwtUtil, never()).extractId(anyString());
    }

    @Test
    void testGetCurrentUserEmail_Success() {
        String token = "valid.jwt.token";
        String bearerToken = "Bearer " + token;
        String expectedEmail = "user@example.com";

        setupMockRequest();
        when(httpServletRequest.getHeader("Authorization")).thenReturn(bearerToken);
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.extractEmail(token)).thenReturn(expectedEmail);

        String result = jwtCurrentUserProvider.getCurrentUserEmail();

        assertEquals(expectedEmail, result);
        verify(httpServletRequest).getHeader("Authorization");
        verify(jwtUtil).validateToken(token);
        verify(jwtUtil).extractEmail(token);
    }

    @Test
    void testGetCurrentUserEmail_MissingAuthorizationHeader() {
        setupMockRequest();
        when(httpServletRequest.getHeader("Authorization")).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> jwtCurrentUserProvider.getCurrentUserEmail());

        assertEquals("Invalid or missing token", exception.getReason());
        verify(httpServletRequest).getHeader("Authorization");
        verifyNoInteractions(jwtUtil);
    }

    @Test
    void testGetCurrentUserEmail_InvalidToken() {
        String token = "invalid.jwt.token";
        String bearerToken = "Bearer " + token;

        setupMockRequest();
        when(httpServletRequest.getHeader("Authorization")).thenReturn(bearerToken);
        when(jwtUtil.validateToken(token)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> jwtCurrentUserProvider.getCurrentUserEmail());

        assertEquals("Invalid token", exception.getReason());
        verify(httpServletRequest).getHeader("Authorization");
        verify(jwtUtil).validateToken(token);
        verify(jwtUtil, never()).extractEmail(anyString());
    }

    @Test
    void testGetCurrentUserRole_Success() {
        String token = "valid.jwt.token";
        String bearerToken = "Bearer " + token;
        String expectedRole = "ADMIN";

        setupMockRequest();
        when(httpServletRequest.getHeader("Authorization")).thenReturn(bearerToken);
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.extractRole(token)).thenReturn(expectedRole);

        String result = jwtCurrentUserProvider.getCurrentUserRole();

        assertEquals(expectedRole, result);
        verify(httpServletRequest).getHeader("Authorization");
        verify(jwtUtil).validateToken(token);
        verify(jwtUtil).extractRole(token);
    }

    @Test
    void testGetCurrentUserRole_MissingAuthorizationHeader() {
        setupMockRequest();
        when(httpServletRequest.getHeader("Authorization")).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> jwtCurrentUserProvider.getCurrentUserRole());

        assertEquals("Invalid or missing token", exception.getReason());
        verify(httpServletRequest).getHeader("Authorization");
        verifyNoInteractions(jwtUtil);
    }

    @Test
    void testGetCurrentUserRole_InvalidToken() {
        String token = "invalid.jwt.token";
        String bearerToken = "Bearer " + token;

        setupMockRequest();
        when(httpServletRequest.getHeader("Authorization")).thenReturn(bearerToken);
        when(jwtUtil.validateToken(token)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> jwtCurrentUserProvider.getCurrentUserRole());

        assertEquals("Invalid token", exception.getReason());
        verify(httpServletRequest).getHeader("Authorization");
        verify(jwtUtil).validateToken(token);
        verify(jwtUtil, never()).extractRole(anyString());
    }

    @Test
    void testGetTokenFromRequest_EmptyAuthorizationHeader() {
        setupMockRequest();
        when(httpServletRequest.getHeader("Authorization")).thenReturn("");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> jwtCurrentUserProvider.getCurrentUserId());

        assertEquals("Invalid or missing token", exception.getReason());
        verify(httpServletRequest).getHeader("Authorization");
        verifyNoInteractions(jwtUtil);
    }

    @Test
    void testGetTokenFromRequest_BearerWithoutSpace() {
        String invalidBearerToken = "Bearertoken";
        setupMockRequest();
        when(httpServletRequest.getHeader("Authorization")).thenReturn(invalidBearerToken);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> jwtCurrentUserProvider.getCurrentUserId());

        assertEquals("Invalid or missing token", exception.getReason());
        verify(httpServletRequest).getHeader("Authorization");
        verifyNoInteractions(jwtUtil);
    }

    @Test
    void testGetTokenFromRequest_BearerWithEmptyToken() {
        String bearerWithEmptyToken = "Bearer ";
        setupMockRequest();
        when(httpServletRequest.getHeader("Authorization")).thenReturn(bearerWithEmptyToken);
        when(jwtUtil.validateToken("")).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> jwtCurrentUserProvider.getCurrentUserId());

        assertEquals("Invalid token", exception.getReason());
        verify(httpServletRequest).getHeader("Authorization");
        verify(jwtUtil).validateToken("");
    }

    @Test
    void testGetTokenFromRequest_BearerWithValidToken() {
        String token = "valid.jwt.token";
        String bearerToken = "Bearer " + token;
        String expectedUserId = "user-123";

        setupMockRequest();
        when(httpServletRequest.getHeader("Authorization")).thenReturn(bearerToken);
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.extractId(token)).thenReturn(expectedUserId);

        String result = jwtCurrentUserProvider.getCurrentUserId();

        assertEquals(expectedUserId, result);
        verify(httpServletRequest).getHeader("Authorization");
        verify(jwtUtil).validateToken(token);
        verify(jwtUtil).extractId(token);
    }

    @Test
    void testMultipleCalls_SameToken() {
        String token = "valid.jwt.token";
        String bearerToken = "Bearer " + token;
        String expectedUserId = "user-123";
        String expectedEmail = "user@example.com";
        String expectedRole = "ADMIN";

        setupMockRequest();
        when(httpServletRequest.getHeader("Authorization")).thenReturn(bearerToken);
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.extractId(token)).thenReturn(expectedUserId);
        when(jwtUtil.extractEmail(token)).thenReturn(expectedEmail);
        when(jwtUtil.extractRole(token)).thenReturn(expectedRole);

        String userId = jwtCurrentUserProvider.getCurrentUserId();
        String userEmail = jwtCurrentUserProvider.getCurrentUserEmail();
        String userRole = jwtCurrentUserProvider.getCurrentUserRole();

        assertEquals(expectedUserId, userId);
        assertEquals(expectedEmail, userEmail);
        assertEquals(expectedRole, userRole);

        verify(httpServletRequest, times(3)).getHeader("Authorization");
        verify(jwtUtil, times(3)).validateToken(token);
        verify(jwtUtil).extractId(token);
        verify(jwtUtil).extractEmail(token);
        verify(jwtUtil).extractRole(token);
    }

    @Test
    void testGetCurrentUserId_NullRequestAttributes() {
        requestContextHolderMock.when(RequestContextHolder::getRequestAttributes).thenReturn(null);

        assertThrows(NullPointerException.class,
                () -> jwtCurrentUserProvider.getCurrentUserId());

        verifyNoInteractions(jwtUtil);
    }

    @Test
    void testGetCurrentUserId_RequestAttributesValid() {
        String token = "valid.jwt.token";
        String bearerToken = "Bearer " + token;
        String expectedUserId = "user-123";

        setupMockRequest();
        when(httpServletRequest.getHeader("Authorization")).thenReturn(bearerToken);
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.extractId(token)).thenReturn(expectedUserId);

        String result = jwtCurrentUserProvider.getCurrentUserId();

        assertEquals(expectedUserId, result);
    }

    @Test
    void testCompleteWorkflow_ValidToken() {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.sample.token";
        String bearerToken = "Bearer " + token;
        String expectedUserId = "user-123";
        String expectedEmail = "admin@example.com";
        String expectedRole = "ADMIN";

        setupMockRequest();
        when(httpServletRequest.getHeader("Authorization")).thenReturn(bearerToken);
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.extractId(token)).thenReturn(expectedUserId);
        when(jwtUtil.extractEmail(token)).thenReturn(expectedEmail);
        when(jwtUtil.extractRole(token)).thenReturn(expectedRole);

        assertEquals(expectedUserId, jwtCurrentUserProvider.getCurrentUserId());
        assertEquals(expectedEmail, jwtCurrentUserProvider.getCurrentUserEmail());
        assertEquals(expectedRole, jwtCurrentUserProvider.getCurrentUserRole());

        verify(httpServletRequest, times(3)).getHeader("Authorization");
        verify(jwtUtil, times(3)).validateToken(token);
        verify(jwtUtil).extractId(token);
        verify(jwtUtil).extractEmail(token);
        verify(jwtUtil).extractRole(token);
    }

    @Test
    void testTokenExtraction_ExactSubstring() {
        String actualToken = "actualTokenValue123";
        String bearerToken = "Bearer " + actualToken;
        String expectedUserId = "user-456";

        setupMockRequest();
        when(httpServletRequest.getHeader("Authorization")).thenReturn(bearerToken);
        when(jwtUtil.validateToken(actualToken)).thenReturn(true);
        when(jwtUtil.extractId(actualToken)).thenReturn(expectedUserId);

        String result = jwtCurrentUserProvider.getCurrentUserId();

        assertEquals(expectedUserId, result);
        verify(jwtUtil).validateToken(actualToken);
        verify(jwtUtil).extractId(actualToken);
    }
}