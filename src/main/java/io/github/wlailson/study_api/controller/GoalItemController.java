package io.github.wlailson.study_api.controller;

import io.github.wlailson.study_api.dto.GoalItemCreateDTO;
import io.github.wlailson.study_api.dto.GoalItemDTO;
import io.github.wlailson.study_api.dto.GoalItemMinDTO;
import io.github.wlailson.study_api.dto.GoalItemUpdateDTO;
import io.github.wlailson.study_api.service.GoalItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/{goalId}/itens")
public class GoalItemController {

    private final GoalItemService service;

    public GoalItemController(GoalItemService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GoalItemDTO> findById(
            @PathVariable Long id,
            @PathVariable Long goalId) {
        GoalItemDTO dto = service.findById(goalId, id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<Page<GoalItemMinDTO>> findAll(@PathVariable Long goalId, Pageable pageable) {
        Page<GoalItemMinDTO> dto = service.findAll(goalId, pageable);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<Void> addGoalItems(
            @PathVariable Long goalId,
            @RequestBody List<GoalItemCreateDTO> dto) {
        service.addGoalItems(goalId, dto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<GoalItemDTO> update(
            @PathVariable Long goalId,
            @PathVariable Long id,
            @RequestBody GoalItemUpdateDTO dto) {
        GoalItemDTO goal = service.update(goalId, id, dto);
        return ResponseEntity.ok(goal);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long goalId, @PathVariable Long id) {
        service.delete(goalId, id);
        return ResponseEntity.noContent().build();
    }
}
