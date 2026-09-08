package io.github.wlailson.study_api.repository;

import io.github.wlailson.study_api.dto.StudySessionMinDTO;
import io.github.wlailson.study_api.model.SessionStatus;
import io.github.wlailson.study_api.model.StudySession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StudySessionRepository extends JpaRepository<StudySession, Long> {

    boolean existsByUserIdAndStatus(Long userId, SessionStatus status);

    Optional<StudySession> findByUserIdAndStatus(Long userId, SessionStatus status);

    boolean existsByIdAndUserId(Long id, Long userId);

    @Query("""
                  SELECT new  io.github.wlailson.study_api.dto.StudySessionMinDTO
                              (obj.id,obj.subject.name,obj.topic,obj.durationInMinutes)
                  FROM StudySession obj
                  WHERE obj.status = 'COMPLETED' AND obj.user.id = :userId
            """)
    Page<StudySessionMinDTO> searchSessions(
            Pageable pageable,
            @Param("userId") Long userId);
}