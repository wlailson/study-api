package io.github.wlailson.study_api.service;

import io.github.wlailson.study_api.dto.RevisionRequestDTO;
import io.github.wlailson.study_api.dto.RevisionResponseDTO;
import io.github.wlailson.study_api.dto.StudySessionRequestDTO;
import io.github.wlailson.study_api.model.Revision;
import io.github.wlailson.study_api.model.RevisionStatus;
import io.github.wlailson.study_api.model.StudySession;
import io.github.wlailson.study_api.model.User;
import io.github.wlailson.study_api.projections.RevisionMinProjection;
import io.github.wlailson.study_api.repository.RevisionRepository;
import io.github.wlailson.study_api.service.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class RevisionService {


    private final RevisionRepository repository;
    private final AuthService authService;

    public RevisionService(
            RevisionRepository repository, AuthService authService) {
        this.repository = repository;
        this.authService = authService;
    }


    @Transactional(readOnly = true)
    public RevisionResponseDTO findById(Long id) {
        Revision revision = getRevision(id);
        return new RevisionResponseDTO(revision);
    }

    public List<RevisionMinProjection> findAll(RevisionStatus status) {

        Long userId = authService.getCurrentUser().getId();

        if (status == null) {
            return repository.searchByUserId(userId);
        }

        return repository.searchByUserIdAndStatus(userId, status);
    }

    @Transactional
    public RevisionResponseDTO conclude(Long id, RevisionRequestDTO request) {

        Revision revision = getPendindRevision(id);
        revision.setDurationInMinutes(request.durationInMinutes());
        revision.setBreakTimeInMinutes(request.breakTimeInMinutes());
        revision.setStatus(RevisionStatus.COMPLETED);
        revision.setCompletedDate(LocalDate.now());

        return new RevisionResponseDTO(revision);
    }

    @Transactional
    public void delete(Long id) {
        Revision revision = getRevision(id);
        repository.delete(revision);
    }

    @Transactional
    void saveRevisions(
            StudySessionRequestDTO request,
            StudySession session) {

        User user = authService.getCurrentUser();

        List<Revision> revisions = request.revisions().stream().map(revisionDTO -> {
            Revision entity = new Revision();
            entity.setStatus(RevisionStatus.PENDING);
            entity.setScheduledDate(revisionDTO.scheduledDate());
            entity.setSession(session);
            entity.setUser(user);
            return entity;
        }).toList();

        repository.saveAll(revisions);

        session.getRevisions().addAll(revisions);
    }

    private Revision getRevision(Long id) {
        User user = authService.getCurrentUser();
        Revision revision = repository.findByIdAndUser_Id(id, user.getId()).orElseThrow(() ->
                new ResourceNotFoundException("Revision not found ID: " + id + ", user: " + user.getName()));
        return revision;
    }

    private Revision getPendindRevision(Long id) {
        User user = authService.getCurrentUser();
        Revision revision = repository.findByIdAndUser_IdAndStatus(id, user.getId(), RevisionStatus.PENDING)
                .orElseThrow(() -> new ResourceNotFoundException("Pending revision not found ID: " + id + ", user: " + user.getName()));
        return revision;
    }
}
