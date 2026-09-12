package io.github.wlailson.study_api.repository;

import io.github.wlailson.study_api.model.Revision;
import io.github.wlailson.study_api.model.RevisionStatus;
import io.github.wlailson.study_api.projections.RevisionMinProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RevisionRepository extends JpaRepository<Revision, Long> {

    Optional<Revision> findByIdAndUser_Id(Long id, Long userId);
    Optional<Revision> findByIdAndUser_IdAndStatus(Long id, Long userId, RevisionStatus status);

    @Query("""
            SELECT
                obj.id AS id,
                obj.status AS status,
                obj.scheduledDate AS scheduledDate
            FROM Revision obj
            WHERE obj.user.id = :userId AND obj.status = :status
            ORDER BY obj.scheduledDate ASC
            """)
    List<RevisionMinProjection> searchByUserIdAndStatus(
            @Param("userId") Long userId,
            @Param("status") RevisionStatus status);


    @Query("""
        SELECT
            obj.id AS id,
            obj.status AS status,
            obj.scheduledDate AS scheduledDate
        FROM Revision obj
        WHERE obj.user.id = :userId
        ORDER BY obj.scheduledDate ASC
        """)
    List<RevisionMinProjection> searchByUserId(
            @Param("userId") Long userId);
}
