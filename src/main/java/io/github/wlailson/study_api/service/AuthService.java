package io.github.wlailson.study_api.service;

import io.github.wlailson.study_api.dto.UserDTO;
import io.github.wlailson.study_api.model.User;
import io.github.wlailson.study_api.repository.UserRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Cacheable(value = "user" + "#id", key = "#externalId")
    public UserDTO getMe() {

        Jwt jwt = getCurrentJwt();

        String externalId = jwt.getSubject();
        String email = jwt.getClaimAsString("email");
        String name = jwt.getClaimAsString("given_name");

        User user = userRepository
                .findByExternalId(externalId)
                .orElseGet(() -> createUser(externalId, email, name));

        return new UserDTO(user);
    }

    public String getCurrentExternalId() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return jwt.getSubject();
    }

    public Jwt getCurrentJwt() {
        return (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


    @Transactional(readOnly = true)
    @Cacheable(value = "currentUser")
    public User getCurrentUser() {
        String externalId = getCurrentExternalId();
        return userRepository.findByExternalId(externalId).orElseThrow(() -> new IllegalStateException("Usuário não encontrado"));
    }

    private User createUser(
            String externalId,
            String email,
            String name
    ) {
        User user = new User();

        user.setExternalId(externalId);
        user.setEmail(email);
        user.setName(name);

        return userRepository.save(user);
    }
}