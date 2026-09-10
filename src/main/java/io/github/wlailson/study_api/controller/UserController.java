package io.github.wlailson.study_api.controller;

import io.github.wlailson.study_api.dto.UserDTO;
import io.github.wlailson.study_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
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
@Tag( name = "Usuários",
        description = "Endpoints para gerenciamento e consulta dos dados do usuário autenticado" )
@SecurityRequirement(name = "oauth2")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @Operation( summary = "Consultar usuário autenticado", description = "Retorna os dados do usuário atualmente autenticado." )
    @ApiResponses({
            @ApiResponse( responseCode = "200", description = "Dados do usuário retornados com sucesso" ),
            @ApiResponse( responseCode = "401", description = "Usuário não autenticado" ),
            @ApiResponse( responseCode = "403", description = "Usuário não possui permissão para acessar o recurso" ) })
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getMe() {
        UserDTO dto = service.getMe();
        return ResponseEntity.ok(dto);
    }
}