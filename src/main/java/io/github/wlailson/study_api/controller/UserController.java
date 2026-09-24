
package io.github.wlailson.study_api.controller;

import io.github.wlailson.study_api.dto.UserDTO;
import io.github.wlailson.study_api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Tag(
        name = "Usuários",
        description = "Endpoints relacionados ao usuário autenticado"
)
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final AuthService service;

    public UserController(AuthService service) {
        this.service = service;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/me")
    @Operation(
            summary = "Retorna o usuário autenticado",
            description = "Retorna os dados do usuário atualmente autenticado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Dados do usuário retornados com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = UserDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Usuário não autenticado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Usuário não possui permissão",
                    content = @Content
            )
    })
    public ResponseEntity<UserDTO> getMe() {
        return ResponseEntity.ok(service.getMe());
    }
}
