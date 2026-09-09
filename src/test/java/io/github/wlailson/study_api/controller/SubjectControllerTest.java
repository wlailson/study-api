package io.github.wlailson.study_api.controller;

import static org.junit.jupiter.api.Assertions.*;

import io.github.wlailson.study_api.dto.SubjectDTO;
import io.github.wlailson.study_api.service.SubjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.*;
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

@WebMvcTest(SubjectController.class)
@WithMockUser(roles = "CLIENT")
class SubjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SubjectService service;

    @Test
    void deveBuscarSubjectPorId() throws Exception {

        SubjectDTO dto = new SubjectDTO(
                1L,
                "Java"
        );

        when(service.findById(1L))
                .thenReturn(dto);

        mockMvc.perform(
                        get("/subjects/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Java"));

        verify(service).findById(1L);
    }

    @Test
    void deveBuscarTodosOsSubjects() throws Exception {

        SubjectDTO dto = new SubjectDTO(
                1L,
                "Java"
        );

        Page<SubjectDTO> page =
                new PageImpl<>(List.of(dto));

        when(service.findAll(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(
                        get("/subjects")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Java"));

        verify(service).findAll(any(Pageable.class));
    }

    @Test
    void deveCriarSubject() throws Exception {

        SubjectDTO dto = new SubjectDTO(
                1L,
                "Java"
        );

        when(service.create(any(SubjectDTO.class)))
                .thenReturn(dto);

        mockMvc.perform(
                        post("/subjects")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {
                                    "name": "Java"
                                }
                                """)
                )
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        "Location",
                        "http://localhost/subjects/1"
                ))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Java"));

        verify(service).create(any(SubjectDTO.class));
    }

    @Test
    void deveAtualizarSubject() throws Exception {

        SubjectDTO dto = new SubjectDTO(
                1L,
                "Spring Boot"
        );

        when(service.update(
                eq(1L),
                any(SubjectDTO.class)
        )).thenReturn(dto);

        mockMvc.perform(
                        put("/subjects/1")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {
                                    "name": "Spring Boot"
                                }
                                """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Spring Boot"));

        verify(service).update(
                eq(1L),
                any(SubjectDTO.class)
        );
    }

    @Test
    void deveDeletarSubject() throws Exception {

        doNothing()
                .when(service)
                .delete(1L);

        mockMvc.perform(
                        delete("/subjects/1")
                                .with(csrf())
                )
                .andExpect(status().isNoContent());

        verify(service).delete(1L);
    }
}