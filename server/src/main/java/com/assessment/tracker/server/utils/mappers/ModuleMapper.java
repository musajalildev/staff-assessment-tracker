package com.assessment.tracker.server.utils.mappers;

import com.assessment.tracker.server.api.DTO.ModuleDTO;
import com.assessment.tracker.server.persistence.entities.Module;

import org.springframework.stereotype.Component;


@Component
public class ModuleMapper implements Mapper<ModuleDTO, Module> {
    public ModuleDTO entityToApi(Module entity) {
        ModuleDTO DTO = new ModuleDTO();

        DTO.setID(entity.getID());
        DTO.setCode(entity.getCode());
        DTO.setTitle(entity.getTitle());
        DTO.setArchived(entity.getArchiveStatus());

        return DTO;
    }

    @Override
    public Module apiToEntity(ModuleDTO DTO) {
        Module entity = new Module();

        entity.setID(DTO.getID());
        entity.setCode(DTO.getCode());
        entity.setTitle(DTO.getTitle());
        entity.setArchived(DTO.isArchived());

        return entity;
    }
}
