package io.github.wlailson.study_api.repository;

import io.github.wlailson.study_api.dto.GoalItemMinDTO;
import io.github.wlailson.study_api.model.Goal;
import io.github.wlailson.study_api.model.GoalItem;
import io.github.wlailson.study_api.model.Subject;
import io.github.wlailson.study_api.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class GoalItemRepositoryTest {

    @Autowired
    private GoalItemRepository repository;

    @Test
    void deveBuscarGoalItemsPeloIdDoGoal() {

        Page<GoalItemMinDTO> result =
                repository.searchAllbyGoalId(
                        1L,
                        PageRequest.of(0, 10)
                );

        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());

        GoalItemMinDTO first = result.getContent().get(0);
        GoalItemMinDTO second = result.getContent().get(1);

        assertEquals(300L, first.targetInMinutes());
        assertEquals("Java", first.subjectName());

        assertEquals(240L, second.targetInMinutes());
        assertEquals("Spring Boot", second.subjectName());
    }

    @Test
    void naoDeveRetornarGoalItemsDeOutroGoal() {

        Page<GoalItemMinDTO> result =
                repository.searchAllbyGoalId(
                        2L,
                        PageRequest.of(0, 10)
                );

        assertEquals(1, result.getTotalElements());

        GoalItemMinDTO dto = result.getContent().get(0);

        assertEquals(180L, dto.targetInMinutes());
        assertEquals("Spring Boot", dto.subjectName());
    }

    @Test
    void deveRespeitarPaginacao() {

        Page<GoalItemMinDTO> result =
                repository.searchAllbyGoalId(
                        1L,
                        PageRequest.of(0, 1)
                );

        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals(2, result.getTotalPages());

        GoalItemMinDTO dto = result.getContent().get(0);

        assertEquals(300L, dto.targetInMinutes());
        assertEquals("Java", dto.subjectName());
    }
}