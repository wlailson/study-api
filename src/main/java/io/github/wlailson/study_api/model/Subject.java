package io.github.wlailson.study_api.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_subject")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "subject")
    private List<GoalItem> goalItems = new ArrayList<>();

    @OneToMany(mappedBy = "subject")
    private List<StudySession> sessions = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Subject(Long id, String name, User user) {
        this.id = id;
        this.name = name;
        this.user = user;
    }

    public Subject() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<GoalItem> getGoalItems() {
        return goalItems;
    }

    public List<StudySession> getSessions() {
        return sessions;
    }
}
