package io.github.wlailson.study_api.service;

import io.github.wlailson.study_api.dto.UserDTO;
import io.github.wlailson.study_api.dto.UserRequestDTO;
import io.github.wlailson.study_api.model.User;
import io.github.wlailson.study_api.security.JwtUtil;
import io.github.wlailson.study_api.service.exceptions.ForbiddenException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Transactional(readOnly = true)
    public UserDTO getMe(){
        return new UserDTO(getCurrentUser());
    }

    public String login(UserRequestDTO dto) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                dto.email(),
                                dto.password()
                        )
                );

        User user = (User) authentication.getPrincipal();

        return "Bearer " + jwtUtil.generateToken(user);
    }

    protected User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    protected void validateOwnership(Long userId) {
        User authenticatedUser = getCurrentUser();
        if (!authenticatedUser.getId().equals(userId)) {
            throw new ForbiddenException("Você não pode acessar este recurso");
        }
    }
}

