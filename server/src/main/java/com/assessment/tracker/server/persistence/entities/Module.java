package com.assessment.tracker.server.persistence.entities;

import com.assessment.tracker.server.api.controller.*;
import com.assessment.tracker.server.api.controllerImpl.*;
import com.assessment.tracker.server.api.DTO.*;

import com.assessment.tracker.server.persistence.entities.*;
import com.assessment.tracker.server.persistence.entities.logging.*;
import com.assessment.tracker.server.persistence.repos.*;
import com.assessment.tracker.server.persistence.services.*;

import com.assessment.tracker.server.utils.mappers.*;
import com.assessment.tracker.server.utils.enums.*;

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
