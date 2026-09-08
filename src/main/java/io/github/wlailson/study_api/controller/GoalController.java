package io.github.wlailson.study_api.controller;

import io.github.wlailson.study_api.dto.GoalDTO;
import io.github.wlailson.study_api.service.GoalService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/goals")
public class GoalController {

    private final GoalService service;

    public GoalController(GoalService service) {
        this.service = service;
    }

    @PostMapping
    public GoalDTO create(GoalDTO goalDTO) {
        return null;
    }

    @GetMapping
    public GoalDTO findById(Long goalId) {
        return null;
    }

    @PutMapping
    public GoalDTO update(GoalDTO goalDTO) {
        return null;
    }

    @DeleteMapping
    public GoalDTO delete(GoalDTO goalDTO) {
        return null;
    }
}
