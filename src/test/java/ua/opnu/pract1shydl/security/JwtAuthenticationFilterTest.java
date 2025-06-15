package ua.opnu.pract1shydl.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import ua.opnu.pract1shydl.dto.auth.UserPrincipal;
import ua.opnu.pract1shydl.enums.Role;
import ua.opnu.pract1shydl.model.MyOAuth2User;
import ua.opnu.pract1shydl.repository.OAuth2UserRepository;
import ua.opnu.pract1shydl.service.CustomUserDetailsService;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    private JwtTokenProvider tokenProvider;
    private CustomUserDetailsService userDetailsService;
    private OAuth2UserRepository oauth2UserRepository;
    private JwtAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        tokenProvider = mock(JwtTokenProvider.class);
        userDetailsService = mock(CustomUserDetailsService.class);
        oauth2UserRepository = mock(OAuth2UserRepository.class);
        filter = new JwtAuthenticationFilter(tokenProvider, userDetailsService, oauth2UserRepository);
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_validJwt_userId() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        String jwt = "valid.jwt.token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(tokenProvider.validateToken(jwt)).thenReturn(true);
        when(tokenProvider.getUsernameFromToken(jwt)).thenReturn("123");

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getAuthorities()).thenReturn(null);
        when(userDetailsService.loadUserById(123L)).thenReturn(userDetails);

        filter.doFilterInternal(request, response, chain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(chain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_validJwt_emailOauth2() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        String jwt = "valid.jwt.token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(tokenProvider.validateToken(jwt)).thenReturn(true);
        when(tokenProvider.getUsernameFromToken(jwt)).thenReturn("user@example.com");

        MyOAuth2User entity = new MyOAuth2User();
        entity.setEmail("user@example.com");
        entity.setRole(Role.USER); //

        UserDetails userDetails = UserPrincipal.create(entity);

        when(oauth2UserRepository.findByEmail("user@example.com")).thenReturn(Optional.of(entity));

        filter.doFilterInternal(request, response, chain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(chain).doFilter(request, response);
    }


    @Test
    void doFilterInternal_invalidToken_shouldSkip() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer invalid.token");
        when(tokenProvider.validateToken("invalid.token")).thenReturn(false);

        filter.doFilterInternal(request, response, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(chain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_missingToken_shouldSkip() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn(null);

        filter.doFilterInternal(request, response, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(chain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_oauthUserNotFound_logsError() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        String jwt = "valid.jwt.token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(tokenProvider.validateToken(jwt)).thenReturn(true);
        when(tokenProvider.getUsernameFromToken(jwt)).thenReturn("missing@example.com");
        when(oauth2UserRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        filter.doFilterInternal(request, response, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(chain).doFilter(request, response);
    }
}
