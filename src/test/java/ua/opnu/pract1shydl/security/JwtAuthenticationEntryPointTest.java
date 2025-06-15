package ua.opnu.pract1shydl.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.AuthenticationException;
import ua.opnu.pract1shydl.dto.auth.ApiResponse;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthenticationEntryPointTest {

    private JwtAuthenticationEntryPoint entryPoint;

    @BeforeEach
    void setUp() {
        entryPoint = new JwtAuthenticationEntryPoint();
    }

    @Test
    void commence_ShouldReturnUnauthorizedResponse() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        AuthenticationException authException = mock(AuthenticationException.class);
        when(authException.getMessage()).thenReturn("Unauthorized");

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        entryPoint.commence(request, response, authException);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");

        printWriter.flush();

        ObjectMapper mapper = new ObjectMapper();
        ApiResponse<?> actualResponse = mapper.readValue(stringWriter.toString(), ApiResponse.class);

        assertEquals("Access Denied. You need to login first.", actualResponse.getMessage());
        assertEquals("Unauthorized", actualResponse.getError());
    }
}
