package io.github.wlailson.study_api.config.customgrant;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomPasswordAuthenticationConverterTest {

    private CustomPasswordAuthenticationConverter converter;

    @BeforeEach
    void setUp() {
        converter = new CustomPasswordAuthenticationConverter();

        OAuth2ClientAuthenticationToken client =
                mock(OAuth2ClientAuthenticationToken.class);

        when(client.isAuthenticated()).thenReturn(true);

        SecurityContextHolder.getContext()
                .setAuthentication(client);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void deveRetornarNullQuandoGrantTypeNaoForPassword() {

        MockHttpServletRequest request = new MockHttpServletRequest();

        request.setParameter("grant_type", "authorization_code");

        Authentication result = converter.convert(request);

        assertNull(result);
    }

    @Test
    void deveLancarExcecaoQuandoUsernameNaoForInformado() {

        MockHttpServletRequest request = new MockHttpServletRequest();

        request.setParameter("grant_type", "password");
        request.setParameter("password", "123456");

        OAuth2AuthenticationException exception =
                assertThrows(
                        OAuth2AuthenticationException.class,
                        () -> converter.convert(request)
                );

        assertEquals(
                "invalid_request",
                exception.getError().getErrorCode()
        );
    }

    @Test
    void deveLancarExcecaoQuandoUsernameEstiverVazio() {

        MockHttpServletRequest request = new MockHttpServletRequest();

        request.setParameter("grant_type", "password");
        request.setParameter("username", "   ");
        request.setParameter("password", "123456");

        OAuth2AuthenticationException exception =
                assertThrows(
                        OAuth2AuthenticationException.class,
                        () -> converter.convert(request)
                );

        assertEquals(
                "invalid_request",
                exception.getError().getErrorCode()
        );
    }

    @Test
    void deveLancarExcecaoQuandoUsernameForDuplicado() {

        MockHttpServletRequest request = new MockHttpServletRequest();

        request.setParameter("grant_type", "password");
        request.addParameter("username", "maria@gmail.com");
        request.addParameter("username", "joao@gmail.com");
        request.setParameter("password", "123456");

        OAuth2AuthenticationException exception =
                assertThrows(
                        OAuth2AuthenticationException.class,
                        () -> converter.convert(request)
                );

        assertEquals(
                "invalid_request",
                exception.getError().getErrorCode()
        );
    }

    @Test
    void deveLancarExcecaoQuandoPasswordNaoForInformado() {

        MockHttpServletRequest request = new MockHttpServletRequest();

        request.setParameter("grant_type", "password");
        request.setParameter("username", "maria@gmail.com");

        OAuth2AuthenticationException exception =
                assertThrows(
                        OAuth2AuthenticationException.class,
                        () -> converter.convert(request)
                );

        assertEquals(
                "invalid_request",
                exception.getError().getErrorCode()
        );
    }

    @Test
    void deveLancarExcecaoQuandoPasswordEstiverVazio() {

        MockHttpServletRequest request = new MockHttpServletRequest();

        request.setParameter("grant_type", "password");
        request.setParameter("username", "maria@gmail.com");
        request.setParameter("password", "   ");

        OAuth2AuthenticationException exception =
                assertThrows(
                        OAuth2AuthenticationException.class,
                        () -> converter.convert(request)
                );

        assertEquals(
                "invalid_request",
                exception.getError().getErrorCode()
        );
    }

    @Test
    void deveLancarExcecaoQuandoPasswordForDuplicado() {

        MockHttpServletRequest request = new MockHttpServletRequest();

        request.setParameter("grant_type", "password");
        request.setParameter("username", "maria@gmail.com");
        request.addParameter("password", "123456");
        request.addParameter("password", "654321");

        OAuth2AuthenticationException exception =
                assertThrows(
                        OAuth2AuthenticationException.class,
                        () -> converter.convert(request)
                );

        assertEquals(
                "invalid_request",
                exception.getError().getErrorCode()
        );
    }

    @Test
    void deveLancarExcecaoQuandoScopeForDuplicado() {

        MockHttpServletRequest request = new MockHttpServletRequest();

        request.setParameter("grant_type", "password");
        request.setParameter("username", "maria@gmail.com");
        request.setParameter("password", "123456");
        request.addParameter("scope", "read");
        request.addParameter("scope", "write");

        OAuth2AuthenticationException exception =
                assertThrows(
                        OAuth2AuthenticationException.class,
                        () -> converter.convert(request)
                );

        assertEquals(
                "invalid_request",
                exception.getError().getErrorCode()
        );
    }

    @Test
    void deveCriarTokenComParametrosValidos() {

        MockHttpServletRequest request = new MockHttpServletRequest();

        request.setParameter("grant_type", "password");
        request.setParameter("username", "maria@gmail.com");
        request.setParameter("password", "123456");

        Authentication result = converter.convert(request);

        assertNotNull(result);
        assertInstanceOf(
                CustomPasswordAuthenticationToken.class,
                result
        );

        CustomPasswordAuthenticationToken token =
                (CustomPasswordAuthenticationToken) result;

        assertEquals("maria@gmail.com", token.getUsername());
        assertEquals("123456", token.getPassword());
        assertTrue(token.getScopes().isEmpty());
    }

    @Test
    void deveCriarTokenComScopes() {

        MockHttpServletRequest request = new MockHttpServletRequest();

        request.setParameter("grant_type", "password");
        request.setParameter("username", "maria@gmail.com");
        request.setParameter("password", "123456");
        request.setParameter("scope", "read write");

        Authentication result = converter.convert(request);

        CustomPasswordAuthenticationToken token =
                (CustomPasswordAuthenticationToken) result;

        assertEquals(
                Set.of("read", "write"),
                token.getScopes()
        );
    }

    @Test
    void deveAceitarParametroExtra() {

        MockHttpServletRequest request = new MockHttpServletRequest();

        request.setParameter("grant_type", "password");
        request.setParameter("username", "maria@gmail.com");
        request.setParameter("password", "123456");
        request.setParameter("device", "mobile");

        Authentication result = converter.convert(request);

        CustomPasswordAuthenticationToken token =
                (CustomPasswordAuthenticationToken) result;

        assertEquals(
                "mobile",
                token.getAdditionalParameters().get("device")
        );
    }
}