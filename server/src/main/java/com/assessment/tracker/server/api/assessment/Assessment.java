package com.assessment.tracker.server.api.assessment;

import jakarta.persistence.*;
import java.util.*;

import com.assessment.tracker.server.api.assessmentFeedback.AssessmentFeedback;
import com.assessment.tracker.server.utils.*;
import com.assessment.tracker.server.persistence.domain.User;
import com.assessment.tracker.server.persistence.module.Module;

@Entity
public class Assessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module module;

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    private AssessmentType assessmentType;

    private AssessmentProgress progress;

    private String title;

    @OneToMany(mappedBy = "assessment")
    private List<AssessmentFeedback> feedback;

    @ManyToOne
    @JoinColumn(name = "setter_id")
    private User setter;

    @ManyToOne
    @JoinColumn(name = "checker_id")
    private User checker;

    // Getters and setters

    public void setID(Integer iD) {
        ID = iD;
    }

    public void setFeedback(List<AssessmentFeedback> feedback) {
        this.feedback = feedback;
    }

    public User getSetter() {
        return setter;
    }

    public void setSetter(User setter) {
        this.setter = setter;
    }

    public User getChecker() {
        return checker;
    }

    public void setChecker(User checker) {
        this.checker = checker;
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

    public void addFeedback(AssessmentFeedback feedback) {
        this.feedback.add(feedback);
        feedback.setAssessment(this);
    }

    public void setAssessmentType(AssessmentType assessmentType) {
        this.assessmentType = assessmentType;
    }

    public Integer getID() {
        return ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
