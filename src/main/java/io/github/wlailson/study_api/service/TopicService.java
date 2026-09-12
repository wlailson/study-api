package io.github.wlailson.study_api.service;

import io.github.wlailson.study_api.model.Topic;
import io.github.wlailson.study_api.model.User;
import io.github.wlailson.study_api.projections.TopicMinProjection;
import io.github.wlailson.study_api.repository.TopicRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TopicService {

    private final TopicRepository repository;
    private final AuthService authService;


    public TopicService(TopicRepository repository, AuthService authService) {
        this.repository = repository;
        this.authService = authService;
    }


    @Transactional
    Topic getOrCreate(String topicName) {
        User user = authService.getCurrentUser();
        return repository
                .findByNameIgnoreCaseAndUser_Id(topicName, user.getId())
                .orElseGet(() -> {
                    Topic topic = new Topic();
                    topic.setName(topicName);
                    topic.setUser(user);
                    return repository.save(topic);
                });
    }


    @Transactional(readOnly = true)
    List<TopicMinProjection> getAllTopics() {
        return repository.searchTopicByUserId(authService.getCurrentUser().getId());
    }
}
