package ua.opnu.pract1shydl.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.opnu.pract1shydl.dto.auth.AuthResponse;
import ua.opnu.pract1shydl.dto.auth.LoginRequest;
import ua.opnu.pract1shydl.dto.auth.RegisterRequest;
import ua.opnu.pract1shydl.dto.auth.UserPrincipal;
import ua.opnu.pract1shydl.enums.Role;
import ua.opnu.pract1shydl.exeptionmy.BadRequestException;
import ua.opnu.pract1shydl.model.UserAuth;
import ua.opnu.pract1shydl.repository.UserAuthRepository;
import ua.opnu.pract1shydl.security.JwtTokenProvider;


import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserAuthRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_Success() {
        RegisterRequest request = new RegisterRequest("newuser", "new@example.com", "pass123");

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(passwordEncoder.encode("pass123")).thenReturn("encodedPass");

        UserAuth savedUser = UserAuth.builder()
                .id(1L)
                .username("newuser")
                .email("new@example.com")
                .password("encodedPass")
                .role(Role.USER)
                .enabled(true)
                .build();

        when(userRepository.save(any(UserAuth.class))).thenReturn(savedUser);

        UserPrincipal principal = new UserPrincipal(1L, "newuser", "new@example.com", "encodedPass", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(principal);
        when(jwtTokenProvider.generateToken(principal)).thenReturn("mocked.jwt.token");

        AuthResponse response = authService.registerUser(request);

        assertEquals("mocked.jwt.token", response.getAccessToken());
        assertEquals("newuser", response.getUser().getUsername());
    }

    @Test
    void registerUser_UsernameExists_ThrowsException() {
        RegisterRequest request = new RegisterRequest("taken", "email@example.com", "pass");

        when(userRepository.existsByUsername("taken")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> authService.registerUser(request));
    }

    @Test
    void registerUser_EmailExists_ThrowsException() {
        RegisterRequest request = new RegisterRequest("freeuser", "used@example.com", "pass");

        when(userRepository.existsByUsername("freeuser")).thenReturn(false);
        when(userRepository.existsByEmail("used@example.com")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> authService.registerUser(request));
    }

    @Test
    void authenticateUser_Success() {
        LoginRequest request = new LoginRequest("user", "password");
        UserPrincipal principal = new UserPrincipal(1L, "user", "user@example.com", "pass", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(principal);
        when(jwtTokenProvider.generateToken(principal)).thenReturn("jwt.token");

        AuthResponse response = authService.authenticateUser(request);

        assertNotNull(response);
        assertEquals("jwt.token", response.getAccessToken());
        assertEquals("user", response.getUser().getUsername());
    }
}