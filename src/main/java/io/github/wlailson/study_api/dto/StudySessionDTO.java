package io.github.wlailson.study_api.dto;

import io.github.wlailson.study_api.model.SessionStatus;
import io.github.wlailson.study_api.model.StudySession;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

public record StudySessionDTO(
        @Schema(description = "ID da sessão de estudo",
                example = "1")
        Long id,

        @Schema(description = "Tópico estudado durante a sessão",
                example = "Herança em Java")
        String topic,

        @Schema(description = "Duração da sessão de estudo em minutos",
                example = "60")
        Long durationInMinutes,

        @Schema(description = "Tempo total de pausas durante a sessão em minutos",
                example = "10")
        Long breakTimeInMinutes,

        @Schema(description = "Status atual da sessão de estudo",
                example = "COMPLETED")
        SessionStatus status,

        @Schema(description = "Data e hora em que a sessão foi iniciada",
                example = "2026-09-07T14:00:00Z")
        Instant startTime,

        @Schema(description = "Data e hora em que a sessão foi encerrada",
                example = "2026-09-07T15:10:00Z")
        Instant endTime,

        @Schema(description = "Usuário associado à sessão de estudo")
        ClientDTO client,

        @Schema(description = "Disciplina estudada durante a sessão")
        SubjectDTO subject,

        @Schema(description = "Lista de revisões associadas à sessão")
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
