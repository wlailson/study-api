package io.github.wlailson.study_api.dto;

import io.github.wlailson.study_api.model.Goal;

import java.time.LocalDate;

public record GoalMinDTO(Long id, String title, LocalDate startDate, LocalDate endDate) {

    public GoalMinDTO(Goal entity) {
        this(entity.getId(), entity.getTitle(), entity.getStartDate(), entity.getEndDate());
    }
}
