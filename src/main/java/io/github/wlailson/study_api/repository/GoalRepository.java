package io.github.wlailson.study_api.repository;

import io.github.wlailson.study_api.model.Goal;
import io.github.wlailson.study_api.model.StudySession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    Page<Goal> findAllByUserId(Long userId, Pageable pageable);
}
