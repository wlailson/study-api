package io.github.wlailson.study_api.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_study_session")
public class StudySession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    private Long durationInMinutes;

    private Long breakTimeInMinutes;

    @Column(nullable = false)
    private LocalDate date = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @OneToMany(mappedBy = "session")
    @OrderBy("scheduledDate ASC")
    private Set<Revision> revisions = new HashSet<>();

    public StudySession(Long id, Topic topic, Long durationInMinutes, Long breakTimeInMinutes, LocalDate date, User user, Subject subject) {
        this.id = id;
        this.topic = topic;
        this.durationInMinutes = durationInMinutes;
        this.breakTimeInMinutes = breakTimeInMinutes;
        this.date = date;
        this.user = user;
        this.subject = subject;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public StudySession() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Long getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(Long durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public Long getBreakTimeInMinutes() {
        return breakTimeInMinutes;
    }

    public void setBreakTimeInMinutes(Long breakTimeInMinutes) {
        this.breakTimeInMinutes = breakTimeInMinutes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Set<Revision> getRevisions() {
        return revisions;
    }
}
