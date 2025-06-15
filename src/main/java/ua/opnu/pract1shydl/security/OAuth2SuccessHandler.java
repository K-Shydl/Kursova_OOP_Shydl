package ua.opnu.pract1shydl.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import ua.opnu.pract1shydl.enums.AuthProvider;
import ua.opnu.pract1shydl.enums.Role;
import ua.opnu.pract1shydl.model.MyOAuth2User;
import ua.opnu.pract1shydl.repository.OAuth2UserRepository;


import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final OAuth2UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");

        MyOAuth2User user = userRepository.findByEmail(email).orElseGet(() -> MyOAuth2User.builder()
                .email(email)
                .name(name)
                .role(Role.USER)
                .provider(AuthProvider.GOOGLE)
                .build());

        user.setName(name);
        userRepository.save(user);

        String token = jwtTokenProvider.generateToken(user);

        // Повертаємо токен як JSON, щоб фронт отримав його і міг зберегти
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        new ObjectMapper().writeValue(response.getWriter(), Map.of(
                "token", token,
                "type", "Bearer"
        ));

    }
}