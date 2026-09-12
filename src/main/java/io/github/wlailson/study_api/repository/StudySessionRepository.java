package io.github.wlailson.study_api.repository;

import io.github.wlailson.study_api.model.StudySession;
import io.github.wlailson.study_api.projections.StudySessionMinProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface StudySessionRepository extends JpaRepository<StudySession, Long> {

    boolean existsByUser_Id(Long userId);

    Optional<StudySession> findByUser_Id(Long userId);

    boolean existsByIdAndUserId(Long id, Long userId);

    Optional<StudySession> findByIdAndUserId(Long id, Long userId);

    @Query("""
            SELECT
                  obj.id AS id,
                  obj.subject.name AS subject,
                  obj.topic.name AS topic,
                  obj.durationInMinutes AS durationInMinutes,
                  obj.date AS date
            FROM StudySession obj
            WHERE obj.user.id = :userId
            AND UPPER(obj.subject.name) LIKE UPPER(CONCAT('%', :name, '%'))
            ORDER BY obj.date DESC
            """)
    Page<StudySessionMinProjection> searchSessions(
            Pageable pageable,
            @Param("name") String name,
            @Param("userId") Long userId);

    @Query(nativeQuery = true, value = """
            SELECT COALESCE( SUM(duration_in_minutes), 0)
            FROM tb_study_session
            WHERE end_time >= :startDate
            AND end_time < :endDate
            AND user_id = :userId
            """)
    Long studied(@Param("userId") Long userId,
                 @Param("startDate") LocalDateTime startDate,
                 @Param("endDate") LocalDateTime endDate
    );
}