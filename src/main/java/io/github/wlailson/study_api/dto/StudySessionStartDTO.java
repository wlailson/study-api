package io.github.wlailson.study_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record StudySessionStartDTO(
        @NotNull(message = "ID da matéria é obrigatório")
        @Positive(message = "ID da matéria deve ser maior que zero")
        @Schema(description = "ID da matéria que será estudada",
                example = "1")
        Long subjectId
) {
}
