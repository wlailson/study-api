package io.github.wlailson.study_api.dto;

import io.github.wlailson.study_api.model.GoalItem;

public record GoalItemUpdateDTO(Long targetInMinutes, Long subjectId) {

    public GoalItemUpdateDTO(GoalItem entity) {
        this(
                entity.getTargetInMinutes(), entity.getSubject().getId()
        );
    }
}
