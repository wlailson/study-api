package io.github.wlailson.study_api.dto;

import java.time.LocalDate;
import java.util.List;

public record GoalCreateDTO(
        String title,
        LocalDate startDate,
        LocalDate endDate,
        List<GoalItemCreateDTO> items
) {
}
