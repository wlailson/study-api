package io.github.wlailson.study_api.dto;

import io.github.wlailson.study_api.projections.SubjectMinProjection;

import java.io.Serializable;

public record SubjectMinDTO(
        Long id,
        String name
) implements Serializable {
    public  SubjectMinDTO (SubjectMinProjection projection) {
        this(
                projection.getId(),
                projection.getName()
        );

    }
}
