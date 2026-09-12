package io.github.wlailson.study_api.controller;

import io.github.wlailson.study_api.dto.StudySessionRequestDTO;
import io.github.wlailson.study_api.dto.StudySessionResponseDTO;
import io.github.wlailson.study_api.dto.StudySessionResponseMinDTO;
import io.github.wlailson.study_api.service.StudySessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sessions")
@Tag(
        name = "Study Sessions",
        description = "Endpoints para gerenciamento das sessões de estudo"
)
@SecurityRequirement(name = "bearerAuth")
public class StudySessionController {

    private final StudySessionService service;

    public StudySessionController(StudySessionService service) {
        this.service = service;
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @GetMapping
    @Operation(
            summary = "Lista as sessões de estudo",
            description = "Retorna uma lista paginada das sessões de estudo do usuário autenticado. "
                    + "É possível filtrar as sessões pelo nome da disciplina."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Sessões encontradas"
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
    public ResponseEntity<Page<StudySessionResponseMinDTO>> findAll(

            @Parameter(
                    name = "name",
                    description = "Nome da disciplina para filtrar as sessões",
                    example = "Java",
                    in = ParameterIn.QUERY
            )
            @RequestParam(name = "name", defaultValue = "")
            String name,

            @Parameter(
                    description = "Parâmetros de paginação e ordenação",
                    in = ParameterIn.QUERY
            )
            @ParameterObject
            @PageableDefault(size = 10, sort = "date")
            Pageable pageable) {

        return ResponseEntity.ok(service.findAll(pageable, name));
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @GetMapping("/{id}")
    @Operation(
            summary = "Busca uma sessão de estudo pelo ID",
            description = "Retorna os dados completos de uma sessão de estudo pertencente ao usuário autenticado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Sessão encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = StudySessionResponseDTO.class
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
                    description = "Sessão de estudo não encontrada",
                    content = @Content
            )
    })
    public ResponseEntity<StudySessionResponseDTO> findById(

            @Parameter(
                    name = "id",
                    description = "ID da sessão de estudo",
                    example = "1",
                    required = true,
                    in = ParameterIn.PATH
            )
            @PathVariable Long id) {

        StudySessionResponseDTO response = service.findById(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{subjectId}")
    @Operation(
            summary = "Cria uma sessão de estudo",
            description = "Cria uma nova sessão de estudo associada à disciplina informada."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Sessão de estudo criada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = StudySessionResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados da sessão inválidos",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Disciplina não encontrada",
                    content = @Content
            )
    })
    public ResponseEntity<StudySessionResponseDTO> saveSession(

            @Parameter(
                    name = "subjectId",
                    description = "ID da disciplina associada à sessão",
                    example = "1",
                    required = true,
                    in = ParameterIn.PATH
            )
            @PathVariable Long subjectId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados da sessão de estudo",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = StudySessionRequestDTO.class
                            )
                    )
            )
            @RequestBody StudySessionRequestDTO request) {

        return ResponseEntity.ok(
                service.saveSession(subjectId, request)
        );
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Exclui uma sessão de estudo",
            description = "Exclui uma sessão de estudo pertencente ao usuário autenticado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Sessão excluída com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Sessão de estudo não encontrada",
                    content = @Content
            )
    })
    public ResponseEntity<Void> deleteSession(

            @Parameter(
                    name = "id",
                    description = "ID da sessão de estudo",
                    example = "1",
                    required = true,
                    in = ParameterIn.PATH
            )
            @PathVariable Long id) {

        service.deleteSession(id);

        return ResponseEntity.noContent().build();
    }
}