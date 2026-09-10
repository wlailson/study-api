package io.github.wlailson.study_api.dto;

import io.github.wlailson.study_api.model.Subject;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SubjectDTO(

        @Schema(description = "ID da disciplina",
                example = "1",
                accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 100, message = "Nome deve possuir no máximo 100 caracteres")
        @Schema(
                description = "Nome da disciplina",
                example = "Java")
        String name

) {

    public SubjectDTO(Subject entity) {
        this(
                entity != null ? entity.getId() : null,
                entity != null ? entity.getName() : null
        );
    }
}
