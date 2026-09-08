package io.github.wlailson.study_api.controller;

import io.github.wlailson.study_api.dto.GoalDTO;
import io.github.wlailson.study_api.service.GoalItemService;
import io.github.wlailson.study_api.service.GoalService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/goal/itens")
public class GoalItemController {

    private final GoalItemService service;

    public GoalItemController(GoalItemService service) {
        this.service = service;
    }

    @GetMapping
    public GoalDTO findById(Long goalId){
        return null;
    }

    @PostMapping
    public GoalDTO newGoal(GoalDTO goalDTO){
        return null;
    }

    @PutMapping
    public GoalDTO update(GoalDTO goalDTO){
        return null;
    }

    @DeleteMapping
    public GoalDTO delete(GoalDTO goalDTO){
        return null;
    }
}
