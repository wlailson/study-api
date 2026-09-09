package io.github.wlailson.study_api.service;

import io.github.wlailson.study_api.authentication.AuthenticatedUser;
import io.github.wlailson.study_api.dto.GoalItemCreateDTO;
import io.github.wlailson.study_api.dto.GoalItemDTO;
import io.github.wlailson.study_api.dto.GoalItemMinDTO;
import io.github.wlailson.study_api.dto.GoalItemUpdateDTO;
import io.github.wlailson.study_api.model.Goal;
import io.github.wlailson.study_api.model.GoalItem;
import io.github.wlailson.study_api.model.Subject;
import io.github.wlailson.study_api.repository.GoalItemRepository;
import io.github.wlailson.study_api.repository.GoalRepository;
import io.github.wlailson.study_api.repository.SubjectRepository;
import io.github.wlailson.study_api.service.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GoalItemService {
    private final GoalItemRepository repository;
    private final AuthenticatedUser authenticatedUser;
    private final SubjectRepository subjectRepository;
    private final GoalRepository goalRepository;

    public GoalItemService(GoalItemRepository repository, AuthenticatedUser authenticatedUser, SubjectRepository subjectRepository, GoalRepository goalRepository) {
        this.repository = repository;
        this.authenticatedUser = authenticatedUser;
        this.subjectRepository = subjectRepository;
        this.goalRepository = goalRepository;
    }

    @Transactional(readOnly = true)
    public GoalItemDTO findById(Long goalId, Long id) {

        GoalItem item = loadEntityById(goalId, id);

        authenticatedUser.validateOwnership(
                item.getGoal().getUser().getId()
        );

        return new GoalItemDTO(item);
    }


    @Transactional(readOnly = true)
    public Page<GoalItemMinDTO> findAll(
            Long goalId,
            Pageable pageable) {

        Goal goal = loadGoal(goalId);

        authenticatedUser.validateOwnership(
                goal.getUser().getId()
        );

        return repository.searchAllbyGoalId(
                goalId,
                pageable
        );
    }

    @Transactional
    public void addGoalItems(
            Long goalId,
            List<GoalItemCreateDTO> dto) {

        Goal goal = loadGoal(goalId);

        Long userId = goal.getUser().getId();

        authenticatedUser.validateOwnership(userId);

        List<GoalItem> goalItems = dto.stream()
                .map(goalItemDTO -> {

                    GoalItem item = new GoalItem();

                    item.setGoal(goal);
                    item.setTargetInMinutes(
                            goalItemDTO.targetInMinutes()
                    );

                    Subject subject = loadSubjectById(
                            goalItemDTO.subjectId(),
                            userId
                    );

                    item.setSubject(subject);

                    return item;
                })
                .toList();

        goal.getGoalItems().addAll(goalItems);
    }

    @Transactional
    public GoalItemDTO update(Long goalId, Long id, GoalItemUpdateDTO dto) {

        Goal goal = loadGoal(goalId);

        Long userId = goal.getUser().getId();

        authenticatedUser.validateOwnership(userId);

        GoalItem item = loadEntityById(goalId, id);

        Subject subject = loadSubjectById(dto.subjectId(), userId);

        item.setSubject(subject);
        item.setTargetInMinutes(dto.targetInMinutes());

        return new GoalItemDTO(item);
    }

    @Transactional
    public void delete(Long goalId, Long id) {
        Goal goal = loadGoal(goalId);
        authenticatedUser.validateOwnership(goal.getUser().getId());
        GoalItem item = loadEntityById(goalId, id);
        goal.getGoalItems().remove(item);
    }

    private GoalItem loadEntityById(Long goalId, Long id) {
        return repository.findByIdAndGoalId(id, goalId).orElseThrow(() -> new ResourceNotFoundException("Goalitem not found"));
    }

    private Subject loadSubjectById(Long subjectId, Long userId) {
        return subjectRepository.findByIdAndUserId(subjectId, userId).orElseThrow(() -> new ResourceNotFoundException("Subject not found " + subjectId));
    }

    private Goal loadGoal(Long id) {
        return goalRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Goal not found"));
    }
}
