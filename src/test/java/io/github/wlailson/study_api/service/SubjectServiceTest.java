package io.github.wlailson.study_api.service;

import io.github.wlailson.study_api.authentication.AuthenticatedUser;
import io.github.wlailson.study_api.dto.SubjectDTO;
import io.github.wlailson.study_api.model.Subject;
import io.github.wlailson.study_api.model.User;
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
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubjectServiceTest {

    @Mock
    private SubjectRepository repository;

    @Mock
    private AuthenticatedUser authenticatedUser;

    @InjectMocks
    private SubjectService service;

    private User user;
    private Subject subject;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(1L);
        user.setName("Maria");
        user.setEmail("maria@gmail.com");

        subject = new Subject();
        subject.setId(1L);
        subject.setName("Java");
        subject.setUser(user);
    }

    // ============================================================
    // FIND BY ID
    // ============================================================

    @Nested
    class FindById {

        @Test
        void shouldReturnSubjectWhenExists() {

            when(repository.findById(1L))
                    .thenReturn(Optional.of(subject));

            SubjectDTO result = service.findById(1L);

            assertNotNull(result);
            assertEquals(1L, result.id());
            assertEquals("Java", result.name());

            verify(repository)
                    .findById(1L);

            verify(authenticatedUser)
                    .validateOwnership(1L);
        }

        @Test
        void shouldThrowResourceNotFoundExceptionWhenSubjectDoesNotExist() {

            when(repository.findById(1L))
                    .thenReturn(Optional.empty());

            assertThrows(
                    ResourceNotFoundException.class,
                    () -> service.findById(1L)
            );

            verify(repository)
                    .findById(1L);

            verify(authenticatedUser, never())
                    .validateOwnership(anyLong());
        }

        @Test
        void shouldValidateSubjectOwnership() {

            when(repository.findById(1L))
                    .thenReturn(Optional.of(subject));

            service.findById(1L);

            verify(authenticatedUser)
                    .validateOwnership(
                            subject.getUser().getId()
                    );
        }
    }

    // ============================================================
    // FIND ALL
    // ============================================================

    @Nested
    class FindAll {

        @Test
        void shouldReturnSubjectsFromAuthenticatedUser() {

            Pageable pageable = Pageable.ofSize(10);

            Page<Subject> page =
                    new PageImpl<>(List.of(subject));

            when(authenticatedUser.get())
                    .thenReturn(user);

            when(repository.findAllByUserId(
                    pageable,
                    user.getId()
            )).thenReturn(page);

            Page<SubjectDTO> result =
                    service.findAll(pageable);

            assertNotNull(result);
            assertEquals(1, result.getTotalElements());

            SubjectDTO dto = result.getContent().get(0);

            assertEquals(1L, dto.id());
            assertEquals("Java", dto.name());

            verify(authenticatedUser)
                    .get();

            verify(repository)
                    .findAllByUserId(
                            pageable,
                            user.getId()
                    );
        }

        @Test
        void shouldReturnEmptyPageWhenUserHasNoSubjects() {

            Pageable pageable = Pageable.ofSize(10);

            Page<Subject> page =
                    new PageImpl<>(List.of());

            when(authenticatedUser.get())
                    .thenReturn(user);

            when(repository.findAllByUserId(
                    pageable,
                    user.getId()
            )).thenReturn(page);

            Page<SubjectDTO> result =
                    service.findAll(pageable);

            assertNotNull(result);
            assertTrue(result.isEmpty());

            verify(repository)
                    .findAllByUserId(
                            pageable,
                            user.getId()
                    );
        }
    }

    // ============================================================
    // CREATE
    // ============================================================

    @Nested
    class Create {

        @Test
        void shouldCreateSubjectSuccessfully() {

            SubjectDTO dto =
                    new SubjectDTO(null, "Spring Boot");

            when(authenticatedUser.get())
                    .thenReturn(user);

            when(repository.save(any(Subject.class)))
                    .thenAnswer(invocation -> {

                        Subject saved =
                                invocation.getArgument(0);

                        saved.setId(10L);

                        return saved;
                    });

            SubjectDTO result =
                    service.create(dto);

            assertNotNull(result);
            assertEquals(10L, result.id());
            assertEquals("Spring Boot", result.name());

            verify(authenticatedUser)
                    .get();

            verify(repository)
                    .save(any(Subject.class));
        }

        @Test
        void shouldCreateSubjectWithCorrectData() {

            SubjectDTO dto =
                    new SubjectDTO(null, "Spring Boot");

            when(authenticatedUser.get())
                    .thenReturn(user);

            when(repository.save(any(Subject.class)))
                    .thenAnswer(invocation -> {

                        Subject saved =
                                invocation.getArgument(0);

                        saved.setId(10L);

                        return saved;
                    });

            service.create(dto);

            ArgumentCaptor<Subject> captor =
                    ArgumentCaptor.forClass(Subject.class);

            verify(repository)
                    .save(captor.capture());

            Subject saved = captor.getValue();

            assertEquals(
                    "Spring Boot",
                    saved.getName()
            );

            assertEquals(
                    user,
                    saved.getUser()
            );
        }
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @Nested
    class Update {

        @Test
        void shouldUpdateSubjectSuccessfully() {

            SubjectDTO dto =
                    new SubjectDTO(
                            1L,
                            "Spring Boot"
                    );

            when(repository.findById(1L))
                    .thenReturn(Optional.of(subject));

            SubjectDTO result =
                    service.update(1L, dto);

            assertNotNull(result);

            assertEquals(
                    "Spring Boot",
                    result.name()
            );

            assertEquals(
                    "Spring Boot",
                    subject.getName()
            );

            verify(repository)
                    .findById(1L);

            verify(authenticatedUser)
                    .validateOwnership(1L);
        }

        @Test
        void shouldThrowResourceNotFoundExceptionWhenSubjectDoesNotExist() {

            SubjectDTO dto =
                    new SubjectDTO(
                            1L,
                            "Spring Boot"
                    );

            when(repository.findById(1L))
                    .thenReturn(Optional.empty());

            assertThrows(
                    ResourceNotFoundException.class,
                    () -> service.update(1L, dto)
            );

            verify(authenticatedUser, never())
                    .validateOwnership(anyLong());
        }

        @Test
        void shouldNotSaveSubjectExplicitly() {

            SubjectDTO dto =
                    new SubjectDTO(
                            1L,
                            "Spring Boot"
                    );

            when(repository.findById(1L))
                    .thenReturn(Optional.of(subject));

            service.update(1L, dto);

            verify(repository, never())
                    .save(any(Subject.class));
        }
    }

    // ============================================================
    // DELETE
    // ============================================================

    @Nested
    class Delete {

        @Test
        void shouldDeleteSubjectSuccessfully() {

            when(repository.findById(1L))
                    .thenReturn(Optional.of(subject));

            service.delete(1L);

            verify(repository)
                    .findById(1L);

            verify(authenticatedUser)
                    .validateOwnership(1L);

            verify(repository)
                    .delete(subject);
        }

        @Test
        void shouldThrowResourceNotFoundExceptionWhenSubjectDoesNotExist() {

            when(repository.findById(1L))
                    .thenReturn(Optional.empty());

            assertThrows(
                    ResourceNotFoundException.class,
                    () -> service.delete(1L)
            );

            verify(repository, never())
                    .delete(any(Subject.class));

            verify(authenticatedUser, never())
                    .validateOwnership(anyLong());
        }
    }
}