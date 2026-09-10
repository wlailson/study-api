package io.github.wlailson.study_api.controller;

import io.github.wlailson.study_api.dto.*;
import io.github.wlailson.study_api.service.StudySessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/sessions")
@Tag(name = "Sessão de Estudos",
        description = "Endpoints para gerenciamento de sessões de estudo")
@SecurityRequirement(name = "oauth2")
public class StudySessionController {

    private final StudySessionService service;

    public StudySessionController(StudySessionService service) {
        this.service = service;
    }


    @Operation(summary = "Listar sessões", description = "Retorna uma lista paginada das sessões de estudo do usuário autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sessões retornadas com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar o recurso")})
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<StudySessionMinDTO>> findAll(
            @ParameterObject Pageable pageable) {
        Page<StudySessionMinDTO> dto = service.findAll(pageable);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Buscar sessão ativa", description = "Retorna a sessão de estudo atualmente em andamento do usuário autenticado.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Sessão ativa retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar o recurso"),
            @ApiResponse(responseCode = "404", description = "Usuário não possui uma sessão de estudo ativa")})
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @GetMapping("/current")
    public ResponseEntity<StudySessionDTO> findSessionInProgress() {
        StudySessionDTO dto = service.findSessionInProgress();
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Buscar sessão por ID", description = "Retorna uma sessão de estudo específica pertencente ao usuário autenticado.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Sessão encontrada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar esta sessão"),
            @ApiResponse(responseCode = "404", description = "Sessão não encontrada")})
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<StudySessionDTO> findById(
            @Parameter(description = "ID da sessão de estudo",
                    example = "1")
            @PathVariable Long id) {
        StudySessionDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Iniciar sessão de estudo", description = "Inicia uma nova sessão de estudo para o usuário autenticado.")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Sessão iniciada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para iniciar uma sessão"),
            @ApiResponse(responseCode = "404", description = "Matéria não encontrada"),
            @ApiResponse(responseCode = "409", description = "Usuário já possui uma sessão de estudo ativa")})
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @PostMapping("/start")
    public ResponseEntity<Void> startSession(
            @Valid @RequestBody StudySessionStartDTO dto) {

        Long sessionId = service.startSession(dto.subjectId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/sessions/{id}")
                .buildAndExpand(sessionId).toUri();
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Finalizar sessão de estudo", description = "Finaliza a sessão de estudo atualmente em andamento e registra seus dados e revisões.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Sessão finalizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para finalizar a sessão"),
            @ApiResponse(responseCode = "404", description = "Usuário não possui uma sessão de estudo ativa")})
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @PostMapping("/end")
    public ResponseEntity<StudySessionDTO> endSession(
            @Valid @RequestBody StudySessionEndDTO dto) {
        return ResponseEntity.ok(service.endSession(dto));
    }

    @Operation(summary = "Atualizar sessão", description = "Atualiza os dados de uma sessão de estudo pertencente ao usuário autenticado.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Sessão atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para atualizar esta sessão"),
            @ApiResponse(responseCode = "404", description = "Sessão não encontrada")})
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @PutMapping("/{sessionId}")
    public ResponseEntity<StudySessionDTO> updateSession(
            @Parameter(description = "ID da sessão de estudo",
                    example = "1")
            @PathVariable Long sessionId,
            @Valid @RequestBody StudySessionUpdateDTO dto) {
        return ResponseEntity.ok(service.updateSession(sessionId, dto));
    }

    @Operation(summary = "Excluir sessão", description = "Exclui uma sessão de estudo pertencente ao usuário autenticado.")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Sessão excluída com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para excluir esta sessão"),
            @ApiResponse(responseCode = "404", description = "Sessão não encontrada")})
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(
            @Parameter(description = "ID da sessão de estudo",
                    example = "1")
            @PathVariable Long id) {
        service.deleteSession(id);
        return ResponseEntity.noContent().build();
    }
}
