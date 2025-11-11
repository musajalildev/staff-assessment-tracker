package com.assessment.tracker.server.persistence.module;

import jakarta.persistence.*;

@Entity
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer ID;
    private Integer moduleLeaderID;
    private String moduleCode;

    public void setID(Integer newID) {
        this.ID = newID;
    }
    public void setModuleLeaderID(Integer newModuleLeaderID) {
        this.moduleLeaderID = newModuleLeaderID;
    }
    public void setModuleCode(String newModuleCode) {
        this.moduleCode = newModuleCode;
    }

    public Integer getID() {
        return ID;
    }
    public Integer getModuleLeaderID() {
        return moduleLeaderID;
    }
    public String getModuleCode() {
        return moduleCode;
    }
}
