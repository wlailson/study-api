package io.github.wlailson.study_api.service;

import io.github.wlailson.study_api.dto.UserDTO;
import io.github.wlailson.study_api.model.User;
import io.github.wlailson.study_api.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service;

    private User user;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(1L);
        user.setName("Maria");
        user.setEmail("maria@gmail.com");
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Nested
    class LoadUserByUsername {

        @Test
        void shouldReturnUserWhenEmailExists() {

            when(repository.findByEmail("maria@gmail.com"))
                    .thenReturn(user);

            UserDetails result =
                    service.loadUserByUsername("maria@gmail.com");

            assertNotNull(result);
            assertEquals(user, result);

            verify(repository)
                    .findByEmail("maria@gmail.com");
        }

        @Test
        void shouldThrowUsernameNotFoundExceptionWhenEmailDoesNotExist() {

            when(repository.findByEmail("maria@gmail.com"))
                    .thenReturn(null);

            assertThrows(
                    UsernameNotFoundException.class,
                    () -> service.loadUserByUsername("maria@gmail.com")
            );

            verify(repository)
                    .findByEmail("maria@gmail.com");
        }
    }

    @Nested
    class GetMe {

        @Test
        void shouldReturnAuthenticatedUser() {

            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            "maria@gmail.com",
                            null
                    );

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);

            when(repository.findByEmail("maria@gmail.com"))
                    .thenReturn(user);

            UserDTO result = service.getMe();

            assertNotNull(result);
            assertEquals(user.getId(), result.id());
            assertEquals(user.getName(), result.name());
            assertEquals(user.getEmail(), result.email());

            verify(repository)
                    .findByEmail("maria@gmail.com");
        }

        @Test
        void shouldThrowUsernameNotFoundExceptionWhenAuthenticatedUserDoesNotExist() {

            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            "maria@gmail.com",
                            null
                    );

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);

            when(repository.findByEmail("maria@gmail.com"))
                    .thenReturn(null);

            assertThrows(
                    UsernameNotFoundException.class,
                    () -> service.getMe()
            );

            verify(repository)
                    .findByEmail("maria@gmail.com");
        }
    }

}