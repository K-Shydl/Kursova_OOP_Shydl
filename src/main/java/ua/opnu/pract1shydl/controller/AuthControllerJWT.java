package ua.opnu.pract1shydl.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.opnu.pract1shydl.dto.auth.ApiResponse;
import ua.opnu.pract1shydl.dto.auth.AuthResponse;
import ua.opnu.pract1shydl.dto.auth.LoginRequest;
import ua.opnu.pract1shydl.dto.auth.RegisterRequest;
import ua.opnu.pract1shydl.service.AuthService;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AuthControllerJWT {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> registerUser(@Valid @RequestBody RegisterRequest request) {
        AuthResponse authResponse = authService.registerUser(request);

        ApiResponse<AuthResponse> response = ApiResponse.<AuthResponse>builder()
                .success(true)
                .message("User registered successfully")
                .data(authResponse)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> authenticateUser(@Valid @RequestBody LoginRequest request) {
        AuthResponse authResponse = authService.authenticateUser(request);

        ApiResponse<AuthResponse> response = ApiResponse.<AuthResponse>builder()
                .success(true)
                .message("User authenticated successfully")
                .data(authResponse)
                .build();

        return ResponseEntity.ok(response);
    }
}
