package com.assessment.tracker.server.persistence.services.logging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assessment.tracker.server.api.controller.*;
import com.assessment.tracker.server.api.controllerImpl.*;
import com.assessment.tracker.server.api.DTO.*;

import com.assessment.tracker.server.persistence.entities.*;
import com.assessment.tracker.server.persistence.entities.logging.*;
import com.assessment.tracker.server.persistence.entities.Module;
import com.assessment.tracker.server.persistence.repos.*;
import com.assessment.tracker.server.persistence.repos.logging.*;
import com.assessment.tracker.server.persistence.services.*;

import com.assessment.tracker.server.utils.mappers.*;
import com.assessment.tracker.server.utils.enums.*;

@Service
public class ModuleLogService {
    private final ModuleLogRepository logRepository;

    @Autowired
    public ModuleLogService(ModuleLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void save(ModuleLog log) {
        logRepository.save(log);
    }

}
