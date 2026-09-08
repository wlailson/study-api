package io.github.wlailson.study_api.dto;

import io.github.wlailson.study_api.model.Revision;

import java.time.LocalDate;

public record RevisionDTO(Long id, LocalDate date) {

    public RevisionDTO(Revision entity) {
        this(entity.getId(), entity.getDate());
    }
}
