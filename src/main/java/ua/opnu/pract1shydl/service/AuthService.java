package ua.opnu.pract1shydl.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.opnu.pract1shydl.dto.auth.*;
import ua.opnu.pract1shydl.enums.Role;
import ua.opnu.pract1shydl.exeptionmy.BadRequestException;
import ua.opnu.pract1shydl.model.UserAuth;
import ua.opnu.pract1shydl.repository.UserAuthRepository;
import ua.opnu.pract1shydl.security.JwtTokenProvider;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserAuthRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @Transactional
    public AuthResponse registerUser(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username is already taken!");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email address already in use!");
        }

        UserAuth user = UserAuth.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .enabled(true)
                .build();

        UserAuth savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getUsername());

        return authenticateAndGenerateToken(request.getUsername(), request.getPassword());
    }

    public AuthResponse authenticateUser(LoginRequest request) {
        return authenticateAndGenerateToken(request.getUsername(), request.getPassword());
    }

    private AuthResponse authenticateAndGenerateToken(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String jwt = tokenProvider.generateToken(userPrincipal);

        UserDto userDto = UserDto.builder()
                .id(userPrincipal.getId())
                .username(userPrincipal.getUsername())
                .email(userPrincipal.getEmail())
                .role(Role.valueOf(userPrincipal.getAuthorities().iterator().next().getAuthority().substring(5)))
                .build();

        return AuthResponse.builder()
                .accessToken(jwt)
                .tokenType("Bearer")
                .expiresIn(3600L) // 1 hour
                .user(userDto)
                .build();
    }
}
