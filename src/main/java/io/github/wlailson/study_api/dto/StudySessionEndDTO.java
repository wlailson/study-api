package io.github.wlailson.study_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record StudySessionEndDTO(
        @NotBlank(message = "Tópico é obrigatório")
        @Size(max = 100, message = "Tópico deve possuir no máximo 100 caracteres")
        @Schema(description = "Tópico estudado durante a sessão",
                example = "Herança em Java")
        String topic,

        @NotNull(message = "Duração é obrigatória")
        @Positive(message = "Duração deve ser maior que zero")
        @Schema(description = "Duração da sessão em minutos",
                example = "60")
        Long durationInMinutes,

        @NotNull(message = "Tempo de pausa é obrigatório")
        @PositiveOrZero(message = "Tempo de pausa não pode ser negativo")
        @Schema(description = "Tempo total de pausas durante a sessão em minutos",
                example = "10")
        Long breakTimeInMinutes,

        @Valid
        @Schema(description = "Lista de revisões associadas à sessão")
        List<RevisionDTO> revisions
) {
}