package io.github.wlailson.study_api.service;

import io.github.wlailson.study_api.authentication.AuthenticatedUser;
import io.github.wlailson.study_api.dto.RevisionDTO;
import io.github.wlailson.study_api.dto.RevisionListDTO;
import io.github.wlailson.study_api.dto.RevisionUpdateDTO;
import io.github.wlailson.study_api.model.Revision;
import io.github.wlailson.study_api.repository.RevisionRepository;
import io.github.wlailson.study_api.service.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RevisionService {


    private final RevisionRepository repository;
    private final AuthenticatedUser authenticatedUser;

    public RevisionService(
            RevisionRepository repository,
            AuthenticatedUser authenticatedUser) {

        this.repository = repository;
        this.authenticatedUser = authenticatedUser;
    }

    @Transactional(readOnly = true)
    public RevisionDTO findById(Long id) {

        Revision revision = getRevision(id);

        authenticatedUser.validateOwnership(
                revision.getSession().getUser().getId()
        );

        return new RevisionDTO(revision);
    }

    @Transactional(readOnly = true)
    public Page<RevisionListDTO> findAll(Pageable pageable) {

        Long userId = authenticatedUser.get().getId();

        return repository.searchAllByUserId(userId, pageable);
    }

    @Transactional
    public RevisionDTO update(Long id, RevisionUpdateDTO dto) {

        Revision revision = getRevision(id);

        authenticatedUser.validateOwnership(
                revision.getSession().getUser().getId()
        );

        revision.setDate(dto.date());

        return new RevisionDTO(revision);
    }

    @Transactional
    public void delete(Long id) {

        Revision revision = getRevision(id);

        authenticatedUser.validateOwnership(
                revision.getSession().getUser().getId()
        );

        repository.delete(revision);
    }

    private Revision getRevision(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Revision not found " + id));
    }
}
