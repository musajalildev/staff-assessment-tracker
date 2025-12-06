package com.assessment.tracker.server.persistence.entities;

import com.assessment.tracker.server.persistence.entities.logging.*;

import jakarta.persistence.*;
import java.util.*;

@Entity
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer ID;
    private Integer moduleLeaderID;
    private String moduleCode;

    @OneToMany(mappedBy = "module")
    private List<Assessment> assessments;

    @OneToMany(mappedBy = "targetModule")
    private List<ModuleLog> logs;

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
