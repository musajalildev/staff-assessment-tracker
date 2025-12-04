package com.assessment.tracker.server.persistence.entities.logging;

import jakarta.persistence.*;
import java.util.*;

import com.assessment.tracker.server.utils.enums.*;
import com.assessment.tracker.server.persistence.entities.*;

@Entity
public class AssessmentLog extends Log {
    @ManyToOne
    @JoinColumn(name = "targetAssessment")
    private Assessment targetAssessment;

    private AssessmentActions actionType;

    private AssessmentProgress previousState;
    private AssessmentProgress newState;

    public Assessment getTargetAssessment() {
        return targetAssessment;
    }

    public void setTargetAssessment(Assessment targetAssessment) {
        this.targetAssessment = targetAssessment;
    }

    public AssessmentActions getActionType() {
        return actionType;
    }

    public void setActionType(AssessmentActions actionType) {
        this.actionType = actionType;
    }

    public AssessmentProgress getPreviousState() {
        return previousState;
    }

    public void setPreviousState(AssessmentProgress previousState) {
        this.previousState = previousState;
    }

    public AssessmentProgress getNewState() {
        return newState;
    }

    public void setNewState(AssessmentProgress newState) {
        this.newState = newState;
    }

}
