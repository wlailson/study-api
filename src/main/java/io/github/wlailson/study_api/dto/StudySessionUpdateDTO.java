package io.github.wlailson.study_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record StudySessionUpdateDTO(
        @NotBlank(message = "Tópico é obrigatório")
        @Size(max = 100, message = "Tópico deve possuir no máximo 100 caracteres")
        @Schema(description = "Tópico estudado durante a sessão",
                example = "Herança em Java")
        String topic,

        @Positive(message = "Duração deve ser maior que zero")
        @Schema(description = "Duração da sessão de estudo em minutos",
                example = "60")
        Long durationInMinutes,

        @Positive(message = "Tempo de pausa deve ser maior que zero")
        @Schema(description = "Tempo de pausa durante a sessão em minutos",
                example = "10")
        Long breakTimeInMinutes
) {
}
