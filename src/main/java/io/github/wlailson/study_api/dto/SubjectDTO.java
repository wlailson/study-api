package io.github.wlailson.study_api.dto;

import io.github.wlailson.study_api.model.Subject;

public record SubjectDTO(Long id, String name) {

    public SubjectDTO(Subject entity) {
        this(
                entity != null ? entity.getId() : null,
                entity != null ? entity.getName() : null
        );
    }
}
