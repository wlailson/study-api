package io.github.wlailson.study_api.service;

import io.github.wlailson.study_api.authentication.AuthenticatedUser;
import io.github.wlailson.study_api.dto.StudySessionDTO;
import io.github.wlailson.study_api.dto.StudySessionEndDTO;
import io.github.wlailson.study_api.dto.StudySessionMinDTO;
import io.github.wlailson.study_api.dto.StudySessionUpdateDTO;
import io.github.wlailson.study_api.model.*;
import io.github.wlailson.study_api.repository.RevisionRepository;
import io.github.wlailson.study_api.repository.StudySessionRepository;
import io.github.wlailson.study_api.repository.SubjectRepository;
import io.github.wlailson.study_api.service.exceptions.ConflictException;
import io.github.wlailson.study_api.service.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class StudySessionService {

    private final StudySessionRepository repository;
    private final SubjectRepository subjectRepository;
    private final AuthenticatedUser authenticatedUser;
    private final RevisionRepository revisionRepository;

    public StudySessionService(StudySessionRepository repository, UserService userService, SubjectRepository subjectRepository, AuthenticatedUser authenticatedUser, RevisionRepository revisionRepository) {
        this.repository = repository;
        this.subjectRepository = subjectRepository;
        this.authenticatedUser = authenticatedUser;
        this.revisionRepository = revisionRepository;
    }

    @Transactional(readOnly = true)
    public StudySessionDTO findById(Long id) {
        StudySession session = loadEntity(id);
        authenticatedUser.validateOwnership(session.getUser().getId());
        return new StudySessionDTO(session);
    }

    @Transactional(readOnly = true)
    public Page<StudySessionMinDTO> findAll(Pageable pageable) {
        User user = authenticatedUser.get();
        return repository.searchSessions(pageable, user.getId());
    }

    @Transactional
    public Long startSession(Long subjectId) {
        User user = authenticatedUser.get();

        if (repository.existsByUserIdAndStatus(user.getId(), SessionStatus.IN_PROGRESS)) {
            throw new ConflictException("User already has an active study session");
        }

        StudySession session = new StudySession();
        session.setSubject(findSubjectById(subjectId));
        session.setStatus(SessionStatus.IN_PROGRESS);
        session.setStartTime(Instant.now());
        session.setUser(user);

        repository.save(session);

        return session.getId();
    }

    @Transactional(readOnly = true)
    public StudySessionDTO findSessionInProgress() {
        User user = authenticatedUser.get();
        StudySession session = getSession(user.getId());
        return new StudySessionDTO(session);
    }

    @Transactional
    public StudySessionDTO endSession(StudySessionEndDTO dto) {
        User user = authenticatedUser.get();

        StudySession session = getSession(user.getId());

        saveRevisions(dto, session);

        session.setEndTime(Instant.now());
        session.setStatus(SessionStatus.COMPLETED);
        session.setDurationInMinutes(dto.durationInMinutes());
        session.setBreakTimeInMinutes(dto.breakTimeInMinutes());
        session.setTopic(dto.topic());

        return new StudySessionDTO(session);
    }

    @Transactional
    public StudySessionDTO updateSession(Long sessionId, StudySessionUpdateDTO dto) {
        StudySession session = loadEntity(sessionId);
        authenticatedUser.validateOwnership(session.getUser().getId());
        session.setTopic(dto.topic());
        session.setDurationInMinutes(dto.durationInMinutes());
        session.setBreakTimeInMinutes(dto.breakTimeInMinutes());
        session.setSubject(findSubjectById(sessionId));

        return new StudySessionDTO(session);
    }

    @Transactional
    public void deleteSession(Long sessionId) {
        StudySession session = loadEntity(sessionId);
        authenticatedUser.validateOwnership(session.getUser().getId());
        repository.deleteById(sessionId);
    }

    private StudySession loadEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
    }

    private StudySession getSession(Long userId) {
        return repository.findByUserIdAndStatus(userId, SessionStatus.IN_PROGRESS)
                .orElseThrow(() -> new ResourceNotFoundException("user does not currently have an active session"));
    }

    private Subject findSubjectById(Long subjectId) {
        return subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("subject not found"));
    }

    private void saveRevisions(StudySessionEndDTO dto, StudySession session) {

        List<Revision> revisions = dto.revisions().stream().map(revisionDTO -> {
            Revision entity = new Revision();
            entity.setDate(revisionDTO.date());
            entity.setSession(session);
            return entity;
        }).toList();

        revisionRepository.saveAll(revisions);
    }


}
