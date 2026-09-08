package io.github.wlailson.study_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_goal_item")
public class GoalItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long targetInMinutes;

    @ManyToOne
    @JoinColumn(name = "goal_id")
    private Goal goal;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    public GoalItem(Long id, Long targetInMinutes, Boolean isCompleted, Goal goal, Subject subject) {
        this.id = id;
        this.targetInMinutes = targetInMinutes;
        this.goal = goal;
        this.subject = subject;
    }

    public GoalItem() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTargetInMinutes() {
        return targetInMinutes;
    }

    public void setTargetInMinutes(Long targetInMinutes) {
        this.targetInMinutes = targetInMinutes;
    }


    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
