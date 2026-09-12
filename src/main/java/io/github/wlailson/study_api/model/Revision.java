package io.github.wlailson.study_api.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "tb_revision")
public class Revision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate scheduledDate;

    private LocalDate completedDate;

    private Long durationInMinutes;

    private Long breakTimeInMinutes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RevisionStatus status = RevisionStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private StudySession session;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Revision(Long id, LocalDate scheduledDate, LocalDate completedDate, Long durationInMinutes, Long breakTimeInMinutes, RevisionStatus status, StudySession session, User user) {
        this.id = id;
        this.scheduledDate = scheduledDate;
        this.completedDate = completedDate;
        this.durationInMinutes = durationInMinutes;
        this.breakTimeInMinutes = breakTimeInMinutes;
        this.status = status;
        this.session = session;
        this.user = user;
    }

    public Revision() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(LocalDate completedDate) {
        this.completedDate = completedDate;
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

    public RevisionStatus getStatus() {
        return status;
    }

    public void setStatus(RevisionStatus status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public StudySession getSession() {
        return session;
    }

    public void setSession(StudySession session) {
        this.session = session;
    }
}
