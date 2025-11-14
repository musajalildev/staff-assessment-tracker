package com.assessment.tracker.server.persistence.domain;

import jakarta.persistence.*;

import java.util.Set;

//entity that grants every possible role an ID
@Entity
@Table(name = "roles")
public class AccountRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int roleID;

    @ManyToMany(mappedBy = "assignedRoles")
    Set<User> assignedUsers;

    private Role role;

    public AccountRole(Role role) {
        this.role = role;
    }

    public AccountRole() {
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }





}
