package io.github.wlailson.study_api.dto;

import io.github.wlailson.study_api.model.Revision;
import io.github.wlailson.study_api.model.RevisionStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record RevisionResponseDTO(
        @Schema(
                description = "ID da revisão",
                example = "1"
        )
        Long id,

        @Schema(
                description = "Nome do tópico relacionado à revisão",
                example = "Spring Boot"
        )
        String topic,

        @Schema(
                description = "Data programada para a revisão",
                example = "2026-09-15"
        )
        LocalDate scheduledDate,

        @Schema(
                description = "Data em que a revisão foi concluída",
                example = "2026-09-15",
                nullable = true
        )
        LocalDate completedDate,

        @Schema(
                description = "Tempo de estudo da revisão em minutos",
                example = "60",
                nullable = true
        )
        Long durationInMinutes,

        @Schema(
                description = "Tempo de intervalo em minutos",
                example = "10",
                nullable = true
        )
        Long breakTimeInMinutes,

        @Schema(
                description = "Status atual da revisão",
                example = "COMPLETED"
        )
        RevisionStatus status

) {

    public RevisionResponseDTO(Revision entity) {
        this(
                entity.getId(),
                entity.getSession().getTopic().getName(),
                entity.getScheduledDate(),
                entity.getCompletedDate(),
                entity.getDurationInMinutes(),
                entity.getBreakTimeInMinutes(),
                entity.getStatus()
        );
    }
}
