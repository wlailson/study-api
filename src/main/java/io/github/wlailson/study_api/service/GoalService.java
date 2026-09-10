package io.github.wlailson.study_api.service;

import io.github.wlailson.study_api.authentication.AuthenticatedUser;
import io.github.wlailson.study_api.dto.GoalCreateDTO;
import io.github.wlailson.study_api.dto.GoalDTO;
import io.github.wlailson.study_api.dto.GoalMinDTO;
import io.github.wlailson.study_api.dto.GoalUpdateDTO;
import io.github.wlailson.study_api.model.Goal;
import io.github.wlailson.study_api.model.GoalItem;
import io.github.wlailson.study_api.model.Subject;
import io.github.wlailson.study_api.model.User;
import io.github.wlailson.study_api.repository.GoalRepository;
import io.github.wlailson.study_api.repository.StudySessionRepository;
import io.github.wlailson.study_api.repository.SubjectRepository;
import io.github.wlailson.study_api.service.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GoalService {
    private final AuthenticatedUser authenticatedUser;
    private final GoalRepository repository;
    private final StudySessionRepository studySessionRepository;
    private final SubjectRepository subjectRepository;

    public GoalService(AuthenticatedUser authenticatedUser, GoalRepository repository, StudySessionRepository studySessionRepository, SubjectRepository subjectRepository) {
        this.authenticatedUser = authenticatedUser;
        this.repository = repository;
        this.studySessionRepository = studySessionRepository;
        this.subjectRepository = subjectRepository;
    }

    @Transactional(readOnly = true)
    public GoalDTO findById(Long id) {
        Goal goal = loadGoal(id);
        Long userId = goal.getUser().getId();
        authenticatedUser.validateOwnership(userId);
        return toDTO(goal);
    }

    @Transactional(readOnly = true)
    public Page<GoalMinDTO> findAll(Pageable pageable) {
        Long userId = authenticatedUser.get().getId();
        authenticatedUser.validateOwnership(userId);
        return repository.findAllByUserId(userId, pageable).map(GoalMinDTO::new);
    }

    @Transactional
    public GoalDTO create(GoalCreateDTO dto) {
        User user = authenticatedUser.get();
        Goal goal = new Goal();
        goal.setUser(user);
        goal.setTitle(dto.title());
        goal.setStartDate(dto.startDate());
        goal.setEndDate(dto.endDate());
        List<GoalItem> goalItems = dto.items().stream().map(itemDTO -> {
            GoalItem item = new GoalItem();
            Subject subject = findSubjectById(itemDTO.subjectId());
            item.setGoal(goal);
            item.setTargetInMinutes(itemDTO.targetInMinutes());
            item.setSubject(subject);
            return item;
        }).toList();
        goal.getGoalItems().addAll(goalItems);
        repository.save(goal);
        return new GoalDTO(goal);
    }


    @Transactional
    public GoalUpdateDTO update(Long id, GoalUpdateDTO dto) {
        Goal goal = loadGoal(id);
        Long userId = goal.getUser().getId();
        authenticatedUser.validateOwnership(userId);
        goal.setTitle(dto.title());
        goal.setStartDate(dto.startDate());
        goal.setEndDate(dto.endDate());
        return new GoalUpdateDTO(goal);
    }

    @Transactional
    public void delete(Long id) {
        Goal goal = loadGoal(id);
        Long userId = goal.getUser().getId();
        authenticatedUser.validateOwnership(userId);
        repository.deleteById(id);
    }

    public GoalDTO toDTO(Goal goal) {
        GoalDTO goalDTO = new GoalDTO(goal, studied(goal, goal.getUser().getId()));
        return goalDTO;
    }

    private Goal loadGoal(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Goal not found " + id));
    }

    private Long studied(Goal goal, Long userId) {
        LocalDateTime startDate = goal.getStartDate().atStartOfDay();
        LocalDateTime endDate = goal.getEndDate().plusDays(1).atStartOfDay();
        return studySessionRepository.studied(userId, startDate, endDate);
    }

    private Subject findSubjectById(Long subjectId) {
        return subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("subject not found " + subjectId));
    }
}
