package io.github.wlailson.study_api.service;

import io.github.wlailson.study_api.authentication.AuthenticatedUser;
import io.github.wlailson.study_api.dto.RevisionDTO;
import io.github.wlailson.study_api.dto.RevisionListDTO;
import io.github.wlailson.study_api.dto.RevisionUpdateDTO;
import io.github.wlailson.study_api.model.Revision;
import io.github.wlailson.study_api.model.StudySession;
import io.github.wlailson.study_api.model.User;
import io.github.wlailson.study_api.repository.RevisionRepository;
import io.github.wlailson.study_api.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.AfterEach;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RevisionServiceTest {


    @Mock
    private RevisionRepository repository;

    @Mock
    private AuthenticatedUser authenticatedUser;

    @InjectMocks
    private RevisionService service;

    private User user;
    private StudySession session;
    private Revision revision;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(1L);

        session = new StudySession();
        session.setUser(user);

        revision = new Revision();
        revision.setId(1L);
        revision.setDate(LocalDate.of(2026, 9, 15));
        revision.setSession(session);
    }

    @AfterEach
    void tearDown() {
        // limpa interações/estado caso necessário
    }

    @Nested
    class FindById {

        @Test
        void deveBuscarRevision() {

            when(repository.findById(1L))
                    .thenReturn(Optional.of(revision));

            RevisionDTO result = service.findById(1L);

            assertNotNull(result);
            assertEquals(1L, result.id());
            assertEquals(
                    LocalDate.of(2026, 9, 15),
                    result.date()
            );

            verify(repository).findById(1L);

            verify(authenticatedUser)
                    .validateOwnership(1L);
        }

        @Test
        void deveLancarExcecaoQuandoNaoEncontrar() {

            when(repository.findById(1L))
                    .thenReturn(Optional.empty());

            assertThrows(
                    ResourceNotFoundException.class,
                    () -> service.findById(1L)
            );

            verify(repository).findById(1L);

            verifyNoInteractions(authenticatedUser);
        }

        @Test
        void deveValidarOwnership() {

            when(repository.findById(1L))
                    .thenReturn(Optional.of(revision));

            service.findById(1L);

            verify(authenticatedUser)
                    .validateOwnership(1L);
        }
    }

    @Nested
    class FindAll {

        @Test
        void deveRetornarRevisionsDoUsuario() {

            Pageable pageable = PageRequest.of(0, 10);

            RevisionListDTO dto = new RevisionListDTO(
                    1L,
                    "Java",
                    LocalDate.of(2026, 9, 15)
            );

            Page<RevisionListDTO> page =
                    new PageImpl<>(List.of(dto));

            when(authenticatedUser.get())
                    .thenReturn(user);

            when(repository.searchAllByUserId(1L, pageable))
                    .thenReturn(page);

            Page<RevisionListDTO> result =
                    service.findAll(pageable);

            assertNotNull(result);
            assertEquals(1, result.getTotalElements());

            assertEquals(
                    1L,
                    result.getContent().get(0).id()
            );

            assertEquals(
                    "Java",
                    result.getContent().get(0).subjectName()
            );

            verify(authenticatedUser).get();

            verify(repository)
                    .searchAllByUserId(1L, pageable);
        }
    }

    @Test
    void deveRetornarPaginaVazia() {

        Pageable pageable = PageRequest.of(0, 10);

        Page<RevisionListDTO> page =
                new PageImpl<>(List.of());

        when(authenticatedUser.get())
                .thenReturn(user);

        when(repository.searchAllByUserId(1L, pageable))
                .thenReturn(page);

        Page<RevisionListDTO> result =
                service.findAll(pageable);

        assertTrue(result.isEmpty());

        verify(repository)
                .searchAllByUserId(1L, pageable);
    }

    @Nested
    class Update {

        @Test
        void deveAtualizarData() {

            when(repository.findById(1L))
                    .thenReturn(Optional.of(revision));

            RevisionUpdateDTO dto =
                    new RevisionUpdateDTO(
                            LocalDate.of(2026, 9, 20)
                    );

            RevisionDTO result =
                    service.update(1L, dto);

            assertEquals(
                    LocalDate.of(2026, 9, 20),
                    result.date()
            );

            assertEquals(
                    LocalDate.of(2026, 9, 20),
                    revision.getDate()
            );

            verify(authenticatedUser)
                    .validateOwnership(1L);

            verify(repository)
                    .findById(1L);

            // Não precisa de repository.save()
            verify(repository, never())
                    .save(any());
        }

        @Test
        void deveLancarExcecaoQuandoNaoEncontrar() {

            when(repository.findById(1L))
                    .thenReturn(Optional.empty());

            RevisionUpdateDTO dto =
                    new RevisionUpdateDTO(
                            LocalDate.of(2026, 9, 20)
                    );

            assertThrows(
                    ResourceNotFoundException.class,
                    () -> service.update(1L, dto)
            );

            verify(repository).findById(1L);

            verifyNoInteractions(authenticatedUser);
        }
    }

    @Nested
    class Delete {

        @Test
        void deveDeletarRevision() {

            when(repository.findById(1L))
                    .thenReturn(Optional.of(revision));

            service.delete(1L);

            verify(repository)
                    .findById(1L);

            verify(authenticatedUser)
                    .validateOwnership(1L);

            verify(repository)
                    .delete(revision);
        }

        @Test
        void deveLancarExcecaoQuandoNaoEncontrar() {

            when(repository.findById(1L))
                    .thenReturn(Optional.empty());

            assertThrows(
                    ResourceNotFoundException.class,
                    () -> service.delete(1L)
            );

            verify(repository)
                    .findById(1L);

            verifyNoInteractions(authenticatedUser);

            verify(repository, never())
                    .delete(any());
        }
    }
}

