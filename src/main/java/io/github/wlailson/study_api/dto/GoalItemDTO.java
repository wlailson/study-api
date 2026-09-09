package io.github.wlailson.study_api.dto;

import io.github.wlailson.study_api.model.GoalItem;

public record GoalItemDTO(Long id, Long targetInMinutes, SubjectDTO subject) {

    public GoalItemDTO(GoalItem entity) {
        this(
                entity.getId(),
                entity.getTargetInMinutes(),
                new SubjectDTO(entity.getSubject())
        );
    }
}
