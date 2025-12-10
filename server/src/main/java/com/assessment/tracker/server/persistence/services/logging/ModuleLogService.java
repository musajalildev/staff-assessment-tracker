package com.assessment.tracker.server.persistence.services.logging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assessment.tracker.server.persistence.entities.logging.*;
import com.assessment.tracker.server.persistence.repos.logging.*;

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
