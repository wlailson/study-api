package io.github.wlailson.study_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record GoalCreateDTO(
        @NotBlank(message = "Título é obrigatório")
        @Size( max = 100, message = "Título deve possuir no máximo 100 caracteres" )
        @Schema( description = "Título da meta de estudo",
                example = "Estudar Java" )
        String title,

        @NotNull(message = "Data de início é obrigatória")
        @Schema( description = "Data de início da meta",
                example = "2026-09-07" )
        LocalDate startDate,

        @NotNull(message = "Data de término é obrigatória")
        @Schema( description = "Data de término da meta",
                example = "2026-12-31" )
        LocalDate endDate,

        @Valid @Schema( description = "Lista de itens que compõem a meta" )
        List<GoalItemCreateDTO> items
) {
}
