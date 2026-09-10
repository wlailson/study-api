package io.github.wlailson.study_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record StudySessionMinDTO(

        @Schema(description = "ID da sessão de estudo",
                example = "1")
        Long id,

        @Schema(description = "Nome da disciplina estudada",
                example = "Java")
        String subjectName,

        @Schema(description = "Tópico estudado durante a sessão",
                example = "Herança em Java")
        String topic,

        @Schema(description = "Duração da sessão de estudo em minutos",
                example = "60")
        Long durationInMinutes

) {
}
