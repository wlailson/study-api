package io.github.wlailson.study_api.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
public class JwtConfig {
    @Bean
    NimbusJwtDecoder jwtDecoder(
            @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuerUri) {

        NimbusJwtDecoder decoder = NimbusJwtDecoder
                .withIssuerLocation(issuerUri)
                .validateType(false)
                .build();

        decoder.setJwtValidator(
                JwtValidators.createAtJwtValidator()
                        .issuer(issuerUri)
                        .audience("study-api")
                        .build()
        );

        return decoder;
    }
}