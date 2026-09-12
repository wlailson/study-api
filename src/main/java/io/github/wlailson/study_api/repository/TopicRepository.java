package io.github.wlailson.study_api.repository;

import io.github.wlailson.study_api.model.Topic;
import io.github.wlailson.study_api.projections.TopicMinProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    Optional<Topic> findByNameIgnoreCaseAndUser_Id(String topicName, Long userId);

    @Query("SELECT obj.id, obj.name FROM Topic obj WHERE obj.user.id = :userId")
    List<TopicMinProjection> searchTopicByUserId(@Param("userId") Long userId);

}
