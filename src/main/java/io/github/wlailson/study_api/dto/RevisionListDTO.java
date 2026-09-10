package io.github.wlailson.study_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record RevisionListDTO(

        @Schema(description = "ID da revisão",
                example = "1")
        Long id,

        @Schema(description = "Nome da disciplina associada à revisão",
                example = "Java")
        String subjectName,

        @Schema(description = "Data programada para a revisão",
                example = "2026-09-14")
        LocalDate date
) {
}
