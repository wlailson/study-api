package io.github.wlailson.study_api.service;

import io.github.wlailson.study_api.dto.UserDTO;
import io.github.wlailson.study_api.dto.UserRequestDTO;
import io.github.wlailson.study_api.model.User;
import io.github.wlailson.study_api.security.JwtUtil;
import io.github.wlailson.study_api.service.exceptions.ForbiddenException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private Authentication authentication;

    private AuthService service;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void login_shouldReturnBearerToken() {

        User user = mock(User.class);

        UserRequestDTO request =
                new UserRequestDTO(
                        "maria@gmail.com",
                        "123456"
                );

        when(authenticationManager.authenticate(any(
                UsernamePasswordAuthenticationToken.class
        ))).thenReturn(authentication);

        when(authentication.getPrincipal())
                .thenReturn(user);

        when(jwtUtil.generateToken(user))
                .thenReturn("jwt-token");

        String response = service().login(request);

        assertEquals("Bearer jwt-token", response);

        verify(authenticationManager).authenticate(
                argThat(auth ->
                        auth instanceof UsernamePasswordAuthenticationToken token
                                && token.getPrincipal().equals("maria@gmail.com")
                                && token.getCredentials().equals("123456")
                )
        );

        verify(jwtUtil).generateToken(user);
    }

    @Test
    void login_shouldPropagateAuthenticationException_whenCredentialsAreInvalid() {

        UserRequestDTO request =
                new UserRequestDTO(
                        "maria@gmail.com",
                        "senha-invalida"
                );

        when(authenticationManager.authenticate(any(
                UsernamePasswordAuthenticationToken.class
        ))).thenThrow(
                new org.springframework.security.core.AuthenticationException(
                        "Credenciais inválidas"
                ) {
                }
        );

        assertThrows(
                org.springframework.security.core.AuthenticationException.class,
                () -> service().login(request)
        );

        verify(jwtUtil, never()).generateToken(any());
    }

    @Test
    void getMe_shouldReturnAuthenticatedUser() {

        User user = mock(User.class);

        setAuthenticatedUser(user);

        UserDTO response = service().getMe();

        assertNotNull(response);
    }

    @Test
    void validateOwnership_shouldNotThrow_whenUserOwnsResource() {

        User user = mock(User.class);

        when(user.getId()).thenReturn(1L);

        setAuthenticatedUser(user);

        assertDoesNotThrow(
                () -> service().validateOwnership(1L)
        );
    }

    @Test
    void validateOwnership_shouldThrowForbiddenException_whenUserDoesNotOwnResource() {

        User user = mock(User.class);

        when(user.getId()).thenReturn(1L);

        setAuthenticatedUser(user);

        assertThrows(
                ForbiddenException.class,
                () -> service().validateOwnership(2L)
        );
    }

    @Test
    void validateOwnership_shouldThrowForbiddenException_whenUserIdIsDifferent() {

        User user = mock(User.class);

        when(user.getId()).thenReturn(10L);

        setAuthenticatedUser(user);

        ForbiddenException exception = assertThrows(
                ForbiddenException.class,
                () -> service().validateOwnership(20L)
        );

        assertEquals(
                "Você não pode acessar este recurso",
                exception.getMessage()
        );
    }

    private AuthService service() {
        if (service == null) {
            service = new AuthService(
                    authenticationManager,
                    jwtUtil
            );
        }

        return service;
    }

    private void setAuthenticatedUser(User user) {

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getAuthorities()
                );

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
    }
}