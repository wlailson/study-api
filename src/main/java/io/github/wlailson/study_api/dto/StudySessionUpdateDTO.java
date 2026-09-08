package io.github.wlailson.study_api.dto;

import io.github.wlailson.study_api.model.StudySession;

public record StudySessionUpdateDTO(
        String topic,
        Long durationInMinutes,
        Long breakTimeInMinutes
) {
}
