package io.github.wlailson.study_api.service;

import io.github.wlailson.study_api.dto.UserDTO;
import io.github.wlailson.study_api.model.User;
import io.github.wlailson.study_api.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = repository.findByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException(
                    "Invalid username or password"
            );
        }

        return user;
    }

    protected User authenticated() {

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

    @Transactional(readOnly = true)
    public UserDTO getMe() {
        User entity = authenticated();
        return new UserDTO(entity);
    }
}