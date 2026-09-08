package io.github.wlailson.study_api.controller;

import io.github.wlailson.study_api.dto.*;
import io.github.wlailson.study_api.service.StudySessionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/sessions")
public class StudySessionController {

    private final StudySessionService service;

    public StudySessionController(StudySessionService service) {
        this.service = service;
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<StudySessionMinDTO>> findAll(
            Pageable pageable) {
        Page<StudySessionMinDTO> dto = service.findAll(pageable);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @GetMapping("/current")
    public ResponseEntity<StudySessionDTO> findSessionInProgress() {
        StudySessionDTO dto = service.findSessionInProgress();
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<StudySessionDTO> findById(@PathVariable Long id) {
        StudySessionDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @PostMapping("/start")
    public ResponseEntity<Void> startSession(@RequestBody StudySessionStartDTO dto) {

        Long sessionId = service.startSession(dto.subjectId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/sessions/{id}")
                .buildAndExpand(sessionId).toUri();
        return ResponseEntity.created(location).build();
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @PostMapping("/end")
    public ResponseEntity<StudySessionDTO> endSession(@RequestBody StudySessionEndDTO dto) {
        return ResponseEntity.ok(service.endSession(dto));
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @PutMapping("/{sessionId}")
    public ResponseEntity<StudySessionDTO> updateSession(
            @PathVariable Long sessionId,
            @RequestBody StudySessionUpdateDTO dto) {
        return ResponseEntity.ok(service.updateSession(sessionId, dto));
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        service.deleteSession(id);
        return ResponseEntity.noContent().build();
    }
}
