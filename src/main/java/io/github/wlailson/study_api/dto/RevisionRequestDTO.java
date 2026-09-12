package io.github.wlailson.study_api.dto;

import java.time.LocalDate;

public record RevisionRequestDTO(
        LocalDate scheduledDate,
        Long breakTimeInMinutes,
        Long durationInMinutes
) {
}
