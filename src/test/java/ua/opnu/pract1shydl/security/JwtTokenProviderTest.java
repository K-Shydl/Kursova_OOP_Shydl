package ua.opnu.pract1shydl.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import ua.opnu.pract1shydl.enums.AuthProvider;
import ua.opnu.pract1shydl.enums.Role;
import ua.opnu.pract1shydl.model.MyOAuth2User;


import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests for JwtTokenProvider")
class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    // Use test values for the secret and token lifetime
    private final String testSecret = "aVeryLongAndSecureSecretKeyForTestingPurposesThatIsAtLeast256BitsLong";
    private final int testExpirationInMs = 60000; // 60 seconds

    @BeforeEach
    void setUp() {
        // Create an instance of our class before each test
        jwtTokenProvider = new JwtTokenProvider();

        // Inject values into private fields that are normally populated by Spring
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret", testSecret);
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpirationInMs", testExpirationInMs);

        // Manually call the @PostConstruct method, as the Spring context is not started
        jwtTokenProvider.init();
    }

    @Test
    @DisplayName("Should generate a valid token for OAuth2UserEntity")
    void generateToken_forOAuth2User_shouldCreateValidToken() {
        // Arrange
        MyOAuth2User user = MyOAuth2User.builder()
                .email("test@example.com")
                .name("Test User")
                .role(Role.USER)
                .provider(AuthProvider.GOOGLE)
                .build();

        // Act
        String token = jwtTokenProvider.generateToken(user);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());

        // Use the public method to get claims, instead of accessing the private key.
        Claims claims = jwtTokenProvider.getAllClaimsFromToken(token);

        assertEquals("test@example.com", claims.getSubject());
        assertEquals("ROLE_USER", claims.get("role", String.class));
        assertEquals("GOOGLE", claims.get("provider", String.class));
        assertEquals("Test User", claims.get("name", String.class));
    }


    @Test
    @DisplayName("Should return false for a malformed token string")
    void validateToken_withMalformedToken_shouldReturnFalse() {
        // Arrange
        String malformedToken = "this.is.not.a.valid.jwt.token";

        // Act
        boolean isValid = jwtTokenProvider.validateToken(malformedToken);

        // Assert
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should return false for a null or empty token")
    void validateToken_withEmptyOrNullToken_shouldReturnFalse() {
        // Act & Assert
        assertFalse(jwtTokenProvider.validateToken(""));
        assertFalse(jwtTokenProvider.validateToken(null));
    }
}