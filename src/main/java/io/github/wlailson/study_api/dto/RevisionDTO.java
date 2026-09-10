package io.github.wlailson.study_api.dto;

import io.github.wlailson.study_api.model.Revision;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record RevisionDTO(

        @Schema(description = "ID da revisão",
                example = "1")
        Long id,

        @Schema(description = "Data programada para a revisão",
                example = "2026-09-14")
        LocalDate date

) {

    public RevisionDTO(Revision entity) {
        this(entity.getId(), entity.getDate());
    }
}
