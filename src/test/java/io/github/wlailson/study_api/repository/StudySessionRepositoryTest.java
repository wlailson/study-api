package io.github.wlailson.study_api.repository;

import io.github.wlailson.study_api.dto.StudySessionMinDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class StudySessionRepositoryTest {

    @Autowired
    private StudySessionRepository repository;

    @Test
    void deveBuscarSomenteSessoesCompletadasDoUsuario() {

        Page<StudySessionMinDTO> result =
                repository.searchSessions(
                        PageRequest.of(0, 10),
                        1L
                );

        assertEquals(3, result.getTotalElements());
        assertEquals(3, result.getContent().size());

        StudySessionMinDTO first = result.getContent().get(0);

        assertEquals(1L, first.id());
        assertEquals("Java", first.subjectName());
        assertEquals("Herança", first.topic());
        assertEquals(60L, first.durationInMinutes());
    }

    @Test
    void deveBuscarSomenteSessoesDoUsuarioInformado() {

        Page<StudySessionMinDTO> result =
                repository.searchSessions(
                        PageRequest.of(0, 10),
                        2L
                );

        assertEquals(2, result.getTotalElements());

        assertEquals(
                "JavaScript",
                result.getContent().get(0).subjectName()
        );

        assertEquals(
                "React",
                result.getContent().get(1).subjectName()
        );
    }

    @Test
    void naoDeveRetornarSessoesEmAndamento() {

        Page<StudySessionMinDTO> result =
                repository.searchSessions(
                        PageRequest.of(0, 10),
                        1L
                );

        assertTrue(
                result.getContent()
                        .stream()
                        .noneMatch(session ->
                                "Relacionamentos JPA".equals(session.topic())
                        )
        );
    }

    @Test
    void deveRespeitarPaginacao() {

        Page<StudySessionMinDTO> result =
                repository.searchSessions(
                        PageRequest.of(0, 2),
                        1L
                );

        assertEquals(3, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals(2, result.getTotalPages());
    }

    @Test
    void deveCalcularTempoEstudadoNoPeriodo() {

        Long result = repository.studied(
                1L,
                LocalDateTime.of(2026, 9, 7, 0, 0),
                LocalDateTime.of(2026, 9, 8, 0, 0)
        );

        assertEquals(150L, result);
    }

    @Test
    void deveRespeitarIntervaloDeDatas() {

        Long result = repository.studied(
                1L,
                LocalDateTime.of(2026, 9, 8, 0, 0),
                LocalDateTime.of(2026, 9, 9, 0, 0)
        );

        assertEquals(120L, result);
    }

    @Test
    void naoDeveContabilizarSessoesEmAndamento() {

        Long result = repository.studied(
                1L,
                LocalDateTime.of(2026, 9, 8, 0, 0),
                LocalDateTime.of(2026, 9, 9, 0, 0)
        );

        assertEquals(120L, result);
    }

    @Test
    void deveRetornarZeroQuandoNaoHouverSessoes() {

        Long result = repository.studied(
                1L,
                LocalDateTime.of(2026, 10, 1, 0, 0),
                LocalDateTime.of(2026, 10, 2, 0, 0)
        );

        assertEquals(0L, result);
    }
}