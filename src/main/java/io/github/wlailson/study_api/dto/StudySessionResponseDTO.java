package io.github.wlailson.study_api.dto;

import io.github.wlailson.study_api.model.StudySession;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record StudySessionResponseDTO(

        @Schema(description = "ID da sessão de estudo",
                example = "1")
        Long id,

        @Schema(description = "Duração da sessão de estudo em minutos",
                example = "60")
        Long durationInMinutes,

        @Schema(description = "Tempo total de pausas durante a sessão em minutos",
                example = "10")
        Long breakTimeInMinutes,

        @Schema(description = "Tópico estudado durante a sessão",
                example = "Herança em Java")
        TopicResponseDTO topic,

        @Schema(description = "Usuário associado à sessão de estudo")
        ClientDTO client,

        @Schema(description = "Disciplina estudada durante a sessão")
        SubjectResponseDTO subject,

        @Schema(description = "Lista de revisões associadas à sessão")
        List<RevisionResponseDTO> revisions
) {

    public StudySessionResponseDTO(StudySession entity) {
        this(entity.getId(),
                entity.getDurationInMinutes(),
                entity.getBreakTimeInMinutes(),
                new TopicResponseDTO(entity.getTopic()),
                new ClientDTO(entity.getUser()),
                new SubjectResponseDTO(entity.getSubject()),
                entity.getRevisions().stream().map(RevisionResponseDTO::new).toList()
        );
    }
}
