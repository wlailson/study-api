package io.github.wlailson.study_api.controller;

import io.github.wlailson.study_api.dto.StudySessionRequestDTO;
import io.github.wlailson.study_api.dto.StudySessionResponseDTO;
import io.github.wlailson.study_api.dto.StudySessionResponseMinDTO;
import io.github.wlailson.study_api.service.StudySessionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudySessionController.class)
class StudySessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudySessionService service;

    @Test
    @WithMockUser(roles = "CLIENT")
    void findAll_shouldReturnSessions() throws Exception {

        StudySessionResponseMinDTO session1 =
                new StudySessionResponseMinDTO(
                        1L,
                        "Java",
                        "Spring Boot",
                        60L,
                        LocalDate.of(2026, 9, 10)
                );

        StudySessionResponseMinDTO session2 =
                new StudySessionResponseMinDTO(
                        2L,
                        "Java",
                        "JPA",
                        90L,
                        LocalDate.of(2026, 9, 11)
                );

        PageImpl<StudySessionResponseMinDTO> page =
                new PageImpl<>(
                        List.of(session1, session2),
                        PageRequest.of(0, 10),
                        2
                );

        when(service.findAll(any(), eq("")))
                .thenReturn(page);

        mockMvc.perform(
                        get("/sessions")
                                .param("page", "0")
                                .param("size", "10")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))

                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].subjectName").value("Java"))
                .andExpect(jsonPath("$.content[0].topic").value("Spring Boot"))
                .andExpect(jsonPath("$.content[0].durationInMinutes").value(60))
                .andExpect(jsonPath("$.content[0].date").value("2026-09-10"))

                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].subjectName").value("Java"))
                .andExpect(jsonPath("$.content[1].topic").value("JPA"))
                .andExpect(jsonPath("$.content[1].durationInMinutes").value(90))
                .andExpect(jsonPath("$.content[1].date").value("2026-09-11"))

                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1));

        verify(service).findAll(any(), eq(""));
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void findAll_shouldFilterByName() throws Exception {

        StudySessionResponseMinDTO session =
                new StudySessionResponseMinDTO(
                        1L,
                        "Java",
                        "Spring Boot",
                        60L,
                        LocalDate.of(2026, 9, 10)
                );

        PageImpl<StudySessionResponseMinDTO> page =
                new PageImpl<>(
                        List.of(session),
                        PageRequest.of(0, 10),
                        1
                );

        when(service.findAll(any(), eq("Java")))
                .thenReturn(page);

        mockMvc.perform(
                        get("/sessions")
                                .param("name", "Java")
                                .param("page", "0")
                                .param("size", "10")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].subjectName").value("Java"))
                .andExpect(jsonPath("$.content[0].topic").value("Spring Boot"))
                .andExpect(jsonPath("$.content[0].durationInMinutes").value(60))
                .andExpect(jsonPath("$.content[0].date").value("2026-09-10"))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(service).findAll(any(), eq("Java"));
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void findById_shouldReturnSession() throws Exception {

        StudySessionResponseDTO response =
                new StudySessionResponseDTO(
                        1L,
                        60L,
                        10L,
                        null,
                        null,
                        null,
                        List.of()
                );

        when(service.findById(1L))
                .thenReturn(response);

        mockMvc.perform(
                        get("/sessions/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.durationInMinutes").value(60))
                .andExpect(jsonPath("$.breakTimeInMinutes").value(10))
                .andExpect(jsonPath("$.revisions").isArray());

        verify(service).findById(1L);
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void saveSession_shouldReturnSession() throws Exception {

        StudySessionResponseDTO response =
                new StudySessionResponseDTO(
                        1L,
                        60L,
                        10L,
                        null,
                        null,
                        null,
                        List.of()
                );

        when(service.saveSession(
                eq(1L),
                any(StudySessionRequestDTO.class)
        )).thenReturn(response);

        String json = """
                {
                    "topic": "Spring Boot",
                    "durationInMinutes": 60,
                    "breakTimeInMinutes": 10,
                    "revisions": []
                }
                """;

        mockMvc.perform(
                        post("/sessions/1")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.durationInMinutes").value(60))
                .andExpect(jsonPath("$.breakTimeInMinutes").value(10))
                .andExpect(jsonPath("$.revisions").isArray());

        verify(service).saveSession(
                eq(1L),
                any(StudySessionRequestDTO.class)
        );
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void deleteSession_shouldReturnNoContent() throws Exception {

        doNothing().when(service).deleteSession(1L);

        mockMvc.perform(
                        delete("/sessions/1")
                                .with(csrf())
                )
                .andExpect(status().isNoContent());

        verify(service).deleteSession(1L);
    }
}