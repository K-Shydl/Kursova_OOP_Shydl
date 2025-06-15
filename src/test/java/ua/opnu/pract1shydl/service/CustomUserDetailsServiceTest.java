package ua.opnu.pract1shydl.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.opnu.pract1shydl.dto.auth.UserPrincipal;
import ua.opnu.pract1shydl.model.UserAuth;
import ua.opnu.pract1shydl.repository.UserAuthRepository;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    @Mock
    private UserAuthRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_UserExists_ReturnsUserPrincipal() {
        // given
        String username = "testuser";
        UserAuth user = new UserAuth();
        user.setId(1L);
        user.setUsername(username);
        user.setPassword("secret");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // when
        UserPrincipal result = (UserPrincipal) customUserDetailsService.loadUserByUsername(username);

        // then
        assertEquals(username, result.getUsername());
        verify(userRepository).findByUsername(username);
    }

    @Test
    void loadUserByUsername_UserNotFound_ThrowsException() {
        // given
        String username = "missing";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // expect
        assertThrows(
                org.springframework.security.core.userdetails.UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(username)
        );
    }

    @Test
    void loadUserById_UserExists_ReturnsUserPrincipal() {
        // given
        Long id = 1L;
        UserAuth user = new UserAuth();
        user.setId(id);
        user.setUsername("testuser");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        // when
        UserPrincipal result = (UserPrincipal) customUserDetailsService.loadUserById(id);

        // then
        assertEquals(id, result.getId());
        verify(userRepository).findById(id);
    }

    @Test
    void loadUserById_UserNotFound_ThrowsException() {
        // given
        Long id = 99L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // expect
        assertThrows(
                org.springframework.security.core.userdetails.UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserById(id)
        );
    }
}
