package io.github.wlailson.study_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RevisionUpdateDTO(
        @NotNull(message = "Data da revisão é obrigatória")
        @Schema(description = "Nova data programada para a revisão",
                example = "2026-09-14")
        LocalDate date

) {
}
