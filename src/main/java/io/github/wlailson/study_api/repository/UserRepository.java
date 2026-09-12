package io.github.wlailson.study_api.repository;

import io.github.wlailson.study_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //User findByEmail(String email);
    Optional<User> findByEmail(String email);
}