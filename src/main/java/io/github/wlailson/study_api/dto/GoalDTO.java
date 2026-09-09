package io.github.wlailson.study_api.dto;

import io.github.wlailson.study_api.model.Goal;

import java.time.LocalDate;
import java.util.List;

public record GoalDTO(
        Long id,
        String title,
        LocalDate startDate,
        LocalDate endDate,
        ClientDTO client,
        List<GoalItemDTO> items,
        Long studied
) {

    public GoalDTO(Goal entity) {
        this(
                entity.getId(),
                entity.getTitle(),
                entity.getStartDate(),
                entity.getEndDate(),
                new ClientDTO(entity.getUser()),
                entity.getGoalItems().stream().map(GoalItemDTO::new).toList(),
                0L
        );
    }

    public GoalDTO(Goal entity, Long studied) {
        this(
                entity.getId(),
                entity.getTitle(),
                entity.getStartDate(),
                entity.getEndDate(),
                new ClientDTO(entity.getUser()),
                entity.getGoalItems().stream().map(GoalItemDTO::new).toList(),
                studied
        );
    }

    public Long getTotal() {
        return items.stream().mapToLong(GoalItemDTO::targetInMinutes).sum();
    }

    public boolean getCompleted() {
        return studied >= getTotal();
    }

    public Integer getProgressPercent() {
        if (getTotal() == 0) {
            return 0;
        }

        return (int) ((studied * 100.0) / getTotal());
    }
}
