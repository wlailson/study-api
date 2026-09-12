package io.github.wlailson.study_api.controller;

import io.github.wlailson.study_api.dto.SubjectRequestDTO;
import io.github.wlailson.study_api.dto.SubjectResponseDTO;
import io.github.wlailson.study_api.projections.SubjectMinProjection;
import io.github.wlailson.study_api.service.SubjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SubjectController.class)
class SubjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SubjectService service;

    @Test
    @WithMockUser(roles = "CLIENT")
    void findById_shouldReturnSubject() throws Exception {

        SubjectResponseDTO response =
                new SubjectResponseDTO(
                        1L,
                        "Java"
                );

        when(service.findById(1L))
                .thenReturn(response);

        mockMvc.perform(
                        get("/subjects/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Java"));

        verify(service).findById(1L);
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void findAll_shouldReturnSubjects() throws Exception {

        SubjectMinProjection subject1 =
                new SubjectProjection(
                        1L,
                        "Java"
                );

        SubjectMinProjection subject2 =
                new SubjectProjection(
                        2L,
                        "Spring"
                );

        when(service.findAll())
                .thenReturn(List.of(subject1, subject2));

        mockMvc.perform(
                        get("/subjects")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))

                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Java"))

                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Spring"));

        verify(service).findAll();
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void create_shouldReturnCreatedSubject() throws Exception {

        SubjectResponseDTO response =
                new SubjectResponseDTO(
                        1L,
                        "Java"
                );

        when(service.create(any(SubjectRequestDTO.class)))
                .thenReturn(response);

        String json = """
                {
                    "name": "Java"
                }
                """;

        mockMvc.perform(
                        post("/subjects")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        "Location",
                        "http://localhost/subjects/1"
                ))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Java"));

        verify(service).create(any(SubjectRequestDTO.class));
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void update_shouldReturnUpdatedSubject() throws Exception {

        SubjectResponseDTO response =
                new SubjectResponseDTO(
                        1L,
                        "Java Avançado"
                );

        when(service.update(
                eq(1L),
                any(SubjectRequestDTO.class)
        )).thenReturn(response);

        String json = """
                {
                    "name": "Java Avançado"
                }
                """;

        mockMvc.perform(
                        put("/subjects/1")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Java Avançado"));

        verify(service).update(
                eq(1L),
                any(SubjectRequestDTO.class)
        );
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void delete_shouldReturnNoContent() throws Exception {

        doNothing().when(service).delete(1L);

        mockMvc.perform(
                        delete("/subjects/1")
                                .with(csrf())
                )
                .andExpect(status().isNoContent());

        verify(service).delete(1L);
    }

    private record SubjectProjection(
            Long id,
            String name
    ) implements SubjectMinProjection {
        @Override
        public Long getId() {
            return 0L;
        }

        @Override
        public String getName() {
            return "";
        }
    }
}