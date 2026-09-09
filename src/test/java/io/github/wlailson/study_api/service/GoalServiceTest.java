package io.github.wlailson.study_api.service;

import static org.junit.jupiter.api.Assertions.*;
import io.github.wlailson.study_api.authentication.AuthenticatedUser;
import io.github.wlailson.study_api.dto.*;
import io.github.wlailson.study_api.model.Goal;
import io.github.wlailson.study_api.model.Subject;
import io.github.wlailson.study_api.model.User;
import io.github.wlailson.study_api.repository.GoalRepository;
import io.github.wlailson.study_api.repository.StudySessionRepository;
import io.github.wlailson.study_api.repository.SubjectRepository;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoalServiceTest {

    @Mock
    private AuthenticatedUser authenticatedUser;

    @Mock
    private GoalRepository repository;

    @Mock
    private StudySessionRepository studySessionRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @InjectMocks
    private GoalService service;

    private User user;
    private Goal goal;
    private Subject subject;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(10L);

        subject = new Subject();
        subject.setId(30L);
        subject.setName("Java");
        subject.setUser(user);

        goal = new Goal(
                20L,
                "Estudar Java",
                LocalDate.of(2026, 9, 1),
                LocalDate.of(2026, 9, 30),
                false,
                user
        );
    }

    // =========================================================
    // FIND BY ID
    // =========================================================

    @Nested
    class FindById {

        @Test
        void deveRetornarGoalQuandoIdExistir() {

            when(repository.findById(20L))
                    .thenReturn(Optional.of(goal));

            when(studySessionRepository.studied(
                    eq(10L),
                    eq(LocalDateTime.of(2026, 9, 1, 0, 0)),
                    eq(LocalDateTime.of(2026, 10, 1, 0, 0))
            )).thenReturn(120L);

            GoalDTO result = service.findById(20L);

            assertNotNull(result);
            assertEquals(20L, result.id());
            assertEquals("Estudar Java", result.title());
            assertEquals(
                    LocalDate.of(2026, 9, 1),
                    result.startDate()
            );
            assertEquals(
                    LocalDate.of(2026, 9, 30),
                    result.endDate()
            );
            assertEquals(120L, result.studied());

            verify(repository).findById(20L);
            verify(authenticatedUser).validateOwnership(10L);

            verify(studySessionRepository).studied(
                    10L,
                    LocalDateTime.of(2026, 9, 1, 0, 0),
                    LocalDateTime.of(2026, 10, 1, 0, 0)
            );
        }

        @Test
        void deveLancarExcecaoQuandoGoalNaoExistir() {

            when(repository.findById(20L))
                    .thenReturn(Optional.empty());

            assertThrows(
                    ResourceNotFoundException.class,
                    () -> service.findById(20L)
            );

            verify(repository).findById(20L);

            verifyNoInteractions(
                    authenticatedUser,
                    studySessionRepository
            );
        }
    }

    // =========================================================
    // FIND ALL
    // =========================================================

    @Nested
    class FindAll {

        @Test
        void deveRetornarGoalsDoUsuario() {

            Pageable pageable = PageRequest.of(0, 10);

            Goal goal2 = new Goal(
                    21L,
                    "Estudar Spring",
                    LocalDate.of(2026, 9, 1),
                    LocalDate.of(2026, 9, 30),
                    false,
                    user
            );

            Page<Goal> page = new PageImpl<>(
                    List.of(goal, goal2),
                    pageable,
                    2
            );

            when(authenticatedUser.get())
                    .thenReturn(user);

            when(repository.findAllByUserId(10L, pageable))
                    .thenReturn(page);

            Page<GoalMinDTO> result =
                    service.findAll(pageable);

            assertNotNull(result);
            assertEquals(2, result.getTotalElements());

            assertEquals(
                    20L,
                    result.getContent().get(0).id()
            );

            assertEquals(
                    "Estudar Java",
                    result.getContent().get(0).title()
            );

            assertEquals(
                    21L,
                    result.getContent().get(1).id()
            );

            assertEquals(
                    "Estudar Spring",
                    result.getContent().get(1).title()
            );

            verify(authenticatedUser).get();
            verify(authenticatedUser).validateOwnership(10L);

            verify(repository)
                    .findAllByUserId(10L, pageable);
        }

        @Test
        void deveRetornarPaginaVaziaQuandoUsuarioNaoPossuirGoals() {

            Pageable pageable = PageRequest.of(0, 10);

            Page<Goal> page =
                    new PageImpl<>(List.of(), pageable, 0);

            when(authenticatedUser.get())
                    .thenReturn(user);

            when(repository.findAllByUserId(10L, pageable))
                    .thenReturn(page);

            Page<GoalMinDTO> result =
                    service.findAll(pageable);

            assertNotNull(result);
            assertTrue(result.isEmpty());
            assertEquals(0, result.getTotalElements());

            verify(repository)
                    .findAllByUserId(10L, pageable);
        }
    }

    // =========================================================
    // CREATE
    // =========================================================

    @Nested
    class Create {

        @Test
        void deveCriarGoalComSeusItems() {

            GoalItemCreateDTO itemDTO =
                    new GoalItemCreateDTO(
                            120L,
                            30L
                    );

            GoalCreateDTO dto =
                    new GoalCreateDTO(
                            "Estudar Java",
                            LocalDate.of(2026, 9, 1),
                            LocalDate.of(2026, 9, 30),
                            List.of(itemDTO)
                    );

            when(authenticatedUser.get())
                    .thenReturn(user);

            when(subjectRepository.findById(30L))
                    .thenReturn(Optional.of(subject));

            when(repository.save(any(Goal.class)))
                    .thenAnswer(invocation ->
                            invocation.getArgument(0));

            GoalDTO result =
                    service.create(dto);

            assertNotNull(result);

            assertEquals(
                    "Estudar Java",
                    result.title()
            );

            assertEquals(
                    LocalDate.of(2026, 9, 1),
                    result.startDate()
            );

            assertEquals(
                    LocalDate.of(2026, 9, 30),
                    result.endDate()
            );

            assertEquals(
                    1,
                    result.items().size()
            );

            assertEquals(
                    120L,
                    result.items()
                            .get(0)
                            .targetInMinutes()
            );

            assertEquals(
                    30L,
                    result.items()
                            .get(0)
                            .subject()
                            .id()
            );

            ArgumentCaptor<Goal> captor =
                    ArgumentCaptor.forClass(Goal.class);

            verify(repository).save(captor.capture());

            Goal savedGoal = captor.getValue();

            assertEquals(
                    user,
                    savedGoal.getUser()
            );

            assertEquals(
                    1,
                    savedGoal.getGoalItems().size()
            );

            assertEquals(
                    goalItemSubjectId(),
                    savedGoal.getGoalItems()
                            .get(0)
                            .getSubject()
                            .getId()
            );

            assertEquals(
                    120L,
                    savedGoal.getGoalItems()
                            .get(0)
                            .getTargetInMinutes()
            );

            assertEquals(
                    savedGoal,
                    savedGoal.getGoalItems()
                            .get(0)
                            .getGoal()
            );
        }

        private Long goalItemSubjectId() {
            return 30L;
        }

        @Test
        void deveLancarExcecaoQuandoSubjectNaoExistir() {

            GoalItemCreateDTO itemDTO =
                    new GoalItemCreateDTO(
                            120L,
                            30L
                    );

            GoalCreateDTO dto =
                    new GoalCreateDTO(
                            "Estudar Java",
                            LocalDate.of(2026, 9, 1),
                            LocalDate.of(2026, 9, 30),
                            List.of(itemDTO)
                    );

            when(authenticatedUser.get())
                    .thenReturn(user);

            when(subjectRepository.findById(30L))
                    .thenReturn(Optional.empty());

            assertThrows(
                    ResourceNotFoundException.class,
                    () -> service.create(dto)
            );

            verify(subjectRepository)
                    .findById(30L);

            verify(repository, never())
                    .save(any());
        }
    }

    // =========================================================
    // UPDATE
    // =========================================================

    @Nested
    class Update {

        @Test
        void deveAtualizarGoal() {

            GoalUpdateDTO dto =
                    new GoalUpdateDTO(
                            20L,
                            "Estudar Java Avançado",
                            LocalDate.of(2026, 9, 5),
                            LocalDate.of(2026, 9, 30)
                    );

            when(repository.findById(20L))
                    .thenReturn(Optional.of(goal));

            GoalUpdateDTO result =
                    service.update(20L, dto);

            assertNotNull(result);

            assertEquals(
                    20L,
                    result.id()
            );

            assertEquals(
                    "Estudar Java Avançado",
                    result.title()
            );

            assertEquals(
                    LocalDate.of(2026, 9, 5),
                    result.startDate()
            );

            assertEquals(
                    LocalDate.of(2026, 9, 30),
                    result.endDate()
            );

            assertEquals(
                    "Estudar Java Avançado",
                    goal.getTitle()
            );

            verify(repository)
                    .findById(20L);

            verify(authenticatedUser)
                    .validateOwnership(10L);

            verify(repository, never())
                    .save(any());
        }

        @Test
        void deveLancarExcecaoQuandoGoalNaoExistir() {

            GoalUpdateDTO dto =
                    new GoalUpdateDTO(
                            20L,
                            "Estudar Java",
                            LocalDate.of(2026, 9, 1),
                            LocalDate.of(2026, 9, 30)
                    );

            when(repository.findById(20L))
                    .thenReturn(Optional.empty());

            assertThrows(
                    ResourceNotFoundException.class,
                    () -> service.update(20L, dto)
            );

            verify(repository)
                    .findById(20L);

            verifyNoInteractions(authenticatedUser);
        }
    }

    // =========================================================
    // DELETE
    // =========================================================

    @Nested
    class Delete {

        @Test
        void deveDeletarGoal() {

            when(repository.findById(20L))
                    .thenReturn(Optional.of(goal));

            service.delete(20L);

            verify(repository)
                    .findById(20L);

            verify(authenticatedUser)
                    .validateOwnership(10L);

            verify(repository)
                    .deleteById(20L);
        }

        @Test
        void deveLancarExcecaoQuandoGoalNaoExistir() {

            when(repository.findById(20L))
                    .thenReturn(Optional.empty());

            assertThrows(
                    ResourceNotFoundException.class,
                    () -> service.delete(20L)
            );

            verify(repository)
                    .findById(20L);

            verifyNoInteractions(authenticatedUser);

            verify(repository, never())
                    .deleteById(anyLong());
        }
    }

}