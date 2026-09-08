package io.github.wlailson.study_api.dto;

import io.github.wlailson.study_api.model.Revision;

import java.time.LocalDate;

public record RevisionDTOCreate(Long id, LocalDate date) {

    public RevisionDTOCreate(Revision entity) {
        this(entity.getId(), entity.getDate());
    }
}
