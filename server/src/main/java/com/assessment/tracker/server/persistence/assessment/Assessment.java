package com.assessment.tracker.server.persistence.assessment;

import com.assessment.tracker.server.utils.AssessmentType;
import jakarta.persistence.*;

@Entity
public class Assessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;
    private Integer moduleID;
    private AssessmentType assessmentType;
    private boolean autoGraded;
    private boolean teamMarked;

    public void setModuleID(Integer moduleID) {
        this.moduleID = moduleID;
    }

    public Integer getID() {return ID;}
    public Integer getModuleID() {return moduleID;}

    public AssessmentType getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(AssessmentType assessmentType) {
        this.assessmentType = assessmentType;
    }

    public boolean isAutoGraded() {
        return autoGraded;
    }

    public void setAutoGraded(boolean autoGraded) {
        this.autoGraded = autoGraded;
    }

    public boolean isTeamMarked() {
        return teamMarked;
    }

    public void setTeamMarked(boolean teamMarked) {
        this.teamMarked = teamMarked;
    }
}
