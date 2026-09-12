package io.github.wlailson.study_api.service;


import io.github.wlailson.study_api.dto.StudySessionRequestDTO;
import io.github.wlailson.study_api.dto.StudySessionResponseDTO;
import io.github.wlailson.study_api.dto.StudySessionResponseMinDTO;
import io.github.wlailson.study_api.model.StudySession;
import io.github.wlailson.study_api.model.User;
import io.github.wlailson.study_api.projections.StudySessionMinProjection;
import io.github.wlailson.study_api.projections.TopicMinProjection;
import io.github.wlailson.study_api.repository.StudySessionRepository;
import io.github.wlailson.study_api.service.exceptions.ConflictException;
import io.github.wlailson.study_api.service.exceptions.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudySessionService {

    private final StudySessionRepository repository;
    private final AuthService authService;
    private final SubjectService subjectService;
    private final TopicService topicService;
    private final RevisionService revisionService;

    public StudySessionService(
            StudySessionRepository repository,
            AuthService authService,
            SubjectService subjectService,
            TopicService topicService, RevisionService revisionService) {
        this.repository = repository;
        this.authService = authService;
        this.subjectService = subjectService;
        this.topicService = topicService;
        this.revisionService = revisionService;
    }


    @Transactional(readOnly = true)
    public StudySessionResponseDTO findById(Long id) {
        StudySession session = loadEntity(id);
        return new StudySessionResponseDTO(session);
    }

    @Transactional(readOnly = true)
    public Page<StudySessionResponseMinDTO> findAll(Pageable pageable, String name) {
        User user = authService.getCurrentUser();

        Page<StudySessionMinProjection> projection = repository.searchSessions(pageable, name, user.getId());

        Page<StudySessionResponseMinDTO> response = projection.map(p -> {
            StudySessionResponseMinDTO dto = new StudySessionResponseMinDTO(
                    p.getId(), p.getSubject(),
                    p.getTopic(),
                    p.getDurationInMinutes(),
                    p.getDate());
            return dto;
        });

        return response;
    }

    @Transactional
    public StudySessionResponseDTO saveSession(Long subjectId, StudySessionRequestDTO request) {

        StudySession session = createEntity(subjectId, request);

        repository.save(session);

        revisionService.saveRevisions(request, session);

        return new StudySessionResponseDTO(session);
    }

    @Transactional
    public void deleteSession(Long sessionId) {
        StudySession session = loadEntity(sessionId);
        String topic = session.getTopic().getName();
        String subject = session.getSubject().getName();
        String user = authService.getCurrentUser().getName();

        try {
            repository.delete(session);
            repository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(
                    "Cannot delete study session The session has associated revisions. " +
                            "Session ID: " + sessionId +
                            ", Subject: " + subject +
                            ", Topic: " + topic +
                            ", User: " + user
            );
        }

    }

    @Transactional(readOnly = true)
    public List<TopicMinProjection> findAllTopics() {
        return topicService.getAllTopics();
    }

    private StudySession createEntity(Long subjectId, StudySessionRequestDTO request) {
        User user = authService.getCurrentUser();

        StudySession session = new StudySession();

        session.setBreakTimeInMinutes(request.breakTimeInMinutes());
        session.setUser(user);
        session.setDurationInMinutes(request.durationInMinutes());
        session.setSubject(subjectService.getSubject(subjectId));
        session.setTopic(topicService.getOrCreate(request.topic()));

        return session;
    }

    private StudySession loadEntity(Long id) {
        User user = authService.getCurrentUser();
        StudySession session = repository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Session not found ID: " + id + ", user: " + user.getName()));

        return session;
    }
}
