package com.assessment.tracker.server.persistence.services.logging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assessment.tracker.server.persistence.entities.logging.*;
import com.assessment.tracker.server.persistence.repos.logging.*;

@Service
public class AssessmentLogService {
    private final AssessmentLogRepository logRepository;

    @Autowired
    public AssessmentLogService(AssessmentLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void save(AssessmentLog log) {
        logRepository.save(log);
    }

}
