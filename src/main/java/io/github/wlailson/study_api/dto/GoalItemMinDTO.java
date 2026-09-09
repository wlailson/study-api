package io.github.wlailson.study_api.dto;

import io.github.wlailson.study_api.model.GoalItem;

public record GoalItemMinDTO(Long targetInMinutes, String subjectName) {

    public GoalItemMinDTO(GoalItem entity) {
        this(
                entity.getTargetInMinutes(),
                entity.getSubject().getName()
        );
    }
}
