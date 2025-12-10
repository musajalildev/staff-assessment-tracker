package com.assessment.tracker.server.persistence.entities;

import com.assessment.tracker.server.utils.enums.*;

import jakarta.persistence.*;

/**
 * Linker table for assigning Users assessment-specific roles
 * Composite primary key of every field
 * It has a many to one relationship with User and Assessment
 * It has an enum of type AssessmentRole that represents the relationship
 * between User and Assessment
 */
@Entity
@Table(name = "assessment_user_roles")
public class AssignedUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "assessment_id")
    private Assessment assessment;

    @Enumerated(EnumType.STRING)
    private AssessmentRole role;

    public AssignedUser() {
    }

    public AssignedUser(User user, AssessmentRole role, Assessment assessment) {
        this.user = user;
        this.role = role;
        this.assessment = assessment;
    }

    public void setRole(AssessmentRole role) {
        this.role = role;
    }

    public AssessmentRole getRole() {
        return role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Assessment getAssessment() {
        return assessment;
    }

    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }
}
