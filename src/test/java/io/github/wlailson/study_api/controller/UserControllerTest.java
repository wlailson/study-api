package io.github.wlailson.study_api.controller;


import io.github.wlailson.study_api.dto.UserDTO;
import io.github.wlailson.study_api.dto.UserRequestDTO;
import io.github.wlailson.study_api.security.JwtUtil;
import io.github.wlailson.study_api.security.SecurityConfig;
import io.github.wlailson.study_api.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService service;

    @MockitoBean
    private CacheManager cacheManager;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
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

        UserDTO response = new UserDTO(
                1L,
                "Maria",
                "maria@gmail.com",
                "61999999999",
                LocalDate.of(2000, 1, 1),
                List.of("CLIENT")
        );

        when(service.getMe())
                .thenReturn(response);

        mockMvc.perform(
                        get("/users/me")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Maria"))
                .andExpect(jsonPath("$.email").value("maria@gmail.com"))
                .andExpect(jsonPath("$.phone").value("61999999999"))
                .andExpect(jsonPath("$.birthdate").value("2000-01-01"))
                .andExpect(jsonPath("$.roles[0]").value("CLIENT"));

        verify(service).getMe();
    }

    @Test
    void getMe_shouldReturnForbidden_whenNotAuthenticated() throws Exception {

        mockMvc.perform(
                        get("/users/me")
                )
                .andExpect(status().isForbidden());
    }
}