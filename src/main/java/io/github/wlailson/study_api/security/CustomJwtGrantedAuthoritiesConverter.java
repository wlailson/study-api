
package io.github.wlailson.study_api.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;

public class CustomJwtGrantedAuthoritiesConverter
        implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final String ROLES_CLAIM =
            "https://study-app/roles";

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {

        List<String> roles =
                jwt.getClaimAsStringList(ROLES_CLAIM);

        if (roles == null) {
            return List.of();
        }

        Collection<GrantedAuthority> authorities = roles.stream()
                .map(role -> (GrantedAuthority)
                        new SimpleGrantedAuthority("ROLE_" + role))
                .toList();

        return authorities;
    }
}
