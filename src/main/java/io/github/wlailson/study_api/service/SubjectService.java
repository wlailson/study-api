package io.github.wlailson.study_api.service;


import io.github.wlailson.study_api.dto.SubjectRequestDTO;
import io.github.wlailson.study_api.dto.SubjectResponseDTO;
import io.github.wlailson.study_api.model.Subject;
import io.github.wlailson.study_api.model.User;
import io.github.wlailson.study_api.projections.SubjectMinProjection;
import io.github.wlailson.study_api.repository.SubjectRepository;
import io.github.wlailson.study_api.service.exceptions.ConflictException;
import io.github.wlailson.study_api.service.exceptions.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SubjectService {

    private final SubjectRepository repository;
    private final AuthService authService;

    public SubjectService(SubjectRepository repository, AuthService authService) {
        this.repository = repository;
        this.authService = authService;
    }


    @Transactional(readOnly = true)
    public SubjectResponseDTO findById(Long id) {
        Subject subject = getSubject(id);
        return new SubjectResponseDTO(subject);
    }

    @Transactional(readOnly = true)
    public List<SubjectMinProjection> findAll() {
        return getAllSubjects();
    }

    @Transactional
    public SubjectResponseDTO create(SubjectRequestDTO request) {
        Subject subject = getOrCreate(request.name());
        repository.save(subject);
        return new SubjectResponseDTO(subject);
    }

    @Transactional
    public SubjectResponseDTO update(Long id, SubjectRequestDTO request) {
        Subject subject = getSubject(id);
        subject.setName(request.name());
        return new SubjectResponseDTO(subject);
    }

    @Transactional
    public void delete(Long id) {
        Subject subject = getSubject(id);
        Long subjectId = subject.getId();
        String userName = authService.getCurrentUser().getName();
        try {
            repository.delete(subject);
            repository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(
                    "Cannot delete subject The subject has associated sessions. " +
                            "Subject ID: " + subjectId +
                            ", User: " + userName
            );
        }
    }

    protected Subject getSubject(Long id) {
        User user = authService.getCurrentUser();
        Subject subject = repository.findByIdAndUser_Id(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found ID: " + id + ", user: " + user.getName()));
        return subject;
    }

    protected List<SubjectMinProjection> getAllSubjects() {
        User user = authService.getCurrentUser();
        return repository.searchSubjectByUserId(user.getId());
    }

    protected Subject getOrCreate(String subjectName) {
        User user = authService.getCurrentUser();
        return repository.findByNameIgnoreCaseAndUser_Id(subjectName, user.getId()).orElseGet(() -> {
            Subject entity = new Subject();
            entity.setName(subjectName);
            entity.setUser(user);
            return entity;
        });
    }
}
