package com.assessment.tracker.server.persistence.entities;

import com.assessment.tracker.server.persistence.entities.Assessment;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class AssessmentStage {
    @Id
    private String assessmentStageId;
    @ManyToOne
    private Assessment assessment;
    // private enum assessment stage
}
