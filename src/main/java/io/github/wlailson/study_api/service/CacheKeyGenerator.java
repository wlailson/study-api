package io.github.wlailson.study_api.service;

import org.springframework.stereotype.Component;

@Component("cacheKeyGenerator")
public class CacheKeyGenerator {

    private final AuthService authService;

    public CacheKeyGenerator(AuthService authService) {
        this.authService = authService;
    }

    public String findById(Long id) {
        return authService.getCurrentUser().getId() + ":id:" + id;
    }

    public String findUserByEmail(String email) {
        return "email:" + email.trim().toLowerCase();
    }
}