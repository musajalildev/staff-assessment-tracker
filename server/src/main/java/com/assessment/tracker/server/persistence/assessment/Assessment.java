package com.assessment.tracker.server.persistence.assessment;

import jakarta.persistence.*;

@Entity
public class Assessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;
    private Integer moduleID;
    //private Enum assessmentType;

    public void setModuleID(Integer moduleID) {
        this.moduleID = moduleID;
    }
    //public void setAssessmentType(Enum assessmentType) {
        //this.assessmentType = assessmentType
    //}

    public Integer getID() {return ID;}
    public Integer getModuleID() {return moduleID;}
}
