package io.github.wlailson.study_api.controller;

import io.github.wlailson.study_api.dto.SubjectRequestDTO;
import io.github.wlailson.study_api.dto.SubjectResponseDTO;
import io.github.wlailson.study_api.projections.SubjectMinProjection;
import io.github.wlailson.study_api.service.SubjectService;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/subjects")
@Tag(
        name = "Disciplinas",
        description = "Endpoints para gerenciamento das disciplinas de estudo"
)
@SecurityRequirement(name = "bearerAuth")
public class SubjectController {

    private final SubjectService service;

    public SubjectController(SubjectService service) {
        this.service = service;
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @GetMapping
    @Operation(
            summary = "Lista as disciplinas",
            description = "Retorna todas as disciplinas pertencentes ao usuário autenticado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Disciplinas encontradas"
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
    public ResponseEntity<List<SubjectMinProjection>> findAll() {

        List<SubjectMinProjection> response = service.findAll();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @GetMapping("/{id}")
    @Operation(
            summary = "Busca uma disciplina pelo ID",
            description = "Retorna os dados de uma disciplina pertencente ao usuário autenticado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Disciplina encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = SubjectResponseDTO.class
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
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Disciplina não encontrada",
                    content = @Content
            )
    })
    public ResponseEntity<SubjectResponseDTO> findById(

            @Parameter(
                    name = "id",
                    description = "ID da disciplina",
                    example = "1",
                    required = true,
                    in = ParameterIn.PATH
            )
            @PathVariable Long id) {

        SubjectResponseDTO response = service.findById(id);

        return ResponseEntity.ok(response);
    }



    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @PostMapping
    @Operation(
            summary = "Cria uma disciplina",
            description = "Cria uma nova disciplina para o usuário autenticado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Disciplina criada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = SubjectResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados da disciplina inválidos",
                    content = @Content
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
    public ResponseEntity<SubjectResponseDTO> create(

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados da nova disciplina",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = SubjectRequestDTO.class
                            )
                    )
            )
            @Valid @RequestBody SubjectRequestDTO request) {

        SubjectResponseDTO subject = service.create(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/subjects/{id}")
                .buildAndExpand(subject.id())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(subject);
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @PutMapping("/{id}")
    @Operation(
            summary = "Atualiza uma disciplina",
            description = "Atualiza os dados de uma disciplina pertencente ao usuário autenticado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Disciplina atualizada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = SubjectResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados da disciplina inválidos",
                    content = @Content
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
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Disciplina não encontrada",
                    content = @Content
            )
    })
    public ResponseEntity<SubjectResponseDTO> update(

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados atualizados da disciplina",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = SubjectRequestDTO.class
                            )
                    )
            )
            @Valid @RequestBody SubjectRequestDTO request,

            @Parameter(
                    name = "id",
                    description = "ID da disciplina",
                    example = "1",
                    required = true,
                    in = ParameterIn.PATH
            )
            @PathVariable Long id) {

        SubjectResponseDTO subject = service.update(id, request);

        return ResponseEntity.ok(subject);
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Exclui uma disciplina",
            description = "Exclui uma disciplina pertencente ao usuário autenticado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Disciplina excluída com sucesso"
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
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Disciplina não encontrada",
                    content = @Content
            )
    })
    public ResponseEntity<Void> delete(

            @Parameter(
                    name = "id",
                    description = "ID da disciplina",
                    example = "1",
                    required = true,
                    in = ParameterIn.PATH
            )
            @PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}