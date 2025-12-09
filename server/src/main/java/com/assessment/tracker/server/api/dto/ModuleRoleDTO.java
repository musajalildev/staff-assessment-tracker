package com.assessment.tracker.server.api.DTO;

import com.assessment.tracker.server.utils.enums.ModuleRoles;
import com.assessment.tracker.server.persistence.entities.User;
import com.assessment.tracker.server.persistence.entities.Module;

import java.util.UUID;

public class ModuleRoleDTO {
    private UUID id;
    private Module module;
    private ModuleRoles role;
    private User user;


    public ModuleRoleDTO() {}
    public ModuleRoleDTO(UUID moduleRoleID, Module module, ModuleRoles moduleRole, User user) {}


    public void setID(UUID id) {this.id = id;}
    public void setModule(Module module) {this.module = module;}
    public void setRole(ModuleRoles role) {this.role = role;}
    public void setUser(User user) {this.user = user;}

    public UUID getID() {return id;}
    public Module getModule() {return module;}
    public ModuleRoles getRole() {return role;}
    public User getUser() {return user;}
}