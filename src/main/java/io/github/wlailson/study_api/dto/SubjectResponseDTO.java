package io.github.wlailson.study_api.dto;

import io.github.wlailson.study_api.model.Subject;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados de uma disciplina")
public record SubjectResponseDTO(

        @Schema(
                description = "ID da disciplina",
                example = "1"
        )
        Long id,

        @Schema(
                description = "Nome da disciplina",
                example = "Java"
        )
        String name
) {

    public SubjectResponseDTO(Subject entity) {
        this(
                entity.getId(),
                entity.getName()
        );
    }
}