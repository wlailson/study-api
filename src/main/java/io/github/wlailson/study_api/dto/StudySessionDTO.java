package io.github.wlailson.study_api.dto;

import io.github.wlailson.study_api.model.SessionStatus;
import io.github.wlailson.study_api.model.StudySession;

import java.time.Instant;
import java.util.List;

public record StudySessionDTO(
        Long id,
        String topic,
        Long durationInMinutes,
        Long breakTimeInMinutes,
        SessionStatus status,
        Instant startTime,
        Instant endTime,
        ClientDTO client,
        SubjectDTO subject,
        List<RevisionDTO> revisions
) {

    public StudySessionDTO(StudySession entity) {
        this(entity.getId(),
                entity.getTopic(),
                entity.getDurationInMinutes(),
                entity.getBreakTimeInMinutes(),
                entity.getStatus(),
                entity.getStartTime(),
                entity.getEndTime(),
                new ClientDTO(entity.getUser()),
                new SubjectDTO(entity.getSubject()),
                entity.getRevisions().stream().map(RevisionDTO::new).toList()
        );
    }
}
