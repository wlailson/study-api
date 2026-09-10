package io.github.wlailson.study_api.dto;

import io.github.wlailson.study_api.model.Role;
import io.github.wlailson.study_api.model.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

public record UserDTO(

        @Schema(description = "ID do usuário",
                example = "1")
        Long id,

        @Schema(description = "Nome completo do usuário",
                example = "Maria Silva")
        String name,

        @Schema(description = "E-mail do usuário",
                example = "maria@gmail.com")
        String email,

        @Schema(description = "Telefone do usuário",
                example = "61996695658")
        String phone,

        @Schema(description = "Data de nascimento do usuário",
                example = "1997-11-01")
        LocalDate birthDate,


        @Schema(description = "Lista de perfis de acesso do usuário",
                example = "[\"ROLE_CLIENT\"]")
        List<String> roles

) {

    public UserDTO(User entity) {
        this(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getBirthDate(),
                entity.getRoles().stream().map(Role::getAuthority).toList()
        );
    }
}