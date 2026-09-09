package io.github.wlailson.study_api.dto;

import io.github.wlailson.study_api.model.GoalItem;

public record GoalItemCreateDTO(Long targetInMinutes, Long subjectId) {

    public GoalItemCreateDTO(GoalItem entity) {
        this(
                entity.getTargetInMinutes(),
                entity.getSubject().getId()
        );
    }
}
