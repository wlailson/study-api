package io.github.wlailson.study_api.repository;

import io.github.wlailson.study_api.model.Revision;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RevisionRepository extends JpaRepository<Revision, Long> {
}
