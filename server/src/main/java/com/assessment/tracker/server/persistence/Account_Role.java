package com.assessment.tracker.server.persistence;

import jakarta.persistence.*;
import java.util.*;

//entity that grants every possible role an ID
@Entity
@Table(name = "account_role")
public class Account_Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long acc_roleID;

    @ManyToMany(mappedBy = "assignedRoles") //referencing it's mirror collection in user class
    Set<User> assignedUsers;//defining relationship

    private Role role;

    public Account_Role(Role role) {
        this.role = role;
    }

    public Account_Role() {
    }


}
