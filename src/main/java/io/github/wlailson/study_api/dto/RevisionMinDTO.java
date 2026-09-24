package io.github.wlailson.study_api.dto;

import io.github.wlailson.study_api.model.RevisionStatus;
import io.github.wlailson.study_api.projections.RevisionMinProjection;

import java.io.Serializable;
import java.time.LocalDate;

public record RevisionMinDTO(Long id, RevisionStatus status, LocalDate scheduledDate) implements Serializable {

    public RevisionMinDTO(RevisionMinProjection projection) {
        this(projection.getId(), projection.getStatus(), projection.getScheduledDate());
    }
}
