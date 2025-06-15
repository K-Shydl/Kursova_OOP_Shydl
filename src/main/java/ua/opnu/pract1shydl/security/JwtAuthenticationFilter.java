package ua.opnu.pract1shydl.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import ua.opnu.pract1shydl.dto.auth.UserPrincipal;

import ua.opnu.pract1shydl.model.MyOAuth2User;
import ua.opnu.pract1shydl.repository.OAuth2UserRepository;
import ua.opnu.pract1shydl.service.CustomUserDetailsService;


import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final OAuth2UserRepository oauth2UserRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                String subject = tokenProvider.getUsernameFromToken(jwt);

                try {
                    Long userId = Long.parseLong(subject);
                    UserDetails userDetails = customUserDetailsService.loadUserById(userId);
                    setAuthentication(userDetails, request);
                } catch (NumberFormatException e) {
                    Optional<MyOAuth2User> oauth2User = oauth2UserRepository.findByEmail(subject);
                    if (oauth2User.isPresent()) {
                        UserDetails userDetails = UserPrincipal.create(oauth2User.get());
                        setAuthentication(userDetails, request);
                    } else {
                        throw new RuntimeException("OAuth2 user not found: " + subject);
                    }
                }
            }
        } catch (Exception ex) {
            log.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private void setAuthentication(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
