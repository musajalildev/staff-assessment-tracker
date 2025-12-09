package com.assessment.tracker.server.api.DTO;

import java.util.UUID;
import com.assessment.tracker.server.utils.enums.*;

public class AssessmentRolesDTO {
    Integer assessmentID;
    UUID userID;
    AssessmentRole role;

    public AssessmentRolesDTO(Integer assessmentID, UUID userID, AssessmentRole role) {
        this.assessmentID = assessmentID;
        this.userID = userID;
        this.role = role;
    }

    public Integer getAssessmentID() {
        return assessmentID;
    }

    public void setAssessmentID(Integer assessmentID) {
        this.assessmentID = assessmentID;
    }

    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    public AssessmentRole getRole() {
        return role;
    }

    public void setRole(AssessmentRole role) {
        this.role = role;
    }

}
