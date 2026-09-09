package io.github.wlailson.study_api.controller;

import io.github.wlailson.study_api.dto.RevisionDTO;
import io.github.wlailson.study_api.dto.RevisionListDTO;
import io.github.wlailson.study_api.dto.RevisionUpdateDTO;
import io.github.wlailson.study_api.service.RevisionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/revisions")
public class RevisionController {
    private final RevisionService service;

    public RevisionController(RevisionService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<RevisionDTO> findById(
            @PathVariable Long id) {

        RevisionDTO dto = service.findById(id);

        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<Page<RevisionListDTO>> findAll(
            Pageable pageable) {

        Page<RevisionListDTO> dto = service.findAll(pageable);

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RevisionDTO> update(
            @PathVariable Long id,
            @RequestBody RevisionUpdateDTO dto) {

        RevisionDTO revision = service.update(id, dto);

        return ResponseEntity.ok(revision);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}
