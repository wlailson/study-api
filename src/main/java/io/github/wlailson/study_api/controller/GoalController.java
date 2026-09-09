package io.github.wlailson.study_api.controller;

import io.github.wlailson.study_api.dto.GoalCreateDTO;
import io.github.wlailson.study_api.dto.GoalDTO;
import io.github.wlailson.study_api.dto.GoalMinDTO;
import io.github.wlailson.study_api.dto.GoalUpdateDTO;
import io.github.wlailson.study_api.service.GoalService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/goals")
public class GoalController {

    private final GoalService service;

    public GoalController(GoalService service) {
        this.service = service;
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<GoalMinDTO>> findAll(Pageable pageable) {
        Page<GoalMinDTO> dto = service.findAll(pageable);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<GoalDTO> findById(@PathVariable Long id) {
        GoalDTO goal = service.findById(id);
        return ResponseEntity.ok(goal);
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<GoalDTO> create(@RequestBody GoalCreateDTO dto) {
        GoalDTO goal = service.create(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/goals/{id}")
                .buildAndExpand(goal.id()).toUri();
        return ResponseEntity.created(location).body(goal);
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<GoalUpdateDTO> update(@PathVariable Long id, @RequestBody GoalUpdateDTO dto) {
        GoalUpdateDTO goal = service.update(id, dto);
        return ResponseEntity.ok(goal);
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
