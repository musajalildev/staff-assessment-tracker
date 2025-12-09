package com.assessment.tracker.server.persistence.entities;

import com.assessment.tracker.server.utils.enums.ModuleRoles;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class ModuleRole {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private ModuleRoles role;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "module_id")
    private Module module;


    public void setID(UUID id) {this.id = id;}
    public void setModule(Module module) {this.module = module;}
    public void setRole(ModuleRoles role) {this.role = role;}
    public void setUser(User user) {this.user = user;}

    public UUID getID() {return id;}
    public Module getModule() {return module;}
    public ModuleRoles getRole() {return role;}
    public User getUser() {return user;}
}
