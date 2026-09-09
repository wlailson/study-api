package io.github.wlailson.study_api.repository;

import io.github.wlailson.study_api.dto.GoalItemMinDTO;
import io.github.wlailson.study_api.model.GoalItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GoalItemRepository extends JpaRepository<GoalItem, Long> {
    Optional<GoalItem> findByIdAndGoalId(Long id, Long goalId);

    @Query("""
            SELECT new io.github.wlailson.study_api.dto.GoalItemMinDTO(obg.targetInMinutes, obg.subject.name)
            FROM GoalItem obg
            WHERE obg.goal.id = :id
            """)
    Page<GoalItemMinDTO> searchAllbyGoalId(@Param("id") Long id, Pageable pageable);
}
