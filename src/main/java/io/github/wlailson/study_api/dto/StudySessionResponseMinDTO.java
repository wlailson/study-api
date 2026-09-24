package io.github.wlailson.study_api.dto;

import java.io.Serializable;
import java.time.LocalDate;

public record StudySessionResponseMinDTO(
        Long id,
        String subjectName,
        String topic,
        Long durationInMinutes,
        LocalDate date
)implements Serializable {

}
