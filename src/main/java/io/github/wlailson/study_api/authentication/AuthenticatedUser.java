package io.github.wlailson.study_api.authentication;

import io.github.wlailson.study_api.model.User;
import io.github.wlailson.study_api.repository.UserRepository;
import io.github.wlailson.study_api.service.exceptions.ForbiddenException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUser {

    private final UserRepository repository;

    public AuthenticatedUser(UserRepository repository) {
        this.repository = repository;
    }

    public User get() {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = repository.findByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return user;
    }

    public void validateOwnership(Long userId) {

        User user = get();

        if (!user.getId().equals(userId)) {
            throw new ForbiddenException(
                    "You are not allowed to access this resource"
            );
        }
    }
}