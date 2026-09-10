package io.github.wlailson.study_api.controller;

import io.github.wlailson.study_api.dto.SubjectDTO;
import io.github.wlailson.study_api.service.SubjectService;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/subjects")
@Tag(name = "Disciplinas",
        description = "Endpoints para gerenciamento das disciplinas de estudo")
@SecurityRequirement(name = "oauth2")
public class SubjectController {

    private final SubjectService service;

    public SubjectController(SubjectService service) {
        this.service = service;
    }

    @Operation(summary = "Buscar disciplina por ID", description = "Retorna uma disciplina específica pertencente ao usuário autenticado.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Disciplina encontrada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar esta disciplina"),
            @ApiResponse(responseCode = "404", description = "Disciplina não encontrada")})
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<SubjectDTO> findById(
            @Parameter(description = "ID da disciplina",
                    example = "1")
            @PathVariable Long id) {
        SubjectDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Listar disciplinas", description = "Retorna uma lista paginada das disciplinas do usuário autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Disciplinas retornadas com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar o recurso")})
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<SubjectDTO>> findAll(
            Pageable pageable) {
        Page<SubjectDTO> dto = service.findAll(pageable);
        return ResponseEntity.ok(dto);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Disciplina criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para criar a disciplina")})
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<SubjectDTO> create(
            @Valid @RequestBody SubjectDTO dto) {

        SubjectDTO subject = service.create(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/subjects/{id}")
                .buildAndExpand(subject.id()).toUri();
        return ResponseEntity.created(location).body(subject);
    }

    @Operation(summary = "Atualizar disciplina", description = "Atualiza uma disciplina pertencente ao usuário autenticado.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Disciplina atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para atualizar esta disciplina"),
            @ApiResponse(responseCode = "404", description = "Disciplina não encontrada")})
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<SubjectDTO> update(
            @Valid @RequestBody SubjectDTO dto,

            @Parameter(description = "ID da disciplina",
                    example = "1")
            @PathVariable Long id) {
        SubjectDTO subject = service.update(id, dto);
        return ResponseEntity.ok(subject);
    }

    @Operation(summary = "Excluir disciplina", description = "Exclui uma disciplina pertencente ao usuário autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Disciplina excluída com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para excluir esta disciplina"),
            @ApiResponse(responseCode = "404", description = "Disciplina não encontrada")})
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID da disciplina",
                    example = "1")
            @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
