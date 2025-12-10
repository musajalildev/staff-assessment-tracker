package com.assessment.tracker.server.persistence.entities;

import jakarta.persistence.*;
import java.util.*;

import com.assessment.tracker.server.persistence.entities.logging.*;
import com.assessment.tracker.server.utils.enums.*;

/**
 * Entity class for representing assessments.
 * Primary key of type Integer with name Id.
 * 
 * It has a many to many association with Users through setters, checkers and
 * involved staff, implemented to the linker table.
 * It has a many to one association with Module through having an associated
 * module.
 * It has a one to many association with AssessmentLog.
 * It has a one to many association with AssessmentFeedback.
 * 
 * In addition to this, it contains the assessment type as an enum of
 * AssessmentType, progress as an enum of AssessmentProgress.
 */
@Entity
public class Assessment {
    // Fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module module;

    private AssessmentType assessmentType;

    private AssessmentProgress progress;

    private String title;

    @OneToMany(mappedBy = "assessment")
    private List<AssessmentFeedback> feedback;

    @OneToMany(mappedBy = "assessment")
    private List<AssignedUser> assignedUsers;

    @OneToMany(mappedBy = "targetAssessment")
    private List<AssessmentLog> logs;

    // constructors
    public Assessment() {
        this.feedback = new ArrayList<>();
    }

    public Assessment(Module module, AssessmentType assessmentType, AssessmentProgress progress, String title) {
        this.module = module;
        this.assessmentType = assessmentType;
        this.progress = AssessmentProgress.CREATED;
        this.title = title;
        this.addSetter(module.getLead());
        this.addChecker(module.getModerator());
    }

    public Assessment(AssessmentType assessmentType, AssessmentProgress progress, String title) {
        this.assessmentType = assessmentType;
        this.progress = progress;
        this.title = title;
    }

    // Getters and setters
    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public void setId(Integer Id) {
        this.Id = Id;
    }

    public void setFeedback(List<AssessmentFeedback> feedback) {
        this.feedback = feedback;
    }

    public List<User> getSetters() {
        return assignedUsers.stream().filter(i -> i.getRole() == AssessmentRole.ROLE_SETTER).map(i -> i.getUser())
                .toList();
    }

    public List<User> getCheckers() {
        return assignedUsers.stream().filter(i -> i.getRole() == AssessmentRole.ROLE_CHECKER).map(i -> i.getUser())
                .toList();
    }

    public List<User> getInvolved() {
        return assignedUsers.stream().map(i -> i.getUser())
                .toList();
    }

    public AssessmentProgress getProgress() {
        return progress;
    }

    public void setProgress(AssessmentProgress progress) {
        this.progress = progress;
    }

    public AssessmentType getAssessmentType() {
        return assessmentType;
    }

    public List<AssessmentFeedback> getFeedback() {
        return feedback;
    }

    public void setAssessmentType(AssessmentType assessmentType) {
        this.assessmentType = assessmentType;
    }

    public Integer getId() {
        return Id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Adder Functions
    public void addFeedback(AssessmentFeedback feedback) {
        this.feedback.add(feedback);
        feedback.setAssessment(this);
    }

    public void addSetter(User user) {
        this.assignedUsers.add(new AssignedUser(user, AssessmentRole.ROLE_SETTER, this));
    }

    public void addChecker(User user) {
        this.assignedUsers.add(new AssignedUser(user, AssessmentRole.ROLE_CHECKER, this));
    }

    public void addInvolved(User user) {
        this.assignedUsers.add(new AssignedUser(user, AssessmentRole.ROLE_INVOLVED, this));
    }

}
