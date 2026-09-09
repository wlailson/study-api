package io.github.wlailson.study_api.controller;

import static org.junit.jupiter.api.Assertions.*;

import io.github.wlailson.study_api.dto.UserDTO;
import io.github.wlailson.study_api.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@WithMockUser(roles = "CLIENT")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService service;

    @Test
    void deveBuscarUsuarioAutenticado() throws Exception {

        UserDTO dto = new UserDTO(
                1L,
                "Maria Silva",
                "maria@gmail.com",
                "61996695658",
                LocalDate.of(1997, 11, 1),
                List.of("ROLE_CLIENT")
        );

        when(service.getMe())
                .thenReturn(dto);

        mockMvc.perform(
                        get("/users/me")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Maria Silva"))
                .andExpect(jsonPath("$.email").value("maria@gmail.com"))
                .andExpect(jsonPath("$.phone").value("61996695658"))
                .andExpect(jsonPath("$.birthDate").value("1997-11-01"))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_CLIENT"));

        verify(service).getMe();
    }
}