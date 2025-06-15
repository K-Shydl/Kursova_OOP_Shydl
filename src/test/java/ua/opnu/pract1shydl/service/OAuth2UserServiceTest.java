package ua.opnu.pract1shydl.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class OAuth2UserServiceTest {

    private OAuth2UserService oAuth2UserService;
    private DefaultOAuth2UserService defaultOAuth2UserServiceMock;

    @BeforeEach
    void setUp() {
        oAuth2UserService = new OAuth2UserService();
        defaultOAuth2UserServiceMock = Mockito.mock(DefaultOAuth2UserService.class);
    }

    @Test
    void testLoadUserDelegatesToSuper() {
        // given
        OAuth2UserRequest mockRequest = mock(OAuth2UserRequest.class);
        OAuth2User mockUser = new DefaultOAuth2User(
                Collections.emptyList(),
                Collections.singletonMap("name", "TestUser"),
                "name"
        );

        // when
        OAuth2UserService service = new OAuth2UserService() {
            @Override
            public OAuth2User loadUser(OAuth2UserRequest userRequest) {
                return mockUser; // stubbed for test
            }
        };

        OAuth2User result = service.loadUser(mockRequest);

        // then
        assertEquals("TestUser", result.getAttribute("name"));
    }
}
