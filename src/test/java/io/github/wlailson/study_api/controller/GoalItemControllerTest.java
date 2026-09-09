package io.github.wlailson.study_api.controller;

import io.github.wlailson.study_api.dto.GoalItemCreateDTO;
import io.github.wlailson.study_api.dto.GoalItemDTO;
import io.github.wlailson.study_api.dto.GoalItemMinDTO;
import io.github.wlailson.study_api.dto.GoalItemUpdateDTO;
import io.github.wlailson.study_api.service.GoalItemService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GoalItemController.class)

class GoalItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GoalItemService service;

    @Test
    @WithMockUser(roles = "CLIENT")
    void deveBuscarGoalItemPorId() throws Exception {

        GoalItemDTO dto = new GoalItemDTO(
                1L,
                300L,
                null
        );

        when(service.findById(1L, 1L))
                .thenReturn(dto);

        mockMvc.perform(get("/1/itens/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.targetInMinutes").value(300));

        verify(service).findById(1L, 1L);
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void deveBuscarItensDoGoal() throws Exception {

        GoalItemMinDTO item = new GoalItemMinDTO(
                300L,
                "Java"
        );

        PageImpl<GoalItemMinDTO> page =
                new PageImpl<>(
                        List.of(item),
                        PageRequest.of(0, 10),
                        1
                );

        when(service.findAll(
                eq(1L),
                any(Pageable.class)
        )).thenReturn(page);

        mockMvc.perform(get("/1/itens"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].targetInMinutes")
                        .value(300))
                .andExpect(jsonPath("$.content[0].subjectName")
                        .value("Java"));

        verify(service).findAll(
                eq(1L),
                any(Pageable.class)
        );
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void deveAdicionarGoalItems() throws Exception {

        doNothing().when(service)
                .addGoalItems(eq(1L), anyList());

        mockMvc.perform(
                        post("/1/itens")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        [
                                            {
                                                "targetInMinutes": 300,
                                                "subjectId": 1
                                            },
                                            {
                                                "targetInMinutes": 240,
                                                "subjectId": 2
                                            }
                                        ]
                                        """)
                )
                .andExpect(status().isNoContent());

        verify(service).addGoalItems(
                eq(1L),
                anyList()
        );
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void deveAtualizarGoalItem() throws Exception {

        GoalItemDTO dto = new GoalItemDTO(
                1L,
                240L,
                null
        );

        when(service.update(
                eq(1L),
                eq(1L),
                any(GoalItemUpdateDTO.class)
        )).thenReturn(dto);

        mockMvc.perform(
                        put("/1/itens/1")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "targetInMinutes": 240,
                                            "subjectId": 2
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.targetInMinutes")
                        .value(240));

        verify(service).update(
                eq(1L),
                eq(1L),
                any(GoalItemUpdateDTO.class)
        );
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void deveDeletarGoalItem() throws Exception {

        doNothing().when(service)
                .delete(1L, 1L);

        mockMvc.perform(
                        delete("/1/itens/1")
                                .with(csrf())
                )
                .andExpect(status().isNoContent());

        verify(service).delete(1L, 1L);
    }
}


