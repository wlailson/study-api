package io.github.wlailson.study_api.dto;

import io.github.wlailson.study_api.model.Goal;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

public record GoalDTO(
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
        LocalDate endDate,

        @Schema(description = "Usuário associado à meta")
        ClientDTO client,

        @Schema(description = "Lista de itens que compõem a meta")
        List<GoalItemDTO> items,

        @Schema(description = "Tempo total estudado em minutos",
                example = "360")
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
