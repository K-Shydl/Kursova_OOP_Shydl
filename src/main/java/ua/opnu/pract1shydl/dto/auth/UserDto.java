package ua.opnu.pract1shydl.dto.auth;

import lombok.*;
import ua.opnu.pract1shydl.enums.Role;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private Role role;
    private LocalDateTime createdAt;
}
