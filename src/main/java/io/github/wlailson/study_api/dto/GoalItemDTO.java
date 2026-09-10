package io.github.wlailson.study_api.dto;

import io.github.wlailson.study_api.model.GoalItem;
import io.swagger.v3.oas.annotations.media.Schema;

public record GoalItemDTO(

        @Schema(description = "ID do item da meta",
                example = "1")
        Long id,

        @Schema(description = "Tempo alvo do item em minutos",
                example = "120")
        Long targetInMinutes,

        @Schema(description = "Disciplina associada ao item")
        SubjectDTO subject

) {

    public GoalItemDTO(GoalItem entity) {
        this(
                entity.getId(),
                entity.getTargetInMinutes(),
                new SubjectDTO(entity.getSubject())
        );
    }
}
