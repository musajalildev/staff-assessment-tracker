package com.assessment.tracker.server.persistence.entities;

import com.assessment.tracker.server.utils.enums.*;

import jakarta.persistence.*;

//entity that grants every possible assesment-specific role an ID
@Entity
@Table(name = "roles")
public class AssignedUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int assignmentID;

    public String username;

    @ManyToOne
    private User user;


    @ManyToOne
    private Assessment assessment;
    //TODO: Assesment constructor and to be set as not optional
    // since role is attached to assessment

    @Enumerated(EnumType.STRING)
    private AssesmentRole role;

    public AssignedUser() {
    }

    public AssignedUser(User user, AssesmentRole role, Assessment assessment) {
        this.user = user;
        this.username=user.getUsername();
        this.role = role;
        this.assessment = assessment;
    }
    public void setRole(AssesmentRole role) {
        this.role = role;
    }

    public AssesmentRole getRole() {
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
