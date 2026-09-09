package io.github.wlailson.study_api.dto;

import java.time.LocalDate;

public record RevisionListDTO(Long id, String subjectName, LocalDate date) {
}
