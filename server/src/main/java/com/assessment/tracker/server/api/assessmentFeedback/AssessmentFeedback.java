package com.assessment.tracker.server.api.assessmentFeedback;

import jakarta.persistence.*;
import com.assessment.tracker.server.api.assessment.Assessment;

@Entity
public class AssessmentFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @ManyToOne
    @JoinColumn(name = "assessmentID")
    private Assessment assessment;

    private String feedback;

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Assessment getAssessment() {
        return assessment;
    }

    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }

    public Integer getID() {
        return ID;
    }

}
