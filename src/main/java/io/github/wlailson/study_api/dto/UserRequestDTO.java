package io.github.wlailson.study_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Credenciais utilizadas para autenticação")
public record UserRequestDTO(

        @Schema(
                description = "E-mail cadastrado do usuário",
                example = "maria@gmail.com"
        )
        String email,

        @Schema(
                description = "Senha do usuário",
                example = "123456",
                format = "password"
        )
        String password
) {
}