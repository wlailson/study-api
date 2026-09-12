package io.github.wlailson.study_api.service;

import io.github.wlailson.study_api.model.User;
import io.github.wlailson.study_api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service;

    @Test
    void loadUserByUsername_shouldReturnUser_whenEmailExists() {

        User user = mock(User.class);

        when(repository.findByEmail("maria@gmail.com"))
                .thenReturn(Optional.of(user));

        UserDetails response =
                service.loadUserByUsername("maria@gmail.com");

        assertNotNull(response);
        assertSame(user, response);

        verify(repository)
                .findByEmail("maria@gmail.com");
    }

    @Test
    void loadUserByUsername_shouldThrowUsernameNotFoundException_whenEmailDoesNotExist() {

        when(repository.findByEmail("maria@gmail.com"))
                .thenReturn(Optional.empty());

        UsernameNotFoundException exception =
                assertThrows(
                        UsernameNotFoundException.class,
                        () -> service.loadUserByUsername("maria@gmail.com")
                );

        assertEquals(
                "Usuário não encontrado: maria@gmail.com",
                exception.getMessage()
        );

        verify(repository)
                .findByEmail("maria@gmail.com");
    }
}