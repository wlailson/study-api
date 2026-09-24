package io.github.wlailson.study_api.dto;

import io.github.wlailson.study_api.model.Topic;

import java.io.Serializable;

public record TopicResponseDTO(Long id, String name) implements Serializable {

    public TopicResponseDTO(Topic entity) {
        this(entity.getId(), entity.getName());
    }
}
