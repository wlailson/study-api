
package io.github.wlailson.study_api.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${cors.origins}")
    private String corsOrigins;

    @Bean
    @Order(1)
    @Profile("test")
    SecurityFilterChain h2SecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/h2-console/**")
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers ->
                        headers.frameOptions(frame -> frame.sameOrigin())
                )
                .authorizeHttpRequests(auth ->
                        auth.anyRequest().permitAll()
                )
                .build();
    }

    @Bean
    @Order(2)
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(
                                        customJwtAuthenticationConverter()
                                )
                        )
                )
                .build();
    }

    @Bean
    JwtAuthenticationConverter customJwtAuthenticationConverter() {
        JwtAuthenticationConverter converter =
                new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(
                new CustomJwtGrantedAuthoritiesConverter()
        );

        return converter;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        String[] origins = corsOrigins.split(",");

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOriginPatterns(Arrays.asList(origins));
        config.setAllowedMethods(Arrays.asList(
                "GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS"
        ));
        config.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type"
        ));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
