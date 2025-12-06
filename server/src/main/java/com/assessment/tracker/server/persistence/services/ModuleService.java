package com.assessment.tracker.server.persistence.services;

import org.springframework.stereotype.Service;

import com.assessment.tracker.server.persistence.entities.Module;
import com.assessment.tracker.server.persistence.repos.*;

@Service
public class ModuleService {
    private final ModuleRepo moduleRepository;

    public ModuleService(ModuleRepo moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    public void save(Module module) {
        moduleRepository.save(module);
    }
}
