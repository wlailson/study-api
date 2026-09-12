package io.github.wlailson.study_api.service;

import io.github.wlailson.study_api.dto.StudySessionRequestDTO;
import io.github.wlailson.study_api.dto.StudySessionResponseDTO;
import io.github.wlailson.study_api.dto.StudySessionResponseMinDTO;
import io.github.wlailson.study_api.model.StudySession;
import io.github.wlailson.study_api.model.User;
import io.github.wlailson.study_api.projections.StudySessionMinProjection;
import io.github.wlailson.study_api.projections.TopicMinProjection;
import io.github.wlailson.study_api.repository.StudySessionRepository;
import io.github.wlailson.study_api.service.exceptions.ConflictException;
import io.github.wlailson.study_api.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudySessionServiceTest {

    @Mock
    private StudySessionRepository repository;

    @Mock
    private AuthService authService;

    @Mock
    private SubjectService subjectService;

    @Mock
    private TopicService topicService;

    @Mock
    private RevisionService revisionService;

    @InjectMocks
    private StudySessionService service;

    @Test
    void findById_shouldReturnSession() {

        User user = mock(User.class);
        StudySession session = mock(StudySession.class);
        var topic = mock(io.github.wlailson.study_api.model.Topic.class);
        var subject = mock(io.github.wlailson.study_api.model.Subject.class);

        when(user.getId()).thenReturn(1L);
        when(user.getName()).thenReturn("Maria");

        when(session.getId()).thenReturn(1L);
        when(session.getTopic()).thenReturn(topic);
        when(session.getSubject()).thenReturn(subject);
        when(session.getUser()).thenReturn(user);
        when(session.getDurationInMinutes()).thenReturn(60L);
        when(session.getBreakTimeInMinutes()).thenReturn(10L);
        when(session.getRevisions()).thenReturn(java.util.Set.of());

        when(topic.getId()).thenReturn(1L);
        when(topic.getName()).thenReturn("Spring Boot");

        when(subject.getId()).thenReturn(1L);
        when(subject.getName()).thenReturn("Java");

        when(authService.getCurrentUser()).thenReturn(user);

        when(repository.findByIdAndUserId(1L, 1L))
                .thenReturn(Optional.of(session));

        StudySessionResponseDTO response = service.findById(1L);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals(60L, response.durationInMinutes());
        assertEquals(10L, response.breakTimeInMinutes());

        verify(authService).getCurrentUser();
        verify(repository).findByIdAndUserId(1L, 1L);
    }

    @Test
    void findById_shouldThrowResourceNotFoundException_whenSessionDoesNotExist() {

        User user = mock(User.class);

        when(user.getId()).thenReturn(1L);
        when(user.getName()).thenReturn("Maria");

        when(authService.getCurrentUser()).thenReturn(user);

        when(repository.findByIdAndUserId(1L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.findById(1L)
        );

        verify(repository).findByIdAndUserId(1L, 1L);
    }

    @Test
    void findAll_shouldReturnSessions() {

        User user = mock(User.class);

        when(user.getId()).thenReturn(1L);

        when(authService.getCurrentUser()).thenReturn(user);

        Pageable pageable = PageRequest.of(0, 10);

        StudySessionMinProjection projection =
                mock(StudySessionMinProjection.class);

        when(projection.getId()).thenReturn(1L);
        when(projection.getSubject()).thenReturn("Java");
        when(projection.getTopic()).thenReturn("Spring Boot");
        when(projection.getDurationInMinutes()).thenReturn(60L);
        when(projection.getDate())
                .thenReturn(LocalDate.of(2026, 9, 10));

        Page<StudySessionMinProjection> page =
                new PageImpl<>(List.of(projection), pageable, 1);

        when(repository.searchSessions(pageable, "", 1L))
                .thenReturn(page);

        Page<StudySessionResponseMinDTO> response =
                service.findAll(pageable, "");

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());

        StudySessionResponseMinDTO dto = response.getContent().get(0);

        assertEquals(1L, dto.id());
        assertEquals("Java", dto.subjectName());
        assertEquals("Spring Boot", dto.topic());
        assertEquals(60L, dto.durationInMinutes());
        assertEquals(
                LocalDate.of(2026, 9, 10),
                dto.date()
        );

        verify(repository)
                .searchSessions(pageable, "", 1L);
    }

    @Test
    void findAll_shouldPassNameFilterToRepository() {

        User user = mock(User.class);

        when(user.getId()).thenReturn(1L);
        when(authService.getCurrentUser()).thenReturn(user);

        Pageable pageable = PageRequest.of(0, 10);

        Page<StudySessionMinProjection> page =
                new PageImpl<>(List.of(), pageable, 0);

        when(repository.searchSessions(
                pageable,
                "Java",
                1L
        )).thenReturn(page);

        Page<StudySessionResponseMinDTO> response =
                service.findAll(pageable, "Java");

        assertNotNull(response);
        assertTrue(response.isEmpty());

        verify(repository)
                .searchSessions(pageable, "Java", 1L);
    }

    @Test
    void findAll_shouldReturnEmptyPage_whenNoSessionsExist() {

        User user = mock(User.class);

        when(user.getId()).thenReturn(1L);
        when(authService.getCurrentUser()).thenReturn(user);

        Pageable pageable = PageRequest.of(0, 10);

        when(repository.searchSessions(
                pageable,
                "",
                1L
        )).thenReturn(
                new PageImpl<>(List.of(), pageable, 0)
        );

        Page<StudySessionResponseMinDTO> response =
                service.findAll(pageable, "");

        assertNotNull(response);
        assertTrue(response.isEmpty());
        assertEquals(0, response.getTotalElements());
    }

    @Test
    void saveSession_shouldSaveSessionAndRevisions() {

        User user = mock(User.class);
        var subject = mock(io.github.wlailson.study_api.model.Subject.class);
        var topic = mock(io.github.wlailson.study_api.model.Topic.class);

        when(authService.getCurrentUser()).thenReturn(user);

        when(subjectService.getSubject(1L))
                .thenReturn(subject);

        when(topicService.getOrCreate("Spring Boot"))
                .thenReturn(topic);

        StudySessionRequestDTO request =
                new StudySessionRequestDTO(
                        "Spring Boot",
                        60L,
                        10L,
                        List.of()
                );

        StudySessionResponseDTO response =
                service.saveSession(1L, request);

        assertNotNull(response);

        verify(subjectService).getSubject(1L);
        verify(topicService).getOrCreate("Spring Boot");
        verify(repository).save(any(StudySession.class));
        verify(revisionService)
                .saveRevisions(eq(request), any(StudySession.class));
    }

    @Test
    void saveSession_shouldUseAuthenticatedUser() {

        User user = mock(User.class);
        var subject = mock(io.github.wlailson.study_api.model.Subject.class);
        var topic = mock(io.github.wlailson.study_api.model.Topic.class);

        when(authService.getCurrentUser()).thenReturn(user);
        when(subjectService.getSubject(1L)).thenReturn(subject);
        when(topicService.getOrCreate("Java")).thenReturn(topic);

        StudySessionRequestDTO request =
                new StudySessionRequestDTO(
                        "Java",
                        120L,
                        10L,
                        List.of()
                );

        service.saveSession(1L, request);

        verify(repository).save(
                argThat(session ->
                        session.getUser() == user
                                && session.getSubject() == subject
                                && session.getTopic() == topic
                                && session.getDurationInMinutes() == 120L
                                && session.getBreakTimeInMinutes() == 10L
                )
        );
    }

    @Test
    void saveSession_shouldThrowException_whenSubjectDoesNotExist() {

        User user = mock(User.class);

        when(authService.getCurrentUser()).thenReturn(user);

        when(subjectService.getSubject(1L))
                .thenThrow(new ResourceNotFoundException("Subject not found"));

        StudySessionRequestDTO request =
                new StudySessionRequestDTO(
                        "Java",
                        60L,
                        10L,
                        List.of()
                );

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.saveSession(1L, request)
        );

        verify(repository, never()).save(any());
        verify(revisionService, never())
                .saveRevisions(any(), any());
    }

    @Test
    void deleteSession_shouldDeleteSession() {

        User user = mock(User.class);
        StudySession session = mock(StudySession.class);
        var topic = mock(io.github.wlailson.study_api.model.Topic.class);
        var subject = mock(io.github.wlailson.study_api.model.Subject.class);

        when(user.getId()).thenReturn(1L);
        when(user.getName()).thenReturn("Maria");

        when(session.getTopic()).thenReturn(topic);
        when(session.getSubject()).thenReturn(subject);

        when(topic.getName()).thenReturn("Spring Boot");
        when(subject.getName()).thenReturn("Java");

        when(authService.getCurrentUser()).thenReturn(user);

        when(repository.findByIdAndUserId(1L, 1L))
                .thenReturn(Optional.of(session));

        service.deleteSession(1L);

        verify(repository).delete(session);
        verify(repository).flush();
    }

    @Test
    void deleteSession_shouldThrowResourceNotFoundException_whenSessionDoesNotExist() {

        User user = mock(User.class);

        when(user.getId()).thenReturn(1L);
        when(user.getName()).thenReturn("Maria");

        when(authService.getCurrentUser()).thenReturn(user);

        when(repository.findByIdAndUserId(1L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.deleteSession(1L)
        );

        verify(repository, never()).delete(any());
        verify(repository, never()).flush();
    }

    @Test
    void deleteSession_shouldThrowConflictException_whenDeleteViolatesConstraint() {

        User user = mock(User.class);
        StudySession session = mock(StudySession.class);
        var topic = mock(io.github.wlailson.study_api.model.Topic.class);
        var subject = mock(io.github.wlailson.study_api.model.Subject.class);

        when(user.getId()).thenReturn(1L);
        when(user.getName()).thenReturn("Maria");

        when(session.getTopic()).thenReturn(topic);
        when(session.getSubject()).thenReturn(subject);

        when(topic.getName()).thenReturn("Spring Boot");
        when(subject.getName()).thenReturn("Java");

        when(authService.getCurrentUser()).thenReturn(user);

        when(repository.findByIdAndUserId(1L, 1L))
                .thenReturn(Optional.of(session));

        doThrow(new DataIntegrityViolationException("FK violation"))
                .when(repository)
                .flush();

        assertThrows(
                ConflictException.class,
                () -> service.deleteSession(1L)
        );

        verify(repository).delete(session);
        verify(repository).flush();
    }

    @Test
    void findAllTopics_shouldReturnTopics() {

        List<TopicMinProjection> topics =
                List.of(mock(TopicMinProjection.class));

        when(topicService.getAllTopics())
                .thenReturn(topics);

        List<TopicMinProjection> response =
                service.findAllTopics();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertSame(topics, response);

        verify(topicService).getAllTopics();
    }
}