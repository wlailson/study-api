package io.github.wlailson.study_api.service;

import io.github.wlailson.study_api.authentication.AuthenticatedUser;
import io.github.wlailson.study_api.dto.SubjectDTO;
import io.github.wlailson.study_api.model.Subject;
import io.github.wlailson.study_api.model.User;
import io.github.wlailson.study_api.repository.SubjectRepository;
import io.github.wlailson.study_api.service.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SubjectService {

    private final SubjectRepository repository;
    private final AuthenticatedUser authenticatedUser;

    public SubjectService(SubjectRepository repository, AuthenticatedUser authenticatedUser) {
        this.repository = repository;
        this.authenticatedUser = authenticatedUser;
    }

    @Transactional(readOnly = true)
    public SubjectDTO findById(Long id) {
        Subject subject = getSubject(id);
        authenticatedUser.validateOwnership(subject.getUser().getId());
        return new SubjectDTO(subject);
    }

    @Transactional(readOnly = true)
    public Page<SubjectDTO> findAll(Pageable pageable) {
        User user = authenticatedUser.get();
        return repository.findAllByUserId(pageable, user.getId()).map(SubjectDTO::new);
    }

    @Transactional
    public SubjectDTO create(SubjectDTO dto) {
        User user = authenticatedUser.get();
        Subject subject = new Subject();
        subject.setName(dto.name());
        subject.setUser(user);
        repository.save(subject);
        return new SubjectDTO(subject);
    }

    @Transactional
    public SubjectDTO update(Long id, SubjectDTO dto) {
        Subject subject = getSubject(id);
        authenticatedUser.validateOwnership(subject.getUser().getId());
        subject.setName(dto.name());
        return new SubjectDTO(subject);
    }

    @Transactional
    public void delete(Long id) {
        Subject subject = getSubject(id);
        authenticatedUser.validateOwnership(subject.getUser().getId());
        repository.delete(subject);
    }

    private Subject getSubject(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));
    }
}
