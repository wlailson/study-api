package io.github.wlailson.study_api.dto;

import io.github.wlailson.study_api.model.User;
import io.swagger.v3.oas.annotations.media.Schema;

public record ClientDTO(

        @Schema(description = "ID do cliente",
                example = "1")
        Long id,

        @Schema(description = "Nome do cliente",
                example = "Maria Silva")
        String name

) {

    public ClientDTO(User entity) {
        this(entity.getId(), entity.getName());
    }
}
