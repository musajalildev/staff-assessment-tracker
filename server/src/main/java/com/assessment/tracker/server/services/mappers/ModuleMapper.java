package com.assessment.tracker.server.services.mappers;

import com.assessment.tracker.server.api.module.ModuleDTO;
import com.assessment.tracker.server.persistence.module.Module;
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
