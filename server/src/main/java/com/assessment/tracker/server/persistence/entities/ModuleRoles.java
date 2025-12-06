package com.assessment.tracker.server.persistence.entities;

import jakarta.persistence.*;

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
