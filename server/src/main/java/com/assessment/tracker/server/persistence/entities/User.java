package com.assessment.tracker.server.persistence.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.*;
import com.assessment.tracker.server.persistence.entities.logging.*;

import com.assessment.tracker.server.utils.enums.*;

/**
 * Entity class for representing a user
 * Has a many to many relationshup with module represented with the linker table
 * ModuleRole
 * Has a many to many relationshup with assessment represented with the linker
 * table AssignedUser
 * 
 * Has a one to many relationship with logs as the actor of the action and the
 * target of the action.
 * In addition to this, it contains the User type as an enum of UserType and
 * fields for password, username, email
 */
@Entity
@Table(name = "users")
public class User {

    // first section for generating columns
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId; // primary key

    @OneToMany
    private List<AssignedUser> assignedUsers;

    @OneToMany
    private List<ModuleRole> moduleUsers;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;// format verification?

    @JsonIgnore
    @Column(nullable = false, unique = true)
    private String password; // required encryption

    @OneToMany(mappedBy = "user")
    private List<Log> actionsTaken;

    @Column(nullable = false, unique = false)
    private UserType userType;

    @OneToMany(mappedBy = "targetUser")
    private List<UserLog> logs;

    // Constructors
    public User(String username, String email, String Password, UserType userType) {
        this.username = username;
        this.password = Password;
        this.email = email;
        this.userType = userType;
    }

    public User() {
    }

    // Getters and Setters

    public void setUserId(UUID userID) {
        this.userId = userID;
    }

    public List<Log> getActionsTaken() {
        return actionsTaken;
    }

    public void setActionsTaken(List<Log> actionsTaken) {
        this.actionsTaken = actionsTaken;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public List<AssignedUser> getAssignedUsers() {
        return assignedUsers;
    }

    public void setAssignedUsers(List<AssignedUser> assignedUsers) {
        this.assignedUsers = assignedUsers;
    }

    public List<ModuleRole> getModuleUsers() {
        return moduleUsers;
    }

    public void setModuleUsers(List<ModuleRole> moduleUsers) {
        this.moduleUsers = moduleUsers;
    }

    public List<UserLog> getLogs() {
        return logs;
    }

    public void setLogs(List<UserLog> logs) {
        this.logs = logs;
    }

}
