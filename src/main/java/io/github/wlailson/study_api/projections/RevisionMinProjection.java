package io.github.wlailson.study_api.projections;

import io.github.wlailson.study_api.model.RevisionStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public interface RevisionMinProjection {
    @Schema(
            description = "ID da revisão",
            example = "1"
    )
    Long getId();

    @Schema(
            description = "Status da revisão",
            example = "PENDING"
    )
    RevisionStatus getStatus();

    @Schema(
            description = "Data programada para a revisão",
            example = "2026-09-15"
    )
    LocalDate getScheduledDate();
}
