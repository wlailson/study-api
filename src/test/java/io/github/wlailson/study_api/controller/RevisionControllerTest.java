package io.github.wlailson.study_api.controller;

import io.github.wlailson.study_api.dto.RevisionDTO;
import io.github.wlailson.study_api.dto.RevisionListDTO;
import io.github.wlailson.study_api.dto.RevisionUpdateDTO;
import io.github.wlailson.study_api.service.RevisionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RevisionController.class)
@WithMockUser(roles = "CLIENT")
class RevisionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RevisionService service;

    @Test
    void deveBuscarRevisionPorId() throws Exception {

        RevisionDTO dto = new RevisionDTO(
                1L,
                LocalDate.of(2026, 9, 7)
        );

        when(service.findById(1L))
                .thenReturn(dto);

        mockMvc.perform(
                        get("/revisions/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.date").value("2026-09-07"));

        verify(service).findById(1L);
    }

    @Test
    void deveBuscarTodasAsRevisions() throws Exception {

        RevisionListDTO dto = new RevisionListDTO(
                1L,
                "Java",
                LocalDate.of(2026, 9, 7)
        );

        Page<RevisionListDTO> page =
                new PageImpl<>(List.of(dto));

        when(service.findAll(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(
                        get("/revisions")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].subjectName").value("Java"))
                .andExpect(jsonPath("$.content[0].date").value("2026-09-07"));

        verify(service).findAll(any(Pageable.class));
    }

    @Test
    void deveAtualizarRevision() throws Exception {

        RevisionDTO dto = new RevisionDTO(
                1L,
                LocalDate.of(2026, 9, 10)
        );

        when(service.update(
                eq(1L),
                any(RevisionUpdateDTO.class)
        )).thenReturn(dto);

        mockMvc.perform(
                        put("/revisions/1")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {
                                    "date": "2026-09-10"
                                }
                                """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.date").value("2026-09-10"));

        verify(service).update(
                eq(1L),
                any(RevisionUpdateDTO.class)
        );
    }

    @Test
    void deveDeletarRevision() throws Exception {

        doNothing()
                .when(service)
                .delete(1L);

        mockMvc.perform(
                        delete("/revisions/1")
                                .with(csrf())
                )
                .andExpect(status().isNoContent());

        verify(service).delete(1L);
    }

    @Test
    void deveAceitarPaginacao() throws Exception {

        Page<RevisionListDTO> page =
                new PageImpl<>(
                        List.of(),
                        PageRequest.of(1, 5),
                        10
                );

        when(service.findAll(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(
                        get("/revisions")
                                .param("page", "1")
                                .param("size", "5")
                )
                .andExpect(status().isOk());

        verify(service).findAll(any(Pageable.class));
    }
}