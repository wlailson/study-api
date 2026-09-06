package io.github.wlailson.study_api.dto;

import io.github.wlailson.study_api.model.Role;
import io.github.wlailson.study_api.model.User;

import java.time.LocalDate;
import java.util.List;

public record UserDTO(Long id, String name, String email, String phone, LocalDate birthdate, List<String> roles) {

    public UserDTO(User entity) {
        this(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getBirthDate(),
                entity.getRoles().stream().map(Role::getAuthority).toList()
        );
    }
}