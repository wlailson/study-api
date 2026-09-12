package io.github.wlailson.study_api.controller;

import io.github.wlailson.study_api.dto.RevisionResponseDTO;
import io.github.wlailson.study_api.model.RevisionStatus;
import io.github.wlailson.study_api.projections.RevisionMinProjection;
import io.github.wlailson.study_api.service.RevisionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
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
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RevisionController.class)
class RevisionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RevisionService service;

    @Test
    @WithMockUser(roles = "CLIENT")
    void findById_shouldReturnRevision() throws Exception {

        RevisionResponseDTO response =
                new RevisionResponseDTO(
                        1L,
                        "Java",
                        LocalDate.of(2026, 9, 15),
                        null,
                        null,
                        null,
                        RevisionStatus.PENDING
                );

        when(service.findById(1L))
                .thenReturn(response);

        mockMvc.perform(
                        get("/revisions/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.topic").value("Java"))
                .andExpect(jsonPath("$.scheduledDate")
                        .value("2026-09-15"))
                .andExpect(jsonPath("$.status")
                        .value("PENDING"));

        verify(service).findById(1L);
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void findAll_shouldReturnAllRevisions() throws Exception {

        RevisionMinProjection revision1 =
                new RevisionProjection(
                        1L,
                        RevisionStatus.PENDING,
                        LocalDate.of(2026, 9, 15)
                );

        RevisionMinProjection revision2 =
                new RevisionProjection(
                        2L,
                        RevisionStatus.COMPLETED,
                        LocalDate.of(2026, 9, 16)
                );

        when(service.findAll(null))
                .thenReturn(List.of(revision1, revision2));

        mockMvc.perform(
                        get("/revisions")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status")
                        .value("PENDING"))
                .andExpect(jsonPath("$[0].scheduledDate")
                        .value("2026-09-15"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].status")
                        .value("COMPLETED"))
                .andExpect(jsonPath("$[1].scheduledDate")
                        .value("2026-09-16"));

        verify(service).findAll(null);
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void findAll_shouldFilterByStatus() throws Exception {

        RevisionMinProjection revision =
                new RevisionProjection(
                        1L,
                        RevisionStatus.PENDING,
                        LocalDate.of(2026, 9, 15)
                );

        when(service.findAll(RevisionStatus.PENDING))
                .thenReturn(List.of(revision));

        mockMvc.perform(
                        get("/revisions")
                                .param("status", "PENDING")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status")
                        .value("PENDING"))
                .andExpect(jsonPath("$[0].scheduledDate")
                        .value("2026-09-15"));

        verify(service).findAll(RevisionStatus.PENDING);
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void findAll_shouldReturnBadRequest_whenStatusIsInvalid()
            throws Exception {

        mockMvc.perform(
                        get("/revisions")
                                .param("status", "INVALID")
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service);
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void conclude_shouldReturnCompletedRevision()
            throws Exception {

        RevisionResponseDTO response =
                new RevisionResponseDTO(
                        1L,
                        "Java",
                        LocalDate.of(2026, 9, 15),
                        LocalDate.of(2026, 9, 15),
                        60L,
                        10L,
                        RevisionStatus.COMPLETED
                );

        when(service.conclude(eq(1L), any()))
                .thenReturn(response);

        String json = """
                {
                    "durationInMinutes": 60,
                    "breakTimeInMinutes": 10
                }
                """;

        mockMvc.perform(
                        put("/revisions/1")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.topic").value("Java"))
                .andExpect(jsonPath("$.scheduledDate")
                        .value("2026-09-15"))
                .andExpect(jsonPath("$.completedDate")
                        .value("2026-09-15"))
                .andExpect(jsonPath("$.durationInMinutes")
                        .value(60))
                .andExpect(jsonPath("$.breakTimeInMinutes")
                        .value(10))
                .andExpect(jsonPath("$.status")
                        .value("COMPLETED"));

        verify(service).conclude(eq(1L), any());
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void delete_shouldReturnNoContent() throws Exception {

        doNothing().when(service).delete(1L);

        mockMvc.perform(
                        delete("/revisions/1")
                                .with(csrf())
                )
                .andExpect(status().isNoContent());

        verify(service).delete(1L);
    }

    private record RevisionProjection(
            Long id,
            RevisionStatus status,
            LocalDate scheduledDate
    ) implements RevisionMinProjection {
        @Override
        public Long getId() {
            return 0L;
        }

        @Override
        public RevisionStatus getStatus() {
            return null;
        }

        @Override
        public LocalDate getScheduledDate() {
            return null;
        }
    }
}