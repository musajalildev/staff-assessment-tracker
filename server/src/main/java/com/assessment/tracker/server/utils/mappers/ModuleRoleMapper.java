package com.assessment.tracker.server.utils.mappers;

import com.assessment.tracker.server.api.DTO.ModuleRoleDTO;
import com.assessment.tracker.server.persistence.entities.ModuleRole;
import org.springframework.beans.factory.annotation.Autowired;

public class ModuleRoleMapper {
    @Autowired
    private UserMapper userMapper;
    private ModuleMapper moduleMapper;

    public ModuleRoleDTO entityToApi(ModuleRole entity) {
        ModuleRoleDTO DTO = new ModuleRoleDTO();

        DTO.setID(entity.getID());
        DTO.setRole(entity.getRole());
        DTO.setUser(
                userMapper.entityToApi(entity.getUser())
        );
        DTO.setModule(
                moduleMapper.entityToApi(entity.getModule())
        );

        return DTO;
    }

    public ModuleRole apiToEntity(ModuleRoleDTO DTO) {
        ModuleRole entity = new ModuleRole();

        entity.setID(DTO.getID());
        entity.setRole(DTO.getRole());
        entity.setUser(
                userMapper.apiToEntity(DTO.getUserDTO())
        );
        entity.setModule(
                moduleMapper.apiToEntity(DTO.getModuleDTO())
        );

        return entity;
    }
}
