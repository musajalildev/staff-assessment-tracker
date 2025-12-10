package com.assessment.tracker.server.api.dto;

import com.assessment.tracker.server.utils.enums.ModuleRoles;
import com.assessment.tracker.server.persistence.entities.User;
import com.assessment.tracker.server.persistence.entities.Module;

import java.util.UUID;

// DTO to represent module roles
public class ModuleRoleDTO {
    private UUID id;
    private ModuleRoles role;
    private UserDTO userDTO;
    private ModuleDTO moduleDTO;

    public ModuleRoleDTO() {
    }

    public ModuleRoleDTO(UUID moduleRoleID, Module module, ModuleRoles moduleRole, User user) {
    }

    public void setID(UUID id) {
        this.id = id;
    }

    public void setRole(ModuleRoles role) {
        this.role = role;
    }

    public void setUser(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public void setModule(ModuleDTO moduleDTO) {
        this.moduleDTO = moduleDTO;
    }

    public UUID getID() {
        return id;
    }

    public ModuleRoles getRole() {
        return role;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public ModuleDTO getModuleDTO() {
        return moduleDTO;
    }
}