package io.github.wlailson.study_api.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_study_session")
public class StudySession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String topic;

    private Long durationInMinutes;

    private Long breakTimeInMinutes;

    @Enumerated(EnumType.STRING)
    private SessionStatus status;

    private Instant startTime;

    private Instant endTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @OneToMany(mappedBy = "session")
    @OrderBy("date ASC")
    private Set<Revision> revisions = new HashSet<>();

    public StudySession(Long id, String topic, Long durationInMinutes, Long breakTimeInMinutes, SessionStatus status, Instant startTime, Instant endTime, User user, Subject subject) {
        this.id = id;
        this.topic = topic;
        this.durationInMinutes = durationInMinutes;
        this.breakTimeInMinutes = breakTimeInMinutes;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.user = user;
        this.subject = subject;
    }

    public StudySession() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
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

    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
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
