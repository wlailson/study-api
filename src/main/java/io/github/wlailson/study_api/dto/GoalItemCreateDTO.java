package io.github.wlailson.study_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record GoalItemCreateDTO(

        @NotNull(message = "Tempo alvo é obrigatório")
        @Positive(message = "Tempo alvo deve ser maior que zero")
        @Schema(description = "Tempo alvo do item em minutos", example = "120")
        Long targetInMinutes,

        @NotNull(message = "ID da disciplina é obrigatório")
        @Positive(message = "ID da disciplina deve ser maior que zero")
        @Schema(description = "ID da disciplina associada ao item", example = "1")
        Long subjectId

) {

}
