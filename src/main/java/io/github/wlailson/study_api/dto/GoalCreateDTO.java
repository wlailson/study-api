package io.github.wlailson.study_api.dto;

import io.github.wlailson.study_api.model.Goal;

import java.time.LocalDate;
import java.util.List;

public record GoalCreateDTO(
        String title,
        LocalDate startDate,
        LocalDate endDate,
        List<GoalItemCreateDTO> items
) {

    public GoalCreateDTO(Goal entity) {
        this(
                entity.getTitle(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getGoalItems().stream().map(GoalItemCreateDTO::new).toList()
        );
    }
}
