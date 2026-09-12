package io.github.wlailson.study_api.service;

import io.github.wlailson.study_api.dto.SubjectRequestDTO;
import io.github.wlailson.study_api.dto.SubjectResponseDTO;
import io.github.wlailson.study_api.model.Subject;
import io.github.wlailson.study_api.model.User;
import io.github.wlailson.study_api.projections.SubjectMinProjection;
import io.github.wlailson.study_api.repository.SubjectRepository;
import io.github.wlailson.study_api.service.exceptions.ConflictException;
import io.github.wlailson.study_api.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubjectServiceTest {

    @Mock
    private SubjectRepository repository;

    @Mock
    private AuthService authService;

    @InjectMocks
    private SubjectService service;

    @Test
    void findById_shouldReturnSubject() {

        User user = mock(User.class);
        Subject subject = mock(Subject.class);

        when(user.getId()).thenReturn(1L);

        when(authService.getCurrentUser())
                .thenReturn(user);

        when(repository.findByIdAndUser_Id(1L, 1L))
                .thenReturn(Optional.of(subject));

        when(subject.getId()).thenReturn(1L);
        when(subject.getName()).thenReturn("Java");

        SubjectResponseDTO response = service.findById(1L);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Java", response.name());

        verify(authService).getCurrentUser();
        verify(repository).findByIdAndUser_Id(1L, 1L);
    }

    @Test
    void findById_shouldThrowResourceNotFoundException_whenSubjectDoesNotExist() {

        User user = mock(User.class);

        when(user.getId()).thenReturn(1L);
        when(user.getName()).thenReturn("Maria");

        when(authService.getCurrentUser())
                .thenReturn(user);

        when(repository.findByIdAndUser_Id(1L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.findById(1L)
        );

        verify(repository).findByIdAndUser_Id(1L, 1L);
    }

    @Test
    void findAll_shouldReturnSubjects() {

        User user = mock(User.class);

        when(user.getId()).thenReturn(1L);

        when(authService.getCurrentUser())
                .thenReturn(user);

        SubjectMinProjection projection =
                mock(SubjectMinProjection.class);

        List<SubjectMinProjection> subjects =
                List.of(projection);

        when(repository.searchSubjectByUserId(1L))
                .thenReturn(subjects);

        List<SubjectMinProjection> response =
                service.findAll();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertSame(projection, response.get(0));

        verify(authService).getCurrentUser();
        verify(repository).searchSubjectByUserId(1L);
    }

    @Test
    void findAll_shouldReturnEmptyList_whenUserHasNoSubjects() {

        User user = mock(User.class);

        when(user.getId()).thenReturn(1L);

        when(authService.getCurrentUser())
                .thenReturn(user);

        when(repository.searchSubjectByUserId(1L))
                .thenReturn(List.of());

        List<SubjectMinProjection> response =
                service.findAll();

        assertNotNull(response);
        assertTrue(response.isEmpty());

        verify(repository).searchSubjectByUserId(1L);
    }

    @Test
    void create_shouldCreateAndReturnSubject() {

        User user = mock(User.class);

        when(authService.getCurrentUser())
                .thenReturn(user);

        when(user.getId()).thenReturn(1L);

        when(repository.findByNameIgnoreCaseAndUser_Id(
                "Java",
                1L
        )).thenReturn(Optional.empty());

        SubjectRequestDTO request =
                new SubjectRequestDTO("Java");

        SubjectResponseDTO response =
                service.create(request);

        assertNotNull(response);
        assertEquals("Java", response.name());

        ArgumentCaptor<Subject> captor =
                ArgumentCaptor.forClass(Subject.class);

        verify(repository).save(captor.capture());

        Subject subject = captor.getValue();

        assertEquals("Java", subject.getName());
        assertSame(user, subject.getUser());

        verify(repository)
                .findByNameIgnoreCaseAndUser_Id("Java", 1L);
    }

    @Test
    void create_shouldReturnExistingSubject_whenNameAlreadyExists() {

        User user = mock(User.class);
        Subject subject = mock(Subject.class);

        when(user.getId()).thenReturn(1L);

        when(authService.getCurrentUser())
                .thenReturn(user);

        when(repository.findByNameIgnoreCaseAndUser_Id(
                "Java",
                1L
        )).thenReturn(Optional.of(subject));

        when(subject.getId()).thenReturn(1L);
        when(subject.getName()).thenReturn("Java");

        SubjectRequestDTO request =
                new SubjectRequestDTO("Java");

        SubjectResponseDTO response =
                service.create(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Java", response.name());

        verify(repository).save(subject);

        verify(repository)
                .findByNameIgnoreCaseAndUser_Id("Java", 1L);
    }

    @Test
    void update_shouldUpdateSubjectName() {

        User user = mock(User.class);
        Subject subject = mock(Subject.class);

        when(user.getId()).thenReturn(1L);

        when(authService.getCurrentUser())
                .thenReturn(user);

        when(repository.findByIdAndUser_Id(1L, 1L))
                .thenReturn(Optional.of(subject));

        when(subject.getId()).thenReturn(1L);
        when(subject.getName()).thenReturn("Java");

        SubjectRequestDTO request =
                new SubjectRequestDTO("Java Avançado");

        SubjectResponseDTO response =
                service.update(1L, request);

        verify(subject).setName("Java Avançado");

        assertNotNull(response);
        assertEquals(1L, response.id());
    }

    @Test
    void update_shouldThrowResourceNotFoundException_whenSubjectDoesNotExist() {

        User user = mock(User.class);

        when(user.getId()).thenReturn(1L);
        when(user.getName()).thenReturn("Maria");

        when(authService.getCurrentUser())
                .thenReturn(user);

        when(repository.findByIdAndUser_Id(1L, 1L))
                .thenReturn(Optional.empty());

        SubjectRequestDTO request =
                new SubjectRequestDTO("Java");

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.update(1L, request)
        );

        verify(repository).findByIdAndUser_Id(1L, 1L);
    }

    @Test
    void delete_shouldDeleteSubject() {

        User user = mock(User.class);
        Subject subject = mock(Subject.class);

        when(user.getId()).thenReturn(1L);
        when(user.getName()).thenReturn("Maria");

        when(authService.getCurrentUser())
                .thenReturn(user);

        when(repository.findByIdAndUser_Id(1L, 1L))
                .thenReturn(Optional.of(subject));

        when(subject.getId()).thenReturn(1L);

        service.delete(1L);

        verify(repository).delete(subject);
        verify(repository).flush();
    }

    @Test
    void delete_shouldThrowResourceNotFoundException_whenSubjectDoesNotExist() {

        User user = mock(User.class);

        when(user.getId()).thenReturn(1L);
        when(user.getName()).thenReturn("Maria");

        when(authService.getCurrentUser())
                .thenReturn(user);

        when(repository.findByIdAndUser_Id(1L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.delete(1L)
        );

        verify(repository, never()).delete(any());
        verify(repository, never()).flush();
    }

    @Test
    void delete_shouldThrowConflictException_whenSubjectHasSessions() {

        User user = mock(User.class);
        Subject subject = mock(Subject.class);

        when(user.getId()).thenReturn(1L);
        when(user.getName()).thenReturn("Maria");

        when(authService.getCurrentUser())
                .thenReturn(user);

        when(repository.findByIdAndUser_Id(1L, 1L))
                .thenReturn(Optional.of(subject));

        when(subject.getId()).thenReturn(1L);

        doThrow(new DataIntegrityViolationException("FK violation"))
                .when(repository)
                .flush();

        assertThrows(
                ConflictException.class,
                () -> service.delete(1L)
        );

        verify(repository).delete(subject);
        verify(repository).flush();
    }
}