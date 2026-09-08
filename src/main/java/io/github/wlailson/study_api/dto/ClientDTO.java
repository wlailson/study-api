package io.github.wlailson.study_api.dto;

import io.github.wlailson.study_api.model.User;

public record ClientDTO(Long id, String name) {

    public ClientDTO(User entity){
       this(entity.getId(), entity.getName());
    }
}
