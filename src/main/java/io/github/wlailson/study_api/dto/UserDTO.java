package io.github.wlailson.study_api.dto;

import io.github.wlailson.study_api.model.User;

import java.io.Serializable;
import java.time.LocalDate;

public record UserDTO(Long id, String name, String email, String phone, LocalDate birthdate) implements Serializable {
    public UserDTO(User entity) {
        this(entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getBirthDate());
    }
}