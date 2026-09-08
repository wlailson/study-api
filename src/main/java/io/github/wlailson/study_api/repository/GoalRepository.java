package io.github.wlailson.study_api.repository;

import io.github.wlailson.study_api.model.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalRepository extends JpaRepository<StudySession, Long> {
}
