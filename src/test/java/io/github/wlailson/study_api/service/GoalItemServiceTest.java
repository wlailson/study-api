package io.github.wlailson.study_api.service;


import io.github.wlailson.study_api.authentication.AuthenticatedUser;
import io.github.wlailson.study_api.dto.GoalItemCreateDTO;
import io.github.wlailson.study_api.dto.GoalItemDTO;
import io.github.wlailson.study_api.dto.GoalItemMinDTO;
import io.github.wlailson.study_api.dto.GoalItemUpdateDTO;
import io.github.wlailson.study_api.model.Goal;
import io.github.wlailson.study_api.model.GoalItem;
import io.github.wlailson.study_api.model.Subject;
import io.github.wlailson.study_api.model.User;
import io.github.wlailson.study_api.repository.GoalItemRepository;
import io.github.wlailson.study_api.repository.GoalRepository;
import io.github.wlailson.study_api.repository.SubjectRepository;
import io.github.wlailson.study_api.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoalItemServiceTest {


    @Mock
    private GoalItemRepository repository;

    @Mock
    private AuthenticatedUser authenticatedUser;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private GoalRepository goalRepository;

    @InjectMocks
    private GoalItemService service;

    private User user;
    private Goal goal;
    private Subject subject;
    private GoalItem goalItem;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(10L);

        goal = new Goal();
        goal.setId(20L);
        goal.setUser(user);

        subject = new Subject();
        subject.setId(30L);
        subject.setName("Java");
        subject.setUser(user);

        goalItem = new GoalItem();
        goalItem.setId(40L);
        goalItem.setGoal(goal);
        goalItem.setSubject(subject);
        goalItem.setTargetInMinutes(120L);
    }

    @Nested
    class FindById {

        @Test
        void deveBuscarGoalItem() {

            when(repository.findByIdAndGoalId(40L, 20L))
                    .thenReturn(Optional.of(goalItem));

            GoalItemDTO result =
                    service.findById(20L, 40L);

            assertNotNull(result);

            assertEquals(40L, result.id());

            assertEquals(
                    120L,
                    result.targetInMinutes()
            );

            assertNotNull(result.subject());

            assertEquals(
                    30L,
                    result.subject().id()
            );

            assertEquals(
                    "Java",
                    result.subject().name()
            );

            verify(repository)
                    .findByIdAndGoalId(40L, 20L);

            verify(authenticatedUser)
                    .validateOwnership(10L);
        }

        @Test
        void deveLancarExcecaoQuandoNaoEncontrar() {

            when(repository.findByIdAndGoalId(40L, 20L))
                    .thenReturn(Optional.empty());

            assertThrows(
                    ResourceNotFoundException.class,
                    () -> service.findById(20L, 40L)
            );

            verify(repository)
                    .findByIdAndGoalId(40L, 20L);

            verifyNoInteractions(authenticatedUser);
        }
    }

    @Nested
    class FindAll {

        @Test
        void deveRetornarGoalItems() {

            Pageable pageable = PageRequest.of(0, 10);

            GoalItemMinDTO dto =
                    new GoalItemMinDTO(
                            120L,
                            "Java"
                    );

            Page<GoalItemMinDTO> page =
                    new PageImpl<>(List.of(dto));

            when(goalRepository.findById(20L))
                    .thenReturn(Optional.of(goal));

            when(repository.searchAllbyGoalId(
                    20L,
                    pageable
            )).thenReturn(page);

            Page<GoalItemMinDTO> result =
                    service.findAll(20L, pageable);

            assertNotNull(result);

            assertEquals(
                    1,
                    result.getTotalElements()
            );

            assertEquals(
                    120L,
                    result.getContent()
                            .get(0)
                            .targetInMinutes()
            );

            assertEquals(
                    "Java",
                    result.getContent()
                            .get(0)
                            .subjectName()
            );

            verify(goalRepository)
                    .findById(20L);

            verify(authenticatedUser)
                    .validateOwnership(10L);

            verify(repository)
                    .searchAllbyGoalId(
                            20L,
                            pageable
                    );
        }
    }

    @Nested
    class AddGoalItems {

        @Test
        void deveAdicionarGoalItems() {

            when(goalRepository.findById(20L))
                    .thenReturn(Optional.of(goal));

            when(subjectRepository.findByIdAndUserId(
                    30L,
                    10L
            )).thenReturn(Optional.of(subject));

            List<GoalItemCreateDTO> dto = List.of(
                    new GoalItemCreateDTO(
                            120L,
                            30L
                    ),
                    new GoalItemCreateDTO(
                            60L,
                            30L
                    )
            );

            service.addGoalItems(20L, dto);

            assertEquals(
                    2,
                    goal.getGoalItems().size()
            );

            GoalItem first =
                    goal.getGoalItems().get(0);

            assertEquals(
                    120L,
                    first.getTargetInMinutes()
            );

            assertEquals(
                    subject,
                    first.getSubject()
            );

            assertEquals(
                    goal,
                    first.getGoal()
            );

            GoalItem second =
                    goal.getGoalItems().get(1);

            assertEquals(
                    60L,
                    second.getTargetInMinutes()
            );

            assertEquals(
                    subject,
                    second.getSubject()
            );

            assertEquals(
                    goal,
                    second.getGoal()
            );

            verify(goalRepository)
                    .findById(20L);

            verify(authenticatedUser)
                    .validateOwnership(10L);

            verify(
                    subjectRepository,
                    times(2)
            ).findByIdAndUserId(30L, 10L);

            verifyNoInteractions(repository);
        }

        @Test
        void deveLancarExcecaoQuandoGoalNaoExistir() {

            when(goalRepository.findById(20L))
                    .thenReturn(Optional.empty());

            List<GoalItemCreateDTO> dto = List.of(
                    new GoalItemCreateDTO(
                            120L,
                            30L
                    )
            );

            assertThrows(
                    ResourceNotFoundException.class,
                    () -> service.addGoalItems(20L, dto)
            );

            verify(goalRepository)
                    .findById(20L);

            verifyNoInteractions(
                    authenticatedUser,
                    repository,
                    subjectRepository
            );
        }

        @Test
        void deveLancarExcecaoQuandoSubjectNaoExistir() {

            when(goalRepository.findById(20L))
                    .thenReturn(Optional.of(goal));

            when(subjectRepository.findByIdAndUserId(
                    99L,
                    10L
            )).thenReturn(Optional.empty());

            List<GoalItemCreateDTO> dto = List.of(
                    new GoalItemCreateDTO(
                            120L,
                            99L
                    )
            );

            assertThrows(
                    ResourceNotFoundException.class,
                    () -> service.addGoalItems(20L, dto)
            );

            assertTrue(
                    goal.getGoalItems().isEmpty()
            );

            verify(subjectRepository)
                    .findByIdAndUserId(99L, 10L);
        }
    }

    @Nested
    class Update {

        @Test
        void deveAtualizarGoalItem() {

            when(goalRepository.findById(20L))
                    .thenReturn(Optional.of(goal));

            when(repository.findByIdAndGoalId(
                    40L,
                    20L
            )).thenReturn(Optional.of(goalItem));

            when(subjectRepository.findByIdAndUserId(
                    30L,
                    10L
            )).thenReturn(Optional.of(subject));

            GoalItemUpdateDTO dto =
                    new GoalItemUpdateDTO(
                            180L,
                            30L
                    );

            GoalItemDTO result =
                    service.update(
                            20L,
                            40L,
                            dto
                    );

            assertEquals(
                    180L,
                    result.targetInMinutes()
            );

            assertEquals(
                    180L,
                    goalItem.getTargetInMinutes()
            );

            assertEquals(
                    subject,
                    goalItem.getSubject()
            );

            verify(authenticatedUser)
                    .validateOwnership(10L);

            verify(repository)
                    .findByIdAndGoalId(40L, 20L);

            verify(subjectRepository)
                    .findByIdAndUserId(30L, 10L);

            verify(repository, never())
                    .save(any());
        }

        @Test
        void deveLancarExcecaoQuandoGoalNaoExistir() {

            when(goalRepository.findById(20L))
                    .thenReturn(Optional.empty());

            GoalItemUpdateDTO dto =
                    new GoalItemUpdateDTO(
                            180L,
                            30L
                    );

            assertThrows(
                    ResourceNotFoundException.class,
                    () -> service.update(
                            20L,
                            40L,
                            dto
                    )
            );

            verifyNoInteractions(
                    authenticatedUser,
                    repository,
                    subjectRepository
            );
        }

        @Test
        void deveLancarExcecaoQuandoGoalItemNaoExistir() {

            when(goalRepository.findById(20L))
                    .thenReturn(Optional.of(goal));

            when(repository.findByIdAndGoalId(
                    40L,
                    20L
            )).thenReturn(Optional.empty());

            GoalItemUpdateDTO dto =
                    new GoalItemUpdateDTO(
                            180L,
                            30L
                    );

            assertThrows(
                    ResourceNotFoundException.class,
                    () -> service.update(
                            20L,
                            40L,
                            dto
                    )
            );

            verify(authenticatedUser)
                    .validateOwnership(10L);

            verify(repository)
                    .findByIdAndGoalId(40L, 20L);

            verifyNoInteractions(subjectRepository);
        }

        @Test
        void deveLancarExcecaoQuandoSubjectNaoPertencerAoUsuario() {

            when(goalRepository.findById(20L))
                    .thenReturn(Optional.of(goal));

            when(repository.findByIdAndGoalId(
                    40L,
                    20L
            )).thenReturn(Optional.of(goalItem));

            when(subjectRepository.findByIdAndUserId(
                    99L,
                    10L
            )).thenReturn(Optional.empty());

            GoalItemUpdateDTO dto =
                    new GoalItemUpdateDTO(
                            180L,
                            99L
                    );

            assertThrows(
                    ResourceNotFoundException.class,
                    () -> service.update(
                            20L,
                            40L,
                            dto
                    )
            );

            verify(subjectRepository)
                    .findByIdAndUserId(99L, 10L);
        }
    }

    @Nested
    class Delete {

        @Test
        void deveRemoverGoalItem() {

            goal.getGoalItems().add(goalItem);

            when(goalRepository.findById(20L))
                    .thenReturn(Optional.of(goal));

            when(repository.findByIdAndGoalId(
                    40L,
                    20L
            )).thenReturn(Optional.of(goalItem));

            service.delete(20L, 40L);

            assertTrue(
                    goal.getGoalItems().isEmpty()
            );

            verify(authenticatedUser)
                    .validateOwnership(10L);

            verify(repository)
                    .findByIdAndGoalId(40L, 20L);

            verify(repository, never())
                    .delete(any());
        }

        @Test
        void deveLancarExcecaoQuandoGoalNaoExistir() {

            when(goalRepository.findById(20L))
                    .thenReturn(Optional.empty());

            assertThrows(
                    ResourceNotFoundException.class,
                    () -> service.delete(20L, 40L)
            );

            verify(goalRepository)
                    .findById(20L);

            verifyNoInteractions(
                    authenticatedUser,
                    repository
            );
        }

        @Test
        void deveLancarExcecaoQuandoGoalItemNaoExistir() {

            when(goalRepository.findById(20L))
                    .thenReturn(Optional.of(goal));

            when(repository.findByIdAndGoalId(
                    40L,
                    20L
            )).thenReturn(Optional.empty());

            assertThrows(
                    ResourceNotFoundException.class,
                    () -> service.delete(20L, 40L)
            );

            verify(authenticatedUser)
                    .validateOwnership(10L);

            verify(repository)
                    .findByIdAndGoalId(40L, 20L);

            verify(repository, never())
                    .delete(any());
        }
    }

}