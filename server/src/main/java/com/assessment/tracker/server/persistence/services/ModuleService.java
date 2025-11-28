package com.assessment.tracker.server.persistence.services;

import org.springframework.stereotype.Service;

import com.assessment.tracker.server.api.controller.*;
import com.assessment.tracker.server.api.controllerImpl.*;
import com.assessment.tracker.server.api.DTO.*;

import com.assessment.tracker.server.persistence.entities.*;
import com.assessment.tracker.server.persistence.entities.Module;
import com.assessment.tracker.server.persistence.repos.*;
import com.assessment.tracker.server.persistence.services.*;

import com.assessment.tracker.server.utils.mappers.*;
import com.assessment.tracker.server.utils.enums.*;

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
