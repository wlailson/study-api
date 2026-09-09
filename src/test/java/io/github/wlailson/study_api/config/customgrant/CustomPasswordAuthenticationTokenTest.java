package io.github.wlailson.study_api.config.customgrant;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomPasswordAuthenticationTokenTest {

    @Test
    void deveCriarTokenComDadosInformados() {

        Authentication clientPrincipal =
                new UsernamePasswordAuthenticationToken(
                        "client",
                        null
                );

        CustomPasswordAuthenticationToken token =
                new CustomPasswordAuthenticationToken(
                        clientPrincipal,
                        Set.of("read", "write"),
                        Map.of("device", "mobile"),
                        "maria@gmail.com",
                        "123456"
                );

        assertEquals(
                "maria@gmail.com",
                token.getUsername()
        );

        assertEquals(
                "123456",
                token.getPassword()
        );

        assertEquals(
                Set.of("read", "write"),
                token.getScopes()
        );

        assertEquals(
                "mobile",
                token.getAdditionalParameters()
                        .get("device")
        );

        assertEquals(
                clientPrincipal,
                token.getPrincipal()
        );
    }

    @Test
    void deveCriarTokenComScopesVaziosQuandoScopesForemNull() {

        Authentication clientPrincipal =
                new UsernamePasswordAuthenticationToken(
                        "client",
                        null
                );

        CustomPasswordAuthenticationToken token =
                new CustomPasswordAuthenticationToken(
                        clientPrincipal,
                        null,
                        null,
                        "maria@gmail.com",
                        "123456"
                );

        assertNotNull(token.getScopes());
        assertTrue(token.getScopes().isEmpty());
    }

    @Test
    void deveCopiarScopesRecebidos() {

        Authentication clientPrincipal =
                new UsernamePasswordAuthenticationToken(
                        "client",
                        null
                );

        Set<String> scopes = new HashSet<>();
        scopes.add("read");

        CustomPasswordAuthenticationToken token =
                new CustomPasswordAuthenticationToken(
                        clientPrincipal,
                        scopes,
                        null,
                        "maria@gmail.com",
                        "123456"
                );

        scopes.add("write");

        assertEquals(
                Set.of("read"),
                token.getScopes()
        );
    }

    @Test
    void naoDevePermitirAlterarScopesDoToken() {

        Authentication clientPrincipal =
                new UsernamePasswordAuthenticationToken(
                        "client",
                        null
                );

        CustomPasswordAuthenticationToken token =
                new CustomPasswordAuthenticationToken(
                        clientPrincipal,
                        Set.of("read"),
                        null,
                        "maria@gmail.com",
                        "123456"
                );

        assertThrows(
                UnsupportedOperationException.class,
                () -> token.getScopes().add("write")
        );
    }
}