package io.github.wlailson.study_api.dto;

import io.github.wlailson.study_api.model.StudySession;

import java.util.List;

public record StudySessionEndDTO(
        String topic,
        Long durationInMinutes,
        Long breakTimeInMinutes,
        List<RevisionDTO> revisions
) {
}