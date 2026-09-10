package io.github.wlailson.study_api.controller;

import io.github.wlailson.study_api.dto.GoalCreateDTO;
import io.github.wlailson.study_api.dto.GoalDTO;
import io.github.wlailson.study_api.dto.GoalMinDTO;
import io.github.wlailson.study_api.dto.GoalUpdateDTO;
import io.github.wlailson.study_api.service.GoalService;
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

@Tag(name = "Metas", description = "Endpoints para gerenciamento de metas de estudo")
@RestController
@RequestMapping("/goals")
@SecurityRequirement(name = "oauth2")
public class GoalController {

    private final GoalService service;

    public GoalController(GoalService service) {
        this.service = service;
    }

    @Operation(summary = "Listar metas", description = "Retorna uma lista paginada das metas do usuário autenticado.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Metas retornadas com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar o recurso")})
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<GoalMinDTO>> findAll(Pageable pageable) {
        Page<GoalMinDTO> dto = service.findAll(pageable);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Buscar meta por ID", description = "Retorna uma meta específica pertencente ao usuário autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Meta encontrada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar esta meta"),
            @ApiResponse(responseCode = "404", description = "Meta não encontrada")})
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<GoalDTO> findById(
            @Parameter(description = "ID da meta",
                    example = "1")
            @PathVariable Long id) {
        GoalDTO goal = service.findById(id);
        return ResponseEntity.ok(goal);
    }

    @Operation(summary = "Criar meta", description = "Cria uma nova meta de estudo para o usuário autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Meta criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para criar a meta")})
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<GoalDTO> create(

            @Valid @RequestBody GoalCreateDTO dto) {
        GoalDTO goal = service.create(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/goals/{id}")
                .buildAndExpand(goal.id()).toUri();
        return ResponseEntity.created(location).body(goal);
    }

    @Operation(summary = "Atualizar meta", description = "Atualiza uma meta de estudo pertencente ao usuário autenticado.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Meta atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para atualizar esta meta"),
            @ApiResponse(responseCode = "404", description = "Meta não encontrada")})
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<GoalUpdateDTO> update(
            @Parameter(description = "ID da meta",
                    example = "1")
            @PathVariable Long id,
            @Valid @RequestBody GoalUpdateDTO dto) {
        GoalUpdateDTO goal = service.update(id, dto);
        return ResponseEntity.ok(goal);
    }

    @Operation(summary = "Excluir meta", description = "Exclui uma meta de estudo pertencente ao usuário autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Meta excluída com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para excluir esta meta"),
            @ApiResponse(responseCode = "404", description = "Meta não encontrada")})
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID da meta",
                    example = "1")
            @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
