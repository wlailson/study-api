package io.github.wlailson.study_api.controller;

import io.github.wlailson.study_api.dto.*;
import io.github.wlailson.study_api.model.SessionStatus;
import io.github.wlailson.study_api.service.StudySessionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudySessionController.class)
@WithMockUser(roles = "CLIENT")
class StudySessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudySessionService service;

    @Test
    void deveBuscarTodasAsSessoes() throws Exception {

        StudySessionMinDTO dto = new StudySessionMinDTO(
                1L,
                "Java",
                "Herança",
                60L
        );

        Page<StudySessionMinDTO> page =
                new PageImpl<>(List.of(dto));

        when(service.findAll(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(
                        get("/sessions")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].subjectName").value("Java"))
                .andExpect(jsonPath("$.content[0].topic").value("Herança"))
                .andExpect(jsonPath("$.content[0].durationInMinutes").value(60));

        verify(service).findAll(any(Pageable.class));
    }

    @Test
    void deveBuscarSessaoEmAndamento() throws Exception {

        StudySessionDTO dto = new StudySessionDTO(
                1L,
                "Herança",
                60L,
                10L,
                SessionStatus.IN_PROGRESS,
                Instant.parse("2026-09-07T10:00:00Z"),
                null,
                null,
                null,
                List.of()
        );

        when(service.findSessionInProgress())
                .thenReturn(dto);

        mockMvc.perform(
                        get("/sessions/current")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.topic").value("Herança"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));

        verify(service).findSessionInProgress();
    }

    @Test
    void deveBuscarSessaoPorId() throws Exception {

        StudySessionDTO dto = new StudySessionDTO(
                1L,
                "Herança",
                60L,
                10L,
                SessionStatus.COMPLETED,
                Instant.parse("2026-09-07T10:00:00Z"),
                Instant.parse("2026-09-07T11:10:00Z"),
                null,
                null,
                List.of()
        );

        when(service.findById(1L))
                .thenReturn(dto);

        mockMvc.perform(
                        get("/sessions/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.topic").value("Herança"))
                .andExpect(jsonPath("$.durationInMinutes").value(60))
                .andExpect(jsonPath("$.breakTimeInMinutes").value(10))
                .andExpect(jsonPath("$.status").value("COMPLETED"));

        verify(service).findById(1L);
    }

    @Test
    void deveIniciarSessao() throws Exception {

        when(service.startSession(1L))
                .thenReturn(10L);

        mockMvc.perform(
                        post("/sessions/start")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {
                                    "subjectId": 1
                                }
                                """)
                )
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        "Location",
                        "http://localhost/sessions/10"
                ));

        verify(service).startSession(1L);
    }

    @Test
    void deveFinalizarSessao() throws Exception {

        StudySessionDTO dto = new StudySessionDTO(
                1L,
                "Herança",
                60L,
                10L,
                SessionStatus.COMPLETED,
                Instant.parse("2026-09-07T10:00:00Z"),
                Instant.parse("2026-09-07T11:10:00Z"),
                null,
                null,
                List.of()
        );

        when(service.endSession(any(StudySessionEndDTO.class)))
                .thenReturn(dto);

        mockMvc.perform(
                        post("/sessions/end")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                                "topic": "Herança",
                                "durationInMinutes": 60,
                                "breakTimeInMinutes": 10,
                                "revisions": []
                            }
                            """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.topic").value("Herança"))
                .andExpect(jsonPath("$.status").value("COMPLETED"));

        verify(service).endSession(any(StudySessionEndDTO.class));
    }

    @Test
    void deveAtualizarSessao() throws Exception {

        StudySessionDTO dto = new StudySessionDTO(
                1L,
                "Polimorfismo",
                90L,
                15L,
                SessionStatus.COMPLETED,
                Instant.parse("2026-09-07T14:00:00Z"),
                Instant.parse("2026-09-07T15:45:00Z"),
                null,
                null,
                List.of()
        );

        when(service.updateSession(
                eq(1L),
                any(StudySessionUpdateDTO.class)
        )).thenReturn(dto);

        mockMvc.perform(
                        put("/sessions/1")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                                "topic": "Polimorfismo",
                                "durationInMinutes": 90,
                                "breakTimeInMinutes": 15
                            }
                            """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.topic").value("Polimorfismo"))
                .andExpect(jsonPath("$.durationInMinutes").value(90))
                .andExpect(jsonPath("$.breakTimeInMinutes").value(15))
                .andExpect(jsonPath("$.status").value("COMPLETED"));

        verify(service).updateSession(
                eq(1L),
                any(StudySessionUpdateDTO.class)
        );
    }

    @Test
    void deveDeletarSessao() throws Exception {

        doNothing()
                .when(service)
                .deleteSession(1L);

        mockMvc.perform(
                        delete("/sessions/1")
                                .with(csrf())
                )
                .andExpect(status().isNoContent());

        verify(service).deleteSession(1L);
    }
}