package io.github.wlailson.study_api.repository;

import io.github.wlailson.study_api.dto.RevisionListDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RevisionRepositoryTest {
    @Autowired
    private RevisionRepository repository;

    @Test
    void deveBuscarTodasAsRevisoesDoUsuario() {

        Page<RevisionListDTO> result =
                repository.searchAllByUserId(
                        1L,
                        PageRequest.of(0, 10)
                );

        assertEquals(7, result.getTotalElements());

        RevisionListDTO first = result.getContent().get(0);

        assertEquals(1L, first.id());
        assertEquals("Java", first.subjectName());
        assertEquals(
                LocalDate.of(2026, 9, 7),
                first.date()
        );
    }

    @Test
    void deveRetornarRevisoesOrdenadasPorData() {

        Page<RevisionListDTO> result =
                repository.searchAllByUserId(
                        1L,
                        PageRequest.of(0, 10)
                );

        List<RevisionListDTO> revisions = result.getContent();

        assertEquals(
                LocalDate.of(2026, 9, 7),
                revisions.get(0).date()
        );

        assertEquals(
                LocalDate.of(2026, 9, 8),
                revisions.get(1).date()
        );

        assertEquals(
                LocalDate.of(2026, 9, 8),
                revisions.get(2).date()
        );

        assertEquals(
                LocalDate.of(2026, 9, 14),
                revisions.get(3).date()
        );
    }

    @Test
    void deveBuscarSomenteRevisoesDoUsuarioInformado() {

        Page<RevisionListDTO> result =
                repository.searchAllByUserId(
                        2L,
                        PageRequest.of(0, 10)
                );

        assertEquals(2, result.getTotalElements());

        assertEquals(
                "JavaScript",
                result.getContent().get(0).subjectName()
        );

        assertEquals(
                "JavaScript",
                result.getContent().get(1).subjectName()
        );
    }

    @Test
    void deveRetornarPaginaVaziaQuandoUsuarioNaoPossuiRevisoes() {

        Page<RevisionListDTO> result =
                repository.searchAllByUserId(
                        4L,
                        PageRequest.of(0, 10)
                );

        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void deveRespeitarPaginacao() {

        Page<RevisionListDTO> result =
                repository.searchAllByUserId(
                        1L,
                        PageRequest.of(0, 2)
                );

        assertEquals(7, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals(4, result.getTotalPages());
    }
}