package io.github.wlailson.study_api.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "tb_revision")
public class Revision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private StudySession session;

    public Revision(Long id, LocalDate date, StudySession session) {
        this.id = id;
        this.date = date;
        this.session = session;
    }

    public Revision() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public StudySession getSession() {
        return session;
    }

    public void setSession(StudySession session) {
        this.session = session;
    }
}
