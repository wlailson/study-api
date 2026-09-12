package io.github.wlailson.study_api.controller;

import io.github.wlailson.study_api.dto.UserDTO;
import io.github.wlailson.study_api.dto.UserRequestDTO;
import io.github.wlailson.study_api.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService service;

    @Test
    @WithMockUser(roles = "CLIENT")
    void login_shouldReturnToken() throws Exception {

        String token = "jwt-token";

        when(service.login(any(UserRequestDTO.class)))
                .thenReturn(token);

        String json = """
            {
                "email": "maria@gmail.com",
                "password": "123456"
            }
            """;

        mockMvc.perform(
                        post("/users/login")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(token));

        verify(service).login(any(UserRequestDTO.class));
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void getMe_shouldReturnAuthenticatedUser() throws Exception {

        UserDTO response = mock(UserDTO.class);

        when(service.getMe())
                .thenReturn(response);

        mockMvc.perform(
                        get("/users/me")
                )
                .andExpect(status().isOk());

        verify(service).getMe();
    }

    @Test
    void getMe_shouldReturnUnauthorized_whenNotAuthenticated()
            throws Exception {

        mockMvc.perform(
                        get("/users/me")
                )
                .andExpect(status().isUnauthorized());
    }
}