package com.assessment.tracker.server.persistence.module;

import org.springframework.stereotype.Service;

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
