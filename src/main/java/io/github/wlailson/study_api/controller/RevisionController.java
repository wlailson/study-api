package io.github.wlailson.study_api.controller;

import io.github.wlailson.study_api.dto.GoalDTO;
import io.github.wlailson.study_api.dto.RevisionDTO;
import io.github.wlailson.study_api.service.RevisionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/revisions")
public class RevisionController {

    private final RevisionService service;

    public RevisionController(RevisionService service) {
        this.service = service;
    }


    @GetMapping
    public GoalDTO findById(Long goalId){
        return null;
    }

    @PutMapping
    public RevisionDTO update(RevisionDTO revisionDTO){
        return null;
    }

    @DeleteMapping
    public RevisionDTO delete(RevisionDTO revisionDTO){
        return null;
    }
}
