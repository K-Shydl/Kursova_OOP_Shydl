package ua.opnu.pract1shydl.dto.auth;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private Boolean success;
    private String message;
    private T data;
    private String error;
    private LocalDateTime timestamp = LocalDateTime.now();
}
