package ua.opnu.pract1shydl.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import ua.opnu.pract1shydl.enums.AuthProvider;
import ua.opnu.pract1shydl.enums.Role;
import ua.opnu.pract1shydl.model.MyOAuth2User;
import ua.opnu.pract1shydl.repository.OAuth2UserRepository;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for OAuth2SuccessHandler")
class OAuth2SuccessHandlerTest {

    @Mock
    private OAuth2UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private OAuth2SuccessHandler successHandler;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @Mock
    private OAuth2User oauthUser;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        when(authentication.getPrincipal()).thenReturn(oauthUser);
        when(oauthUser.getAttribute("email")).thenReturn("testuser@example.com");
        when(oauthUser.getAttribute("name")).thenReturn("Test User");
    }

    @Test
    @DisplayName("Must create a new user if it does not exist")
    void onAuthenticationSuccess_whenNewUser_thenCreateUserAndGenerateToken() throws Exception {
        // Arrange
        String userEmail = "testuser@example.com";
        String userName = "Test User";
        String expectedToken = "new-user-jwt-token";

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        when(jwtTokenProvider.generateToken(any(MyOAuth2User.class))).thenReturn(expectedToken);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        // Act
        successHandler.onAuthenticationSuccess(request, response, authentication);

        // Assert
        ArgumentCaptor<MyOAuth2User> userCaptor = ArgumentCaptor.forClass(MyOAuth2User.class);
        verify(userRepository).save(userCaptor.capture());
        MyOAuth2User savedUser = userCaptor.getValue();

        assertEquals(userEmail, savedUser.getEmail());
        assertEquals(userName, savedUser.getName());
        assertEquals(Role.USER, savedUser.getRole());
        assertEquals(AuthProvider.GOOGLE, savedUser.getProvider());

        verify(jwtTokenProvider).generateToken(savedUser);

        printWriter.flush();
        String jsonResponse = stringWriter.toString();
        Map<String, String> responseMap = objectMapper.readValue(jsonResponse, Map.class);

        assertEquals(expectedToken, responseMap.get("token"));
        assertEquals("Bearer", responseMap.get("type"));

        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
    }

    @Test
    @DisplayName("Must update existing user")
    void onAuthenticationSuccess_whenExistingUser_thenUpdateUserAndGenerateToken() throws Exception {
        // Arrange
        String userEmail = "testuser@example.com";
        String oldName = "Old Name";
        String newName = "Test User";
        String expectedToken = "existing-user-jwt-token";

        MyOAuth2User existingUser = MyOAuth2User.builder()
                .id(1L)
                .email(userEmail)
                .name(oldName)
                .role(Role.ADMIN)
                .provider(AuthProvider.GOOGLE)
                .build();

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(existingUser));
        when(jwtTokenProvider.generateToken(any(MyOAuth2User.class))).thenReturn(expectedToken);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        // Act
        successHandler.onAuthenticationSuccess(request, response, authentication);

        // Assert
        ArgumentCaptor<MyOAuth2User> userCaptor = ArgumentCaptor.forClass(MyOAuth2User.class);
        verify(userRepository).save(userCaptor.capture());
        MyOAuth2User savedUser = userCaptor.getValue();

        assertEquals(newName, savedUser.getName());
        assertEquals(userEmail, savedUser.getEmail());
        assertEquals(Role.ADMIN, savedUser.getRole());
        assertEquals(AuthProvider.GOOGLE, savedUser.getProvider());
        assertNotEquals(oldName, savedUser.getName());

        verify(userRepository).findByEmail(userEmail);

        verify(jwtTokenProvider).generateToken(savedUser);

        printWriter.flush();
        String jsonResponse = stringWriter.toString();
        Map<String, String> responseMap = objectMapper.readValue(jsonResponse, Map.class);

        assertEquals(expectedToken, responseMap.get("token"));
        assertEquals("Bearer", responseMap.get("type"));
    }
}
