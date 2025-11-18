package com.assessment.tracker.server.persistence.moduleUserRole;

import jakarta.persistence.*;

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
