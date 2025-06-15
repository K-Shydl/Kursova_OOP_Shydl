package ua.opnu.pract1shydl.dto.auth;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private Long expiresIn;
    private UserDto user;
}