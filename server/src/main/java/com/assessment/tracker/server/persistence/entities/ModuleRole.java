package com.assessment.tracker.server.persistence.entities;

import com.assessment.tracker.server.utils.enums.ModuleRoles;
import jakarta.persistence.*;
import java.util.*;

/**
 * Linker Table to represent module-specific roles for users
 * It has a many to one relationship with User and Module
 * It has an enum of type ModuleRole that represents the relationship
 * between User and Module
 */
@Entity
public class ModuleRole {

    private ModuleRoles role;
    UUID Id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "module_id")
    private Module module;

    public ModuleRole() {
    }

    public ModuleRole(User user, ModuleRoles role, Module module) {
        this.module = module;
        this.role = role;
        this.user = user;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public void setRole(ModuleRoles role) {
        this.role = role;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Module getModule() {
        return module;
    }

    public ModuleRoles getRole() {
        return role;
    }

    public User getUser() {
        return user;
    }

    public UUID getId() {
        return Id;
    }

    public void setId(UUID id) {
        Id = id;
    }
}
