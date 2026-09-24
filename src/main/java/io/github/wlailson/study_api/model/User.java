
        package io.github.wlailson.study_api.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "tb_user")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id", nullable = false, unique = true)
    private String externalId;

    private String name;

    @Column(unique = true)
    private String email;

    private String phone;

    private LocalDate birthDate;

    @OneToMany(mappedBy = "user")
    private Set<Subject> subjects = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private List<Topic> topics = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<StudySession> sessions = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Revision> revisions = new ArrayList<>();

    public User() {
    }

    public User(
            Long id,
            String externalId,
            String name,
            String email,
            String phone,
            LocalDate birthDate
    ) {
        this.id = id;
        this.externalId = externalId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Set<Subject> getSubjects() {
        return subjects;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public List<StudySession> getSessions() {
        return sessions;
    }

    public List<Revision> getRevisions() {
        return revisions;
    }
}
