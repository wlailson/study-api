package io.github.wlailson.study_api.repository;

import io.github.wlailson.study_api.model.Subject;
import io.github.wlailson.study_api.projections.SubjectMinProjection;
import io.github.wlailson.study_api.projections.TopicMinProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Optional<Subject> findByNameIgnoreCaseAndUser_Id(String name, Long userId);
    Optional<Subject> findByIdAndUser_Id(Long id, Long userId);

    @Query("SELECT obj.id AS id, obj.name AS name FROM Subject obj WHERE obj.user.id = :userId")
    List<SubjectMinProjection> searchSubjectByUserId(@Param("userId") Long userId);
}
