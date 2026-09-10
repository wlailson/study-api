package io.github.wlailson.study_api.controller;

import io.github.wlailson.study_api.dto.GoalItemCreateDTO;
import io.github.wlailson.study_api.dto.GoalItemDTO;
import io.github.wlailson.study_api.dto.GoalItemMinDTO;
import io.github.wlailson.study_api.dto.GoalItemUpdateDTO;
import io.github.wlailson.study_api.service.GoalItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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

import java.util.List;

@RestController
@RequestMapping("/{goalId}/itens")
@Tag(name = "Itens de meta",
        description = "Endpoints para gerenciamento dos itens de uma meta de estudo")
@SecurityRequirement(name = "oauth2")
public class GoalItemController {

    private final GoalItemService service;

    public GoalItemController(GoalItemService service) {
        this.service = service;
    }

    @Operation(summary = "Buscar item por ID", description = "Retorna um item específico pertencente à meta informada.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item encontrado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar este item"),
            @ApiResponse(responseCode = "404", description = "Meta ou item não encontrado")})
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<GoalItemDTO> findById(
            @Parameter(
                    description = "ID da meta",
                    example = "1",
                    required = true,
                    in = ParameterIn.PATH)
            @PathVariable Long id,
            @Parameter(
                    description = "ID do item da meta",
                    example = "1",
                    required = true,
                    in = ParameterIn.PATH)
            @PathVariable Long goalId) {
        GoalItemDTO dto = service.findById(goalId, id);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Listar itens da meta", description = "Retorna uma lista paginada dos itens pertencentes à meta informada.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Itens retornados com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para acessar esta meta"),
            @ApiResponse(responseCode = "404", description = "Meta não encontrada")})
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<GoalItemMinDTO>> findAll(
            @Parameter(description = "ID da meta",
                    example = "1",
                    required = true,
                    in = ParameterIn.PATH)
            @PathVariable Long goalId,
            Pageable pageable) {
        Page<GoalItemMinDTO> dto = service.findAll(goalId, pageable);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Adicionar itens à meta", description = "Adiciona uma lista de novos itens à meta de estudo informada.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Itens adicionados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para alterar esta meta"),
            @ApiResponse(responseCode = "404", description = "Meta não encontrada")})
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Void> addGoalItems(
            @Parameter(description = "ID da meta",
                    example = "1",
                    required = true,
                    in = ParameterIn.PATH)
            @PathVariable Long goalId,
            @Valid @RequestBody List<GoalItemCreateDTO> dto) {
        service.addGoalItems(goalId, dto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Atualizar item da meta", description = "Atualiza um item pertencente à meta informada.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para atualizar este item"),
            @ApiResponse(responseCode = "404", description = "Meta ou item não encontrado")})
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<GoalItemDTO> update(
            @Parameter(
                    description = "ID da meta",
                    example = "1",
                    required = true, in =
                    ParameterIn.PATH)
            @PathVariable Long goalId,

            @Parameter(
                    description = "ID do item da meta",
                    example = "1",
                    required = true,
                    in = ParameterIn.PATH)
            @PathVariable Long id,
            @Valid @RequestBody GoalItemUpdateDTO dto) {
        GoalItemDTO goal = service.update(goalId, id, dto);
        return ResponseEntity.ok(goal);
    }

    @Operation(summary = "Excluir item da meta", description = "Exclui um item pertencente à meta informada.")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Item excluído com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Usuário não possui permissão para excluir este item"),
            @ApiResponse(responseCode = "404", description = "Meta ou item não encontrado")})
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(
                    description = "ID da meta",
                    example = "1",
                    required = true,
                    in = ParameterIn.PATH)
            @PathVariable Long goalId,

            @Parameter(
                    description = "ID do item da meta",
                    example = "1",
                    required = true,
                    in = ParameterIn.PATH)
            @PathVariable Long id) {
        service.delete(goalId, id);
        return ResponseEntity.noContent().build();
    }
}
