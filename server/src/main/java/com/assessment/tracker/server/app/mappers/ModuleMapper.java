package com.assessment.tracker.server.app.mappers;

import com.assessment.tracker.server.api.dto.*;

import com.assessment.tracker.server.persistence.entities.Module;

import com.assessment.tracker.server.utils.Mapper;
import org.springframework.stereotype.Component;

@Component
public class ModuleMapper implements Mapper<ModuleDTO, Module> {
    @Override
    public ModuleDTO entityToApi(Module entity) {
        return null;
    }

    @Override
    public Module apiToEntity(ModuleDTO moduleDTO) {
        return null;
    }
}
