package io.github.wlailson.study_api.projections;

import java.time.LocalDate;

public interface StudySessionMinProjection {

    Long getId();

    String getSubject();

    String getTopic();

    Long getDurationInMinutes();

    LocalDate getDate();
}
