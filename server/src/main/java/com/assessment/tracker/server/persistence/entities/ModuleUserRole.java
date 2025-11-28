package com.assessment.tracker.server.persistence.entities;

import jakarta.persistence.*;
import com.assessment.tracker.server.api.controller.*;
import com.assessment.tracker.server.api.controllerImpl.*;
import com.assessment.tracker.server.api.DTO.*;

import com.assessment.tracker.server.persistence.entities.*;
import com.assessment.tracker.server.persistence.repos.*;
import com.assessment.tracker.server.persistence.services.*;

import com.assessment.tracker.server.utils.mappers.*;
import com.assessment.tracker.server.utils.enums.*;

@Entity
public class ModuleUserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer moduleID;
    private Integer moduleRoleID;
    private Integer userID;

    public void addUserRole(Integer moduleID, Integer moduleRoleID, Integer userID) {
        this.moduleID = moduleID;
        this.moduleRoleID = moduleRoleID;
        this.userID = userID;
    }

    public Integer getModuleID() {return moduleID;}
    public Integer getModuleRoleID() {return moduleRoleID;}
    public Integer getUserID() {return userID;}
}
