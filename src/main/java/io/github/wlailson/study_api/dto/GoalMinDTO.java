package io.github.wlailson.study_api.dto;

import io.github.wlailson.study_api.model.Goal;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record GoalMinDTO(

        @Schema(description = "ID da meta",
                example = "1")
        Long id,

        @Schema(description = "Título da meta de estudo",
                example = "Estudar Java")
        String title,

        @Schema(description = "Data de início da meta",
                example = "2026-09-07")
        LocalDate startDate,

        @Schema(description = "Data de término da meta",
                example = "2026-12-31")
        LocalDate endDate

) {

    public GoalMinDTO(Goal entity) {
        this(entity.getId(), entity.getTitle(), entity.getStartDate(), entity.getEndDate());
    }
}
