package io.github.wlailson.study_api.dto;

import io.github.wlailson.study_api.model.Topic;

public record TopicResponseDTO(Long id, String name) {

    public TopicResponseDTO(Topic entity) {
        this(entity.getId(), entity.getName());
    }
}
