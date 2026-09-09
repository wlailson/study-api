package io.github.wlailson.study_api.config.customgrant;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContext;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.time.Instant;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomPasswordAuthenticationProviderTest {

    @Mock
    private OAuth2AuthorizationService authorizationService;

    @Mock
    private OAuth2TokenGenerator<OAuth2Token> tokenGenerator;

    @Mock
    private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthorizationServerContext authorizationServerContext;

    private CustomPasswordAuthenticationProvider provider;

    private RegisteredClient registeredClient;

    @BeforeEach
    void setUp() {

        provider = new CustomPasswordAuthenticationProvider(
                authorizationService,
                tokenGenerator,
                userDetailsService,
                passwordEncoder
        );

        registeredClient = RegisteredClient
                .withId("client-id")
                .clientId("client")
                .clientSecret("{noop}secret")
                .scope("ROLE_CLIENT")
                .scope("ROLE_ADMIN")
                .authorizationGrantType(
                        new AuthorizationGrantType("password")
                )
                .authorizationGrantType(
                        AuthorizationGrantType.REFRESH_TOKEN
                )
                .clientSettings(
                        ClientSettings.builder().build()
                )
                .build();

        AuthorizationServerContextHolder.setContext(
                authorizationServerContext
        );
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
        AuthorizationServerContextHolder.resetContext();
    }

    @Test
    void deveSuportarCustomPasswordAuthenticationToken() {

        assertTrue(
                provider.supports(
                        CustomPasswordAuthenticationToken.class
                )
        );
    }

    @Test
    void naoDeveSuportarOutroTipoDeAuthentication() {

        assertFalse(
                provider.supports(
                        UsernamePasswordAuthenticationToken.class
                )
        );
    }

    @Test
    void deveAutenticarUsuarioEGerarAccessToken() {

        OAuth2ClientAuthenticationToken clientPrincipal =
                authenticatedClient();

        CustomPasswordAuthenticationToken authentication =
                passwordAuthentication(clientPrincipal);

        UserDetails user = User.withUsername("maria@gmail.com")
                .password("encoded-password")
                .authorities("ROLE_CLIENT")
                .build();

        when(userDetailsService.loadUserByUsername("maria@gmail.com"))
                .thenReturn(user);

        when(passwordEncoder.matches(
                "123456",
                "encoded-password"
        )).thenReturn(true);

        OAuth2AccessToken generatedAccessToken =
                new OAuth2AccessToken(
                        OAuth2AccessToken.TokenType.BEARER,
                        "access-token",
                        Instant.now(),
                        Instant.now().plusSeconds(3600),
                        Set.of("ROLE_CLIENT")
                );

        OAuth2RefreshToken generatedRefreshToken =
                new OAuth2RefreshToken(
                        "refresh-token",
                        Instant.now(),
                        Instant.now().plusSeconds(7200)
                );

        when(tokenGenerator.generate(any(OAuth2TokenContext.class)))
                .thenReturn(generatedAccessToken)
                .thenReturn(generatedRefreshToken);

        Authentication result =
                provider.authenticate(authentication);

        assertNotNull(result);

        assertInstanceOf(
                OAuth2AccessTokenAuthenticationToken.class,
                result
        );

        OAuth2AccessTokenAuthenticationToken token =
                (OAuth2AccessTokenAuthenticationToken) result;

        assertEquals(
                "access-token",
                token.getAccessToken().getTokenValue()
        );

        assertNotNull(token.getRefreshToken());

        assertEquals(
                "refresh-token",
                token.getRefreshToken().getTokenValue()
        );

        verify(userDetailsService)
                .loadUserByUsername("maria@gmail.com");

        verify(passwordEncoder)
                .matches(
                        "123456",
                        "encoded-password"
                );

        verify(tokenGenerator, times(2))
                .generate(any(OAuth2TokenContext.class));

        verify(authorizationService)
                .save(any(OAuth2Authorization.class));
    }


    @Test
    void deveGerarRefreshTokenQuandoClientPermitir() {

        OAuth2ClientAuthenticationToken clientPrincipal =
                authenticatedClient();

        CustomPasswordAuthenticationToken authentication =
                passwordAuthentication(clientPrincipal);

        UserDetails user = User.withUsername("maria@gmail.com")
                .password("encoded-password")
                .authorities("ROLE_CLIENT")
                .build();

        when(userDetailsService.loadUserByUsername("maria@gmail.com"))
                .thenReturn(user);

        when(passwordEncoder.matches(
                "123456",
                "encoded-password"
        )).thenReturn(true);

        OAuth2AccessToken accessToken =
                new OAuth2AccessToken(
                        OAuth2AccessToken.TokenType.BEARER,
                        "access-token",
                        Instant.now(),
                        Instant.now().plusSeconds(3600),
                        Set.of("ROLE_CLIENT")
                );

        OAuth2RefreshToken refreshToken =
                new OAuth2RefreshToken(
                        "refresh-token",
                        Instant.now(),
                        Instant.now().plusSeconds(7200)
                );

        when(tokenGenerator.generate(any(OAuth2TokenContext.class)))
                .thenReturn(accessToken)
                .thenReturn(refreshToken);

        Authentication result =
                provider.authenticate(authentication);

        OAuth2AccessTokenAuthenticationToken token =
                (OAuth2AccessTokenAuthenticationToken) result;

        assertNotNull(token.getAccessToken());
        assertEquals(
                "access-token",
                token.getAccessToken().getTokenValue()
        );

        assertNotNull(token.getRefreshToken());
        assertEquals(
                "refresh-token",
                token.getRefreshToken().getTokenValue()
        );

        verify(tokenGenerator, times(2))
                .generate(any(OAuth2TokenContext.class));

        verify(authorizationService)
                .save(any(OAuth2Authorization.class));
    }

    @Test
    void naoDeveGerarRefreshTokenQuandoClientNaoPermitir() {

        registeredClient = RegisteredClient
                .withId("client-id")
                .clientId("client")
                .clientSecret("{noop}secret")
                .scope("ROLE_CLIENT")
                .authorizationGrantType(
                        new AuthorizationGrantType("password")
                )
                .build();

        OAuth2ClientAuthenticationToken clientPrincipal =
                authenticatedClient();

        CustomPasswordAuthenticationToken authentication =
                passwordAuthentication(clientPrincipal);

        UserDetails user = User.withUsername("maria@gmail.com")
                .password("encoded-password")
                .authorities("ROLE_CLIENT")
                .build();

        when(userDetailsService.loadUserByUsername("maria@gmail.com"))
                .thenReturn(user);

        when(passwordEncoder.matches(
                "123456",
                "encoded-password"
        )).thenReturn(true);

        OAuth2AccessToken accessToken =
                new OAuth2AccessToken(
                        OAuth2AccessToken.TokenType.BEARER,
                        "access-token",
                        Instant.now(),
                        Instant.now().plusSeconds(3600),
                        Set.of("ROLE_CLIENT")
                );

        when(tokenGenerator.generate(any(OAuth2TokenContext.class)))
                .thenReturn(accessToken);

        Authentication result =
                provider.authenticate(authentication);

        OAuth2AccessTokenAuthenticationToken token =
                (OAuth2AccessTokenAuthenticationToken) result;

        assertNotNull(token.getAccessToken());
        assertNull(token.getRefreshToken());

        verify(tokenGenerator, times(1))
                .generate(any(OAuth2TokenContext.class));

        verify(authorizationService)
                .save(any(OAuth2Authorization.class));
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoForEncontrado() {

        OAuth2ClientAuthenticationToken clientPrincipal =
                authenticatedClient();

        CustomPasswordAuthenticationToken authentication =
                passwordAuthentication(clientPrincipal);

        when(userDetailsService.loadUserByUsername(
                "maria@gmail.com"
        )).thenThrow(
                new org.springframework.security.core.userdetails
                        .UsernameNotFoundException("User not found")
        );

        OAuth2AuthenticationException exception =
                assertThrows(
                        OAuth2AuthenticationException.class,
                        () -> provider.authenticate(authentication)
                );

        assertEquals(
                "Invalid credentials",
                exception.getError().getErrorCode()
        );

        verify(tokenGenerator, never())
                .generate(any(OAuth2TokenContext.class));

        verify(authorizationService, never())
                .save(any());
    }

    @Test
    void deveLancarExcecaoQuandoSenhaForInvalida() {

        OAuth2ClientAuthenticationToken clientPrincipal =
                authenticatedClient();

        CustomPasswordAuthenticationToken authentication =
                passwordAuthentication(clientPrincipal);

        UserDetails user = User.withUsername("maria@gmail.com")
                .password("encoded-password")
                .authorities("ROLE_CLIENT")
                .build();

        when(userDetailsService.loadUserByUsername(
                "maria@gmail.com"
        )).thenReturn(user);

        when(passwordEncoder.matches(
                "123456",
                "encoded-password"
        )).thenReturn(false);

        OAuth2AuthenticationException exception =
                assertThrows(
                        OAuth2AuthenticationException.class,
                        () -> provider.authenticate(authentication)
                );

        assertEquals(
                "Invalid credentials",
                exception.getError().getErrorCode()
        );

        verify(tokenGenerator, never())
                .generate(any(OAuth2TokenContext.class));

        verify(authorizationService, never())
                .save(any());
    }

    @Test
    void deveLancarExcecaoQuandoUsernameForDiferenteDoUsuario() {

        OAuth2ClientAuthenticationToken clientPrincipal =
                authenticatedClient();

        CustomPasswordAuthenticationToken authentication =
                new CustomPasswordAuthenticationToken(
                        clientPrincipal,
                        Set.of(),
                        null,
                        "maria@gmail.com",
                        "123456"
                );

        UserDetails user = User.withUsername("joao@gmail.com")
                .password("encoded-password")
                .authorities("ROLE_CLIENT")
                .build();

        when(userDetailsService.loadUserByUsername(
                "maria@gmail.com"
        )).thenReturn(user);

        when(passwordEncoder.matches(
                "123456",
                "encoded-password"
        )).thenReturn(true);

        OAuth2AuthenticationException exception =
                assertThrows(
                        OAuth2AuthenticationException.class,
                        () -> provider.authenticate(authentication)
                );

        assertEquals(
                "Invalid credentials",
                exception.getError().getErrorCode()
        );

        verify(tokenGenerator, never())
                .generate(any(OAuth2TokenContext.class));

        verify(authorizationService, never())
                .save(any());
    }

    @Test
    void deveLancarExcecaoQuandoAccessTokenNaoForGerado() {

        OAuth2ClientAuthenticationToken clientPrincipal =
                authenticatedClient();

        CustomPasswordAuthenticationToken authentication =
                passwordAuthentication(clientPrincipal);

        UserDetails user = User.withUsername("maria@gmail.com")
                .password("encoded-password")
                .authorities("ROLE_CLIENT")
                .build();

        when(userDetailsService.loadUserByUsername(
                "maria@gmail.com"
        )).thenReturn(user);

        when(passwordEncoder.matches(
                "123456",
                "encoded-password"
        )).thenReturn(true);

        when(tokenGenerator.generate(any(OAuth2TokenContext.class)))
                .thenReturn(null);

        OAuth2AuthenticationException exception =
                assertThrows(
                        OAuth2AuthenticationException.class,
                        () -> provider.authenticate(authentication)
                );

        assertEquals(
                "server_error",
                exception.getError().getErrorCode()
        );

        verify(authorizationService, never())
                .save(any());
    }

    @Test
    void deveLancarExcecaoQuandoRefreshTokenGeradoForInvalido() {

        OAuth2ClientAuthenticationToken clientPrincipal =
                authenticatedClient();

        CustomPasswordAuthenticationToken authentication =
                passwordAuthentication(clientPrincipal);

        UserDetails user = User.withUsername("maria@gmail.com")
                .password("encoded-password")
                .authorities("ROLE_CLIENT")
                .build();

        when(userDetailsService.loadUserByUsername(
                "maria@gmail.com"
        )).thenReturn(user);

        when(passwordEncoder.matches(
                "123456",
                "encoded-password"
        )).thenReturn(true);

        OAuth2AccessToken accessToken =
                new OAuth2AccessToken(
                        OAuth2AccessToken.TokenType.BEARER,
                        "access-token",
                        Instant.now(),
                        Instant.now().plusSeconds(3600)
                );

        OAuth2AccessToken invalidRefreshToken =
                new OAuth2AccessToken(
                        OAuth2AccessToken.TokenType.BEARER,
                        "wrong-token",
                        Instant.now(),
                        Instant.now().plusSeconds(3600)
                );

        when(tokenGenerator.generate(any(OAuth2TokenContext.class)))
                .thenReturn(accessToken)
                .thenReturn(invalidRefreshToken);

        OAuth2AuthenticationException exception =
                assertThrows(
                        OAuth2AuthenticationException.class,
                        () -> provider.authenticate(authentication)
                );

        assertEquals(
                "server_error",
                exception.getError().getErrorCode()
        );

        verify(tokenGenerator, times(2))
                .generate(any(OAuth2TokenContext.class));

        verify(authorizationService, never())
                .save(any());
    }

    private OAuth2ClientAuthenticationToken authenticatedClient() {

        OAuth2ClientAuthenticationToken client =
                new OAuth2ClientAuthenticationToken(
                        registeredClient,
                        ClientAuthenticationMethod.CLIENT_SECRET_BASIC,
                        "secret"
                );

        SecurityContextHolder.getContext()
                .setAuthentication(client);

        return client;
    }

    private CustomPasswordAuthenticationToken passwordAuthentication(
            OAuth2ClientAuthenticationToken client) {

        return new CustomPasswordAuthenticationToken(
                client,
                Set.of("ROLE_CLIENT"),
                null,
                "maria@gmail.com",
                "123456"
        );
    }
}
