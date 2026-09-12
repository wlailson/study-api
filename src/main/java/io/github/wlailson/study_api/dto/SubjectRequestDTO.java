package io.github.wlailson.study_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para criação ou atualização de uma disciplina")
public record SubjectRequestDTO(

        @NotBlank(message = "Nome é obrigatório")
        @Size(
                max = 100,
                message = "Nome deve possuir no máximo 100 caracteres"
        )
        @Schema(
                description = "Nome da disciplina",
                example = "Java",
                maxLength = 100,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String name
) {
}