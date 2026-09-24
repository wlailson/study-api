package io.github.wlailson.study_api.service;

import io.github.wlailson.study_api.dto.RevisionMinDTO;
import io.github.wlailson.study_api.dto.RevisionRequestDTO;
import io.github.wlailson.study_api.dto.RevisionResponseDTO;
import io.github.wlailson.study_api.dto.StudySessionRequestDTO;
import io.github.wlailson.study_api.model.*;
import io.github.wlailson.study_api.projections.RevisionMinProjection;
import io.github.wlailson.study_api.repository.RevisionRepository;
import io.github.wlailson.study_api.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RevisionServiceTest {

    @Mock
    private RevisionRepository repository;

    @Mock
    private AuthService authService;

    @InjectMocks
    private RevisionService service;

    private User user;
    private StudySession session;
    private Topic topic;
    private Revision revision;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(1L);
        user.setName("Maria");

        topic = new Topic();
        topic.setId(1L);
        topic.setName("Spring Boot");

        session = new StudySession();
        session.setTopic(topic);

        revision = new Revision();
        revision.setId(10L);
        revision.setUser(user);
        revision.setSession(session);
        revision.setStatus(RevisionStatus.PENDING);
        revision.setScheduledDate(
                LocalDate.of(2026, 9, 15)
        );
    }

    @Test
    void findById_shouldReturnRevision_whenRevisionExists() {

        when(authService.getCurrentUser())
                .thenReturn(user);

        when(repository.findByIdAndUser_Id(10L, 1L))
                .thenReturn(Optional.of(revision));

        RevisionResponseDTO result =
                service.findById(10L);

        assertNotNull(result);
        assertEquals(10L, result.id());
        assertEquals(
                RevisionStatus.PENDING,
                result.status()
        );

        verify(repository)
                .findByIdAndUser_Id(10L, 1L);
    }

    @Test
    void findById_shouldThrowException_whenRevisionDoesNotExist() {

        when(authService.getCurrentUser())
                .thenReturn(user);

        when(repository.findByIdAndUser_Id(10L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.findById(10L)
        );

        verify(repository)
                .findByIdAndUser_Id(10L, 1L);
    }

    @Test
    void findAll_shouldReturnAllUserRevisions_whenStatusIsNull() {

        when(authService.getCurrentUser())
                .thenReturn(user);

        RevisionMinProjection projection =
                mock(RevisionMinProjection.class);

        when(projection.getId())
                .thenReturn(10L);

        when(projection.getStatus())
                .thenReturn(RevisionStatus.PENDING);

        when(projection.getScheduledDate())
                .thenReturn(
                        LocalDate.of(2026, 9, 15)
                );

        when(repository.searchByUserId(1L))
                .thenReturn(List.of(projection));

        List<RevisionMinDTO> result =
                service.findAll(null);

        assertNotNull(result);
        assertEquals(1, result.size());

        RevisionMinDTO dto = result.get(0);

        assertEquals(10L, dto.id());
        assertEquals(
                RevisionStatus.PENDING,
                dto.status()
        );
        assertEquals(
                LocalDate.of(2026, 9, 15),
                dto.scheduledDate()
        );

        verify(repository)
                .searchByUserId(1L);

        verify(repository, never())
                .searchByUserIdAndStatus(
                        anyLong(),
                        any()
                );
    }

    @Test
    void findAll_shouldReturnFilteredRevisions_whenStatusIsProvided() {

        when(authService.getCurrentUser())
                .thenReturn(user);

        RevisionMinProjection projection =
                mock(RevisionMinProjection.class);

        when(projection.getId())
                .thenReturn(10L);

        when(projection.getStatus())
                .thenReturn(RevisionStatus.PENDING);

        when(projection.getScheduledDate())
                .thenReturn(
                        LocalDate.of(2026, 9, 15)
                );

        when(repository.searchByUserIdAndStatus(
                1L,
                RevisionStatus.PENDING
        )).thenReturn(List.of(projection));

        List<RevisionMinDTO> result =
                service.findAll(RevisionStatus.PENDING);

        assertNotNull(result);
        assertEquals(1, result.size());

        RevisionMinDTO dto = result.get(0);

        assertEquals(10L, dto.id());
        assertEquals(
                RevisionStatus.PENDING,
                dto.status()
        );
        assertEquals(
                LocalDate.of(2026, 9, 15),
                dto.scheduledDate()
        );

        verify(repository)
                .searchByUserIdAndStatus(
                        1L,
                        RevisionStatus.PENDING
                );

        verify(repository, never())
                .searchByUserId(anyLong());
    }

    @Test
    void findAll_shouldReturnEmptyList_whenUserHasNoRevisions() {

        when(authService.getCurrentUser())
                .thenReturn(user);

        when(repository.searchByUserId(1L))
                .thenReturn(List.of());

        List<RevisionMinDTO> result =
                service.findAll(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(repository)
                .searchByUserId(1L);
    }

    @Test
    void conclude_shouldCompleteRevision() {

        when(authService.getCurrentUser())
                .thenReturn(user);

        when(repository.findByIdAndUser_IdAndStatus(
                10L,
                1L,
                RevisionStatus.PENDING
        )).thenReturn(Optional.of(revision));

        RevisionRequestDTO request =
                new RevisionRequestDTO(
                        LocalDate.of(2026, 9, 20),
                        10L,
                        60L
                );

        RevisionResponseDTO result =
                service.conclude(10L, request);

        assertNotNull(result);

        assertEquals(
                RevisionStatus.COMPLETED,
                revision.getStatus()
        );

        assertEquals(
                60L,
                revision.getDurationInMinutes()
        );

        assertEquals(
                10L,
                revision.getBreakTimeInMinutes()
        );

        assertEquals(
                LocalDate.now(),
                revision.getCompletedDate()
        );

        verify(repository)
                .findByIdAndUser_IdAndStatus(
                        10L,
                        1L,
                        RevisionStatus.PENDING
                );

        verify(repository, never())
                .save(any());
    }

    @Test
    void conclude_shouldThrowException_whenRevisionDoesNotExistOrIsAlreadyCompleted() {

        when(authService.getCurrentUser())
                .thenReturn(user);

        when(repository.findByIdAndUser_IdAndStatus(
                10L,
                1L,
                RevisionStatus.PENDING
        )).thenReturn(Optional.empty());

        RevisionRequestDTO request =
                new RevisionRequestDTO(
                        LocalDate.of(2026, 9, 20),
                        10L,
                        60L
                );

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.conclude(10L, request)
        );

        verify(repository)
                .findByIdAndUser_IdAndStatus(
                        10L,
                        1L,
                        RevisionStatus.PENDING
                );
    }

    @Test
    void delete_shouldDeleteRevision_whenRevisionExists() {

        when(authService.getCurrentUser())
                .thenReturn(user);

        when(repository.findByIdAndUser_Id(10L, 1L))
                .thenReturn(Optional.of(revision));

        service.delete(10L);

        verify(repository)
                .findByIdAndUser_Id(10L, 1L);

        verify(repository)
                .delete(revision);
    }

    @Test
    void delete_shouldThrowException_whenRevisionDoesNotExist() {

        when(authService.getCurrentUser())
                .thenReturn(user);

        when(repository.findByIdAndUser_Id(10L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.delete(10L)
        );

        verify(repository, never())
                .delete(any());
    }

    @Test
    void saveRevisions_shouldCreateAndSaveRevisions() {

        StudySession session =
                new StudySession();

        RevisionRequestDTO revisionRequest1 =
                new RevisionRequestDTO(
                        LocalDate.of(2026, 9, 15),
                        null,
                        null
                );

        RevisionRequestDTO revisionRequest2 =
                new RevisionRequestDTO(
                        LocalDate.of(2026, 9, 20),
                        null,
                        null
                );

        StudySessionRequestDTO request =
                mock(StudySessionRequestDTO.class);

        when(authService.getCurrentUser())
                .thenReturn(user);

        when(request.revisions())
                .thenReturn(
                        List.of(
                                revisionRequest1,
                                revisionRequest2
                        )
                );

        service.saveRevisions(
                request,
                session
        );

        ArgumentCaptor<List<Revision>> captor =
                ArgumentCaptor.forClass(List.class);

        verify(repository)
                .saveAll(captor.capture());

        List<Revision> savedRevisions =
                captor.getValue();

        assertEquals(
                2,
                savedRevisions.size()
        );

        assertEquals(
                RevisionStatus.PENDING,
                savedRevisions.get(0).getStatus()
        );

        assertEquals(
                RevisionStatus.PENDING,
                savedRevisions.get(1).getStatus()
        );

        assertEquals(
                LocalDate.of(2026, 9, 15),
                savedRevisions.get(0).getScheduledDate()
        );

        assertEquals(
                LocalDate.of(2026, 9, 20),
                savedRevisions.get(1).getScheduledDate()
        );

        assertEquals(
                user,
                savedRevisions.get(0).getUser()
        );

        assertEquals(
                user,
                savedRevisions.get(1).getUser()
        );

        assertEquals(
                session,
                savedRevisions.get(0).getSession()
        );

        assertEquals(
                session,
                savedRevisions.get(1).getSession()
        );

        assertEquals(
                2,
                session.getRevisions().size()
        );

        assertTrue(
                session.getRevisions()
                        .containsAll(savedRevisions)
        );
    }

    @Test
    void saveRevisions_shouldNotSaveAnything_whenRevisionListIsEmpty() {

        StudySession session =
                new StudySession();

        StudySessionRequestDTO request =
                mock(StudySessionRequestDTO.class);

        when(authService.getCurrentUser())
                .thenReturn(user);

        when(request.revisions())
                .thenReturn(List.of());

        service.saveRevisions(
                request,
                session
        );

        verify(repository)
                .saveAll(List.of());

        assertTrue(
                session.getRevisions().isEmpty()
        );
    }
}