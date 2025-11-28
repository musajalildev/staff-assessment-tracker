package com.assessment.tracker.server.persistence.entities;
import com.assessment.tracker.server.api.controller.*;
import com.assessment.tracker.server.api.controllerImpl.*;
import com.assessment.tracker.server.api.DTO.*;

import com.assessment.tracker.server.persistence.entities.*;
import com.assessment.tracker.server.persistence.repos.*;
import com.assessment.tracker.server.persistence.services.*;

import com.assessment.tracker.server.utils.mappers.*;
import com.assessment.tracker.server.utils.enums.*;

import jakarta.persistence.*;

//entity that grants every possible role an ID
@Entity
@Table(name = "roles")
public class AssignedUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int assignmentID;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private Role role;


    public AssignedUser() {
    }

    public AssignedUser(User user, Role role) {
        this.user = user;
        this.role = role;
    }
    public void setRole(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getAssignmentID() {
        return assignmentID;
    }
    public void setAssignmentID(int assignmentID) {
        this.assignmentID = assignmentID;
    }
}



