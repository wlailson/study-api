package io.github.wlailson.study_api.repository;

import io.github.wlailson.study_api.dto.RevisionListDTO;
import io.github.wlailson.study_api.model.Revision;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RevisionRepository extends JpaRepository<Revision, Long> {


    @Query("""
            SELECT new io.github.wlailson.study_api.dto.RevisionListDTO(obj.id,obj.session.subject.name,obj.date)
            FROM Revision obj
            WHERE obj.session.user.id = :userId
            ORDER BY obj.date ASC
            """)
    Page<RevisionListDTO> searchAllByUserId(
            @Param("userId")
            Long userId,
            Pageable pageable);
}
