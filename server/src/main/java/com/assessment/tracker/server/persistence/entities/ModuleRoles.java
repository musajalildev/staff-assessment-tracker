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
public class ModuleRoles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;
    private String roleName;

    public void setRole(String roleName){ //Add to repo
        this.roleName = roleName;
    }

    public Integer getID() {return ID;}
    public String getRoleName() {return roleName;}
}
