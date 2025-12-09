package com.assessment.tracker.server.app.mappers;

import com.assessment.tracker.server.api.dto.ModuleRoleDTO;
import com.assessment.tracker.server.persistence.entities.ModuleRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModuleRoleMapper {

    private final UserMapper userMapper;
    private final ModuleMapper moduleMapper;

    @Autowired
    public ModuleRoleMapper(UserMapper userMapper, ModuleMapper moduleMapper) {
        this.moduleMapper = moduleMapper;
        this.userMapper = userMapper;
    }

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
