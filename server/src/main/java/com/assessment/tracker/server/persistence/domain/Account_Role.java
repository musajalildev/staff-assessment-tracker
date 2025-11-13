package com.assessment.tracker.server.persistence.domain;

import jakarta.persistence.*;

//entity that grants every possible role an ID
@Entity
@Table(name = "account_role")
public class Account_Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int acc_roleID;

    private Role role;

    public Account_Role(Role role) {
        this.role = role;
    }

    public Account_Role() {
    }


}
