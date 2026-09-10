package io.github.wlailson.study_api.controller;

import io.github.wlailson.study_api.dto.RevisionDTO;
import io.github.wlailson.study_api.dto.RevisionListDTO;
import io.github.wlailson.study_api.dto.RevisionUpdateDTO;
import io.github.wlailson.study_api.service.RevisionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/revisions")
@Tag(name = "Revisões",
        description = "Endpoints para gerenciamento das revisões de estudo")
@SecurityRequirement(name = "oauth2")
public class RevisionController {
    private final RevisionService service;

    public RevisionController(RevisionService service) {
        this.service = service;
    }

    @Operation(summary = "Buscar revisão por ID", description = "Retorna uma revisão específica pertencente ao usuário autenticado.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Revisão encontrada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar esta revisão"),
            @ApiResponse(responseCode = "404", description = "Revisão não encontrada")})
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<RevisionDTO> findById(

            @Parameter(description = "ID da revisão",
                    example = "1")
            @PathVariable Long id) {

        RevisionDTO dto = service.findById(id);

        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Listar revisões", description = "Retorna uma lista paginada das revisões pertencentes ao usuário autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Revisões retornadas com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar o recurso")})
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<RevisionListDTO>> findAll(
            Pageable pageable) {

        Page<RevisionListDTO> dto = service.findAll(pageable);

        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Atualizar revisão", description = "Atualiza os dados de uma revisão pertencente ao usuário autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Revisão atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para atualizar esta revisão"),
            @ApiResponse(responseCode = "404", description = "Revisão não encontrada")})
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<RevisionDTO> update(
            @Parameter(description = "ID da revisão",
                    example = "1")
            @PathVariable Long id,
            @Valid @RequestBody RevisionUpdateDTO dto) {

        RevisionDTO revision = service.update(id, dto);

        return ResponseEntity.ok(revision);
    }

    @Operation(summary = "Excluir revisão", description = "Exclui uma revisão pertencente ao usuário autenticado.")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Revisão excluída com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para excluir esta revisão"),
            @ApiResponse(responseCode = "404", description = "Revisão não encontrada")})
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID da revisão",
                    example = "1")
            @PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}
