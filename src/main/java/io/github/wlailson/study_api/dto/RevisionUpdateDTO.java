package io.github.wlailson.study_api.dto;

import io.github.wlailson.study_api.model.Revision;

import java.time.LocalDate;

public record RevisionUpdateDTO(LocalDate date) {

    public RevisionUpdateDTO(Revision entity) {
        this(entity.getDate());
    }
}
