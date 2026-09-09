package io.github.wlailson.study_api.service;

import io.github.wlailson.study_api.authentication.AuthenticatedUser;
import io.github.wlailson.study_api.dto.*;
import io.github.wlailson.study_api.model.*;
import io.github.wlailson.study_api.repository.RevisionRepository;
import io.github.wlailson.study_api.repository.StudySessionRepository;
import io.github.wlailson.study_api.repository.SubjectRepository;
import io.github.wlailson.study_api.service.exceptions.ConflictException;
import io.github.wlailson.study_api.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudySessionServiceTest {


    @Mock
    private StudySessionRepository repository;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private AuthenticatedUser authenticatedUser;

    @Mock
    private RevisionRepository revisionRepository;

    @InjectMocks
    private StudySessionService service;

    private User user;
    private Subject subject;
    private StudySession session;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(1L);
        user.setName("Maria");
        user.setEmail("maria@gmail.com");

        subject = new Subject();
        subject.setId(1L);
        subject.setName("Java");

        session = new StudySession();
        session.setId(1L);
        session.setUser(user);
        session.setSubject(subject);
        session.setStatus(SessionStatus.IN_PROGRESS);
        session.setStartTime(Instant.now());
    }

    @Nested
    class FindById {

        @Test
        void shouldReturnSessionWhenExists() {

            when(repository.findById(1L))
                    .thenReturn(Optional.of(session));

            StudySessionDTO result = service.findById(1L);

            assertNotNull(result);

            verify(repository).findById(1L);
            verify(authenticatedUser)
                    .validateOwnership(1L);
        }

        @Test
        void shouldThrowResourceNotFoundExceptionWhenSessionDoesNotExist() {

            when(repository.findById(1L))
                    .thenReturn(Optional.empty());

            assertThrows(
                    ResourceNotFoundException.class,
                    () -> service.findById(1L)
            );

            verify(repository).findById(1L);

            verify(authenticatedUser, never())
                    .validateOwnership(anyLong());
        }

        @Test
        void shouldValidateSessionOwnership() {

            when(repository.findById(1L))
                    .thenReturn(Optional.of(session));

            service.findById(1L);

            verify(authenticatedUser)
                    .validateOwnership(user.getId());
        }
    }

    @Nested
    class FindAll {

        @Test
        void shouldReturnSessionsFromAuthenticatedUser() {

            Pageable pageable = Pageable.ofSize(10);

            Page<StudySessionMinDTO> page =
                    new PageImpl<>(List.of());

            when(authenticatedUser.get())
                    .thenReturn(user);

            when(repository.searchSessions(
                    pageable,
                    user.getId()
            )).thenReturn(page);

            Page<StudySessionMinDTO> result =
                    service.findAll(pageable);

            assertNotNull(result);
            assertSame(page, result);

            verify(authenticatedUser).get();

            verify(repository)
                    .searchSessions(
                            pageable,
                            user.getId()
                    );
        }
    }

    @Nested
    class StartSession {

        @Test
        void shouldStartSessionSuccessfully() {

            when(authenticatedUser.get())
                    .thenReturn(user);

            when(repository.existsByUserIdAndStatus(
                    user.getId(),
                    SessionStatus.IN_PROGRESS
            )).thenReturn(false);

            when(subjectRepository.findById(1L))
                    .thenReturn(Optional.of(subject));

            when(repository.save(any(StudySession.class)))
                    .thenAnswer(invocation -> {

                        StudySession saved =
                                invocation.getArgument(0);

                        saved.setId(10L);

                        return saved;
                    });

            Long result = service.startSession(1L);

            assertEquals(10L, result);

            verify(repository)
                    .existsByUserIdAndStatus(
                            1L,
                            SessionStatus.IN_PROGRESS
                    );

            verify(subjectRepository)
                    .findById(1L);

            verify(repository)
                    .save(any(StudySession.class));
        }

        @Test
        void shouldCreateSessionWithCorrectData() {

            when(authenticatedUser.get())
                    .thenReturn(user);

            when(repository.existsByUserIdAndStatus(
                    1L,
                    SessionStatus.IN_PROGRESS
            )).thenReturn(false);

            when(subjectRepository.findById(1L))
                    .thenReturn(Optional.of(subject));

            when(repository.save(any(StudySession.class)))
                    .thenAnswer(invocation -> {

                        StudySession saved =
                                invocation.getArgument(0);

                        saved.setId(10L);

                        return saved;
                    });

            service.startSession(1L);

            ArgumentCaptor<StudySession> captor =
                    ArgumentCaptor.forClass(StudySession.class);

            verify(repository).save(captor.capture());

            StudySession saved = captor.getValue();

            assertEquals(user, saved.getUser());
            assertEquals(subject, saved.getSubject());
            assertEquals(
                    SessionStatus.IN_PROGRESS,
                    saved.getStatus()
            );
            assertNotNull(saved.getStartTime());
        }

        @Test
        void shouldThrowConflictExceptionWhenUserAlreadyHasActiveSession() {

            when(authenticatedUser.get())
                    .thenReturn(user);

            when(repository.existsByUserIdAndStatus(
                    1L,
                    SessionStatus.IN_PROGRESS
            )).thenReturn(true);

            assertThrows(
                    ConflictException.class,
                    () -> service.startSession(1L)
            );

            verify(repository, never())
                    .save(any());

            verify(subjectRepository, never())
                    .findById(anyLong());
        }

        @Test
        void shouldThrowResourceNotFoundExceptionWhenSubjectDoesNotExist() {

            when(authenticatedUser.get())
                    .thenReturn(user);

            when(repository.existsByUserIdAndStatus(
                    1L,
                    SessionStatus.IN_PROGRESS
            )).thenReturn(false);

            when(subjectRepository.findById(99L))
                    .thenReturn(Optional.empty());

            assertThrows(
                    ResourceNotFoundException.class,
                    () -> service.startSession(99L)
            );

            verify(repository, never())
                    .save(any());
        }
    }

    @Nested
    class FindSessionInProgress {

        @Test
        void shouldReturnActiveSession() {

            when(authenticatedUser.get())
                    .thenReturn(user);

            when(repository.findByUserIdAndStatus(
                    user.getId(),
                    SessionStatus.IN_PROGRESS
            )).thenReturn(Optional.of(session));

            StudySessionDTO result =
                    service.findSessionInProgress();

            assertNotNull(result);

            verify(repository)
                    .findByUserIdAndStatus(
                            1L,
                            SessionStatus.IN_PROGRESS
                    );
        }

        @Test
        void shouldThrowResourceNotFoundExceptionWhenThereIsNoActiveSession() {

            when(authenticatedUser.get())
                    .thenReturn(user);

            when(repository.findByUserIdAndStatus(
                    1L,
                    SessionStatus.IN_PROGRESS
            )).thenReturn(Optional.empty());

            assertThrows(
                    ResourceNotFoundException.class,
                    () -> service.findSessionInProgress()
            );
        }
    }

    @Nested
    class EndSession {

        @Test
        void shouldEndSessionSuccessfully() {

            StudySessionEndDTO dto = new StudySessionEndDTO(
                    "Spring Security",
                    120L,
                    15L,
                    List.of()
            );

            when(authenticatedUser.get())
                    .thenReturn(user);

            when(repository.findByUserIdAndStatus(
                    1L,
                    SessionStatus.IN_PROGRESS
            )).thenReturn(Optional.of(session));

            when(revisionRepository.saveAll(any()))
                    .thenReturn(List.of());

            StudySessionDTO result = service.endSession(dto);

            assertNotNull(result);

            assertEquals(
                    SessionStatus.COMPLETED,
                    session.getStatus()
            );

            assertEquals(
                    120L,
                    session.getDurationInMinutes()
            );

            assertEquals(
                    15L,
                    session.getBreakTimeInMinutes()
            );

            assertEquals(
                    "Spring Security",
                    session.getTopic()
            );

            assertNotNull(session.getEndTime());

            verify(revisionRepository)
                    .saveAll(any());
        }

        @Test
        void shouldThrowResourceNotFoundExceptionWhenThereIsNoActiveSession() {

            StudySessionEndDTO dto = new StudySessionEndDTO(
                    "Java",
                    60L,
                    10L,
                    List.of()
            );

            when(authenticatedUser.get())
                    .thenReturn(user);

            when(repository.findByUserIdAndStatus(
                    1L,
                    SessionStatus.IN_PROGRESS
            )).thenReturn(Optional.empty());

            assertThrows(
                    ResourceNotFoundException.class,
                    () -> service.endSession(dto)
            );

            verify(revisionRepository, never())
                    .saveAll(any());
        }

        @Test
        void shouldSaveRevisionsWhenEndingSession() {

            RevisionDTO revisionDTO = new RevisionDTO(
                    null,
                    LocalDate.of(2026, 9, 15)
            );

            StudySessionEndDTO dto = new StudySessionEndDTO(
                    "Java",
                    100L,
                    20L,
                    List.of(revisionDTO)
            );

            when(authenticatedUser.get())
                    .thenReturn(user);

            when(repository.findByUserIdAndStatus(
                    1L,
                    SessionStatus.IN_PROGRESS
            )).thenReturn(Optional.of(session));

            when(revisionRepository.saveAll(any()))
                    .thenReturn(List.of());

            service.endSession(dto);

            ArgumentCaptor<List<Revision>> captor =
                    ArgumentCaptor.forClass(List.class);

            verify(revisionRepository)
                    .saveAll(captor.capture());

            List<Revision> revisions = captor.getValue();

            assertEquals(1, revisions.size());

            Revision revision = revisions.get(0);

            assertEquals(
                    LocalDate.of(2026, 9, 15),
                    revision.getDate()
            );

            assertEquals(
                    session,
                    revision.getSession()
            );
        }
    }

    @Nested
    class UpdateSession {

        @Test
        void shouldUpdateSessionSuccessfully() {

            StudySessionUpdateDTO dto =
                    new StudySessionUpdateDTO(
                            "Spring Security",
                            120L,
                            15L
                    );

            when(repository.findById(1L))
                    .thenReturn(Optional.of(session));

            StudySessionDTO result =
                    service.updateSession(1L, dto);

            assertNotNull(result);

            assertEquals(
                    "Spring Security",
                    session.getTopic()
            );

            assertEquals(
                    120L,
                    session.getDurationInMinutes()
            );

            assertEquals(
                    15L,
                    session.getBreakTimeInMinutes()
            );

            verify(authenticatedUser)
                    .validateOwnership(1L);
        }

        @Test
        void shouldThrowResourceNotFoundExceptionWhenSessionDoesNotExist() {

            StudySessionUpdateDTO dto =
                    new StudySessionUpdateDTO(
                            "Java",
                            100L,
                            10L
                    );

            when(repository.findById(1L))
                    .thenReturn(Optional.empty());

            assertThrows(
                    ResourceNotFoundException.class,
                    () -> service.updateSession(1L, dto)
            );

            verify(authenticatedUser, never())
                    .validateOwnership(anyLong());
        }
    }

    @Nested
    class DeleteSession {

        @Test
        void shouldDeleteSessionSuccessfully() {

            when(repository.findById(1L))
                    .thenReturn(Optional.of(session));

            service.deleteSession(1L);

            verify(authenticatedUser)
                    .validateOwnership(1L);

            verify(repository)
                    .deleteById(1L);
        }

        @Test
        void shouldThrowResourceNotFoundExceptionWhenSessionDoesNotExist() {

            when(repository.findById(1L))
                    .thenReturn(Optional.empty());

            assertThrows(
                    ResourceNotFoundException.class,
                    () -> service.deleteSession(1L)
            );

            verify(repository, never())
                    .deleteById(anyLong());

            verify(authenticatedUser, never())
                    .validateOwnership(anyLong());
        }
    }

}