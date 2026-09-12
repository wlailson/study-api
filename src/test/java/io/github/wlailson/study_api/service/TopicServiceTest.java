package io.github.wlailson.study_api.service;

import io.github.wlailson.study_api.model.Topic;
import io.github.wlailson.study_api.model.User;
import io.github.wlailson.study_api.projections.TopicMinProjection;
import io.github.wlailson.study_api.repository.TopicRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TopicServiceTest {

    @Mock
    private TopicRepository repository;

    @Mock
    private AuthService authService;

    @InjectMocks
    private TopicService service;

    @Test
    void getOrCreate_shouldReturnExistingTopic() {

        User user = mock(User.class);
        Topic topic = mock(Topic.class);

        when(user.getId()).thenReturn(1L);

        when(authService.getCurrentUser())
                .thenReturn(user);

        when(repository.findByNameIgnoreCaseAndUser_Id(
                "Spring Boot",
                1L
        )).thenReturn(Optional.of(topic));

        Topic response = service.getOrCreate("Spring Boot");

        assertSame(topic, response);

        verify(authService).getCurrentUser();

        verify(repository)
                .findByNameIgnoreCaseAndUser_Id(
                        "Spring Boot",
                        1L
                );

        verify(repository, never()).save(any());
    }

    @Test
    void getOrCreate_shouldCreateTopic_whenTopicDoesNotExist() {

        User user = mock(User.class);

        when(user.getId()).thenReturn(1L);

        when(authService.getCurrentUser())
                .thenReturn(user);

        when(repository.findByNameIgnoreCaseAndUser_Id(
                "Spring Boot",
                1L
        )).thenReturn(Optional.empty());

        Topic savedTopic = new Topic();

        when(repository.save(any(Topic.class)))
                .thenReturn(savedTopic);

        Topic response = service.getOrCreate("Spring Boot");

        assertSame(savedTopic, response);

        ArgumentCaptor<Topic> captor =
                ArgumentCaptor.forClass(Topic.class);

        verify(repository).save(captor.capture());

        Topic topic = captor.getValue();

        assertEquals("Spring Boot", topic.getName());
        assertSame(user, topic.getUser());

        verify(repository)
                .findByNameIgnoreCaseAndUser_Id(
                        "Spring Boot",
                        1L
                );
    }

    @Test
    void getAllTopics_shouldReturnTopics() {

        User user = mock(User.class);

        when(user.getId()).thenReturn(1L);

        when(authService.getCurrentUser())
                .thenReturn(user);

        TopicMinProjection projection =
                mock(TopicMinProjection.class);

        List<TopicMinProjection> topics =
                List.of(projection);

        when(repository.searchTopicByUserId(1L))
                .thenReturn(topics);

        List<TopicMinProjection> response =
                service.getAllTopics();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertSame(projection, response.get(0));

        verify(authService).getCurrentUser();

        verify(repository)
                .searchTopicByUserId(1L);
    }

    @Test
    void getAllTopics_shouldReturnEmptyList_whenUserHasNoTopics() {

        User user = mock(User.class);

        when(user.getId()).thenReturn(1L);

        when(authService.getCurrentUser())
                .thenReturn(user);

        when(repository.searchTopicByUserId(1L))
                .thenReturn(List.of());

        List<TopicMinProjection> response =
                service.getAllTopics();

        assertNotNull(response);
        assertTrue(response.isEmpty());

        verify(repository)
                .searchTopicByUserId(1L);
    }
}