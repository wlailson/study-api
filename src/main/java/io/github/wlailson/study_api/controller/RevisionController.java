package io.github.wlailson.study_api.controller;

import io.github.wlailson.study_api.dto.RevisionRequestDTO;
import io.github.wlailson.study_api.dto.RevisionResponseDTO;
import io.github.wlailson.study_api.model.RevisionStatus;
import io.github.wlailson.study_api.projections.RevisionMinProjection;
import io.github.wlailson.study_api.service.RevisionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/revisions")
@Tag(
        name = "Revisions",
        description = "Endpoints para gerenciamento das revisões de estudo"
)
@SecurityRequirement(name = "bearerAuth")
public class RevisionController {

    private final RevisionService service;

    public RevisionController(RevisionService service) {
        this.service = service;
    }

    @Operation(
            summary = "Buscar revisão por ID",
            description = "Retorna os dados de uma revisão pertencente ao usuário autenticado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Revisão encontrada"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Revisão não encontrada",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Usuário não autenticado",
                    content = @Content
            )
    })
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<RevisionResponseDTO> findById(
            @Parameter(
                    name = "id",
                    description = "ID da revisão",
                    example = "1",
                    required = true,
                    in = ParameterIn.PATH
            )
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(
            summary = "Listar revisões",
            description = "Retorna as revisões do usuário autenticado. É possível filtrar pelo status da revisão."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de revisões retornada com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Status informado é inválido",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Usuário não autenticado",
                    content = @Content
            )
    })
    @Parameter(
            name = "status",
            description = "Filtra as revisões pelo status",
            example = "PENDING",
            schema = @Schema(implementation = RevisionStatus.class),
            in = ParameterIn.QUERY
    )
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<RevisionMinProjection>> findAll(
            @RequestParam(required = false) RevisionStatus status
    ) {
        return ResponseEntity.ok(service.findAll(status));
    }

    @Operation(
            summary = "Concluir revisão",
            description = "Conclui uma revisão pendente informando o tempo de estudo e o tempo de intervalo."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Revisão concluída com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados da requisição inválidos",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Revisão não encontrada ou já concluída",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Usuário não autenticado",
                    content = @Content
            )
    })
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<RevisionResponseDTO> conclude(
            @Parameter(
                    name = "id",
                    description = "ID da revisão",
                    example = "1",
                    required = true,
                    in = ParameterIn.PATH
            )
            @PathVariable Long id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados necessários para concluir a revisão",
                    required = true
            )
            @Valid @RequestBody RevisionRequestDTO request
    ) {
        return ResponseEntity.ok(service.conclude(id, request));
    }

    @Operation(
            summary = "Excluir revisão",
            description = "Exclui uma revisão pertencente ao usuário autenticado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Revisão excluída com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Revisão não encontrada",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Usuário não autenticado",
                    content = @Content
            )
    })
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(
                    name = "id",
                    description = "ID da revisão",
                    example = "1",
                    required = true,
                    in = ParameterIn.PATH
            )
            @PathVariable Long id
    ) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}