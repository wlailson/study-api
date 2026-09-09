package io.github.wlailson.study_api.controller;

import io.github.wlailson.study_api.dto.GoalCreateDTO;
import io.github.wlailson.study_api.dto.GoalDTO;
import io.github.wlailson.study_api.dto.GoalMinDTO;
import io.github.wlailson.study_api.dto.GoalUpdateDTO;
import io.github.wlailson.study_api.service.GoalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GoalController.class)
class GoalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GoalService service;

    @Test
    @WithMockUser(roles = "CLIENT")
    void deveBuscarTodosOsGoals() throws Exception {

        GoalMinDTO goal = new GoalMinDTO(
                1L,
                "Aprender Java",
                LocalDate.of(2026, 9, 7),
                LocalDate.of(2026, 9, 13)
        );

        PageImpl<GoalMinDTO> page =
                new PageImpl<>(
                        List.of(goal),
                        PageRequest.of(0, 10),
                        1
                );

        when(service.findAll(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/goals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].title")
                        .value("Aprender Java"))
                .andExpect(jsonPath("$.content[0].startDate")
                        .value("2026-09-07"))
                .andExpect(jsonPath("$.content[0].endDate")
                        .value("2026-09-13"));

        verify(service).findAll(any(Pageable.class));
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void deveBuscarGoalPorId() throws Exception {

        GoalDTO goal = new GoalDTO(
                1L,
                "Aprender Java",
                LocalDate.of(2026, 9, 7),
                LocalDate.of(2026, 9, 13),
                null,
                List.of(),
                150L
        );

        when(service.findById(1L))
                .thenReturn(goal);

        mockMvc.perform(get("/goals/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title")
                        .value("Aprender Java"))
                .andExpect(jsonPath("$.studied")
                        .value(150));

        verify(service).findById(1L);
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void deveCriarGoal() throws Exception {

        GoalDTO goal = new GoalDTO(
                1L,
                "Aprender Java",
                LocalDate.of(2026, 9, 7),
                LocalDate.of(2026, 9, 13),
                null,
                List.of(),
                0L
        );

        when(service.create(any(GoalCreateDTO.class)))
                .thenReturn(goal);

        mockMvc.perform(
                        post("/goals")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "title": "Aprender Java",
                                        "startDate": "2026-09-07",
                                        "endDate": "2026-09-13",
                                        "items": []
                                    }
                                    """)
                )
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        "Location",
                        "http://localhost/goals/1"
                ))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title")
                        .value("Aprender Java"));

        verify(service).create(any(GoalCreateDTO.class));
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void deveAtualizarGoal() throws Exception {

        GoalUpdateDTO goal = new GoalUpdateDTO(
                1L,
                "Dominar Java",
                LocalDate.of(2026, 9, 7),
                LocalDate.of(2026, 9, 20)
        );

        when(service.update(
                eq(1L),
                any(GoalUpdateDTO.class)
        )).thenReturn(goal);

        mockMvc.perform(
                        put("/goals/1")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "id": 1,
                                            "title": "Dominar Java",
                                            "startDate": "2026-09-07",
                                            "endDate": "2026-09-20"
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title")
                        .value("Dominar Java"))
                .andExpect(jsonPath("$.startDate")
                        .value("2026-09-07"))
                .andExpect(jsonPath("$.endDate")
                        .value("2026-09-20"));

        verify(service).update(
                eq(1L),
                any(GoalUpdateDTO.class)
        );
    }


    @Test
    @WithMockUser(roles = "CLIENT")
    void deveDeletarGoal() throws Exception {

        doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/goals/1").with(csrf()))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(service).delete(1L);
    }


}