package io.github.wlailson.study_api.repository;

import io.github.wlailson.study_api.model.Revision;
import io.github.wlailson.study_api.model.RevisionStatus;
import io.github.wlailson.study_api.model.StudySession;
import io.github.wlailson.study_api.model.Subject;
import io.github.wlailson.study_api.model.Topic;
import io.github.wlailson.study_api.model.User;
import io.github.wlailson.study_api.projections.RevisionMinProjection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
class RevisionRepositoryTest {

    @Autowired
    private RevisionRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private StudySessionRepository studySessionRepository;

    private User user;
    private User anotherUser;

    private Topic userTopic;
    private Topic anotherUserTopic;

    private Subject userSubject;
    private Subject anotherUserSubject;

    @BeforeEach
    void setUp() {
        user = userRepository.save(createUser("Maria"));
        anotherUser = userRepository.save(createUser("Joao"));

        userTopic = createTopic(user);
        anotherUserTopic = createTopic(anotherUser);

        userSubject = createSubject(user);
        anotherUserSubject = createSubject(anotherUser);
    }

    @Test
    void findByIdAndUser_Id_shouldReturnRevision_whenRevisionBelongsToUser() {
        Revision revision = createRevision(
                user,
                userTopic,
                userSubject,
                RevisionStatus.PENDING,
                LocalDate.of(2026, 9, 15)
        );

        Optional<Revision> result = repository.findByIdAndUser_Id(
                revision.getId(),
                user.getId()
        );

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(revision.getId());
        assertThat(result.get().getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    void findByIdAndUser_Id_shouldReturnEmpty_whenRevisionBelongsToAnotherUser() {
        Revision revision = createRevision(
                user,
                userTopic,
                userSubject,
                RevisionStatus.PENDING,
                LocalDate.of(2026, 9, 15)
        );

        Optional<Revision> result = repository.findByIdAndUser_Id(
                revision.getId(),
                anotherUser.getId()
        );

        assertThat(result).isEmpty();
    }

    @Test
    void findByIdAndUser_Id_shouldReturnEmpty_whenRevisionDoesNotExist() {
        Optional<Revision> result = repository.findByIdAndUser_Id(
                999L,
                user.getId()
        );

        assertThat(result).isEmpty();
    }

    @Test
    void findByIdAndUser_IdAndStatus_shouldReturnRevision_whenDataMatches() {
        Revision revision = createRevision(
                user,
                userTopic,
                userSubject,
                RevisionStatus.PENDING,
                LocalDate.of(2026, 9, 15)
        );

        Optional<Revision> result =
                repository.findByIdAndUser_IdAndStatus(
                        revision.getId(),
                        user.getId(),
                        RevisionStatus.PENDING
                );

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(revision.getId());
        assertThat(result.get().getStatus()).isEqualTo(RevisionStatus.PENDING);
    }

    @Test
    void findByIdAndUser_IdAndStatus_shouldReturnEmpty_whenStatusDoesNotMatch() {
        Revision revision = createRevision(
                user,
                userTopic,
                userSubject,
                RevisionStatus.PENDING,
                LocalDate.of(2026, 9, 15)
        );

        Optional<Revision> result =
                repository.findByIdAndUser_IdAndStatus(
                        revision.getId(),
                        user.getId(),
                        RevisionStatus.COMPLETED
                );

        assertThat(result).isEmpty();
    }

    @Test
    void findByIdAndUser_IdAndStatus_shouldReturnEmpty_whenUserDoesNotMatch() {
        Revision revision = createRevision(
                user,
                userTopic,
                userSubject,
                RevisionStatus.PENDING,
                LocalDate.of(2026, 9, 15)
        );

        Optional<Revision> result =
                repository.findByIdAndUser_IdAndStatus(
                        revision.getId(),
                        anotherUser.getId(),
                        RevisionStatus.PENDING
                );

        assertThat(result).isEmpty();
    }

    @Test
    void searchByUserId_shouldReturnOnlyUserRevisions() {
        Revision userRevision = createRevision(
                user,
                userTopic,
                userSubject,
                RevisionStatus.PENDING,
                LocalDate.of(2026, 9, 15)
        );

        createRevision(
                anotherUser,
                anotherUserTopic,
                anotherUserSubject,
                RevisionStatus.PENDING,
                LocalDate.of(2026, 9, 16)
        );

        List<RevisionMinProjection> result =
                repository.searchByUserId(user.getId());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(userRevision.getId());
        assertThat(result.get(0).getStatus()).isEqualTo(RevisionStatus.PENDING);
        assertThat(result.get(0).getScheduledDate())
                .isEqualTo(LocalDate.of(2026, 9, 15));
    }

    @Test
    void searchByUserId_shouldReturnEmpty_whenUserHasNoRevisions() {
        List<RevisionMinProjection> result =
                repository.searchByUserId(user.getId());

        assertThat(result).isEmpty();
    }

    @Test
    void searchByUserId_shouldReturnRevisionsOrderedByScheduledDate() {
        Revision later = createRevision(
                user,
                userTopic,
                userSubject,
                RevisionStatus.PENDING,
                LocalDate.of(2026, 9, 20)
        );

        Revision earlier = createRevision(
                user,
                userTopic,
                userSubject,
                RevisionStatus.PENDING,
                LocalDate.of(2026, 9, 10)
        );

        Revision middle = createRevision(
                user,
                userTopic,
                userSubject,
                RevisionStatus.PENDING,
                LocalDate.of(2026, 9, 15)
        );

        List<RevisionMinProjection> result =
                repository.searchByUserId(user.getId());

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getId()).isEqualTo(earlier.getId());
        assertThat(result.get(1).getId()).isEqualTo(middle.getId());
        assertThat(result.get(2).getId()).isEqualTo(later.getId());
    }

    @Test
    void searchByUserIdAndStatus_shouldReturnOnlyRevisionsWithStatus() {
        Revision pending = createRevision(
                user,
                userTopic,
                userSubject,
                RevisionStatus.PENDING,
                LocalDate.of(2026, 9, 15)
        );

        createRevision(
                user,
                userTopic,
                userSubject,
                RevisionStatus.COMPLETED,
                LocalDate.of(2026, 9, 16)
        );

        List<RevisionMinProjection> result =
                repository.searchByUserIdAndStatus(
                        user.getId(),
                        RevisionStatus.PENDING
                );

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(pending.getId());
        assertThat(result.get(0).getStatus()).isEqualTo(RevisionStatus.PENDING);
    }

    @Test
    void searchByUserIdAndStatus_shouldNotReturnAnotherUserRevision() {
        Revision userRevision = createRevision(
                user,
                userTopic,
                userSubject,
                RevisionStatus.PENDING,
                LocalDate.of(2026, 9, 15)
        );

        createRevision(
                anotherUser,
                anotherUserTopic,
                anotherUserSubject,
                RevisionStatus.PENDING,
                LocalDate.of(2026, 9, 16)
        );

        List<RevisionMinProjection> result =
                repository.searchByUserIdAndStatus(
                        user.getId(),
                        RevisionStatus.PENDING
                );

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(userRevision.getId());
    }

    @Test
    void searchByUserIdAndStatus_shouldReturnEmpty_whenNoRevisionMatchesStatus() {
        createRevision(
                user,
                userTopic,
                userSubject,
                RevisionStatus.PENDING,
                LocalDate.of(2026, 9, 15)
        );

        List<RevisionMinProjection> result =
                repository.searchByUserIdAndStatus(
                        user.getId(),
                        RevisionStatus.COMPLETED
                );

        assertThat(result).isEmpty();
    }

    @Test
    void searchByUserIdAndStatus_shouldReturnRevisionsOrderedByScheduledDate() {
        Revision later = createRevision(
                user,
                userTopic,
                userSubject,
                RevisionStatus.PENDING,
                LocalDate.of(2026, 9, 20)
        );

        Revision earlier = createRevision(
                user,
                userTopic,
                userSubject,
                RevisionStatus.PENDING,
                LocalDate.of(2026, 9, 10)
        );

        Revision middle = createRevision(
                user,
                userTopic,
                userSubject,
                RevisionStatus.PENDING,
                LocalDate.of(2026, 9, 15)
        );

        createRevision(
                user,
                userTopic,
                userSubject,
                RevisionStatus.COMPLETED,
                LocalDate.of(2026, 9, 5)
        );

        List<RevisionMinProjection> result =
                repository.searchByUserIdAndStatus(
                        user.getId(),
                        RevisionStatus.PENDING
                );

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getId()).isEqualTo(earlier.getId());
        assertThat(result.get(1).getId()).isEqualTo(middle.getId());
        assertThat(result.get(2).getId()).isEqualTo(later.getId());
    }

    private User createUser(String name) {
        User user = new User();

        user.setName(name);
        user.setEmail(
                name.toLowerCase()
                        + System.nanoTime()
                        + "@gmail.com"
        );
        user.setPassword("123456");
        user.setPhone("61999999999");
        user.setBirthDate(LocalDate.of(2000, 1, 1));

        return user;
    }

    private Topic createTopic(User user) {
        Topic topic = new Topic();

        topic.setName("Java");
        topic.setUser(user);

        return topicRepository.save(topic);
    }

    private Subject createSubject(User user) {
        Subject subject = new Subject();

        subject.setName("Programacao");
        subject.setUser(user);

        return subjectRepository.save(subject);
    }

    private StudySession createSession(
            User user,
            Topic topic,
            Subject subject
    ) {
        StudySession session = new StudySession();

        session.setTopic(topic);
        session.setSubject(subject);
        session.setUser(user);
        session.setDurationInMinutes(60L);
        session.setBreakTimeInMinutes(10L);
        session.setDate(LocalDate.of(2026, 9, 10));

        return studySessionRepository.save(session);
    }

    private Revision createRevision(
            User user,
            Topic topic,
            Subject subject,
            RevisionStatus status,
            LocalDate scheduledDate
    ) {
        StudySession session = createSession(
                user,
                topic,
                subject
        );

        Revision revision = new Revision();

        revision.setUser(user);
        revision.setSession(session);
        revision.setStatus(status);
        revision.setScheduledDate(scheduledDate);

        return repository.save(revision);
    }
}