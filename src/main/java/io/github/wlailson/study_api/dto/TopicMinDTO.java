package io.github.wlailson.study_api.dto;

import io.github.wlailson.study_api.projections.TopicMinProjection;

public record TopicMinDTO(Long id, String name) {

    public TopicMinDTO(TopicMinProjection projection) {
        this(projection.getId(), projection.getName());
    }
}
