package com.assessment.tracker.server.utils.mappers;

import com.assessment.tracker.server.api.DTO.ModuleRoleDTO;
import com.assessment.tracker.server.persistence.entities.ModuleRole;

public class ModuleRoleMapper {
    public ModuleRoleDTO entityToApi(ModuleRole entity) {
        ModuleRoleDTO DTO = new ModuleRoleDTO();

        DTO.setID(entity.getID());
        DTO.setModule(entity.getModule());
        DTO.setRole(entity.getRole());
        DTO.setUser(entity.getUser());

        return DTO;
    }

    public ModuleRole apiToEntity(ModuleRoleDTO DTO) {
        ModuleRole entity = new ModuleRole();

        entity.setID(DTO.getID());
        entity.setModule(DTO.getModule());
        entity.setRole(DTO.getRole());
        entity.setUser(DTO.getUser());

        return entity;
    }
}
