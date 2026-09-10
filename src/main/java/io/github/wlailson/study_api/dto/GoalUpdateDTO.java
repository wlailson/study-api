package io.github.wlailson.study_api.dto;

import io.github.wlailson.study_api.model.Goal;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record GoalUpdateDTO(
        @Schema( description = "ID da meta",
                example = "1",
                accessMode =
                Schema.AccessMode.READ_ONLY )
        Long id,

        @NotBlank(message = "Título é obrigatório")
        @Size( max = 100, message = "Título deve possuir no máximo 100 caracteres" )
        @Schema( description = "Título da meta de estudo", example = "Estudar Java" )
        String title,

        @NotNull(message = "Data de início é obrigatória")
        @Schema( description = "Data de início da meta",
                example = "2026-09-07" )
        LocalDate startDate,

        @NotNull(message = "Data de término é obrigatória")
        @Schema( description = "Data de término da meta",
                example = "2026-12-31" )
        LocalDate endDate) {

    public GoalUpdateDTO(Goal entity) {
        this(entity.getId(), entity.getTitle(), entity.getStartDate(), entity.getEndDate());
    }
}
