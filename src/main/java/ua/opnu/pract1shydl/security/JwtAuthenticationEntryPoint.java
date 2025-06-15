package ua.opnu.pract1shydl.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import ua.opnu.pract1shydl.dto.auth.ApiResponse;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException {
        log.error("Responding with unauthorized error. Message - {}", e.getMessage());

        httpServletResponse.setContentType("application/json");
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ApiResponse<Object> response = ApiResponse.builder()
                .success(false)
                .message("Access Denied. You need to login first.")
                .error("Unauthorized")
                .build();

        ObjectMapper mapper = new ObjectMapper();
        httpServletResponse.getWriter().write(mapper.writeValueAsString(response));
    }
}
